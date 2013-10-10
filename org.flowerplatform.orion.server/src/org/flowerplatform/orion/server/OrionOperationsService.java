package org.flowerplatform.orion.server;

import java.io.File;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.orion.internal.server.servlets.file.NewFileServlet;
import org.eclipse.orion.server.core.OrionConfiguration;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.editor.remote.EditorOperationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrionOperationsService extends EditorOperationsService {

	private static final Logger logger = LoggerFactory.getLogger(OrionOperationsService.class);

	@Override
	protected String getResourcePathFromLink(String path) {		
		try {
			IPath iPath = new Path(path).removeFirstSegments(1);			
			IFileStore fileStore = NewFileServlet.getFileStore(null, iPath);
			File file = fileStore.toLocalFile(EFS.NONE, null);
			return CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(file);
		} catch (CoreException e) {
			return null;
		}
		
	}
	
}
