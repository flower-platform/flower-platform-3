package org.flowerplatform.editor.model.controller {
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.transferable_object.TransferableObject;
	import org.flowerplatform.editor.model.NotationDiagramShell;
	import org.flowerplatform.editor.model.remote.command.MoveResizeServerCommand;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.tool.controller.drag.AbsoluteLayoutChildPlaceHolderDragController;
	import org.flowerplatform.flexdiagram.ui.MoveResizePlaceHolder;
	
	public class AbsoluteNodePlaceHolderDragController extends AbsoluteLayoutChildPlaceHolderDragController {
		public function AbsoluteNodePlaceHolderDragController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		override public function drop(model:Object):void {
			var movePlaceHolder:MoveResizePlaceHolder = diagramShell.modelToExtraInfoMap[model].movePlaceHolder;
			var command:MoveResizeServerCommand = new MoveResizeServerCommand();
			command.id = Node(model).layoutConstraint_RH.referenceIdAsString;
			command.newX = movePlaceHolder.x;
			command.newY = movePlaceHolder.y;
			NotationDiagramShell(diagramShell).editorStatefulClient.attemptUpdateContent(null, command);
		}
		
	}
}