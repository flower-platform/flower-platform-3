package org.flowerplatform.editor.model.action {
	
	import org.flowerplatform.editor.model.EditorModelPlugin;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.Node;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class RenameAction extends TextInputAction {
		public function RenameAction() {
			super();
			
			label = "Rename";
			icon = EditorModelPlugin.getInstance().getResourceUrl("images/rename.png");
		}
		
		override public function get visible():Boolean {
			if (selection != null && selection.length == 1) {
				var node:Node = Node(selection.getItemAt(0));
				if (node.viewType == "class") {
					return false;
				}
				return true;
			}
			return false;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var text:String = node.viewDetails.label;
			askForTextInput(text, "Rename", "Rename",
				function(name:String):void {
					NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).service_setInplaceEditorText(node.id, name);
				});
		}
	}
}