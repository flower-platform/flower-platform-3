package org.flowerplatform.flexdiagram.tool {
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.ui.Keyboard;
	
	import mx.core.IDataRenderer;
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class InplaceEditorTool extends Tool implements IWakeUpableTool {
		
		public static const ID:String = "InplaceEditorTool";
		
		public function InplaceEditorTool(diagramShell:DiagramShell) {
			super(diagramShell);
			
			WakeUpTool.wakeMeUpIfEventOccurs(this, WakeUpTool.MOUSE_UP, -1);
		}
		
		public function wakeUp(eventType:String, ctrlPressed:Boolean, shiftPressed:Boolean):Boolean {
			var renderer:IVisualElement = getRendererFromDisplayCoordinates();
			if (renderer is IDataRenderer && !(renderer is DiagramRenderer)) {
				var model:Object = IDataRenderer(renderer).data;
				if (diagramShell.getControllerProvider(model).getInplaceEditorController(model) != null) {
					if (diagramShell.selectedItems.getItemIndex(model) == -1) {
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
			context = new Object();
			context.model = IDataRenderer(getRendererFromDisplayCoordinates()).data;
			
			diagramRenderer.addEventListener(MouseEvent.CLICK, mouseClickHandler);
			
			diagramShell.getControllerProvider(context.model).
				getInplaceEditorController(context.model).activate(context.model);
		}
				
		override public function deactivateAsMainTool():void {
			diagramShell.getControllerProvider(context.model).
				getInplaceEditorController(context.model).deactivate(context.model);
			
			context = null;
			diagramRenderer.removeEventListener(MouseEvent.CLICK, mouseClickHandler);			
		}
		
		private function keyDownHandler(event:KeyboardEvent):void {
			switch (event.keyCode) {
				case Keyboard.F2:
					diagramShell.mainTool = this;
					break;
				case Keyboard.ENTER:
					if (this == diagramShell.mainTool) {
						diagramShell.getControllerProvider(context.model).
							getInplaceEditorController(context.model).commit(context.model);
					}					
					break;
				case Keyboard.ESCAPE:
					if (this == diagramShell.mainTool) {
						diagramShell.getControllerProvider(context.model).
							getInplaceEditorController(context.model).abort(context.model);
					}
					break;
			}
		}
		
		private function mouseClickHandler(event:MouseEvent):void {			
			if (getRendererFromDisplay(event.target) != diagramShell.getRendererForModel(context.model)) {
				diagramShell.getControllerProvider(context.model).
					getInplaceEditorController(context.model).abort(context.model);
			}
		}
	}
	
}