/* license-start
* 
* Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation version 3.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
* 
* Contributors:
*   Crispico - Initial API and implementation
*
* license-end
*/
package org.flowerplatform.editor.remote;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.editor.EditorPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditorService {

	private static final Logger logger = LoggerFactory.getLogger(EditorService.class);
	
	public static final String SERVICE_ID = "editorService";
	
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
	 * @flowerModelElementId _TeUxAEhHEeKn-dlTSOkszw
	 */
	public List<String> getFriendlyEditableResourcePathList(ServiceInvocationContext context, List<String> canonicalEditableResourcePathList) {
		List<String> friendlyEditableResourcePathList = new ArrayList<String>();
		for (String canonicalEditableResourcePath : canonicalEditableResourcePathList) {
			String editorName = URLGenerateNavigateUtilities.getEditorNameFromExternalEditableResourcePath(canonicalEditableResourcePath);
			EditorStatefulService editorStatefulService = EditorPlugin.getInstance().getEditorStatefulServiceByEditorName(editorName);
			if (editorStatefulService == null) {
				logger.error("Could not obtain EditorStatefulService with editorName = " + editorName); 
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
	 * 
	 * @flowerModelElementId _TeVYEkhHEeKn-dlTSOkszw
	 */
	public boolean navigateFriendlyEditableResourcePathList(ServiceInvocationContext context, String openResources, int selectResourceAtIndex) {
		List<String> friendlyEditableResourcePathList = parseFriendlyEditableResourcePathList(openResources);
		
		// It keeps computed info about the "friendly editableResourcePath" to be selected (if found and if demanded by selectResourceAtIndex method parameter).
		boolean selectedResourceAtIndexIsValid = false;
		String selectedCanonicalOpenableResourcePath = null;
		EditorStatefulService selectedEditorStatefulService = null;
		
		StringBuffer validationProblems = new StringBuffer(); 
		for (String friendlyEditableResourcePath : friendlyEditableResourcePathList) {
			try { // Run navigation logic independent one another path
				String editorName = URLGenerateNavigateUtilities.getEditorNameFromExternalEditableResourcePath(friendlyEditableResourcePath);
				if (URLGenerateNavigateUtilities.isStringEmpty(editorName)) { // Validation
					validationProblems.append("Could not determine editor for path '" + friendlyEditableResourcePath + "' .").append("\n");
					continue;
				}

				EditorStatefulService editorStatefulService = EditorPlugin.getInstance().getEditorStatefulServiceByEditorName(editorName);
				if (editorStatefulService == null) { // Validation
					validationProblems.append("Could not find editor '" + editorName + "' for path '" + friendlyEditableResourcePath + "' .").append("\n");
					continue;
				}
				
				// Determine the canonical editableResourcePath to speak the language of EditorStatefulService.
				String canonicalEditableResourcePath = editorStatefulService.getCanonicalEditableResourcePath(friendlyEditableResourcePath, context.getCommunicationChannel(), validationProblems);
				if (canonicalEditableResourcePath == null) {
					continue;
				}

				String canonicalOpenableResourcePath = URLGenerateNavigateUtilities.getOpenableResourcePathFromExternalEditableResourcePath(canonicalEditableResourcePath);
				EditableResource editableResource = editorStatefulService.subscribeClientForcefully(context.getCommunicationChannel(), canonicalOpenableResourcePath, true);
				if (editableResource == null) { // Validation
					validationProblems.append("Could not find '" + canonicalOpenableResourcePath + "' . Either it doesn't exist, or it failed during load.").append("\n");
					continue;
				}
				
				if (friendlyEditableResourcePathList.indexOf(friendlyEditableResourcePath) == selectResourceAtIndex) { // Caches info about the friendly editableResourcePath that must be revealed in an editor.
					selectedResourceAtIndexIsValid = true;
					selectedCanonicalOpenableResourcePath = canonicalOpenableResourcePath;
					selectedEditorStatefulService = editorStatefulService;
				}
					
				String canonicalFragment = URLGenerateNavigateUtilities.getFragmentFromExternalEditableResourcePath(canonicalEditableResourcePath);
				if (!URLGenerateNavigateUtilities.isStringEmpty(canonicalFragment)) // No need to navigate if there is no fragment.
					editorStatefulService.navigateToFragment(context.getCommunicationChannel(), canonicalOpenableResourcePath, canonicalFragment);
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
	private List<String> parseFriendlyEditableResourcePathList(String openResources) {
		openResources = openResources.replace("\r\n", ",").replace("\n", ",").replace("\r", ",").trim(); // make it easier to process when it is multiline
		
		List<String> friendlyEditableResourcePathList = new ArrayList<String>();
		for (String openResourceItem : openResources.split(",")) {
			openResourceItem = openResourceItem.trim(); // make it easier to process
			if (openResourceItem.length() > 0)
				friendlyEditableResourcePathList.add(openResourceItem);
		}	
		return friendlyEditableResourcePathList;
	}
		
	public static class URLGenerateNavigateUtilities {

		public static String getEditorNameFromExternalEditableResourcePath(String externalEditableResourcePath) {
			return getTokenFromEditableResourcePath(externalEditableResourcePath, 0);
		}
		
		public static String getOpenableResourcePathFromExternalEditableResourcePath(String externalEditableResourcePath) {
			return getTokenFromEditableResourcePath(externalEditableResourcePath, 1);
		}

		static String getFragmentFromExternalEditableResourcePath(String externalEditableResourcePath) {
			return getTokenFromEditableResourcePath(externalEditableResourcePath, 2);
		}
		
		private static String getTokenFromEditableResourcePath(String externalEitableResourcePath, int index) {
			if (externalEitableResourcePath == null)
				return null;
			String[] tokens = externalEitableResourcePath.split("(:/)|(#)"); // format like editor_name :/ openable_resource # fragment
			return tokens.length > index ? tokens[index].trim() : null;
		}

		public static boolean isStringEmpty(String string) {
			return string == null || string.trim().length() == 0;
		}
	}
}
