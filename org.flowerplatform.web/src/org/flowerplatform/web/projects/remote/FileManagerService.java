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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
						new DisplaySimpleMessageClientCommand(WebPlugin
								.getInstance().getMessage(title), WebPlugin
								.getInstance().getMessage(message),
								DisplaySimpleMessageClientCommand.ICON_ERROR));
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
				Map<Object, Object> clientContext = new HashMap<Object,Object>();
				clientContext.put(AbstractTreeStatefulService.EXPAND_NODE_KEY, true);
				List<PathFragment> pathOfNode = service.getPathForNode(object,
						"fsFile", null);
				service.openNode(
						new StatefulServiceInvocationContext(context
								.getCommunicationChannel(),clientID), pathOfNode, clientContext);	
				// TODO uncomment this, after talk with Cristi
				/*String[] dirs = name.split("/");
				pathOfNode.add(new PathFragment(dirs[0], "fsFile"));
				service.openNode(
						new StatefulServiceInvocationContext(context
								.getCommunicationChannel(),clientID), pathOfNode, clientContext);*/
				FileEvent event = new FileEvent(theDir, FileEvent.FILE_CREATED);
				CommonPlugin.getInstance().getFileEventDispatcher()
						.dispatch(event);
			} else {
				printMessageToUser(context,
						"explorer.createDirectory.error.title",
						"explorer.createDirectory.error.couldNotCreate");
			}
		} else {
			printMessageToUser(context, "explorer.createDirectory.error.title",
					"explorer.createDirectory.error.alreadyExists");
		}
	}

}
