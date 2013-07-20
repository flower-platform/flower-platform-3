package org.flowerplatform.editor.mindmap {
	
	import org.flowerplatform.editor.mindmap.remote.MindMapDiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.DiagramEditorFrontend;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.flexdiagram.DiagramShell;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapDiagramEditorFrontend extends DiagramEditorFrontend {
				
		override protected function getDiagramShellInstance():DiagramShell {
			var diagram:NotationMindMapDiagramShell = new NotationMindMapDiagramShell();
			diagram.editorStatefulClient = MindMapDiagramEditorStatefulClient(editorStatefulClient);
			return diagram;			
		}
		
	}
}