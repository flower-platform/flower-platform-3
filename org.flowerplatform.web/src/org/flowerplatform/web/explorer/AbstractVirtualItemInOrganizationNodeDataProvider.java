package org.flowerplatform.web.explorer;

import java.io.File;
import java.util.List;

import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.INodeByPathRetriever;
import org.flowerplatform.communication.tree.remote.PathFragment;

/**
 * @author Cristian Spiescu
 */
public abstract class AbstractVirtualItemInOrganizationNodeDataProvider extends AbstractVirtualItemNodeDataProvider implements INodeByPathRetriever {

	@Override
	public Object getNodeByPath(List<PathFragment> fullPath, GenericTreeContext context) {
		if (fullPath == null || fullPath.size() != 2 || !Organization_RootChildrenProvider.NODE_TYPE_ORGANIZATION.equals(fullPath.get(0).getType())) {
			throw new IllegalArgumentException("We were expecting a path with 2 items (no 0 being an org), but we got: " + fullPath);
		}
		return new Pair<File, String>(new File(CommonPlugin.getInstance().getWorkspaceRoot(), fullPath.get(0).getName()), fullPath.get(1).getType());
	}

}
