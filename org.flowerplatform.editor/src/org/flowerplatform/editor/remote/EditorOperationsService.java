package org.flowerplatform.editor.remote;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.editor.EditorPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditorOperationsService {

	private static final Logger logger = LoggerFactory.getLogger(EditorOperationsService.class);
	
	public String getFriendlyNameDecoded(String friendlyName) {
		try {
			friendlyName = URLDecoder.decode(friendlyName, "UTF-8");		
		} catch (UnsupportedEncodingException e) {
			logger.error("Could not decode using UTF-8 charset : " + friendlyName);
		}
		return friendlyName;
	}
	
	/**
	 * An <em>external editableResourcePath</em> is either a <em>canonical editableResourcePath</em or a <em>friendly editableResourcePath</em>.
	 * It's format is <b>editor_name :/ openable_resource # fragment </b>
	 * <p>
	 * A <b>friendly editableResourcePath</b> is human readable while a <b>canonical editableResourcePath</b> is the internal representation
	 * of the Project Explorer Path Fragments.
	 * 
	 * @see EditorStatefulService#getCanonicalEditableResourcePath()
	 * @see EditorStatefulService#getFriendlierEditableResourcePath()
	 * @see #navigateFriendlyEditableResourcePathList()
	 * 
	 * @author Sorin
	 * @author Cristi
	 * @flowerModelElementId _TeUxAEhHEeKn-dlTSOkszw
	 */
	@RemoteInvocation
	public List<String> getFriendlyEditableResourcePathList(List<String> canonicalEditableResourcePathList) {
		List<String> friendlyEditableResourcePathList = new ArrayList<String>();
		for (String canonicalEditableResourcePath : canonicalEditableResourcePathList) {
			DecodedLink decodedLink = new DecodedLink(canonicalEditableResourcePath);
			
			EditorStatefulService editorStatefulService = null;
			if (decodedLink.editorName != null) {
				editorStatefulService = EditorPlugin.getInstance().getEditorStatefulServiceByEditorName(decodedLink.editorName);
			}
			if (editorStatefulService == null) {
				logger.error("Could not obtain EditorStatefulService with editorName = " + decodedLink.editorName); 
				continue;
			}			
			friendlyEditableResourcePathList.add(editorStatefulService.getFriendlyEditableResourcePath(canonicalEditableResourcePath));			
		}
		return friendlyEditableResourcePathList;
	}
	
	/**
	 * Called from client side with openResources and selectResourceAtIndex parameters to open editors for the user.
	 * If problems happen (like incorrect format of the paths) then a message is presented for each path with the source of error.
	 * <p>
	 * This tries to speak the language of EditorStatefulService by transforming from friendly to canonical editableResourcePath.
	 * 
	 * @see EditorStatefulService#getCanonicalEditableResourcePath(String)
	 * 
	 * @param openResources comma separated friendly editableResourcePaths
	 * @param selectResourceAtIndex optional, which resource's editor to reveal at the end.
	 *      
	 * @return <code>true</code> if the resource was opened successfully, <code>false</code> otherwise
	 *      
	 * @author Sorin
	 * @author Mariana
	 * @author Cristi
	 * 
	 * @flowerModelElementId _TeVYEkhHEeKn-dlTSOkszw
	 */
	@RemoteInvocation
	public boolean navigateFriendlyEditableResourcePathList(ServiceInvocationContext context, String openResources, int selectResourceAtIndex) {
		List<String> friendlyEditableResourcePathList = parseFriendlyEditableResourcePathList(openResources);
		
		// It keeps computed info about the "friendly editableResourcePath" to be selected (if found and if demanded by selectResourceAtIndex method parameter).
		boolean selectedResourceAtIndexIsValid = false;
		String selectedCanonicalOpenableResourcePath = null;
		EditorStatefulService selectedEditorStatefulService = null;
		
		StringBuffer validationProblems = new StringBuffer(); 
		for (String friendlyEditableResourcePath : friendlyEditableResourcePathList) {
			try { // Run navigation logic independent one another path
				DecodedLink decodedLink = new DecodedLink(friendlyEditableResourcePath);
//				String editorName = URLGenerateNavigateUtilities.getEditorNameFromExternalEditableResourcePath(friendlyEditableResourcePath);
				if (decodedLink.editorName == null) {
					// the link doesn't contain the editor name; so we try to find the default
					// editor, based on its extension
					String contentType = EditorPlugin.getInstance().getContentTypeFromFileName(decodedLink.resourcePath);
					ContentTypeDescriptor descriptor = EditorPlugin.getInstance().getContentTypeDescriptorsMap().get(contentType);
					if (descriptor == null) {
						validationProblems.append("Could not determine content type for path '" + friendlyEditableResourcePath + "' .").append("\n");
						continue;
					} else {
						if (!descriptor.getCompatibleEditors().isEmpty()) {
							decodedLink.editorName = descriptor.getCompatibleEditors().get(0);	
						}
					}
				}

				EditorStatefulService editorStatefulService = EditorPlugin.getInstance().getEditorStatefulServiceByEditorName(decodedLink.editorName);
				if (editorStatefulService == null) { // Validation
					validationProblems.append("Could not find editor '" + decodedLink.editorName + "' for path '" + friendlyEditableResourcePath + "' .").append("\n");
					continue;
				}
				
				// Determine the canonical editableResourcePath to speak the language of EditorStatefulService.
				String canonicalEditableResourcePath = editorStatefulService.getCanonicalEditableResourcePath(friendlyEditableResourcePath, context.getCommunicationChannel(), validationProblems);
				if (canonicalEditableResourcePath == null) {
					continue;
				}

				EditableResource editableResource = editorStatefulService.subscribeClientForcefully(context.getCommunicationChannel(), getResourcePath(decodedLink.resourcePath), true);
				if (editableResource == null) { // Validation
					validationProblems.append("Could not find '" + decodedLink.resourcePath + "' . Either it doesn't exist, or it failed during load.").append("\n");
					continue;
				}
				
				if (friendlyEditableResourcePathList.indexOf(friendlyEditableResourcePath) == selectResourceAtIndex) { // Caches info about the friendly editableResourcePath that must be revealed in an editor.
					selectedResourceAtIndexIsValid = true;
					selectedCanonicalOpenableResourcePath = decodedLink.resourcePath;
					selectedEditorStatefulService = editorStatefulService;
				}
					
				if (decodedLink.fragment != null) // No need to navigate if there is no fragment.
					editorStatefulService.navigateToFragment(context.getCommunicationChannel(), decodedLink.resourcePath, decodedLink.fragment);
			} catch (Throwable t) {
				logger.error("Error happened while performing logic to navigate path : " + friendlyEditableResourcePath, t);
				validationProblems.append("Internal error happened while trying to navigate to path '" + friendlyEditableResourcePath + "'.").append("\n");
			}
		}
		if (selectedResourceAtIndexIsValid) // Makes sense to select tab only when multiple paths are opened and the selected path is valid.
			selectedEditorStatefulService.revealEditor(context.getCommunicationChannel(), selectedCanonicalOpenableResourcePath);
		
		if (logger.isTraceEnabled() && selectResourceAtIndex >= 0 && !selectedResourceAtIndexIsValid) 
			logger.trace("Could not reveal the " + selectResourceAtIndex + "-th resource because it could not be opened from openResources = " + openResources);
		
		if (validationProblems.toString().length() != 0) { // Show to user the collected problems with a single popup message.
			context.getCommunicationChannel().appendOrSendCommand(
					new DisplaySimpleMessageClientCommand("Warning", 
													"There were some problems while opening resources!",
													validationProblems.toString(), 
													DisplaySimpleMessageClientCommand.ICON_WARNING));
			return false;
		}
		return true;
	}
	
	/**
	 * @flowerModelElementId _TeV_I0hHEeKn-dlTSOkszw
	 */
	protected List<String> parseFriendlyEditableResourcePathList(String openResources) {
		openResources = openResources.replace("\r\n", ",").replace("\n", ",").replace("\r", ",").trim(); // make it easier to process when it is multiline
		
		List<String> friendlyEditableResourcePathList = new ArrayList<String>();
		for (String openResourceItem : openResources.split(",")) {
			openResourceItem = openResourceItem.trim(); // make it easier to process
			if (openResourceItem.length() > 0)
				friendlyEditableResourcePathList.add(openResourceItem);
		}	
		return friendlyEditableResourcePathList;
	}	
	
	protected String getResourcePath(String path) {
		return path;
	}
	
	/**
	 * @author Cristi
	 */
	public static class DecodedLink {
		public String editorName;
		public String resourcePath;
		public String fragment;

		public DecodedLink(String externalEditableResourcePath) {
			// e.g. editor:/path/to/url#fragment
			// editor and fragment are optional
			final String regex = "(?:(.*?):/)?([^#]*)(?:#(.+))?";					
			Matcher matcher = Pattern.compile(regex).matcher(externalEditableResourcePath);
			if (!matcher.find()) {
				// this shouldn't happen, as the expression matches any string
			}

			editorName = getNullIfStringEmpty(matcher.group(1));
			resourcePath = getNullIfStringEmpty(matcher.group(2));
			fragment = getNullIfStringEmpty(matcher.group(3));
		}
		
		private String getNullIfStringEmpty(String s) {
			if (s != null && s.trim().length() == 0) {
				return null;
			} else {
				return s;
			}
		}
	}
}
