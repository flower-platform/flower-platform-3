package org.flowerplatform.flexutil.form {
	
	import spark.skins.mobile.CalloutSkin;
	
	/**
	 * @author Mariana
	 */
	public class ErrorCalloutSkin extends CalloutSkin {
		
		public function ErrorCalloutSkin() {
			super();
			
			setStyle("backgroundColor", 0xFE0000);
			frameThickness = 5;
			arrowWidth = 30;
			arrowHeight = 10;
		}
		
	}
}