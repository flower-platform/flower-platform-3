package org.flowerplatform.editor.model.renderer {
	import org.flowerplatform.editor.model.EditorModelPlugin;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class AttributesSeparatorRenderer extends SeparatorRenderer {
		
		public function AttributesSeparatorRenderer() {
			title = "attributes";
			serviceMethod = "addNew_attribute";
			newChildIcon = EditorModelPlugin.getInstance().getResourceUrl("images/obj16/SyncProperty.gif");
			newChildLabel = "+attr1:int";
		}
	}
}