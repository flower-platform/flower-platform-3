package org.flowerplatform.editor.model.renderer {
	import flash.events.Event;
	
	import org.flowerplatform.editor.model.EditorModelPlugin;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.View;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.web.common.WebCommonPlugin;
	
	import spark.components.IconItemRenderer;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class BoxChildIconItemRenderer extends IconItemRenderer {
		public function BoxChildIconItemRenderer() {
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
		
		override protected function drawBorder(unscaledWidth:Number, unscaledHeight:Number):void
		{
//			super.drawBorder(unscaledWidth, unscaledHeight);
		}
		
		override public function set data(value:Object):void {
			if (data != null) {
				View(data).removeEventListener(DiagramEditorStatefulClient.VIEW_DETAILS_UPDATED_EVENT, viewDetailsUpdatedHandler);
			}
			super.data = value;
			if (data != null) {
				View(data).addEventListener(DiagramEditorStatefulClient.VIEW_DETAILS_UPDATED_EVENT, viewDetailsUpdatedHandler);
				
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
		}
	}
}