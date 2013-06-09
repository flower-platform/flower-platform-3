package org.flowerplatform.flexdiagram.tool {
	
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.ui.Keyboard;
	
	import mx.core.Application;
	import mx.core.FlexGlobals;
	import mx.core.IDataRenderer;
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	
	import spark.components.Application;
	
	public class SelectOnClickTool extends Tool implements IWakeUpableTool {
		
		public function SelectOnClickTool(diagramShell:DiagramShell) {
			super(diagramShell);
			
			WakeUpTool.wakeMeUpIfEventOccurs(this, WakeUpTool.MOUSE_CLICK);			
		}
		
		public function wakeUp(eventType:String):Boolean {
			var renderer:IVisualElement = getRendererFromDisplayCoordinates();					
			return renderer != null;
		}
		
		override public function activateDozingMode():void {
			diagramRenderer.stage.addEventListener(KeyboardEvent.KEY_DOWN, keyDownHandler);
			diagramRenderer.stage.addEventListener(KeyboardEvent.KEY_UP, keyUpHandler);
		}
		
		override public function deactivateDozingMode():void {
			diagramRenderer.stage.removeEventListener(KeyboardEvent.KEY_DOWN, keyDownHandler);
			diagramRenderer.stage.removeEventListener(KeyboardEvent.KEY_UP, keyUpHandler);
		}
		
		override public function activateAsMainTool():void {			
			var renderer:IDataRenderer = IDataRenderer(getRendererFromDisplayCoordinates());
			if (renderer is DiagramRenderer) {
				diagramShell.selectedItems.removeAll();
			} else {
				var model:Object = renderer.data;
				var selected:Boolean = diagramShell.selectedItems.getItemIndex(model) != -1;
				
				if (context.ctrlPressed) {
					if (selected) {
						diagramShell.selectedItems.removeItem(model);
					} else {
						diagramShell.selectedItems.addItem(model);
						diagramShell.mainSelectedItem = model;
					}				
				} else if (context.shiftPressed) {
					if (!selected) {
						diagramShell.selectedItems.addItem(model);
					}
					diagramShell.mainSelectedItem = model;
				} else {
					diagramShell.selectedItems.removeAll();
					diagramShell.selectedItems.addItem(model);										
				}
			}
			diagramShell.mainToolFinishedItsJob();
		}
		
		override public function deactivateAsMainTool():void {			
		}
		
		private function keyDownHandler(event:KeyboardEvent):void {
			context.ctrlPressed = event.ctrlKey;
			context.shiftPressed = event.shiftKey;
		}
		
		private function keyUpHandler(event:KeyboardEvent):void {
			context.ctrlPressed = event.ctrlKey;
			context.shiftPressed = event.shiftKey;
		}
	}
	
}