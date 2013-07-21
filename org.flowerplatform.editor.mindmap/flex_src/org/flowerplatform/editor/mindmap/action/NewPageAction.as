package org.flowerplatform.editor.mindmap.action {
	import org.flowerplatform.editor.mindmap.MindMapModelPlugin;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class NewPageAction extends NewMindMapNodeAction {
		
		public function NewPageAction() {
			super();
			label = "New Page";
			icon = MindMapModelPlugin.getInstance().getResourceUrl("images/page.png");
			viewType = "page";
		}
	}
}