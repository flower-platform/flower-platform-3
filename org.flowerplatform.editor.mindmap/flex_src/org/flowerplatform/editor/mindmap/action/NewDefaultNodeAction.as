package org.flowerplatform.editor.mindmap.action {
	import org.flowerplatform.editor.mindmap.MindMapModelPlugin;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NewDefaultNodeAction extends NewMindMapNodeAction {
		public function NewDefaultNodeAction() {
			super();
			label = "New Node";
			viewType = "default";
		}
	}
}