package org.flowerplatform.flexdiagram.tool {
	import flash.display.DisplayObject;
	import flash.display.Stage;
	import flash.events.KeyboardEvent;
	import flash.geom.Point;
	import flash.ui.Keyboard;
	
	import mx.core.IDataRenderer;
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	
	import spark.components.Scroller;
	
	[Bindable]
	
	/**
	 * @author Cristina Constantinescu
	 */	
	public class Tool {
		
		protected var diagramShell:DiagramShell;
		
		public var context:Object = new Object();
				
		public function Tool(diagramShell:DiagramShell) {
			this.diagramShell = diagramShell;
		}		
					
		public function activateDozingMode():void {				
		}
		
		public function deactivateDozingMode():void { 				
		}
		
		public function activateAsMainTool():void {			
		}
		
		public function deactivateAsMainTool():void {			
		}
		
		public function get diagramRenderer():DiagramRenderer {
			return DiagramRenderer(diagramShell.diagramRenderer);
		}
			
		protected function getRendererFromDisplayCoordinates():IVisualElement {
			var stage:Stage = DisplayObject(diagramShell.diagramRenderer).stage;
			var arr:Array = stage.getObjectsUnderPoint(new Point(stage.mouseX, stage.mouseY));
						
			var renderer:IVisualElement;
			var i:int;
			for (i = arr.length - 1; i >= 0;  i--) {
				renderer = getRendererFromDisplay(arr[i]);
				if (renderer != null) {					
					return renderer;
				}
			}
			return null;
		}
		
		protected function getRendererFromDisplay(obj:Object):IVisualElement {			
			// in order for us to traverse its hierrarchy
			// it has to be a DisplayObject
			if (!(obj is DisplayObject)) {
				return null;
			}
			
			// traverse all the obj's hierarchy	
			while (obj != null) {
				if (obj is DiagramRenderer) {
					return IVisualElement(obj);
				}
				if (obj is IDataRenderer && diagramShell.modelToExtraInfoMap[IDataRenderer(obj).data] != null) {
					// found it
					return IVisualElement(obj);					
				}
				obj = DisplayObject(obj).parent;
			}
			
			// no found on the obj's hierarchy
			return null;
		}
		
		protected function globalToDiagram(x:Number, y:Number):Point { 
			var localPoint:Point = diagramRenderer.globalToLocal(new Point(x, y));
			localPoint = diagramRenderer.localToContent(localPoint);
			return localPoint;
		}	
	}	
	
}