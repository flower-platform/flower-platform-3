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
		
		private var ctrlPressed:Boolean;
		private var shiftPressed:Boolean;
		
		public function SelectOnClickTool(diagramShell:DiagramShell) {
			super(diagramShell);
			
			WakeUpTool.wakeMeUpIfEventOccurs(this, "mouseClick");
		}
		
		public function wakeUp():Boolean {
			var renderer:IVisualElement = getRendererFromDisplayCoordinates();
			return (renderer is DiagramRenderer) || (renderer != null && !(renderer is DiagramRenderer));
		}
		
		override public function activateDozingMode():void {
//			diagramRenderer.addEventListener(MouseEvent.MOUSE_DOWN, mouseDownHandler);
			diagramRenderer.stage.addEventListener(KeyboardEvent.KEY_DOWN, keyDownHandler);
		}
		
		override public function deactivateDozingMode():void { 	
//			diagramRenderer.addEventListener(MouseEvent.MOUSE_DOWN, mouseDownHandler);
			diagramRenderer.stage.addEventListener(KeyboardEvent.KEY_DOWN, keyDownHandler);
		}
		
		override public function activateAsMainTool():void {			
			var renderer:IDataRenderer = IDataRenderer(getRendererFromDisplayCoordinates());
			if (renderer is DiagramRenderer) {
				diagramShell.selectedItems.removeAll();				
			} else {
				var model:Object = renderer.data;
				var selected:Boolean = diagramShell.selectedItems.getItemIndex(model) != -1;
				
				if (ctrlPressed) {
					if (selected) {
						diagramShell.selectedItems.removeItem(model);
					} else {
						diagramShell.selectedItems.addItem(model);
						diagramShell.mainSelectedItem = model;
					}				
				} else if (shiftPressed) {
					if (!selected) {
						diagramShell.selectedItems.addItem(model);
					}
					diagramShell.mainSelectedItem = model;
				} else if (!selected) {			
					diagramShell.selectedItems.addItem(model);
				}
			}
			diagramShell.mainToolFinishedItsJob();
		}
		
		override public function deactivateAsMainTool():void {		
			
		}
		
		private function keyDownHandler(event:KeyboardEvent):void {
			ctrlPressed = event.ctrlKey;
			shiftPressed = event.shiftKey;
		}
	}
	
}