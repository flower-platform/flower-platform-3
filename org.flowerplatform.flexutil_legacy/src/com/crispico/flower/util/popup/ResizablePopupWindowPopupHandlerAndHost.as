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
package com.crispico.flower.util.popup {
	import com.crispico.flower.util.spinner.ModalSpinner;
	
	import flash.display.DisplayObject;
	
	import mx.collections.IList;
	import mx.containers.ControlBar;
	
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.flexutil.popup.IPopupHandler;
	import org.flowerplatform.flexutil.popup.IPopupHost;
	
	public class ResizablePopupWindowPopupHandlerAndHost extends ResizablePopupWindow implements IPopupHandler, IPopupHost	{
		
		protected var popupContent:IPopupContent;
		
		public function ResizablePopupWindowPopupHandlerAndHost() {
			super();		
		}
		
		public function setHeight(value:int):IPopupHandler	{
			height = value;
			return this;
		}
		
		public function setTitle(value:String):IPopupHandler {
			title = value;
			return this;
		}
		
		public function setPopupContent(value:IPopupContent):IPopupHandler {
			popupContent = value;
			popupContent.percentHeight = 100;
			popupContent.percentWidth = 100;
			return this;
		}
		
		public function get activePopupContent():IPopupContent {
			return popupContent;
		}
		
		public function setActivePopupContent(value:IPopupContent, viaFocusIn:Boolean = false):void {
		}
		
		public function setWidth(value:int):IPopupHandler {
			width = value;
			return this;
		}
		
		public function show(modal:Boolean = true):void {
			IPopupContent(popupContent).popupHost = this;
			addElement(popupContent);
			showPopup(NaN, NaN, null, modal);
		}
		
		/**
		 * In this case, this class, ResizablePopupWindow is not used any more.
		 * The existing ModalSpinner mechanism is used for displaying the popup content.
		 */
		public function showModalOverAllApplication():void {
			var modalSpinner:ModalSpinner = new ModalSpinnerPopupHost(popupContent);
			ModalSpinner.addGlobalModalSpinner(null, modalSpinner);
		}
		
		public function selectionChanged():IList {
			// not supported		
			return null;
		}
		
		public function setIcon(value:Object):void {
			titleIconURL = String(value);
		}
		
		public function setLabel(value:String):void {
			title = value;
		}
		
		/**
		 * @author Mariana
		 */
		public function displayCloseButton(value:Boolean):void {
			showCloseButton = value;
		}
		
		/**
		 * @author Mariana
		 */
		public function addToControlBar(value:Object):void {
			if (controlBar == null) {
				controlBar = new ControlBar();
				controlBar.document = this;
				controlBar.enabled = true;
				
				var cBar:ControlBar = ControlBar(controlBar);
				cBar.setStyle("horizontalAlign", "center");
				cBar.setStyle("verticalAlign", "middle");
				addChild(cBar);
			}
			
			ControlBar(controlBar).addChild(DisplayObject(value));
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function showSpinner(text:String):void {	
			ModalSpinner.addModalSpinner(this, text);
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function hideSpinner():void {		
			ModalSpinner.removeModalSpinner(this);
		}
		
	}
}