package org.flowerplatform.flexdiagram.renderer {
	import flash.display.DisplayObject;
	import flash.events.FocusEvent;
	import flash.geom.Rectangle;
	
	import mx.core.UIComponent;
	import mx.managers.IFocusManagerComponent;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.visual_children.IVisualChildrenController;
	import org.flowerplatform.flexdiagram.util.infinitegroup.InfiniteDataRenderer;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class DiagramRenderer extends InfiniteDataRenderer implements IDiagramShellAware, IVisualChildrenRefreshable, IAbsoluteLayoutRenderer, IFocusManagerComponent {

		private var _diagramShell:DiagramShell;
		protected var visualChildrenController:IVisualChildrenController;
		private var _shouldRefreshVisualChildren:Boolean;
		private var _noNeedToRefreshRect:Rectangle;
		
		public var viewPortRectOffsetTowardOutside:int = 0;
								
		public function get diagramShell():DiagramShell {
			return _diagramShell;
		}
		
		public function set diagramShell(value:DiagramShell):void {
			_diagramShell = value;
		}
		
		public function get shouldRefreshVisualChildren():Boolean {
			return _shouldRefreshVisualChildren;
		}
		
		public function set shouldRefreshVisualChildren(value:Boolean):void {
			_shouldRefreshVisualChildren = value;
		}
		
		public function get noNeedToRefreshRect():Rectangle {
			return _noNeedToRefreshRect;
		}
		
		public function set noNeedToRefreshRect(value:Rectangle):void {
			_noNeedToRefreshRect = value; 
		}

		override public function set data(value:Object):void {
			super.data = value;
			if (data == null) {
				visualChildrenController = null;
			} else {
				visualChildrenController = diagramShell.getControllerProvider(data).getVisualChildrenController(data);
			}
		}
		
		public function getViewportRect():Rectangle {
			return new Rectangle(horizontalScrollPosition - viewPortRectOffsetTowardOutside, verticalScrollPosition - viewPortRectOffsetTowardOutside, width + 2 * viewPortRectOffsetTowardOutside, height + 2 * viewPortRectOffsetTowardOutside);
		}
		
		public function setContentRect(rect:Rectangle):void {
			
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			if (visualChildrenController != null) {
				visualChildrenController.refreshVisualChildren(data);
			}
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			
			graphics.clear();
			graphics.lineStyle(1);
			graphics.beginFill(0xCCCCCC, 0);
			graphics.drawRect(horizontalScrollPosition, verticalScrollPosition, width, height);
		}
		
		override protected function focusInHandler(event:FocusEvent):void {
			super.focusInHandler(event);
			
			diagramShell.activateTools();			
		}
		
		override protected function focusOutHandler(event:FocusEvent):void {
			super.focusOutHandler(event);
			
			var parent:DisplayObject;
			if (!getBounds(stage).contains(stage.mouseX, stage.mouseY)) { // if outside diagram area
				diagramShell.deactivateTools();	
			}							
		}	
				
	}
}