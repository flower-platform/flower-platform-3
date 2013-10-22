package org.flowerplatform.orion.server;

import java.io.File;

import org.eclipse.core.runtime.Path;
import org.flowerplatform.codesync.projects.IProjectsProvider;
import org.flowerplatform.common.CommonPlugin;

/**
 * @author Cristina Constantinescu
 */
public class OrionProjectsProvider implements IProjectsProvider {

	@Override
	public File getFile(File project, String path) {		
		return new File(project, path);
	}

	@Override
	public File getContainingProjectForFile(File file) {
		Path path = new Path(CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(file));
		return new File(CommonPlugin.getInstance().getWorkspaceRoot(), path.segment(0));		
	}

	@Override
	public String getPathRelativeToProject(File file) {		
		Path path = new Path(CommonPlugin.getInstance().getPathRelativeToWorkspaceRoot(file));
		File project = new File(CommonPlugin.getInstance().getWorkspaceRoot(), path.segment(0));
		return file.getAbsolutePath().substring(project.getParentFile().getAbsolutePath().length());		
	}
	
}
