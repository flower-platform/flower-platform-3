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
package org.flowerplatform.editor.model {
	import flash.events.Event;
	import flash.events.FocusEvent;
	import flash.events.MouseEvent;
	import flash.geom.Rectangle;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.containers.HBox;
	import mx.containers.HDividedBox;
	import mx.events.CollectionEvent;
	import mx.events.FlexEvent;
	import mx.managers.IFocusManagerComponent;
	
	import org.flowerplatform.editor.EditorFrontend;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.util.infinitegroup.InfiniteScroller;
	import org.flowerplatform.flexutil.popup.IAction;
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.flexutil.popup.IPopupHost;
	
	public class DiagramEditorFrontend extends EditorFrontend implements IPopupContent, IFocusManagerComponent {
	
		public var diagramShell:DiagramShell;
		
		protected var _popupHost:IPopupHost;
		
		override protected function creationCompleteHandler(event:FlexEvent):void {
			diagramShell.selectedItems.addEventListener(CollectionEvent.COLLECTION_CHANGE, selectionChangedHandler);
		}
		
		protected function selectionChangedHandler(e:Event):void {
			if (popupHost) {
				popupHost.refreshActions(this);
			}
		}
		
		protected function getDiagramShellInstance():DiagramShell {
			throw new Error("This should be implemented by subclasses!");
		}
				
		override protected function createChildren():void {
			var diagramContainer:HDividedBox = new HDividedBox();
			
			var scroller:InfiniteScroller = new InfiniteScroller();
			scroller.percentWidth = 100;
			scroller.percentHeight = 100;
			diagramContainer.addChild(scroller);
			editor = diagramContainer;
			
			var diagramRenderer:DiagramRenderer = new DiagramRenderer();
			scroller.viewport = diagramRenderer;
			diagramRenderer.horizontalScrollPosition = diagramRenderer.verticalScrollPosition = 0;
			
			diagramShell = getDiagramShellInstance();
			diagramShell.diagramRenderer = diagramRenderer;
						
			super.createChildren();			
		}
		
		override public function executeContentUpdateLogic(content:Object, isFullContent:Boolean):void {
		}

		override public function disableEditing():void {
		}
		
		override public function enableEditing():void {
		}
		
		public function getActions(selection:IList):Vector.<IAction>{			
			return null;
		}
		
		public function getSelection():IList	{		
			return diagramShell.selectedItems;
		}
		
		public function get popupHost():IPopupHost {
			return _popupHost;
		}
		
		public function set popupHost(value:IPopupHost):void {
			_popupHost = value;
		}
		
		override protected function focusInHandler(event:FocusEvent):void {
			super.focusInHandler(event);
			popupHost.activePopupContent = this;
		}
		
	}
}