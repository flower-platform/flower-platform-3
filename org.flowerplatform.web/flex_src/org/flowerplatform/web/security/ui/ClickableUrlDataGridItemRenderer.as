package org.flowerplatform.web.security.ui {
	
	import flash.events.MouseEvent;
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	
	import mx.controls.LinkButton;
	
	/**
	 * Custom data grid item renderer that displays a hyperlink. To use it,
	 * set the <code>urlField</code> to the property that contains the URL.
	 * 
	 * @author Mariana
	 */ 
	public class ClickableUrlDataGridItemRenderer extends LinkButton {
		
		public var urlField:String;
		
		override public function set data(value:Object):void {
			var url:String = value[urlField];
			label = url;
			setStyle("paddingLeft", 5);
			setStyle("textDecoration", "underline");
			
			addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {
				navigateToURL(new URLRequest(url), "_blank");
			});
			
			super.data = value;
		}
	}
}