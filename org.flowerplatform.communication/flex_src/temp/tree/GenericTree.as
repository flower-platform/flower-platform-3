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
package  temp.tree {
	import com.crispico.flower.util.tree.CustomTree;
	
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.ui.Keyboard;
	
	import mx.collections.ArrayCollection;
	import mx.controls.listClasses.IListItemRenderer;
	import mx.controls.treeClasses.TreeItemRenderer;
	import mx.core.ClassFactory;
	import mx.core.UITextField;
	import mx.core.mx_internal;
	import mx.events.DragEvent;
	import mx.events.ListEvent;
	import mx.events.ListEventReason;
	import mx.events.TreeEvent;
	import mx.styles.CSSStyleDeclaration;
	import mx.styles.StyleManager;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	
	import temp.tree.remote.GenericTreeStatefulClientOld;

	use namespace mx_internal;
	
	/**
	 * 	 
	 * Subclasses that extend this class must:
	 * <ul>
	 * 	<li> provide a service id (<code>serviceId</code>)
	 * 	<li> for dispatched trees: 
	 * 		set the <code>dispatchedEnabled = true</code>  
	 * 	<li> for trees with context menu: 
	 * 		set the <code>contextMenuEnabled = true</code> and provide a <code>fillContextMenuFunction</code> 
	 * 		or override <code>fillContextMenu()</code>.
	 * 	<li> for trees with IED: 
	 * 		set <code>inplaceEditorEnabled = true</code>
	 *  <li> for trees with DND behaviour:
	 * 		set <code>dragAndDropEnabled = true</code>
	 * 		override methods <code>isDragAndDropOperationAccepted</code> and <code>performDrop</code>
	 * 		in order to give the logic behinde the drag and drop opperation
	 * </ul>
	 * 
	 * @see GenericTreeStatefulService Java doc.
	 * 
	 * @author Cristi
	 * @author Cristina
	 * @flowerModelElementId _4xATADR-EeCGErbqxW555A
	 */
	public class GenericTree extends CustomTree 
//		implements IContextMenuLogicProvider, IActionProvider2 
	{
		
		/**
		 * @flowerModelElementId _lTttEBN0EeKR8sYuzDGiDQ
		 */
		public static const DEFAULT_CLIENT_ID_PREFIX:String = "Generic Tree";
				
		/**
		 * @flowerModelElementId _lTuUIRN0EeKR8sYuzDGiDQ
		 */
		public var wrapServiceInvocationCommandWithCompoundCommand:Boolean;
		
		/**
		 * If <code>true</code> initial data will be requested when
		 * the tree initializes.
		 * 
		 * @flowerModelElementId _VZ82Hw71EeKbvNML8mcTuA
		 */		
		public var requestInitialDataAutomatically:Boolean;
		
		/**
		 * @flowerModelElementId _X9_Q0MBsEeG5PP70DrXYIQ
		 */
		public var requestDataFromServer:Boolean;
		
		/**
		 * @flowerModelElementId _JVcLAJp4EeGg5ZWNBtAGAA
		 */
		public var dispatchEnabled:Boolean;
		
		/**
		 * Context used to customize data on tree.
		 * Used when requesting data from server.
		 * @flowerModelElementId _GYg-8KiyEeGmuvNuOAQmXg
		 */
		private var _context:Object = new Object();
		
		/**
		 * @see Setter doc.
		 * @flowerModelElementId _hMrOUKD5EeG5ENNne79MAQ
		 */
		private var _fillContextMenuFunction:Function;
		
		/**
		 * @see Setter doc.
		 * @flowerModelElementId _DmRY8KKgEeGYz6sIcvSzpg
		 */
		public var _contextMenuEnabled:Boolean;
		
		/**
		 * @see Setter doc.
		 * @flowerModelElementId _zN4YwKKgEeGYz6sIcvSzpg
		 */
		public var _inplaceEditorEnabled:Boolean;
		
		/**
		 * @see setter
		 * @flowerModelElementId _Vb3IgNvaEeGkyOdU8tCo8w
		 */
		private var _dragAndDropEnabled:Boolean;
		
		/**
		 *  the cursor icon if the draggAndDrop operation is not allowed
		 * @flowerModelElementId _cHDtgOIiEeGBrf6fNB7udA
		 */
		protected var rejectCursor:Class;
		
		/**
		 * the cursor icon if the draggAndDrop operation is allowed
		 * @flowerModelElementId _cHDtguIiEeGBrf6fNB7udA
		 */
		protected var copyCursor:Class;
		
		/**
		 * @see itemCloseHandler()
		 * @see configureScrollBars()
		 * @flowerModelElementId _kj3IdP0CEeGZPtPdwyatgg
		 */
		private var oldVerticalScrollPosition:Number=-1;
			
		/**
		 * @flowerModelElementId _urVI0A73EeKbvNML8mcTuA
		 */
		public var statefulClient:GenericTreeStatefulClientOld;
		
		/**
		 * @flowerModelElementId _cZ0XsJp0EeGg5ZWNBtAGAA
		 */
		public var serviceId:String;
		
		/**
		 * @flowerModelElementId _lWRR0BN0EeKR8sYuzDGiDQ
		 */
		public var clientIdPrefix:String = DEFAULT_CLIENT_ID_PREFIX;
		
		/**
		 * @see GenericTreeStatefulClient.removeUIAndRelatedElementsAndStatefulClientBecauseUnsubscribedForcefully
		 */ 
		public var removeUIAndRelatedElementsAndStatefulClientBecauseUnsubscribedForcefullyFunction:Function;
		
		/**
		 * @flowerModelElementId _VxbV0DTuEeCWJOrqWwArag
		 */
		public function GenericTree() {			
			dataDescriptor = new TreeNodeDataDescriptor();
			itemRenderer = new ClassFactory(TreeNodeItemRenderer);
			
			labelField = "label";			
			childrenField = "children";
			checkField = "checked";
			
			showRoot = false;
			requestInitialDataAutomatically = false;
			requestDataFromServer = true;
			
			var rootNode:TreeNode = createRootTreeNode();
			rootNode.hasChildren = true;
			rootNode.children = new ArrayCollection();
			dataProvider = rootNode;
			
			addEventListener(TreeEvent.ITEM_OPEN, itemOpenHandler);
			addEventListener(TreeEvent.ITEM_CLOSE, itemCloseHandler);	
			addEventListener(TreeEvent.ITEM_OPENING, itemOpeningHandler);
			
			doubleClickEnabled = true;
			addEventListener(MouseEvent.DOUBLE_CLICK, executeNodeMouseHandler);
			addEventListener(KeyboardEvent.KEY_DOWN, executeNodeKeyboardHandler);
		}
		
		/**
		 * Used only during construction.
		 * This isn't necessaty to be overriden.		
		 * @flowerModelElementId _7GXawEznEeGsUPSh9UfXpw
		 */ 
		protected function createRootTreeNode():TreeNode {			
			return new TreeNode();
		}
		
		public function get context():Object {
			return this._context;
		}
		
		public function set context(value:Object):void {
			this._context = value;
		}
					
		/**
		 * If data is requested from server, then calls <code>openNode()</code>
		 * for current selection.
		 * 
		 * We manually dispatch the clickEvent because in the flex dispatching events flow 
		 * the mouse clickEvent on a expandArrow it is swallowed and it doesn't bubbles
		 * to the stage target. 
		 * And, because (for example in the ContextMenuManager) we need to know also when 
		 * we have click on a expand arrow, we manually dispatch the click event.
		 * 
		 * @flowerModelElementId _YO5jYDUmEeCWJOrqWwArag
		 */ 
		protected function itemOpenHandler(event:TreeEvent):void {
			if (requestDataFromServer) {
				var treeNode:TreeNode = TreeNode(event.item);
				statefulClient.openNode(treeNode);
			}
			
			//dispatching click event
			var clickEvent:MouseEvent = new MouseEvent(MouseEvent.CLICK, true, false, 0, 0);
			event.target.dispatchEvent(clickEvent);
		}
		
		/**
		 * If data is requested from server, then calls <code>closeNode()</code>
		 * for current selection.
		 * 
		 * Dispatching the clickEvent just like in the <code>itemOpenHandler</code>
		 * function.
		 * <p>
		 * Also removes children's content, and collapses the children nodes, 
		 * otherwise, when the node is expanded again, the children will still 
		 * have the disclosure icon open, but no subnodes.
		 * 
		 * @see #itemOpenHandler
		 * 
		 * @author Cristina
		 * @author Mariana
		 * 
		 * @flowerModelElementId _wCBmAJ9ZEeGYPK0E1LmMXw
		 */ 
		protected function itemCloseHandler(event:TreeEvent):void {
			if (requestDataFromServer && dispatchEnabled) {
				var treeNode:TreeNode = TreeNode(event.item);
				statefulClient.closeNode(treeNode);
				
				// remove children's content
				for each (var child:TreeNode in treeNode.children) {
					if (child.children != null) {
						child.children = new ArrayCollection();	
						child.children.refresh();
						// collapse the child node
						expandItem(child, false);
					}		
				}		
			}		
			
			// @see itemOpenHandlerFunction
			var clickEvent:MouseEvent = new MouseEvent(MouseEvent.CLICK, true, false, 0, 0);
			event.target.dispatchEvent(clickEvent);
		}
		
		/**
		 * When click-ing on disclosure icon (+/-), this event is dispatched (not the click event),
		 * so we must refresh the CM.
		 */ 
		protected function itemOpeningHandler(event:TreeEvent):void {
//			ContextMenuManager.INSTANCE.refresh(this);
		}
		
		// Context menu functions
		public function get contextMenuEnabled():Boolean {			
			return _contextMenuEnabled;
		}
		
		/**
		 * Enables/Disables the context menu functionality for this tree.	 	
		 * <br>
		 * By default, context menu is disabled. 
		 * @flowerModelElementId _GZBVQKiyEeGmuvNuOAQmXg
		 */
		public function set contextMenuEnabled(value:Boolean):void {			
//			if (_contextMenuEnabled == value) {
//				return;
//			}
//			if (value) {
//				ContextMenuManager.INSTANCE.registerClient(this, true, this, this, beforeFillContextMenuFunction, null, null, this);	
//				addEventListener(ListEvent.ITEM_CLICK, clickHandler);
//			} else {
//				ContextMenuManager.INSTANCE.unregisterClient(this, null);				
//				removeEventListener(ListEvent.ITEM_CLICK, clickHandler);
//			}
//			_contextMenuEnabled = value;			
		}
		
		/**
		 * Sets the function that will be used to fill the context menu with actions.
		 * 
		 *  <p/> The signature function should be : 
		 * 	<pre> function handler(contextMenu:FlowerContextMenu):void </pre>
		 * 
		 * @flowerModelElementId _GZEYkaiyEeGmuvNuOAQmXg
		 */ 
		public function set fillContextMenuFunction(value:Function):void {		
			_fillContextMenuFunction = value;
		}
		
//		/**
//		 * If <code>contextMenuEnabled</code>, 
//		 * then calls the corresponding <code>fillContextMenuFunction</code>.
//		 * <br>
//		 * Otherwise, subclasses must extends this method 
//		 * in order to populate the context menu with actions.
//		 * 
//		 * @flowerModelElementId _eIwxYKD5EeG5ENNne79MAQ
//		 */
//		public function fillContextMenu(contextMenu:FlowerContextMenu):void {
//			if (_contextMenuEnabled) {				
//				_fillContextMenuFunction(contextMenu);				
//			}			
//		}
		
		/**
		 * @flowerModelElementId _GZJREKiyEeGmuvNuOAQmXg
		 */
		public function getSelection():ArrayCollection {
			return new ArrayCollection(selectedItems);
		}
		
		/**
		 * Needed by the Context Menu framework in order to know the rectangular area of the main selection in order to draw near
		 * it the associtated Context Menu. It returns coordinates relative to the stage.
		 * 
		 * <p> The behaviour is :
		 * <ul>
		 * 	<li> if there is no selected item there is no need for context menu
		 * 	<li> if the selected item does not have an item renderer it may be due to the cause that it was recycled to be used
		 * 		on a other item, so this is the case where the context menu should be thrown in the corner
		 * 	<li> if the selected item has an item renderer, the coordinates of the rectangel of the label is returned. 
		 * </ul>
		 * @flowerModelElementId _GZKfMKiyEeGmuvNuOAQmXg
		 */ 
		public function get displayAreaOfSelection():Rectangle {
			if (selectedItems.length == 0) 
				return null;
			
			// If the navigator tree uses other types of item renderes this logic has to be revised.
			var selectedItemRenderer:TreeItemRenderer = TreeItemRenderer(itemToItemRenderer(selectedItems[0]));
			// If there is no item renderer it means that the selected element is not visible
			if (selectedItemRenderer == null)
				return null;
			var itemRendererLabel:UITextField = UITextField(selectedItemRenderer.getLabel());
			
			var upperLeft:Point = itemRendererLabel.localToGlobal(new Point(0,0));
			//??ultimul mesuredWidth nu trebuia de fapt sa fie measuredHeight??
			var downRight:Point = itemRendererLabel.localToGlobal(new Point(itemRendererLabel.measuredWidth, itemRendererLabel.measuredHeight));
			// For having the width and the height of the rectangle we need to substract the x and y of the down right corner from the upper left corner.  
			return new Rectangle(upperLeft.x, upperLeft.y, downRight.x - upperLeft.x, downRight.y - upperLeft.y);  			
		}
		
		/**
		 * @flowerModelElementId _GZLtUKiyEeGmuvNuOAQmXg
		 */
		public function isOverSelection(event:MouseEvent):Boolean {
			var point:Point = new Point(event.stageX, event.stageY);
			if (selectedItems.length == 0) 
				return false;
			
			// If the navigator tree uses other types of item renderes this logic has to be revised.
			var selectedItemRenderer:TreeItemRenderer = TreeItemRenderer(itemToItemRenderer(selectedItems[0]));
			// If there is no item renderer it means that the selected element is not visible
			if (selectedItemRenderer == null)
				return false;
			var itemRendererLabel:UITextField = UITextField(selectedItemRenderer.getLabel());
			
			var upperLeft:Point = itemRendererLabel.localToGlobal(new Point(0,0));
			//??ultimul mesuredWidth nu trebuia de fapt sa fie measuredHeight??
			var downRight:Point = itemRendererLabel.localToGlobal(new Point(itemRendererLabel.measuredWidth, itemRendererLabel.measuredHeight));
			// For having the width and the height of the rectangle we need to substract the x and y of the down right corner from the upper left corner.  
			if (point.x >= upperLeft.x 
				&& point.y >= upperLeft.y 
				&& point.x <= downRight.x
				&& point.y <= downRight.y) {
				return true;
			}
			return false;
		}
		
//		/**
//		 * @flowerModelElementId _GZNigaiyEeGmuvNuOAQmXg
//		 */
//		public function clickHandler(event:ListEvent):void {
//			ContextMenuManager.INSTANCE.refresh(this);
//		}
//		
//		/**
//		 * @flowerModelElementId _GZP-wKiyEeGmuvNuOAQmXg
//		 */
//		public function getContext():ActionContext {
//			return null;
//		}
//		
//		private function beforeFillContextMenuFunction(contextMenu:FlowerContextMenu):void {
//			contextMenu.beforeCloseContextMenuFunction = beforeCloseContextMenu;			
//		}
//		
//		private function beforeCloseContextMenu():void {
//			SingletonRefsFromPrePluginEra.flowerContributionRepository.cleanActionContext();
//		}
		
		public function setFocusOnMainSelectedObject():void {
		}
		
		// IED functions
		public function get inplaceEditorEnabled():Boolean {			
			return _inplaceEditorEnabled;
		}
		
		/**
		 * Enables/Disables the ied functionality for this tree.
		 * <br>
		 * By default, ied is disabled. 
		 * @flowerModelElementId _GZQl0KiyEeGmuvNuOAQmXg
		 */ 
		public function set inplaceEditorEnabled(value:Boolean):void {			
			if (_inplaceEditorEnabled == value) {
				return;
			}
			if (value) {
				GenericTreeInplaceEditorManager.registerTree(this);
			} else {
				GenericTreeInplaceEditorManager.unregisterTree(this);
			}
			_inplaceEditorEnabled = value;			
		}
		
		/**
		 * @return <code>true</code> if given node can be edited.
		 * @flowerModelElementId _3NwFYM9HEeGM5vCMuHxbuQ
		 */ 
		public function canEditNode(node:TreeNode):Boolean {
			return true;
		}
		
		/**
		 * Starts editing the given <code>node</code>.
		 * @flowerModelElementId _3Nwsc89HEeGM5vCMuHxbuQ
		 */ 
		public function editNode(node:TreeNode):void {
			GenericTreeInplaceEditorManager.activeTrees[statefulClient.getStatefulClientId()].startEditing(node);
		}
		
		/**
		 * Opens the ied for a given tree node. 
		 * 
		 * @see GenericTreeInplaceEditorManager
		 * @flowerModelElementId _GZTpIKiyEeGmuvNuOAQmXg
		 */
		public function openEditor(treeNode:TreeNode):void {
			selectedItem = treeNode;
			scrollToIndex(selectedIndex);
			var r:IListItemRenderer = itemToItemRenderer(treeNode);
			
			var pos:Point = itemRendererToIndices(r);
			
			editable = true;    		
			// send events to open the editor
			var listEvent:ListEvent = new ListEvent(ListEvent.ITEM_EDIT_BEGINNING, false, true);            
			listEvent.rowIndex = pos.y;
			listEvent.columnIndex = 0;
			listEvent.itemRenderer = r;
			dispatchEvent(listEvent);  	
		}
		
		/**
		 * Closes the ied for selected node. 
		 * 
		 * @see GenericTreeInplaceEditorManager
		 * @flowerModelElementId _GZVeUaiyEeGmuvNuOAQmXg
		 */
		public function closeEditor():void {   	
			editable = false;
			// the method called will send events to close the editor
			// (we are sending a CANCELLED event because we want only to close the editor,
			// without saving data)	    		        	
			endEdit(ListEventReason.CANCELLED);    		
		}  
		
		
		public function get dragAndDropEnabled():Boolean {
			return _dragAndDropEnabled;
		}
		/**
		 * This property activates/deactivates the dragAndDrop from tree to tree
		 * and from tree to diagram
		 * 
		 * @flowerModelElementId _S02NgNvbEeGkyOdU8tCo8w
		 */
		public function set dragAndDropEnabled(dragAndDropEnabled:Boolean):void {
			this._dragAndDropEnabled = dragAndDropEnabled;
			
			// Activates the default DND behavior of the Tree component.
			// This behavior will be customized by overriding the functions
			// showDropFeedback and dragDropHandler 
			this.dragEnabled = _dragAndDropEnabled;
			this.dropEnabled = _dragAndDropEnabled;
			
			if (_dragAndDropEnabled && rejectCursor == null) {
				var styleDeclaration:CSSStyleDeclaration = StyleManager.getStyleDeclaration("mx.managers.DragManager");
				//var styleDeclaration:CSSStyleDeclaration = FlexGlobals.topLevelApplication.styleManager.getStyleDeclaration("DragManager");
				
				if (styleDeclaration != null) {
					rejectCursor = styleDeclaration.getStyle("rejectCursor");
					copyCursor = styleDeclaration.getStyle("copyCursor");
				}
			}
		}
		
		/**
		 * We override this function because flex by default shows as feedback for
		 * the drag and drop operation a horizontal bar between items where the object
		 * should be inserted. But we need an eclipse-like behavior.
		 * 
		 * So this function highlights the target item because flex deactivates the default
		 * mouse over item hightlighting when the drag operation happends.
		 * 
		 * Shows accept/rejectCursor by checking if the dragAndDrop operation
		 * is valid considering the selected nodes and the target one.
		 * @flowerModelElementId _cHJ0IOIiEeGBrf6fNB7udA
		 */
		override public function showDropFeedback(event:DragEvent):void {
			// make super operations because we need to enable tree scolling when dragging
			super.showDropFeedback(event);
			// don't show the drop indicator 
			dropIndicator.visible = false;
			
			var rowCount:int = rowInfo.length;
			var rowNum:int = 0;
			var yy:int = rowInfo[rowNum].height;
			var pt:Point = globalToLocal(new Point(event.stageX, event.stageY));
			while (rowInfo[rowNum] && pt.y >= yy) {
				if (rowNum != rowInfo.length-1) {
					rowNum++;
					yy += rowInfo[rowNum].height;
				}
				else {
					// now we're past all rows.  adding a pixel or two should be enough.
					// at this point yOffset doesn't really matter b/c we're past all elements
					// but might as well try to keep it somewhat correct
					yy += rowInfo[rowNum].height;
					rowNum++;
				}
			}
			
			//hightlight the targetItem if is not in the selection
			var itemShouldBeHighlighted:Boolean = true;
			for each (var index:int in selectedIndices) {
				if (index == rowNum) {
					itemShouldBeHighlighted = false;
					break;
				}
			}
			
			if (itemShouldBeHighlighted)
				drawItem(listItems[rowNum][0], false, true);
			else
				clearHighlightIndicator(highlightIndicator, highlightItemRenderer);
			
			var draggedItems:Array = event.dragSource.dataForFormat("treeItems") as Array;
			
			var target:Object = (listItems[rowNum][0] as TreeNodeItemRenderer).data;
			//var target:Object = (dataProvider as ArrayCollection).getItemIndex((listItems[rowNum][0] as ItemRenderer).itemIndex);
			
			if (isDragAndDropOperationAccepted(target, draggedItems)) {
				this.cursorManager.removeAllCursors();
				this.cursorManager.setCursor(copyCursor);
			} else {
				this.cursorManager.removeAllCursors();
				this.cursorManager.setCursor(rejectCursor);
			}
		}
		
		/**
		 * We override this function because we don't want the default tree behavior
		 * of reordering items from the dataProvider
		 * 
		 * Instead we want to perform some actions on the the server side when the drop happens on the tree
		 * By default a generic tree doesn't perform any actions. It only computes the target node and 
		 * the subclasses should take care of handling the drop event having the context from the supercalss.
		 * @flowerModelElementId _cHJ0JOIiEeGBrf6fNB7udA
		 */
		override protected function dragDropHandler(event:DragEvent):void {
			// this will allow tree scrolling when dragging
			hideDropFeedback(event);
					
			var rowCount:int = rowInfo.length;
			var rowNum:int = 0;
			var yy:int = rowInfo[rowNum].height;
			var pt:Point = globalToLocal(new Point(event.stageX, event.stageY));
			while (rowInfo[rowNum] && pt.y >= yy) {
				if (rowNum != rowInfo.length-1) {
					rowNum++;
					yy += rowInfo[rowNum].height;
				}
				else {
					// now we're past all rows.  adding a pixel or two should be enough.
					// at this point yOffset doesn't really matter b/c we're past all elements
					// but might as well try to keep it somewhat correct
					yy += rowInfo[rowNum].height;
					rowNum++;
				}
			}
			
			var target:Object = (listItems[rowNum][0] as TreeNodeItemRenderer).data;
			if (isDragAndDropOperationAccepted(target, event.dragSource.dataForFormat("treeItems") as Array))
				statefulClient.performDrop(target, event.dragSource.dataForFormat("treeItems") as Array); 
			this.cursorManager.removeAllCursors();
		}
		
		/**
		 * Function used to update the cursor (accept/reject cursor) in the drag operation.
		 * It is called olso to decide if a <code>performDrop()</code> method should be called
		 * from the dragDropHandler
		 * 
		 * By default it returns false. Subclasses should implement this method in order to add
		 * specific logic to the DND opeartion 
		 * @flowerModelElementId _cHKbMuIiEeGBrf6fNB7udA
		 */ 
		protected function isDragAndDropOperationAccepted(dropTarget:Object, draggedItems:Array):Boolean {
			return false;
		}
		
		/**
		 * We override this function because we want to stop the default comportament of the tree
		 * that removes and re-add the droped item at the drop target.
		 * The actual move will be made when the response of the server will be receive
		 */
		override protected function dragCompleteHandler(event:DragEvent):void {
			this.cursorManager.removeAllCursors();
			// this will remove tree scrolling afer dropping
			resetDragScrolling();
		}
		
		override protected function dragExitHandler(event:DragEvent):void {
			this.cursorManager.removeAllCursors();
		}
		
		private function executeNodeKeyboardHandler(event:KeyboardEvent):void {
			if (_inplaceEditorEnabled && GenericTreeInplaceEditorManager.activeTrees[statefulClient.getStatefulClientId()].isEditing())
				return; // While editing so ignore.
			if (event.keyCode == Keyboard.ENTER || event.keyCode == Keyboard.F3)
				executeNode(); 
		}
		
		private function executeNodeMouseHandler(event:MouseEvent):void {
			if (_inplaceEditorEnabled && GenericTreeInplaceEditorManager.activeTrees[statefulClient.getStatefulClientId()].isEditing())
				return; // While editing so ignore.
			executeNode();		
		}
			
		/**
		 * Method intended to be overriden to provide behavior for executing the selected node(s).
		 */
		protected function executeNode():void {
		}
			
		/**
		 * Little hack made to keep the old verticalScrollPosition if new value is 0.
		 * 
		 * <p>
		 * Flex sets verticalScrollPosition to 0 if it finds data out of sync. 
		 * We are in this case because we set data after expanding a node.
		 * 
		 * @author Cristina
		 */ 
		override protected function configureScrollBars():void {
			super.configureScrollBars();
			
			if (verticalScrollPosition == 0 && oldVerticalScrollPosition != -1) {
				verticalScrollPosition = oldVerticalScrollPosition;
			}
			oldVerticalScrollPosition = -1;
		}
		
		override public function expandItem(item:Object, open:Boolean,
											animate:Boolean = false,
											dispatchEvent:Boolean = false,    
											cause:Event = null):void {
			oldVerticalScrollPosition = verticalScrollPosition;
			super.expandItem(item, open, animate, dispatchEvent, cause);
		}
	}
}