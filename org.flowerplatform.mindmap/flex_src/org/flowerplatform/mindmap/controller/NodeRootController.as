package org.flowerplatform.mindmap.controller {
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapRootController;
	import org.flowerplatform.mindmap.Diagram;
	import org.flowerplatform.mindmap.MindMapEditorDiagramShell;
	
	public class NodeRootController extends ControllerBase implements IMindMapRootController {
		
		public function NodeRootController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function getMindMapRoot():Object {
			return Diagram(diagramShell.rootModel).rootNode;
		}
		
	}
}