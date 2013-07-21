package org.flowerplatform.editor.mindmap.action {
	import org.flowerplatform.editor.mindmap.MindMapModelPlugin;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class NewHeadline3Action extends NewMindMapNodeAction {
		
		public function NewHeadline3Action() {
			super();
			label = "New Heading 3";
			icon = MindMapModelPlugin.getInstance().getResourceUrl("images/heading_3.png");
			viewType = "heading 3";
		}
	}
}