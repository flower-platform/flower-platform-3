package org.flowerplatform.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.editor.EditorPlugin;
import org.flowerplatform.editor.file.PlainFileAccessController;

/**
 * @author Cristina Constantinescu
 * @author Sebastian Solomon
 */
public class WebFileAccessController extends PlainFileAccessController {

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

	@Override
	public boolean isDirectory(Object file) {
		// TODO Auto-generated method stub
		return EditorPlugin.getInstance().getFileAccessController()
				.isDirectory(file);
	}

	@Override
	public Object getParentFile(Object file) {
		return ((File) file).getParentFile();
	}

	@Override
	public boolean createNewFile(Object file) {
		try {
			return ((File) file).createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Object createNewFile(Object file, String name) {
		return new File((File) file, name);
	}

	@Override
	public boolean exists(Object file) {
		return ((File) file).exists();
	}

}
