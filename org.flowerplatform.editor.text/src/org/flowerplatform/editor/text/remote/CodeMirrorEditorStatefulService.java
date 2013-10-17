package org.flowerplatform.editor.text.remote;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.editor.EditorPlugin;
import org.flowerplatform.editor.remote.EditableResource;
import org.flowerplatform.editor.remote.EditableResourceClient;
import org.flowerplatform.editor.remote.FileBasedEditorStatefulService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristina Constatinescu
 */
public class CodeMirrorEditorStatefulService extends FileBasedEditorStatefulService {

	private static final Logger logger = LoggerFactory.getLogger(CodeMirrorEditorStatefulService.class);

	public CodeMirrorEditorStatefulService() {		
		CommunicationPlugin.getInstance().getCommunicationChannelManager().addWebCommunicationLifecycleListener(this);
	}
	
	@Override
	protected boolean areLocalUpdatesAppliedImmediately() {
		return true;
	}

	@Override
	protected EditableResource createEditableResourceInstance() {
		return new TextEditableResource();
	}

	@Override
	protected void loadEditableResource(StatefulServiceInvocationContext context, EditableResource editableResource) throws FileNotFoundException {
		super.loadEditableResource(context, editableResource);
		TextEditableResource er = (TextEditableResource) editableResource;
		er.setFileContent(EditorPlugin.getInstance().getFileAccessController().getContent(er.getFile()));
		er.setDirty(false);
	}

	@Override
	protected void disposeEditableResource(EditableResource editableResource) {
		// do nothing
	}

	@Override
	public void sendFullContentToClient(EditableResource editableResource, EditableResourceClient client) {
		TextEditorUpdate update = new TextEditorUpdate();
		update.setNewText(((TextEditableResource)editableResource).getFileContent().toString());
		sendContentUpdateToClient(editableResource, client, Collections.singleton(update), true);
	}

	/**
	 * Writes the content in the {@link TextEditableResource#getFileContent()} to the 
	 * text file. Also refreshes the file so the system will detect the changes; 
	 * otherwise an exception will be thrown the next time this file is opened.
	 * 
	 * <p> NOTE: after saving the file content from the resource still has the delimiters converted. 
	 * 
	 */
	@Override
	public void doSave(EditableResource editableResource) {
		TextEditableResource er = (TextEditableResource) editableResource;
		StringBuffer content = er.getFileContent();
		EditorPlugin.getInstance().getFileAccessController().setContent(er.getFile(), content);
		er.setDirty(false);
	}
	
	@Override
	protected void updateEditableResourceContentAndDispatchUpdates(StatefulServiceInvocationContext context, EditableResource editableResource, Object updatesToApply) {
		TextEditableResource textEditableResource = (TextEditableResource) editableResource;
		
		// update the file contents
		textEditableResource.setDirty(true);
		@SuppressWarnings("unchecked")
		List<TextEditorUpdate> textEditorUpdates = (List<TextEditorUpdate>) updatesToApply;
		for (TextEditorUpdate update : textEditorUpdates) {
			if (logger.isTraceEnabled()) {
				logger.trace("Applying text updates for editorInput = {}: {}", textEditableResource.getEditorInput(), update);
			}
			textEditableResource.replaceContent(update.getOffset(), update.getOldTextLength(), update.getNewText());
		}
		
		for (EditableResourceClient otherClient : textEditableResource.getClients()) {
			if (!otherClient.getCommunicationChannel().equals(context.getCommunicationChannel())) {
				if (logger.isTraceEnabled()) {
					logger.trace("Redispatching text updates to client = {}", otherClient.getCommunicationChannel());
				}
				sendContentUpdateToClient(textEditableResource, otherClient, textEditorUpdates, false);
			}
		}
	}
	
	/**
	 * Encode only special characters from name.
	 * 
	 * @author Cristina
	 */
	@Override
	public String getFriendlyNameEncoded(String friendlyName) {		
		try {
			friendlyName = friendlyName.replace(",", URLEncoder.encode(",", "UTF-8"));					
		} catch (UnsupportedEncodingException e) {
			logger.error("Could not encode using UTF-8 charset : " + friendlyName);
		}
		return super.getFriendlyNameEncoded(friendlyName);
	}
			
	public void selectRange(CommunicationChannel channel, String editableResourcePath, int offset, int length) {
		TextEditableResource textEditableResource = (TextEditableResource)editableResources.get(editableResourcePath);
		if (textEditableResource == null) {
			logger.warn("SelectRange called for " + editableResourcePath + " but it is not opened");
			return; // Is not loaded
		}
		EditableResourceClient client = textEditableResource.getEditableResourceClientByCommunicationChannel(channel);
		if (client == null) {
			logger.warn("SelectRange called for " + editableResourcePath + " but it is not opened for client " + channel);
			return; // Not loaded by specific client
		}
		invokeClientMethod(channel, client.getStatefulClientId(), "selectRange", new Object[] {offset, length});
	}
			
}
