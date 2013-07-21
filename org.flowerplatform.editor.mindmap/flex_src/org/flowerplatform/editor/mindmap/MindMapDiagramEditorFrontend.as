package org.flowerplatform.editor.mindmap {
	
	import mx.collections.IList;
	
	import org.flowerplatform.editor.mindmap.remote.MindMapDiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.DiagramEditorFrontend;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexutil.popup.IAction;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapDiagramEditorFrontend extends DiagramEditorFrontend {
				
		override protected function getDiagramShellInstance():DiagramShell {
			var diagram:NotationMindMapDiagramShell = new NotationMindMapDiagramShell();
			diagram.editorStatefulClient = MindMapDiagramEditorStatefulClient(editorStatefulClient);
			return diagram;			
		}
		
		override public function getActions(selection:IList):Vector.<IAction> {
			var result:Vector.<IAction> = new Vector.<IAction>();
			
			var actions:Vector.<IAction> = MindMapModelPlugin.getInstance().mindmapDiagramClassFactoryActionProvider.getActions(selection);
			if (actions != null) {
				for each (var action:IAction in actions) {
					result.push(action);
				}
			}		
			return result;
		}		
		
	}
}