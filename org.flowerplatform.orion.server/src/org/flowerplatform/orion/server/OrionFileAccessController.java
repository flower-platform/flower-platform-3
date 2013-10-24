package org.flowerplatform.orion.server;

import java.io.File;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.orion.internal.server.servlets.file.NewFileServlet;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.editor.EditorPlugin;
import org.flowerplatform.editor.file.FileAccessController;

/**
 * @author Cristina Constantinescu
 */
public class OrionFileAccessController extends FileAccessController {

	@Override
	public String getPath(Object file) {		
		String decodedPath = EditorPlugin.getInstance().getFriendlyNameDecoded((String) file);
	
		return CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot((File) getFile(decodedPath));
	}

	@Override
	public Object getFile(String path) {		
		IPath iPath = new Path(path).removeFirstSegments(1);			
		
		@SuppressWarnings("restriction")
		IFileStore fileStore = NewFileServlet.getFileStore(null, iPath);
		try {
			return fileStore.toLocalFile(EFS.NONE, null);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}

}
