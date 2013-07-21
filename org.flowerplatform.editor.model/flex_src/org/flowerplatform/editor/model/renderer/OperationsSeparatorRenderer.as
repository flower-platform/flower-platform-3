package org.flowerplatform.editor.model.renderer {
	import org.flowerplatform.editor.model.EditorModelPlugin;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class OperationsSeparatorRenderer extends SeparatorRenderer {
		
		public function OperationsSeparatorRenderer() {
			title = "operations";
			serviceMethod = "addNew_operation";
			newChildIcon = EditorModelPlugin.getInstance().getResourceUrl("images/obj16/SyncOperation.gif");
			newChildLabel = "+getAttr(x:String,y:String):int";
		}
	}
}