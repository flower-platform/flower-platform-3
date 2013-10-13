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
package com.crispico.flower.util.layout {
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.containers.Canvas;
	import mx.containers.VBox;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.context_menu.FillContextMenuEvent;
	import org.flowerplatform.flexutil.popup.ActionUtil;
	import org.flowerplatform.flexutil.popup.IAction;
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.flexutil.popup.IPopupHost;
	import org.flowerplatform.flexutil.popup.selection.ISelectionForServerProvider;
	import org.flowerplatform.flexutil.popup.selection.ISelectionProvider;
	
	import spark.components.Button;
	import spark.components.HGroup;
	
	/**
	 * There is an issue with NavigatorContent: https://github.com/flex-users/flexlib/issues/301
	 * so we use Canvas instead of a NavigatorContent that should wrap spark components.
	 */
	[DefaultProperty("popupContent")]
	public class PopupHostViewWrapper extends VBox implements IPopupHost {
		
		public var allActions:Vector.<IAction>
		
		public var selection:IList;
		
		protected var rootActionsAlreadyCalculated:ArrayList;
		
		protected var buttonBar:HGroup;
		
		protected var _popupContent:IPopupContent;

		public function get activePopupContent():IPopupContent {
			return _popupContent;
		}

		public function setActivePopupContent(value:IPopupContent, viaFocusIn:Boolean = false):void {
			if (value == null) {
				return;
			}
			if (_popupContent != null) {
				throw new Error("Illegal usage. This setter can be called only once!");
			}
			_popupContent = value;
			activePopupContent.percentHeight = 100;
			activePopupContent.percentWidth = 100;
			activePopupContent.popupHost = this;
			FlexUtilGlobals.getInstance().selectionManager.viewContentActivated(this, activePopupContent, false);
		}
		
		public function PopupHostViewWrapper(popupContent:IPopupContent = null) {
			super();
			percentHeight = 100;
			percentWidth = 100;
			addEventListener(FillContextMenuEvent.FILL_CONTEXT_MENU, fillContextMenuHandler); 
			setStyle("verticalGap", 0);
			setActivePopupContent(popupContent);
		}
		
		override protected function createChildren():void {
			if (activePopupContent == null) {
				throw new Error("Illegal state. The popupContent shouldn't be null.");
			}
			super.createChildren();
			buttonBar = new HGroup();
			buttonBar.percentWidth = 100;
			addChild(buttonBar);
			buttonBar.paddingTop = 2;
			buttonBar.paddingBottom = 2;
			buttonBar.paddingLeft = 2;
			buttonBar.paddingRight = 2;
			buttonBar.horizontalAlign = "right";
			buttonBar.height = 24;
			addElement(activePopupContent);
		}
		
		protected function fillContextMenuHandler(event:FillContextMenuEvent):void {
			event.allActions = allActions;
			event.selection = selection;
			event.rootActionsAlreadyCalculated = rootActionsAlreadyCalculated;
		}
		
		/**
		 * Fills the action bar with the corresponding actions and caches the selection and
		 * actions, to be able to provide it, if right click menu is caught.
		 */
		public function selectionChanged():IList {
			var popupContent:IPopupContent = activePopupContent;
			
			if (!(popupContent is ISelectionProvider)) {
				return null;
			}
			
			selection = ISelectionProvider(popupContent).getSelection();
			allActions = popupContent.getActions(selection);
			
			buttonBar.removeAllElements();
			rootActionsAlreadyCalculated = new ArrayList();
			ActionUtil.processAndIterateActions(null, allActions, selection, this, function (action:IAction):void {
				if (action.preferShowOnActionBar) {
					var actionButton:ActionButton = new ActionButton();
					actionButton.label = action.label;
					actionButton.setStyle("icon", FlexUtilGlobals.getInstance().adjustImageBeforeDisplaying(action.icon));
					actionButton.action = action;
					actionButton.viewWrapper = this;
					buttonBar.addElement(actionButton);
				} else {
					rootActionsAlreadyCalculated.addItem(action);
				}
			});
			return selection;
		}
		
		public function setLabel(value:String):void
		{
		}
		
		public function setIcon(value:Object):void
		{
		}
		
		/**
		 * @author Mariana
		 */
		public function displayCloseButton(value:Boolean):void {
		}
		
		/**
		 * @author Mariana
		 */
		public function addToControlBar(value:Object):void {
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function showSpinner(text:String):void {			
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function hideSpinner():void {			
		}
	}
}