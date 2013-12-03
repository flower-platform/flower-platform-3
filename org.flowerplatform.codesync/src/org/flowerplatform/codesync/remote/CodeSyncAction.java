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
package org.flowerplatform.codesync.remote;

import java.io.File;

import org.flowerplatform.communication.command.AbstractServerCommand;
import org.flowerplatform.editor.EditorPlugin;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;

/**
 * @author Mariana
 */
public class CodeSyncAction extends AbstractServerCommand {

	public String path;
	
	public String technology;
	
	@Override
	public void executeCommand() {
		File file = null;
		try {
			file = (File) EditorPlugin.getInstance().getFileAccessController().getFile(path);
		} catch (Exception e) {
			throw new RuntimeException(String.format("Error while getting resource %s", path), e);
		}
		if (file != null) {
			File project = (File)CodeSyncPlugin.getInstance().getProjectAccessController().getContainingProjectForFile(file);
			CodeSyncPlugin.getInstance().getCodeSyncAlgorithmRunner().runCodeSyncAlgorithm(project, file, technology, communicationChannel, true);
		}
	}

}