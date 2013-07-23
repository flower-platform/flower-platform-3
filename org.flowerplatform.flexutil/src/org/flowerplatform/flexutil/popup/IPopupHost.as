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
package org.flowerplatform.flexutil.popup {
	
	public interface IPopupHost {
		function get activePopupContent():IPopupContent;
		function set activePopupContent(value:IPopupContent):void;
		function setLabel(value:String):void;
		function setIcon(value:Object):void;
		function refreshActions(popupContent:IPopupContent):void;	
		
		function displayCloseButton(value:Boolean):void;
		function addToControlBar(value:Object):void;
		
		/**
		 * @author Cristina Constantinescu
		 */ 
		function showSpinner(text:String):void;
		function hideSpinner():void;
	}
}