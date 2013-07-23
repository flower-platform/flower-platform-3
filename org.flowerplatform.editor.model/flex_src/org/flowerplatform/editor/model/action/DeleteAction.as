package org.flowerplatform.editor.model.action {
	
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.common.WebCommonPlugin;

	/**
	 * @author Mariana Gheorghe
	 */
	public class DeleteAction extends ActionBase {
		
		public function DeleteAction() {
			super();
			
			label = "Delete";
			icon = WebCommonPlugin.getInstance().getResourceUrl("images/common/cancel_delete.png");
		}
		
		override public function get visible():Boolean {
			if (selection != null && selection.length == 1) {
				var node:Node = Node(selection.getItemAt(0));
				if (node.viewType == "classTitle") {
					return false;
				}
				return true;
			}
			return false;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).service_deleteView(node.id);
		}
	}
}