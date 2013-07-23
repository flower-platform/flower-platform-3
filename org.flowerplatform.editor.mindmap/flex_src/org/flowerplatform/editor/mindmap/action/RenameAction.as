package org.flowerplatform.editor.mindmap.action {
	import org.flowerplatform.editor.mindmap.NotationMindMapDiagramShell;
	import org.flowerplatform.editor.mindmap.remote.MindMapDiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.EditorModelPlugin;
	import org.flowerplatform.editor.model.action.TextInputAction;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.MindMapNode;
	import org.flowerplatform.emf_model.notation.Node;
	
	public class RenameAction extends TextInputAction {
		public function RenameAction() {
			super();
			
			label = "Rename";
			icon = EditorModelPlugin.getInstance().getResourceUrl("images/rename.png");			
		}
		
		override public function get visible():Boolean {
			if (selection != null && selection.length == 1) {
				var node:MindMapNode = MindMapNode(selection.getItemAt(0));
				return node.side != 0;
			}
			return false;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var text:String = node.viewDetails.text;
			askForTextInput(text, "Rename", "Rename",
				function(name:String):void {
					MindMapDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).service_setText(node.id, name);
				});
		}
	}
}