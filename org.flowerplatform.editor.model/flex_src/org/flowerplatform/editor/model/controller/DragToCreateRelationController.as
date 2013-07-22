package org.flowerplatform.editor.model.controller {

	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.tool.controller.IDragToCreateRelationController;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class DragToCreateRelationController extends ControllerBase implements IDragToCreateRelationController {
	
		public function DragToCreateRelationController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function activate(model:Object):void {
			trace ("activate " + model);
		}
		
		public function drag(model:Object):void {
			trace("update");
		}
		
		public function drop(model:Object):void {
			trace("drop " + model);
			NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE)
				.service_addNewConnection(diagramShell.mainTool.context.model.id, model.id);
			
			diagramShell.mainToolFinishedItsJob();
		}
		
		public function deactivate(model:Object):void {
			trace("deactivate");
		}
	}
}