package org.flowerplatform.communication.tree;

import java.util.List;

import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;

public interface INodeDataProvider extends INodePopulator {
	
	PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context);
	
	String getLabelForLog(Object node, String nodeType);
	
	/**
	 * This method can safely use the {@link GenericTreeStatefulService#getVisibleNodes()}. 
	 */
	String getInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> fullPath);
	
	/**
	 * This method can safely use the {@link GenericTreeStatefulService#getVisibleNodes()}. 
	 */
	boolean setInplaceEditorText(StatefulServiceInvocationContext context, List<PathFragment> path, String text);
}
