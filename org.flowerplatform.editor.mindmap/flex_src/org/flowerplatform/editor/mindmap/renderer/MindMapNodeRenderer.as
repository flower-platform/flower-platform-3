package org.flowerplatform.editor.mindmap.renderer {
	
	import flash.events.Event;
	
	import mx.events.FlexEvent;
	import mx.events.PropertyChangeEvent;
	import mx.events.ResizeEvent;
	
	import org.flowerplatform.communication.transferable_object.ReferenceHolder;
	import org.flowerplatform.editor.model.EditorModelPlugin;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.Bounds;
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
			minHeight = 0;
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
			addEventListener(ResizeEvent.RESIZE, resizeHandler);
			resizeHandler(null);				
		}
		
		protected function resizeHandler(event:ResizeEvent):void {				
			data.width = measuredWidth;
			data.height = measuredHeight;
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
			
			if (data.children.length > 0 && data.expanded == false) {
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
				
				var bounds:Bounds = Bounds(ReferenceHolder(Node(data).layoutConstraint_RH).referencedObject);
				bounds.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
				View(data).removeEventListener(DiagramEditorStatefulClient.VIEW_DETAILS_UPDATED_EVENT, viewDetailsUpdatedHandler);
			}
			
			super.data = value;
			
			if (data != null) {
				View(data).addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
				
				bounds = Bounds(ReferenceHolder(Node(data).layoutConstraint_RH).referencedObject);
				bounds.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
				View(data).addEventListener(DiagramEditorStatefulClient.VIEW_DETAILS_UPDATED_EVENT, viewDetailsUpdatedHandler);
				
				x = bounds.x;
				y = bounds.y;
				measuredWidth = bounds.width;
				measuredHeight = bounds.height;
				
				if (View(data).viewDetails != null) {
					viewDetailsUpdatedHandler(null);
				}
			}
		}
				
		protected function viewDetailsUpdatedHandler(event:Event):void {
			label = View(data).viewDetails.label;
			if (iconDisplay) {
				iconDisplay.source = getImage(null);
			}
			invalidateDisplayList();
		}
		
		private function modelChangedHandler(event:PropertyChangeEvent):void {
			if (event.target is Bounds) {
				var bounds:Bounds = Bounds(event.target);
				switch (event.property) {
					case "x":
						x = data.x;
						break;
					case "y":
						y = data.y;
						break;
					case "height":
						measuredHeight = bounds.height;
						break;
					case "width":
						measuredWidth = bounds.width;
						break;
					case "expanded":
						invalidateDisplayList();
						break;
				}
			} else {				
				invalidateDisplayList();				
			}			
		}
		
	}
}