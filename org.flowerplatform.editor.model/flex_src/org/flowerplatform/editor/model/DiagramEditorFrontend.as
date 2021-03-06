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
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.containers.HDividedBox;
	import mx.core.FlexGlobals;
	import mx.events.CollectionEvent;
	import mx.events.FlexEvent;
	import mx.managers.IFocusManagerComponent;
	
	import org.flowerplatform.editor.EditorFrontend;
	import org.flowerplatform.editor.model.properties.remote.DiagramSelectedItem;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.Diagram;
	import org.flowerplatform.emf_model.notation.Edge;
	import org.flowerplatform.emf_model.notation.View;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.event.ExecuteDragToCreateEvent;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.tool.SelectOrDragToCreateElementTool;
	import org.flowerplatform.flexdiagram.tool.WakeUpTool;
	import org.flowerplatform.flexdiagram.tool.toolbar.Toolbar;
	import org.flowerplatform.flexdiagram.util.ZoomToolbar;
	import org.flowerplatform.flexdiagram.util.infinitegroup.InfiniteScroller;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.action.MenuClosedEvent;
	import org.flowerplatform.flexutil.selection.ISelectionForServerProvider;
	import org.flowerplatform.flexutil.selection.ISelectionProvider;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	import org.flowerplatform.flexutil.view_content_host.IViewHost;
	import org.flowerplatform.flexutil.view_content_host.IViewHostAware;
	
	/**
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */	
	public class DiagramEditorFrontend extends EditorFrontend implements IViewContent, IFocusManagerComponent, ISelectionProvider, ISelectionForServerProvider, IViewHostAware {
	
		/**
		 * @author Cristian Spiescu
		 * @author Cristina Constantinescu
		 */ 
		public var diagramShell:DiagramShell;
		
		protected var _viewHost:IViewHost;
		
		override protected function creationCompleteHandler(event:FlexEvent):void {
			diagramShell.selectedItems.addEventListener(CollectionEvent.COLLECTION_CHANGE, selectionChangedHandler);
			// this event will be dispatched by dragToCreate tools to show create options to client
			diagramShell.addEventListener(ExecuteDragToCreateEvent.DRAG_TO_CREATE_EVENT, openMenuHandler);
		}
		
		protected function selectionChangedHandler(e:Event):void {
			if (!diagramShell.selectedItems.removeEventsCanBeIgnored) { // catch events only if necessary
				FlexUtilGlobals.getInstance().selectionManager.selectionChanged(viewHost, this);
			}
		}
		
		/**		
		 * @author Cristina Constantinescu
		 */ 
		protected function openMenuHandler(e:ExecuteDragToCreateEvent):void {			
			if (!viewHost.openMenu(stage.mouseX, stage.mouseY, e.context, "new")) { // no actions, call close logic
				menuClosedHandler();
			} else if (e.shouldFinishToolJobAfterExecution) { // the tool must be deactivated after closing the menu
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
			diagramShell.mainTool.jobFinished();
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
			
			var toolbar:Toolbar = new Toolbar();
			toolbar.diagramShell = diagramShell;
			toolbar.addTool(
				EditorModelPlugin.getInstance().getMessage("tool.wakeUp.label"),
				EditorModelPlugin.getInstance().getResourceUrl("images/cursor_drag_arrow.png"),
				diagramShell.tools[WakeUpTool],
				EditorModelPlugin.getInstance().getMessage("tool.wakeUp.toolTip"),
				true);
			toolbar.addTool(
				EditorModelPlugin.getInstance().getMessage("tool.selectOrDragToCreateElement.label"),
				EditorModelPlugin.getInstance().getResourceUrl("images/select.png"),
				diagramShell.tools[SelectOrDragToCreateElementTool],
				EditorModelPlugin.getInstance().getMessage("tool.selectOrDragToCreateElement.toolTip"));
			
			toolbarsArea.addElement(toolbar);
			
			var zoomToolbar:ZoomToolbar = new ZoomToolbar(diagramShell);			
			toolbarsArea.addElement(zoomToolbar);
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
		
		/**
		 * @author Razvan Tache
		 * @author Cristina Constantinescu
		 */
		public function convertSelectionToSelectionForServer(selection:IList):IList {
			if (selection == null) 
				return selection;
			
			var selectedItems:ArrayCollection = new ArrayCollection();
			for (var i:int = 0; i < selection.length; i++) {
				var node:View = View(selection.getItemAt(i));//.id / 
				var diagramEditableResourcePath:String = NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).editableResourcePath;
				var xmiID:String = node.idAsString;
				var serviceID:String = NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).getStatefulServiceId();
		
				var diagramSelectedItem:DiagramSelectedItem = new DiagramSelectedItem(xmiID, diagramEditableResourcePath, serviceID);
			
				if (node is Diagram) { // for diagram consider its viewType as diagramSelectedItem.itemType
					diagramSelectedItem.itemType = node.viewType;
				} else if (node is Edge) {
					diagramSelectedItem.itemType = "relation";
				} else {
					diagramSelectedItem.itemType = "codeSyncElement";
				}
				selectedItems.addItem(diagramSelectedItem);
			}			
			
			return selectedItems;
		}
	}
}
