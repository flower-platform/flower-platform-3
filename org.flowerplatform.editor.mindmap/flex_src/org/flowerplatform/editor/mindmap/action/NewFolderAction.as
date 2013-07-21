package org.flowerplatform.editor.mindmap.action {
	import mx.collections.ArrayList;
	
	import org.flowerplatform.editor.mindmap.MindMapModelPlugin;
	import org.flowerplatform.editor.mindmap.remote.MindMapDiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.MindMapNode;

	/**
	 * @author Cristina Constantinescu
	 */ 
	public class NewFolderAction extends NewMindMapNodeAction {
		
		public function NewFolderAction() {
			super();
			label = "New Folder";
			icon = MindMapModelPlugin.getInstance().getResourceUrl("images/folder.gif");
			viewType = "folder";
		}
		
	}
}