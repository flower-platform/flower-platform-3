package org.flowerplatform.editor.model.action {
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.flexutil.popup.ActionBase;
	
	public class DragOnDiagramAction extends ActionBase {
		public function DragOnDiagramAction() {
			// TODO CS/FP2 msg
			label = "Add to Diagram";
			preferShowOnActionBar = true;
			super();
		}
		
		override public function get visible():Boolean {
			return DiagramEditorStatefulClient.TEMP_INSTANCE != null;
		}
		
		override public function run():void {
			var pathsWithRoot:ArrayList = new ArrayList();
			for (var i:int = 0; i < selection.length; i++) {
				var treeNode:TreeNode = TreeNode(selection.getItemAt(i));
				var path:ArrayCollection = treeNode.getPathForNode(true);
				pathsWithRoot.addItem(path);
			}
			NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).service_handleDragOnDiagram(pathsWithRoot);
		}
		
	}
}