package com.crispico.flower.mp.codesync.base.communication
{
	import com.crispico.flower.mp.codesync.base.editor.CodeSyncEditorStatefulClient;
	
	import org.flowerplatform.communication.tree.remote.GenericTreeStatefulClient;
	
	/**
	 * @author Mariana
	 */
	public class DiffTreeStatefulClient extends GenericTreeStatefulClient {
		
		private var codeSyncEditorStatefulClient:CodeSyncEditorStatefulClient;
		
		public function executeDiffAction(actionType:int, diffIndex:int, node:DiffTreeNode):void {
			codeSyncEditorStatefulClient.executeDiffAction(actionType, diffIndex, node);
		}
		
	}
}