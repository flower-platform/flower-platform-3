package org.flowerplatform.web.git.explorer;

import java.util.Collections;

import org.flowerplatform.communication.tree.IGenericTreeStatefulServiceAware;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.web.explorer.AbstractVirtualItemChildrenProvider;
import org.flowerplatform.web.git.GitNodeType;
import org.flowerplatform.web.git.GitPlugin;

/**
 * Parent node = Organization (i.e. File).<br/>
 * Child node is a virtual item, i.e. Pair<Org File, nodeType>.
 * 
 * @author Cristina Constantinescu
 */
public class GitRepositories_OrganizationChildrenProvider extends AbstractVirtualItemChildrenProvider implements IGenericTreeStatefulServiceAware {

	public GitRepositories_OrganizationChildrenProvider() {
		super();
		childNodeTypes = Collections.singletonList(GitNodeType.NODE_TYPE_GIT_REPOSITORIES);
	}
	
	@Override
	public void setGenericTreeStatefulService(GenericTreeStatefulService genericTreeStatefulService) {
		GitPlugin.getInstance().getTreeStatefulServicesDisplayingGitContent().add(genericTreeStatefulService);
	}

}
