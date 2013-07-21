package org.flowerplatform.flexdiagram.tool {
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.events.SoftKeyboardEvent;
	import flash.events.SoftKeyboardTrigger;
	import flash.ui.Keyboard;
	
	import mx.core.IDataRenderer;
	import mx.core.IVisualElement;
	import mx.events.FlexEvent;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.tool.controller.IInplaceEditorController;
	
	import spark.components.RichEditableText;
	import spark.core.IDisplayText;
	import spark.core.IEditableText;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class InplaceEditorTool extends Tool implements IWakeUpableTool {
		
		public static const ID:String = "InplaceEditorTool";
		
		public function InplaceEditorTool(diagramShell:DiagramShell) {
			super(diagramShell);
			
			WakeUpTool.wakeMeUpIfEventOccurs(diagramShell, this, WakeUpTool.MOUSE_DOWN, -1);
			WakeUpTool.wakeMeUpIfEventOccurs(diagramShell, this, WakeUpTool.MOUSE_UP);
		}
		
		public function wakeUp(eventType:String, initialEvent:MouseEvent):Boolean {
			if (eventType == WakeUpTool.MOUSE_DOWN) {
				context.wakedByMouseDownEvent = false;
				var renderer:IVisualElement = getRendererFromDisplayCoordinates();
				if (renderer is IDataRenderer && !(renderer is DiagramRenderer)) {
					var model:Object = IDataRenderer(renderer).data;
					if (diagramShell.getControllerProvider(model).getInplaceEditorController(model) != null) {
						var selected:Boolean = diagramShell.selectedItems.getItemIndex(model) != -1;
						if (!selected || (selected && diagramShell.selectedItems.length > 1)) {
							// if not selected or multiple selection
							return false;
						}
						context.wakedByMouseDownEvent = !initialEvent.ctrlKey && !initialEvent.shiftKey;
						return false;
					}
				}
			} else if (context.wakedByMouseDownEvent) {
				return true;
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
				if (inplaceEditorController.canActivate(context.model)) {
					inplaceEditorController.activate(context.model);
				}
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
			var renderer:IVisualElement = getRendererFromDisplayCoordinates(true);
			if (renderer == null || IDataRenderer(renderer).data != context.model) { // abort if click somewhere else
				if (diagramShell.getControllerProvider(context.model).
					getInplaceEditorController(context.model).canActivate(context.model)) {
					diagramShell.getControllerProvider(context.model).
						getInplaceEditorController(context.model).commit(context.model);
				}
			}
		}
		
		override public function reset():void {				
			delete context.wakedByMouseDownEvent;
		}
	}
	
}