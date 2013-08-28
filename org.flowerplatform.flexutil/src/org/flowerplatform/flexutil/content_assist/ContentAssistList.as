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
	import mx.core.ClassFactory;
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
		
		private var previousPattern:String;
		
		public function ContentAssistList() {
			super();
			itemRenderer = new ClassFactory(ContentAssistItemRenderer);
			
			addEventListener(IndexChangeEvent.CHANGE, itemClickHandler);
			
			visible = false;
		}
		
		public function setDispatcher(dispatcher:IVisualElement):void {
			this.dispatcher = dispatcher;
			dispatcher.addEventListener(KeyboardEvent.KEY_UP, displayContentAssistHandler);
		}
		
		public function setContentAssistProvider(provider:IContentAssistProvider):void {
			this.contentAssistProvider = provider;
			var properties:Object = new Object();
			properties['contentAssistProvider'] = provider;
			ClassFactory(itemRenderer).properties = properties;
		}
		
		/**
		 * Listens for arrow keys to navigate through the content assist items.
		 * 
		 * <p>
		 * Should only be registered when the list is visible.
		 * @see displayHideContentAssist()
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
					return;
				}
				case Keyboard.ESCAPE: {
					displayHideContentAssist(false);
					return;
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
				trace ("navigate through list");
				evt.stopImmediatePropagation();
			}
		}
		
		/**
		 * Listens for user input to update the content assist list litems.
		 * 
		 * <p>
		 * Should only be registered when the list is visible.
		 * @see displayHideContentAssist()
		 */
		protected function updateContentAssistListItems(evt:KeyboardEvent):void {
			if (visible) {
			trace ("update content assist items");
				if (evt.charCode > 0 || evt.keyCode == Keyboard.LEFT || evt.keyCode == Keyboard.RIGHT
					|| evt.keyCode == Keyboard.BACKSPACE || evt.keyCode == Keyboard.DELETE) {
					getContentAssistItems();
				}
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
				
				index++;
				dispatcher.text = 
					text.substr(0, index) +
					selectedItem.item +
					text.substr(dispatcher.textDisplay.selectionActivePosition);
				index += selectedItem.item.length;
				dispatcher.selectRange(index, index);
				
				// hide
				displayHideContentAssist(false);
			}
		}
		
		/**
		 * Gets the content assist items from the <code>contentAssistProvider</code> if
		 * the new <code>pattern</code> is different from the <code>previousPattern</code>.
		 */
		public function getContentAssistItems():void {
			var pattern:String = getTextFromDispatcher();
			trace ("get content assist items -", pattern);
			if (pattern != null && pattern != previousPattern) {
				previousPattern = pattern;
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
				dispatcher.addEventListener(KeyboardEvent.KEY_DOWN, manageContentAssistHandler, true);
				dispatcher.addEventListener(KeyboardEvent.KEY_UP, updateContentAssistListItems, true);
			} else {
				dataProvider = null;
				previousPattern = null;
				dispatcher.removeEventListener(KeyboardEvent.KEY_DOWN, manageContentAssistHandler);
				dispatcher.removeEventListener(KeyboardEvent.KEY_UP, updateContentAssistListItems);
			}
		}
	}
}