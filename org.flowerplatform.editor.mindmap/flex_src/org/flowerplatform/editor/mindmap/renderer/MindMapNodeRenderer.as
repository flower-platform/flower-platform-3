package org.flowerplatform.editor.mindmap.renderer {
	
	import flash.events.Event;
	
	import mx.events.FlexEvent;
	import mx.events.PropertyChangeEvent;
	import mx.events.ResizeEvent;
	
	import org.flowerplatform.communication.transferable_object.ReferenceHolder;
	import org.flowerplatform.editor.model.EditorModelPlugin;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.Bounds;
	import org.flowerplatform.emf_model.notation.MindMapNode;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.emf_model.notation.View;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	import spark.components.IconItemRenderer;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapNodeRenderer extends IconItemRenderer {
		
		private static const circleRadius:int = 3;
		
		public function MindMapNodeRenderer() {
			super();
			iconFunction = getImage;
			minHeight = 10;
			minWidth = 10;
			setStyle("verticalAlign", "middle");
			cacheAsBitmap = true;
		}
		
		private function getImage(object:Object):Object {
			if (View(data).viewDetails) {
				var iconUrls:Array = View(data).viewDetails.iconUrls;
				return FlexUtilGlobals.getInstance().adjustImageBeforeDisplaying(EditorModelPlugin.getInstance().getComposedImageUrl(iconUrls));
			}
			return null;
		}
		
		protected function creationCompleteHandler(event:FlexEvent):void {	
			
		}
		
		override protected function setElementSize(element:Object, width:Number, height:Number):void {
			super.setElementSize(element, width, height);
			if (element == labelDisplay) {
				if (data.width != width) {
					data.width = width;
				}
				if (data.height != height) {
					data.height = height;
				}
			}
		}
				
		override protected function drawBorder(unscaledWidth:Number, unscaledHeight:Number):void {			
		}
		
		override protected function drawBackground(unscaledWidth:Number, unscaledHeight:Number):void {				
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {			
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			
			graphics.clear();
			graphics.lineStyle(1, 0x808080);
			graphics.beginFill(0xCCCCCC, 0);
			graphics.drawRoundRect(0, 0, unscaledWidth, unscaledHeight, 10, 10);		
			
			if (MindMapNode(data).hasChildren && data.expanded == false) {
				if (data.side == MindMapDiagramShell.LEFT) {
					graphics.drawCircle(-circleRadius, height/2, circleRadius);
				} else if (data.side == MindMapDiagramShell.RIGHT) {						
					graphics.drawCircle(width + circleRadius, height/2, circleRadius);
				}
			}
		}
		
		override public function set data(value:Object):void {
			if (super.data == value) {
				return;
			}
			if (super.data != null) {
				View(data).removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
				View(data).removeEventListener(DiagramEditorStatefulClient.VIEW_DETAILS_UPDATED_EVENT, viewDetailsUpdatedHandler);				
			}
			
			super.data = value;
			
			if (data != null) {
				View(data).addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
				View(data).addEventListener(DiagramEditorStatefulClient.VIEW_DETAILS_UPDATED_EVENT, viewDetailsUpdatedHandler);
								
				x = data.x;
				y = data.y;
				minWidth = data.width;
				minHeight = data.height;
				
				if (View(data).viewDetails != null) {
					viewDetailsUpdatedHandler(null);
				}
			}
		}
				
		protected function viewDetailsUpdatedHandler(event:Event):void {
			label = View(data).viewDetails.text;
			if (iconDisplay) {
				iconDisplay.source = getImage(null);
			}
			invalidateDisplayList();
		}
		
		private function modelChangedHandler(event:PropertyChangeEvent):void {			
			switch (event.property) {
				case "x":
					x = data.x;					
					break;
				case "y":
					y = data.y;
					break;
				case "height":
//					measuredHeight = data.height;
					break;
				case "width":
//					measuredWidth = data.width;
					break;				
			}				
			invalidateDisplayList();
		}
		
	}
}