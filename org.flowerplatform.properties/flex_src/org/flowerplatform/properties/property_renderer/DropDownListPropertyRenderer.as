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
package org.flowerplatform.properties.property_renderer {
	import com.flextras.mobile.dropDownList.DropDownList;
	
	import flash.events.FocusEvent;
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	import spark.components.DropDownList;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class DropDownListPropertyRenderer extends BasicPropertyRenderer {
		
		[Bindable]
		public var dropDownList:spark.components.DropDownList;
		
		/**
		 * Signature: function getDataProviderHandler(callbackObject:Object, callbackFunction:Function):void		 
		 */ 
		public var requestDataProviderHandler:Function;
		
		public function DropDownListPropertyRenderer() {
			super();
		}
		
		override protected function createChildren():void {			
			super.createChildren();
			
			if (FlexUtilGlobals.getInstance().isMobile) {
				dropDownList = new com.flextras.mobile.dropDownList.DropDownList();										
			} else {
				dropDownList = new spark.components.DropDownList();											
			}
						
			dropDownList.percentWidth = 100;
			dropDownList.percentHeight = 100;		
			requestDataProviderHandler(this, requestDataProviderCallbackHandler);
			
			addElement(dropDownList);			
		}
		
		override public function set data(value:Object):void {
			super.data = value;
			dropDownList.selectedItem = data.value;
			dropDownList.enabled = !data.readOnly;
			
			if (!data.readOnly) {				
				handleListeningOnEvent(FocusEvent.FOCUS_OUT, this, dropDownList);
			}			
		}
		
		private function requestDataProviderCallbackHandler(result:ArrayCollection):void {
			dropDownList.dataProvider = result;
		}
	}
}