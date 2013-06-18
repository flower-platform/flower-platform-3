package org.flowerplatform.flexdiagram.tool {
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.ui.Keyboard;
	
	import mx.core.IDataRenderer;
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.tool.controller.IInplaceEditorController;
	
	import spark.components.RichEditableText;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class InplaceEditorTool extends Tool implements IWakeUpableTool {
		
		public static const ID:String = "InplaceEditorTool";
		
		public function InplaceEditorTool(diagramShell:DiagramShell) {
			super(diagramShell);
			
			WakeUpTool.wakeMeUpIfEventOccurs(this, WakeUpTool.MOUSE_DOWN, -1);
		}
		
		public function wakeUp(eventType:String, ctrlPressed:Boolean, shiftPressed:Boolean):Boolean {
			var renderer:IVisualElement = getRendererFromDisplayCoordinates();
			if (renderer is IDataRenderer && !(renderer is DiagramRenderer)) {
				var model:Object = IDataRenderer(renderer).data;
				if (diagramShell.getControllerProvider(model).getInplaceEditorController(model) != null) {
					var selected:Boolean = diagramShell.selectedItems.getItemIndex(model) != -1;
					if (!selected || (selected && diagramShell.selectedItems.length > 1)) {
						// if not selected or multiple selection
						return false;
					}
					return !ctrlPressed && !shiftPressed;
				}
			}
			return false;
		}
		
		override public function activateDozingMode():void {
			diagramRenderer.stage.addEventListener(KeyboardEvent.KEY_DOWN, keyDownHandler);
		}
		
		override public function deactivateDozingMode():void {
			diagramRenderer.stage.removeEventListener(KeyboardEvent.KEY_DOWN, keyDownHandler);
		}
		
		override public function activateAsMainTool():void {
			context.model = IDataRenderer(getRendererFromDisplayCoordinates()).data;
			
			diagramRenderer.addEventListener(MouseEvent.CLICK, mouseClickHandler);
			
			var inplaceEditorController:IInplaceEditorController = diagramShell.getControllerProvider(context.model).getInplaceEditorController(context.model);
			if (inplaceEditorController != null) {
				inplaceEditorController.activate(context.model);
			}
		}
				
		override public function deactivateAsMainTool():void {
			var inplaceEditorController:IInplaceEditorController = diagramShell.getControllerProvider(context.model).getInplaceEditorController(context.model);
			if (inplaceEditorController != null) {
				inplaceEditorController.deactivate(context.model);
			}
			
			delete context.model;
			diagramRenderer.removeEventListener(MouseEvent.CLICK, mouseClickHandler);			
		}
		
		private function keyDownHandler(event:KeyboardEvent):void {
			switch (event.keyCode) {
				case Keyboard.F2: // active tool
					diagramShell.mainTool = this;
					break;
				case Keyboard.ENTER: // commit value
					if (this == diagramShell.mainTool) {
						diagramShell.getControllerProvider(context.model).
							getInplaceEditorController(context.model).commit(context.model);
					}					
					break;
				case Keyboard.ESCAPE: // abort
					if (this == diagramShell.mainTool) {
						diagramShell.getControllerProvider(context.model).
							getInplaceEditorController(context.model).abort(context.model);
					}
					break;
			}
		}
		
		private function mouseClickHandler(event:MouseEvent):void {			
			if (!(event.target is RichEditableText)) { // abort if click somewhere else
				diagramShell.getControllerProvider(context.model).
					getInplaceEditorController(context.model).abort(context.model);
			}
		}
	}
	
}