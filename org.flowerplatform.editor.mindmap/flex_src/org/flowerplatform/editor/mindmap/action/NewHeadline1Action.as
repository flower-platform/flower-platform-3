package org.flowerplatform.editor.mindmap.action {
	import org.flowerplatform.editor.mindmap.MindMapModelPlugin;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class NewHeadline1Action extends NewMindMapNodeAction {
		
		public function NewHeadline1Action() {
			super();
			label = "New Heading 1";
			icon = MindMapModelPlugin.getInstance().getResourceUrl("images/heading_1.png");
			viewType = "heading 1";
		}
	}
}