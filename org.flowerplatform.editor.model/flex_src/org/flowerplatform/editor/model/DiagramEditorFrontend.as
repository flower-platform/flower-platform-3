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
	
	import mx.collections.IList;
	import mx.containers.HDividedBox;
	import mx.core.FlexGlobals;
	import mx.core.UIComponent;
	import mx.events.CollectionEvent;
	import mx.events.FlexEvent;
	import mx.managers.IFocusManagerComponent;
	
	import org.flowerplatform.editor.EditorFrontend;
	import org.flowerplatform.flexdiagram.CreateModelEvent;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.util.infinitegroup.InfiniteScroller;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.action.MenuClosedEvent;
	import org.flowerplatform.flexutil.selection.ISelectionProvider;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	import org.flowerplatform.flexutil.view_content_host.IViewHost;
	
	/**
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */ 
	public class DiagramEditorFrontend extends EditorFrontend implements IViewContent, IFocusManagerComponent, ISelectionProvider {
	
		public var diagramShell:DiagramShell;
		
		protected var _viewHost:IViewHost;
		
		override protected function creationCompleteHandler(event:FlexEvent):void {
			diagramShell.selectedItems.addEventListener(CollectionEvent.COLLECTION_CHANGE, selectionChangedHandler);
			// this event will be dispatched by dragToCreate tools to show create options to client
			UIComponent(diagramShell.diagramRenderer).addEventListener(CreateModelEvent.SHOW_CREATE_OPTIONS, openMenuHandler);
		}
		
		protected function selectionChangedHandler(e:Event):void {
			FlexUtilGlobals.getInstance().selectionManager.selectionChanged(viewHost, this);
		}
		
		/**		
		 * @author Cristina Constantinescu
		 */ 
		protected function openMenuHandler(e:CreateModelEvent):void {			
			if (!viewHost.openMenu(stage.mouseX, stage.mouseY, e.context, "new")) { // no actions, call close logic
				menuClosedHandler();
			} else if (e.finishToolJobAfter) { // the tool must be deactivated after closing the menu
				FlexGlobals.topLevelApplication.addEventListener(MenuClosedEvent.MENU_CLOSED, menuClosedHandler);
			}
		}
		
		/**		
		 * @author Cristina Constantinescu
		 */ 
		protected function menuClosedHandler(e:MenuClosedEvent = null):void {
			if (e != null) {
				FlexGlobals.topLevelApplication.removeEventListener(MenuClosedEvent.MENU_CLOSED, menuClosedHandler);
			}
			// deactivate tool
			diagramShell.mainToolFinishedItsJob();
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
		
		public function get viewHost():IViewHost {
			return _viewHost;
		}
		
		public function set viewHost(value:IViewHost):void {
			_viewHost = value;
		}
		
	}
}