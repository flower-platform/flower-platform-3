package org.flowerplatform.editor.mindmap {
	
	import org.flowerplatform.editor.model.DiagramEditorFrontend;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.flexdiagram.DiagramShell;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapDiagramEditorFrontend extends DiagramEditorFrontend {
				
		override protected function getDiagramShellInstance():DiagramShell {
			return new NotationMindMapDiagramShell();			
		}
		
	}
}