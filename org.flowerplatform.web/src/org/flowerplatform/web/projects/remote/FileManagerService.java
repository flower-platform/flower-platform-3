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
package org.flowerplatform.web.projects.remote;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.remote.AbstractTreeStatefulService;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.file_event.FileEvent;
import org.flowerplatform.web.WebPlugin;

/**
 * @author Tache Razvan Mihai
 */
public class FileManagerService {

	public void printMessageToUser(ServiceInvocationContext context,
			String title, String message) {
		context.getCommunicationChannel()
				.appendCommandToCurrentHttpResponse(
						new DisplaySimpleMessageClientCommand(
								WebPlugin.getInstance().getMessage(title), 
								WebPlugin.getInstance().getMessage(message),
								DisplaySimpleMessageClientCommand.ICON_ERROR));
	}
	
	public void printSimpleMessageToUser(ServiceInvocationContext context,
			String title, String message) {
		context.getCommunicationChannel()
				.appendCommandToCurrentHttpResponse(
						new DisplaySimpleMessageClientCommand(title, message,
								DisplaySimpleMessageClientCommand.ICON_ERROR));
	}

	private boolean deleteFolder(final File folder) {

		File[] files = folder.listFiles();
		if (files != null) { // some JVMs return null for empty dirs
			for (File f : files) {
				if (f.isDirectory()) {
					deleteFolder(f);
				} else {
					f.delete();
				}
			}
		}
		return folder.delete();
	}
	
	private void expandNodesAfterCreate(ServiceInvocationContext context, GenericTreeStatefulService service, String clientID, Object object, File file, String fileName) {
		Map<Object, Object> clientContext = new HashMap<Object,Object>();
		clientContext.put(AbstractTreeStatefulService.EXPAND_NODE_KEY, true);
		@SuppressWarnings("unchecked")
		IResource resource = ProjectsService.getInstance().getProjectWrapperResourceFromFile(((Pair<File, Object>) object).a);
		List<PathFragment> pathOfNode = null;
		if(resource != null) {
			if(resource.getType() == IResource.FOLDER ) {
				pathOfNode = service.getPathForNode(object,
						"projFile", null);	
			} else {
				pathOfNode = service.getPathForNode(object,
						"project", null);
			}
		} else {
			pathOfNode = service.getPathForNode(object,
					"fsFile", null);
		}
		
		service.openNode(
				new StatefulServiceInvocationContext(context
						.getCommunicationChannel(),clientID), pathOfNode, clientContext);	
		
		String[] dirs = fileName.split("/");
		resource = ProjectsService.getInstance().getProjectWrapperResourceFromFile(file);
		for (int i = 0; i < dirs.length - 1; i++) {
			if(resource != null) {
				if(resource.getType() == IResource.FOLDER ) {
					pathOfNode.add(new PathFragment(dirs[i], "projFile"));	
				} else {
					pathOfNode.add(new PathFragment(dirs[i], "project"));
				}
			} else {
				pathOfNode.add(new PathFragment(dirs[i], "fsFile"));
			}
			service.openNode(
					new StatefulServiceInvocationContext(context
							.getCommunicationChannel(),clientID), pathOfNode, clientContext);
		}
	}
	public void createDirectory(ServiceInvocationContext context,
			List<PathFragment> pathWithRoot, String name) {

		GenericTreeStatefulService service = GenericTreeStatefulService
				.getServiceFromPathWithRoot(pathWithRoot);
		String clientID = GenericTreeStatefulService.getClientIDFromPathWithRoot(pathWithRoot);
		Object object = GenericTreeStatefulService.getNodeByPathFor(
				pathWithRoot, null);

		@SuppressWarnings("unchecked")
		String path = ((Pair<File, Object>) object).a.getPath() + "\\" + name;
		
		File theDir = new File(path);
		if (!theDir.exists()) {
			boolean result = theDir.mkdirs();
			if (result) {
				expandNodesAfterCreate(context, service, clientID, object, theDir, name);
				
				FileEvent event = new FileEvent(theDir, FileEvent.FILE_CREATED);
				CommonPlugin.getInstance().getFileEventDispatcher()
						.dispatch(event);
			} else {
				printMessageToUser(context,
						"explorer.createDirectory.error.title",
						"explorer.createDirectory.error.couldNotCreate");
			}
		} else 
			if(theDir.isFile()) {
				printMessageToUser(context, 
						"explorer.createDirectory.error.title",
						"explorer.createDirectory.error.alreadyExistsAsFile");
			} else {
				printMessageToUser(context, 
						"explorer.createDirectory.error.title",
						"explorer.createDirectory.error.alreadyExists");
			}
	}
	
	public void createFile(ServiceInvocationContext context,
			List<PathFragment> pathWithRoot, String name) {
		
		GenericTreeStatefulService service = GenericTreeStatefulService
				.getServiceFromPathWithRoot(pathWithRoot);
		String clientID = GenericTreeStatefulService.getClientIDFromPathWithRoot(pathWithRoot);
		Object object = GenericTreeStatefulService.getNodeByPathFor(
				pathWithRoot, null);

		@SuppressWarnings("unchecked")
		String path = ((Pair<File, Object>) object).a.getPath() + "\\" + name;
		
		File file = new File(path);
		
		try {		
			if(!file.exists()) {
				file.getParentFile().mkdirs();
				boolean result = file.createNewFile();
				if(result) {
					expandNodesAfterCreate(context, service, clientID, object, file, name);
					
					FileEvent event = new FileEvent(file, FileEvent.FILE_CREATED);
					CommonPlugin.getInstance().getFileEventDispatcher()
							.dispatch(event);
				} else {
					printMessageToUser(context,
							"explorer.createFile.error.title",
							"explorer.createFile.error.couldNotCreate");
				}
			} else 
				if(file.isDirectory()) {
					printMessageToUser(context, 
							"explorer.createFile.error.title",
							"explorer.createFile.error.alreadyExistsAsDirectory");
				}
				else {
					printMessageToUser(context, 
							"explorer.createFile.error.title",
							"explorer.createFile.error.alreadyExists");
				}
			
		} catch (IOException e) {
			printSimpleMessageToUser(context,
					"Error",
					e.getMessage() + " Check the console for more informations");
			e.printStackTrace();
		}
	}
	
	public void deleteFile(ServiceInvocationContext context,
			List<PathFragment> pathWithRoot) {
		
		Object object = GenericTreeStatefulService.getNodeByPathFor(
				pathWithRoot, null);
		
		@SuppressWarnings("unchecked")
		String path = ((Pair<File, Object>) object).a.getPath();
		
		File file = new File(path);
		
		if(file.exists()) {
			IResource resource = ProjectsService.getInstance().getProjectWrapperResourceFromFile(file);
			if( resource != null && resource.getType() == IResource.PROJECT ) {
				printSimpleMessageToUser(context, "error", "The selected folder is a project, please delete it from the project menu");
			} else {	
				boolean result = deleteFolder(file);
				if(result) {
					FileEvent event = new FileEvent(file, FileEvent.FILE_DELETED);
					CommonPlugin.getInstance().getFileEventDispatcher()
							.dispatch(event);
				} else {
					printMessageToUser(context,
							"explorer.delete.error.title",
							"explorer.delete.error.couldNotDelete");
				} 
			}
		} else {
			printMessageToUser(context,
					"explorer.delete.error.title",
					"explorer.delete.error.fileAlreadyDeleted");
		}
	}

}
