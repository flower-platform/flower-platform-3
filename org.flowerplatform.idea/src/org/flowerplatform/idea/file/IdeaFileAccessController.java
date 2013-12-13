package org.flowerplatform.idea.file;

import java.io.File;
import java.io.FileNotFoundException;

import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.editor.file.PlainFileAccessController;

/**
 * @author Cristina Constantinescu
 */
public class IdeaFileAccessController extends PlainFileAccessController {

	@Override
	public String getPath(Object file) {		
		return CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot((File) file);
	}

	@Override
	public Object getFile(String path) throws FileNotFoundException {
		File file = new File(CommonPlugin.getInstance().getWorkspaceRoot(), path);
		if (!file.exists()) {
			throw new FileNotFoundException(path);
		}
		return file;
	}

}
