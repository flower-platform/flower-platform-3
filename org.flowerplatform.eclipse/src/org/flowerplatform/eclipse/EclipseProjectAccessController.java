package org.flowerplatform.eclipse;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.flowerplatform.codesync.projects.IProjectAccessController;

/**
 * @author Mariana Gheorghe
 * @author Sebastian Solomon
 */
public class EclipseProjectAccessController implements IProjectAccessController {

	@Override
	public Object getFile(Object project, String path) {
		return ((IProject)project).getFile(path);
	}
	
	@Override
	public Object getFolder(Object project, String path) {
		return ((IProject)project).getFolder(path);
	}

	@Override
	public Object getContainingProjectForFile(Object file) {
		if (file == null){
			return "";
		}
		return ((IResource)file).getProject();
	}

	@Override
	public String getPathRelativeToProject(Object file) {
		if (file == null){
			return "";
		}
		return ((IResource)file).getProjectRelativePath().toOSString();
	}

}
