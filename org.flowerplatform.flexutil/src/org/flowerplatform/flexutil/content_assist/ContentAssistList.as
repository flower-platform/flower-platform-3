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
package org.flowerplatform.flexutil.content_assist {
	
	import flash.events.Event;
	import flash.events.IEventDispatcher;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.ui.Keyboard;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	
	import spark.components.List;
	import spark.components.TextArea;
	import spark.components.TextInput;
	import spark.events.IndexChangeEvent;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class ContentAssistList extends List {
		
		private var dispatcher:Object;
		
		private var contentAssistProvider:IContentAssistProvider;
		
		public function ContentAssistList() {
			super();
			
			addEventListener(IndexChangeEvent.CHANGE, itemClickHandler);
			
			visible = false;
		}
		
		public function setDispatcher(dispatcher:IVisualElement):void {
			this.dispatcher = dispatcher;
			dispatcher.addEventListener(KeyboardEvent.KEY_UP, displayContentAssistHandler);
		}
		
		public function setContentAssistProvider(provider:IContentAssistProvider):void {
			this.contentAssistProvider = provider;
		}
		
		/**
		 * Listens for arrow keys to navigate through the content assist items.
		 */
		protected function manageContentAssistHandler(evt:KeyboardEvent):void {
			trace ("key down - key ", evt.keyCode);
			
			var offset:int = 0;
			switch (evt.keyCode) {
				case Keyboard.DOWN: {
					offset = 1;
					break;
				}
				case Keyboard.UP: {
					offset = -1;
					break;
				}
				case Keyboard.PAGE_DOWN: {
					offset = 10;
					break;
				}
				case Keyboard.PAGE_UP: {
					offset = -10;
					break;
				}
				case Keyboard.ENTER: {
					selectContentAssistItem();
					break;
				}
				case Keyboard.ESCAPE: {
					displayHideContentAssist(false);
					break;
				}
			}
			
			if (offset != 0) {
				selectedIndex += offset;
				var length:int = dataProvider.length;
				if (selectedIndex < 0) {
					selectedIndex = length + offset;
				}
				if (selectedIndex >= length) {
					selectedIndex = length - selectedIndex;
				}
				ensureIndexIsVisible(selectedIndex);
			}
		}
		
		/**
		 * Listens for triggers to display the content assist items.
		 */
		protected function displayContentAssistHandler(evt:KeyboardEvent):void {
			trace ("key up - char ", String.fromCharCode(evt.charCode));
			
			if (contentAssistProvider.getTriggerCharacters().contains(evt.charCode)) {
				getContentAssistItems();				// trigger character, e.g. :
			}
			if (evt.ctrlKey && evt.charCode == Keyboard.SPACE) {	// CTRL + SPACE
				getContentAssistItems();
			}
		}
		
		public function itemClickHandler(evt:IndexChangeEvent):void {
			selectContentAssistItem();
		}
		
		private function selectContentAssistItem():void {
			if (selectedIndex > -1) {
				var text:String = dispatcher.text;
				var index:int = getDelimiterIndex();
				if (index == -1) {
					throw new Error("Invalid input for content assist");
				}
				
				text = text.substr(0, index + 1);
				text += selectedItem;
				dispatcher.text = text;
				
				// hide
				displayHideContentAssist(false);
			}
		}
		
		public function getContentAssistItems():void {
			var pattern:String = getTextFromDispatcher();
			trace ("get content assist items -", pattern);
			if (pattern != null) {
				contentAssistProvider.getContentAssistItems(pattern, setContentAssistItems);
			}
		}
		
		public function setContentAssistItems(items:IList):void {
			trace ("set content assist items -", items.length);
			dataProvider = items;
			selectedIndex = 0;
			displayHideContentAssist(true);
		}
		
		/**
		 * Returns the string from the first delimiter to the left to the end of the text.
		 */
		private function getTextFromDispatcher():String {
			var text:String = dispatcher.text;
			var n:int = dispatcher.textDisplay.selectionActivePosition;
			return text.substring(getDelimiterIndex() + 1, n); 
		}
		
		private function getDelimiterIndex():int {
			var text:String = dispatcher.text;
			var i:int;
			var n:int = dispatcher.textDisplay.selectionActivePosition;
			for(i = n - 1; i >= 0; i--) {
				var char:String = text.charAt(i);
				if ((char == ' ') || (char == ':') || (char == ',') 
					|| (char == '(') || (char == ')'))
					break;
			}
			return i;
		}
		
		private function displayHideContentAssist(display:Boolean):void {
			visible = display;
			if (display) {
				dispatcher.addEventListener(KeyboardEvent.KEY_DOWN, manageContentAssistHandler);
			} else {
				dataProvider = null;
				dispatcher.removeEventListener(KeyboardEvent.KEY_DOWN, manageContentAssistHandler);
			}
		}
	}
}