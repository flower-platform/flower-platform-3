/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
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
				if (url) {
					navigateToURL(new URLRequest(url), "_blank");
				}
			});
			
			super.data = value;
		}
	}
}