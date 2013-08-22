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
import java.util.List;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.web.WebPlugin;

/**
 * @author Tache Razvan Mihai
 */
public class FileManagerService {

	public void createDirectory(ServiceInvocationContext context,
			List<PathFragment> pathWithRoot, String name) {

		Object object = GenericTreeStatefulService.getNodeByPathFor(
				pathWithRoot, null);

		@SuppressWarnings("unchecked")
		String path = ((Pair<File, Object>) object).a.getPath() + "\\" + name;

		File theDir = new File(path);
		if (!theDir.exists()) {
			boolean result = theDir.mkdir();
			if (!result) {
				// TODO handle this
			}
		} else {
			context.getCommunicationChannel()
					.appendCommandToCurrentHttpResponse(
							new DisplaySimpleMessageClientCommand(
									WebPlugin
											.getInstance()
											.getMessage(
													"explorer.createDirectory.error.title"),
									WebPlugin
											.getInstance()
											.getMessage(
													"explorer.createDirectory.error.alreadyExists"),
									DisplaySimpleMessageClientCommand.ICON_ERROR));
		}
	}
	
}
