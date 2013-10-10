package org.flowerplatform.orion.server;

import java.io.File;

import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.editor.file.FileAccessController;

/**
 * @author Cristina Constantinescu
 */
public class OrionFileAccessController extends FileAccessController {

	@Override
	public String getPath(Object file) {		
		return CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot((File) file);
	}

	@Override
	public Object getFile(String path) {
		// TODO: implement
		return null;
	}

}
