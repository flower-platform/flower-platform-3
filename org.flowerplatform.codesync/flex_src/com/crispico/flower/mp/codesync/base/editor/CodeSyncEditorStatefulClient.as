package com.crispico.flower.mp.codesync.base.editor {
	import com.crispico.flower.flexdiagram.action.ActionContext;
	import com.crispico.flower.mp.codesync.base.communication.DiffTreeNode;
	
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.tree.remote.GenericTreeStatefulClient;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.remote.EditorStatefulClient;
	
	/**
	 * @author Mariana
	 */
	public class CodeSyncEditorStatefulClient extends EditorStatefulClient {
	
		public var diffTreeStatefulClient:Object = new Object();
		
		override public function getStatefulServiceId():String {
			return "CodeSyncEditorStatefulService";
		}
		
		[RemoteInvocation]
		public function updateNode(treeType:int, path:ArrayCollection, newNode:TreeNode, expandNode:Boolean = false, collapseNode:Boolean = false, selectNode:Boolean = false):void {			
			diffTreeStatefulClient[treeType].updateNode(path, newNode, collapseNode, selectNode);
		}
		
		///////////////////////////////////////////////////////////////
		// Wrappers for service methods
		///////////////////////////////////////////////////////////////
		
		public function openNode(node:IEventDispatcher, context:ActionContext):void {
			invokeServiceMethod("openNode", [node, context]); 
		}
		
		public function executeDiffAction(actionType:int, diffIndex:int, node:DiffTreeNode, context:ActionContext):void {
			invokeServiceMethod("executeDiffAction", [editableResourcePath, actionType, diffIndex, node, context]);
		}
		
		public function synchronize():void {
			invokeServiceMethod("synchronize", [editableResourcePath]);
		}
		
		public function applySelectedActions():void {
			invokeServiceMethod("applySelectedActions", [editableResourcePath, true]);
		}
		
		public function cancelSelectedActions():void {
			invokeServiceMethod("cancelSelectedActions", [editableResourcePath, true]);
		}
		
	}
}