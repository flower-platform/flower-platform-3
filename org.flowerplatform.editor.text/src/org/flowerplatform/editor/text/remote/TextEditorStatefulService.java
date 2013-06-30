package org.flowerplatform.editor.text.remote;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.editor.remote.EditableResource;
import org.flowerplatform.editor.remote.EditableResourceClient;
import org.flowerplatform.editor.remote.FileBasedEditorStatefulService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristi
 * @author Mariana
 * 
 * @flowerModelElementId _M-WJsAcIEeK49485S7r3Vw
 */
public class TextEditorStatefulService extends FileBasedEditorStatefulService {


//	/**
//	 * @see #createRegexConfiguration()
//	 * @flowerModelElementId _VOxasDfPEeKY0qaVeMRK2Q
//	 */
//	protected RegexConfiguration regexConfiguration;
	
	/**
	 * @flowerModelElementId _M-WwwQcIEeK49485S7r3Vw
	 */
	private static final Logger logger = LoggerFactory.getLogger(TextEditorStatefulService.class);

	public static final String CLIENT_KEYSTROKE_AGGREGATION_INTERVAL = "client.keystroke.aggregation.interval"; //milliseconds

//	static {
//		FlowerWebProperties.INSTANCE.addProperty(new AddIntegerProperty(CLIENT_KEYSTROKE_AGGREGATION_INTERVAL, "3000"));
//	}
	
	public TextEditorStatefulService() {
		createRegexConfiguration();
	}
	
	/**
	 * @flowerModelElementId _M-WwwwcIEeK49485S7r3Vw
	 */
	@Override
	protected boolean areLocalUpdatesAppliedImmediately() {
		return true;
	}

	/**
	 * @flowerModelElementId _M-X-4gcIEeK49485S7r3Vw
	 */
	@Override
	protected EditableResource createEditableResourceInstance() {
		return new TextEditableResource();
	}

	/**
	 * If there is an error at content loading, throw {@link FileNotFoundException} if the file
	 * does not exist.
	 * 
	 * Note: testing that the file exists will fail before loading the contents, because the resource
	 * is not refreshed and thus may be out of sync with the file system. The test is safe to do after
	 * the content is loaded, because loading also triggers refreshing.
	 * 
	 * @author Mariana
	 * 
	 * @flowerModelElementId _M-X-5QcIEeK49485S7r3Vw
	 */
	@Override
	protected void loadEditableResource(StatefulServiceInvocationContext context, EditableResource editableResource) throws FileNotFoundException {
		super.loadEditableResource(context, editableResource);
		TextEditableResource er = (TextEditableResource) editableResource;
		String content;
		try {
			content = FileUtils.readFileToString(er.getFile());
		} catch (Throwable e) {
			throw new RuntimeException("Error while loading file content " + er.getFile(), e);
		}
		
		er.setFileContent(new StringBuffer(content));
		
		// See class comment for purpose of eoln delimiter converting.
		String replacedEolnDelimiter = Utilities.makeFlexCompatibleDelimiters(er.getFileContent()); // Inplace replacement of delimiters.
		er.setReplacedEolnDelimiter(replacedEolnDelimiter);
		
		er.setDirty(false);
	}

	@Override
	protected void disposeEditableResource(EditableResource editableResource) {
		// do nothing
	}

	/**
	 * @flowerModelElementId _M-Yl8wcIEeK49485S7r3Vw
	 */
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
	 * @flowerModelElementId _M-ZNBAcIEeK49485S7r3Vw
	 */
	@Override
	public void doSave(EditableResource editableResource) {
		TextEditableResource er = (TextEditableResource) editableResource;
		StringBuffer content = er.getFileContent();
		String replacedEolnDelimiter = er.getReplacedEolnDelimiter();

		content = Utilities.revertFlexCompatibleDelimiters(content, replacedEolnDelimiter); // Creates a new copy does not affect the editableResource content.
		Utilities.saveFileContent(er.getFile(), content);
		er.setDirty(false);
	}
	
