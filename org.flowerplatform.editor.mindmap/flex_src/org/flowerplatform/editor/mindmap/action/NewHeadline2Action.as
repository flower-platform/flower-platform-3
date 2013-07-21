package org.flowerplatform.editor.mindmap.action {
	import org.flowerplatform.editor.mindmap.MindMapModelPlugin;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class NewHeadline2Action extends NewMindMapNodeAction {
		
		public function NewHeadline2Action() {
			super();
			label = "New Heading 2";
			icon = MindMapModelPlugin.getInstance().getResourceUrl("images/heading_2.png");
			viewType = "heading 2";
		}
	}
}