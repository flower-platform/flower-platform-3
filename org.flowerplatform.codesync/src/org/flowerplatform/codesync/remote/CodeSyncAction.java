package org.flowerplatform.codesync.remote;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.flowerplatform.communication.command.AbstractServerCommand;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.web.projects.remote.ProjectsService;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;

/**
 * @author Mariana
 */
public class CodeSyncAction extends AbstractServerCommand {

	public List<PathFragment> pathWithRoot;
	
	@Override
	public void executeCommand() {
		IResource resource = ProjectsService.getInstance().getProjectWrapperResourceFromFile(pathWithRoot);
		CodeSyncPlugin.getInstance().getCodeSyncAlgorithmRunner().runCodeSyncAlgorithm(resource.getProject(), resource, "java", communicationChannel);
	}

}
