package org.flowerplatform.communication.tree;

import java.util.List;

import org.flowerplatform.communication.tree.remote.PathFragment;

public interface INodeByPathRetriever {
	Object getNodeByPath(List<PathFragment> fullPath, GenericTreeContext context);
}
