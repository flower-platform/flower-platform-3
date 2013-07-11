package com.crispico.flower.mp.codesync.base.action {
	
	import com.crispico.flower.flexdiagram.action.BaseAction;
	import com.crispico.flower.mp.codesync.base.DiffTree;
	import com.crispico.flower.mp.codesync.base.communication.DiffTreeNode;
	import com.crispico.flower.mp.codesync.base.editor.CodeSyncEditorStatefulClient;
	
	import mx.collections.ArrayCollection;
	
	public class DiffAction extends BaseAction {
		
		private var diffActionEntry:DiffActionEntry;
		
		public var tree:DiffTree;
		
		public function DiffAction(diffActionEntry:DiffActionEntry, tree:DiffTree) {
			this.diffActionEntry = diffActionEntry;
			this.tree = tree;
		}
		
		public override function run(selectedEditParts:ArrayCollection):void {
			var actionType:int = diffActionEntry != null ? diffActionEntry.actionType : -1;
			var diffIndex:int = diffActionEntry != null ? diffActionEntry.diffIndex : -1;
			var node:DiffTreeNode = DiffTreeNode(selectedEditParts[0]);
			tree.codeSyncEditorStatefulClient.executeDiffAction(actionType, diffIndex, node, tree.getContext());
		}
	}
}