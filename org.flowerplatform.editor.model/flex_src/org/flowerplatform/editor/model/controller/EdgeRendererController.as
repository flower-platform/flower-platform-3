package org.flowerplatform.editor.model.controller {
	import org.flowerplatform.emf_model.notation.Edge;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.renderer.ConnectionRendererController;
	import org.flowerplatform.flexdiagram.renderer.connection.ConnectionFigure;
	
	public class EdgeRendererController extends ConnectionRendererController {
		public function EdgeRendererController(diagramShell:DiagramShell) {
			super(diagramShell, ConnectionFigure);
		}
		
		override public function getSourceModel(connectionModel:Object):Object {
			return Edge(connectionModel).source_RH.referencedObject;
		}
		
		override public function getTargetModel(connectionModel:Object):Object {
			return Edge(connectionModel).target_RH.referencedObject;
		}
		
		
	}
}