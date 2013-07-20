package org.flowerplatform.codesync.wiki;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.web.projects.remote.ProjectsService;

import com.crispico.flower.mp.codesync.base.ICodeSyncAlgorithmRunner;
import com.crispico.flower.mp.codesync.wiki.WikiPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;

/**
 * @author Mariana Gheorghe
 */
public class WikiSyncAlgorithmRunner implements ICodeSyncAlgorithmRunner {

	@Override
	public void runCodeSyncAlgorithm(IProject project, IResource resource, String technology, CommunicationChannel communicationChannel, boolean showDialog) {
		File projectFile = ProjectsService.getInstance().getFileFromProjectWrapperResource(project);
		String name = projectFile.getPath();
		// this will be a temporary tree, do not send the project
		CodeSyncRoot leftRoot = WikiPlugin.getInstance().getWikiTree(null, projectFile, name, technology);
		CodeSyncRoot rightRoot = WikiPlugin.getInstance().getWikiTree(project, null, name, technology);
		
		WikiPlugin.getInstance().updateTree(leftRoot, rightRoot, project, technology, communicationChannel, true);
	}

}
