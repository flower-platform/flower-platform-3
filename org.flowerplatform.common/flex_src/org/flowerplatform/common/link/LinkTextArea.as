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
package org.flowerplatform.common.link {
	
	import mx.events.FlexEvent;
	import mx.utils.StringUtil;
	
	import org.flowerplatform.common.CommonPlugin;
	import org.flowerplatform.flexutil.text.TextUtils;
	
	import spark.components.TextArea;
	
	/**
	 * @author Cristina Constatinescu
	 */ 
	public class LinkTextArea extends TextArea {
		
		public var textToDisplayAsTip:String;
		
		public function LinkTextArea(){
			super();
			addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
		}
		
		private function creationCompleteHandler(event:FlexEvent):void {
			TextUtils.setTextComponentHint(this, textToDisplayAsTip);
		}
		
		public function getText():String {
			return StringUtil.trim(getStyle("color") != "0x999999" ? text : ""); // if null returns '' 
		}
		
		public function handleLink(defaultCommand:String = null):Boolean {
			var text:String = getText();			
			if (text.length == 0) {
				return false; // No path to handle
			}
			CommonPlugin.getInstance().handleLink(text, defaultCommand);
			
			return true;
		}
		
	}
}