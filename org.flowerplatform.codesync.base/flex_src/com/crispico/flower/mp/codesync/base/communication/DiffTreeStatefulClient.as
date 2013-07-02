package com.crispico.flower.mp.codesync.base.communication
{
	import org.flowerplatform.communication.tree.remote.GenericTreeStatefulClient;
	
	
	/**
	 * @author Mariana
	 */
	public class DiffTreeStatefulClient extends GenericTreeStatefulClient {
		
		public function executeDiffAction(actionType:int, diffIndex:int, node:DiffTreeNode):void {
//			invokeServiceMethod("executeDiffAction", [actionType, diffIndex, node, genericTree.context]);
		}
		
	}
}