package org.flowerplatform.communication.tree;

public interface IChildNodeAdjusterBeforeProcessing {
	Object adjustChild(Object originalChild, String nodeType, NodeInfo nodeInfo);
}
