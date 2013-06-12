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
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class SelectOnClickTool extends Tool implements IWakeUpableTool {
		
		public static const ID:String = "SelectOnClickTool";
		
		public function SelectOnClickTool(diagramShell:DiagramShell) {
			super(diagramShell);
			
			WakeUpTool.wakeMeUpIfEventOccurs(this, WakeUpTool.MOUSE_UP);	
		}
		
		public function wakeUp(eventType:String, ctrlPressed:Boolean, shiftPressed:Boolean):Boolean {
			context.ctrlPressed = ctrlPressed;
			context.shiftPressed = shiftPressed;
			var renderer:IVisualElement = getRendererFromDisplayCoordinates();	
						
			var model:Object;
			if (renderer is DiagramRenderer) {
				return true;
			}
			if (renderer is IDataRenderer) {
				model = IDataRenderer(renderer).data;
				if (diagramShell.getControllerProvider(model).getSelectionController(model) != null) {	
					if ((diagramShell.selectedItems.getItemIndex(model) != -1) && diagramShell.selectedItems.length > 1) {
						return true;
					}
					return (diagramShell.selectedItems.getItemIndex(model) == -1) || ctrlPressed || shiftPressed;
				}				
			}			
			return false;
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
			delete context.ctrlPressed;
			delete context.shiftPressed;		
		}
		
	}	
}