package org.flowerplatform.editor.mindmap.action {
	import org.flowerplatform.editor.mindmap.MindMapModelPlugin;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class NewParagraphAction extends NewMindMapNodeAction {
		
		public function NewParagraphAction() {
			super();
			label = "New Paragraph";
			icon = MindMapModelPlugin.getInstance().getResourceUrl("images/paragraph.png");
			viewType = "paragraph";
		}
		
	}
}