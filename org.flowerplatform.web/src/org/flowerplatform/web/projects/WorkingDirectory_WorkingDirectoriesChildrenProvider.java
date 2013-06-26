package org.flowerplatform.web.projects;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.IChildrenProvider;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.web.entity.WorkingDirectory;
import org.flowerplatform.web.projects.remote.ProjectsService;

public class WorkingDirectory_WorkingDirectoriesChildrenProvider implements IChildrenProvider {

	public static final String NODE_TYPE_WORKING_DIRECTORY = "workingDirectory";

	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {
		// the file points to the org dir
		@SuppressWarnings("unchecked")
		File organizationFile = ((Pair<File, String>) node).a;

		List<WorkingDirectory> list = ProjectsService.getInstance().getWorkingDirectoriesForOrganizationName(organizationFile.getName());
		Collection<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>(list.size());
		
		for (WorkingDirectory wd : list) {
			// we pass the File pointing to the working directory, and the WorkingDirectory
			Pair<File, WorkingDirectory> childToBeAdjusted = new Pair<File, WorkingDirectory>(new File(organizationFile, wd.getPathFromOrganization()), wd);
			Pair<Object, String> child = new Pair<Object, String>(childToBeAdjusted, NODE_TYPE_WORKING_DIRECTORY);
			result.add(child);
		}
		return result;		
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
		return true;
	}

}
