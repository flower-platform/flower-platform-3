package org.flowerplatform.flexdiagram.samples.renderer {
	import spark.components.IconItemRenderer;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class SubModelIconItemRenderer extends IconItemRenderer {
		public function SubModelIconItemRenderer() {
			super();
			labelField = "name";
			iconFunction = getImage;
			minHeight = 0;
			setStyle("verticalAlign", "middle");
			cacheAsBitmap = true;
		}
		
		private function getImage(object:Object):Object {
			return String("http://wwwimages.adobe.com/include/style/default/SiteHeader/info.png");
		}
		
		override protected function drawBorder(unscaledWidth:Number, unscaledHeight:Number):void
		{
//			super.drawBorder(unscaledWidth, unscaledHeight);
		}
		
	}
}