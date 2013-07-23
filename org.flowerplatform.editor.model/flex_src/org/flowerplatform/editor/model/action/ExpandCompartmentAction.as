package org.flowerplatform.editor.model.action {
	
	import org.flowerplatform.editor.model.EditorModelPlugin;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.flexutil.popup.ActionBase;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class ExpandCompartmentAction extends ActionBase {
		
		public function ExpandCompartmentAction() {
			super();
			
			icon = EditorModelPlugin.getInstance().getResourceUrl("images/obj16/expandall.gif");
		}
		
		override public function get visible():Boolean {
			if (selection != null && selection.length == 1) {
				var node:Node = Node(selection.getItemAt(0));
				if (node.viewType == "class") {
					return true;
				}
			}
			return false;
		}
	}
}