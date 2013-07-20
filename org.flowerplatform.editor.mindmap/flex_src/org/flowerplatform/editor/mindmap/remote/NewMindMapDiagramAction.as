package org.flowerplatform.editor.mindmap.remote {
	
	import org.flowerplatform.editor.model.remote.NewDiagramAction;
	
	/**
	 * @author Mariana Gheorghe
	 */
	[RemoteClass(alias="org.flowerplatform.editor.mindmap.remote.NewMindMapDiagramAction")]
	public class NewMindMapDiagramAction extends NewDiagramAction {
		
		public function NewMindMapDiagramAction() {
			super();
			
			label = "New MindMap";
			name = "NewMindmap.mindmap";
		}
	}
}