	/**
	 * @flowerModelElementId _M-Z0EwcIEeK49485S7r3Vw
	 */
	@Override
	protected void updateEditableResourceContentAndDispatchUpdates(CommunicationChannel originatingCommunicationChannel, String originatingStatefulClientId, EditableResource editableResource, Object updatesToApply) {

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
			if (!otherClient.getCommunicationChannel().equals(originatingCommunicationChannel)) {
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
		
	/**
	 * Grouping a bunch of atomic handling methods. 
	 */
	public static class Utilities {
		/**
		 * Replaces EOLN_RN_DELIMITER and EOLN_R_DELIMITER to EOLN_N_DELIMITER inplace.  
		 * @param content the content to work on
		 * @return the replacer delimiter if one existed or null if no replacing was needed.
		 */
		public static String makeFlexCompatibleDelimiters(StringBuffer content) {
			String replacedEolnDelimiter = null;
			// If end of line different from \n then convert. Multiple eolns do not work.
			if (content.indexOf(TextEditableResource.EOLN_RN_DELIMITER) >= 0 || content.indexOf(TextEditableResource.EOLN_R_DELIMITER) >= 0) { 
				replacedEolnDelimiter = content.indexOf(TextEditableResource.EOLN_RN_DELIMITER) >= 0 ? TextEditableResource.EOLN_RN_DELIMITER : TextEditableResource.EOLN_R_DELIMITER;
				content.replace(0, content.length(), content.toString().replace(replacedEolnDelimiter, TextEditableResource.EOLN_N_DELIMITER));
			}
			return replacedEolnDelimiter;
		}
		
		/**
		 * Replaces EOLN_N_DELIMITER delimiter with<code> replacedEolnDelimiter</code> by creating a new copy.
		 * @param content to content work on
		 * @param replacedEolnDelimiter the delimiter to replace the flex compatible delimiter. Can be null and
		 * 		in this case there will be no replacing resulting in returning the initial content.
		 */
		public static StringBuffer revertFlexCompatibleDelimiters(StringBuffer content, String replacedEolnDelimiter) {
			if (replacedEolnDelimiter == null)
				return content;
			else
				return new StringBuffer(content.toString().replace(TextEditableResource.EOLN_N_DELIMITER, replacedEolnDelimiter));
		}
		
		/**
		 * @flowerModelElementId _Yw6WNqXLEeG-cPK59Sm4Wg
		 */
		private static void saveFileContent(File file, StringBuffer fileContent) {
//			ByteArrayInputStream bais = new ByteArrayInputStream(fileContent.toString().getBytes());
//			try {
//				logger.trace("Saving text file {}", file.getName());
//				file.setContents(bais, true, true, null);
//			} catch (Exception e) {
//				throw new RuntimeException("Error while saving the file " + file, e);
//			} finally {
//				try {
//					bais.close();
//				} catch (IOException e) {
//					throw new RuntimeException("Error while saving the file " + file, e);
//				}
//			}
		}		
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
	

	/**
	 * Every TextEditorStatefulService specific for a type of editor is responsible for defining 
	 * it's regex configuration used in determining for example the index for given attribute/method or the attribute/method
	 * for a given index.
	 * @see #selectRangeFor()
	 * @see #findElementCategoryAndNameForPosition()
	 */
	protected void createRegexConfiguration() {
	}

	
	/**
	 * @flowerModelElementId _DHpgcDfTEeKY0qaVeMRK2Q
	 */
	public String[] findElementCategoryAndNameForPosition(String editableResourcePath, int position) {
		// TODO implement
		return null;
	}

	///////////////////////////////////////////////////////////////
	// @RemoteInvocation methods
	///////////////////////////////////////////////////////////////
	
//	/**
//	 * @flowerModelElementId _2Mex4DfPEeKY0qaVeMRK2Q
//	 */
//	@RemoteInvocation
//	public void selectRangeFor(StatefulServiceInvocationContext context, String editableResourcePath, String category, String searchString) {
//		TextEditableResource editableResource = (TextEditableResource)editableResources.get(editableResourcePath);
//		if (editableResource == null) 
//			return; // Is not loaded
//		
//		RegexProcessingSession searchSession = regexConfiguration.startSession(editableResource.getFileContent().toString());
//		int[] searchResult = searchSession.findRangeFor(category, searchString);
//		
//		if (searchResult == null) {
//			if (logger.isTraceEnabled()) 
//				logger.trace(String.format("Could not find %s %s in %s", category, searchString, editableResourcePath));				
//			return; // The given category and searchString could not be found
//		}
//		 
//		int offset = searchResult[0];
//		int length = searchResult[1] - searchResult[0];
//		
//		if (logger.isTraceEnabled())
//			logger.trace(String.format("Selecting range offset = %s , length = %s , from category = %s with searchString = %s, inside %s ", offset, length, category, searchString, editableResourcePath));
//		
//		selectRange(context.getCommunicationChannel(), editableResourcePath, offset, length);
//	}
//
//	/**
//	 * By default it tries to navigate to <code>category</code> using the <code>searchString</code> found in the fragment. 
//	 * Note: at the moment this method by default knows how to interpret only the first parameter passed to the opening resource.
//	 * 
//	 * @author Sorin
//	 */
//	public void navigateToFragment(CommunicationChannel channel, String editableResourcePath, String fragment) {
//		if (fragment == null)
//			return;
//		String[] categoryWithSearchString = fragment.split("="); 
//		if (categoryWithSearchString.length != 2)
//			return; // Could not recognize format must by category=searchString
//		String category = categoryWithSearchString[0].trim();
//		String searchString = categoryWithSearchString[1].trim();
//		
//		selectRangeFor(new StatefulServiceInvocationContext(channel), editableResourcePath, category, searchString);
//	}
//	
//	@Override
//	protected boolean isResourceChangedNotificationInteresting(IResource resource) {
//		return resource instanceof IFile;
//	}
}
