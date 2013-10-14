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
	import mx.core.IVisualElement;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;

	public interface IPopupHandler {
		function setTitle(value:String):IPopupHandler;
		function setWidth(value:int):IPopupHandler;
		function setHeight(value:int):IPopupHandler;
		function setViewContent(value:IViewContent):IPopupHandler;
		
		function show(modal:Boolean = true):void;
		function showModalOverAllApplication():void;
	}
}