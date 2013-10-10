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
package org.flowerplatform.flexutil.mobile.popup {
	import flash.events.Event;
	
	import mx.collections.IList;
	import mx.core.FlexGlobals;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	import mx.events.FlexEvent;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.mobile.spinner.MobileSpinner;
	import org.flowerplatform.flexutil.popup.ActionUtil;
	import org.flowerplatform.flexutil.popup.IAction;
	import org.flowerplatform.flexutil.popup.IComposedAction;
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.flexutil.popup.IPopupHost;
	
	import spark.components.CalloutButton;
	import spark.components.Group;
	import spark.components.Label;
	import spark.components.SkinnableContainer;
	import spark.components.View;
	import spark.components.ViewMenuItem;
	import spark.components.supportClasses.ButtonBase;
	import spark.components.supportClasses.SkinnableComponent;
	import spark.primitives.BitmapImage;
	
	public class WrapperViewBase extends View implements IPopupHost {
		
		protected var openMenuAction:OpenMenuAction;
		
		// TODO CS
//		protected var showOpenEditorsCalloutButton:ShowOpenEditorsCalloutButton;
		
		protected var iconComponent:BitmapImage;
		
		protected var labelComponent:Label;
		
		protected var allActionsForActivePopupContent:Vector.<IAction>;
		
		protected var selectionForActivePopupContent:IList;
		
		protected var spinner:MobileSpinner;
		
		public function WrapperViewBase() {
			super();
			openMenuAction = new OpenMenuAction(this);
			// TODO CS
//			showOpenEditorsCalloutButton = new ShowOpenEditorsCalloutButton();
//			showOpenEditorsCalloutButton.visible = false;
//			showOpenEditorsCalloutButton.includeInLayout = false;
			addEventListener(FlexEvent.MENU_KEY_PRESSED, menuKeyPressedEvent);
		}
		
		protected function menuKeyPressedEvent(event:FlexEvent):void {
			// we do this so that the main app logic (that just opens the menu) won't execute
			event.preventDefault();
			if (openMenuAction.enabled) {
				openMenuAction.run();
			}
		}
		
		override protected function createChildren():void {
			super.createChildren();

			iconComponent = new BitmapImage();
			labelComponent = new Label();
			titleContent = [iconComponent, labelComponent]; 
		}
		
		public function get activePopupContent():IPopupContent {
			throw new Error("Should be implemented");
		}
		
		public function set activePopupContent(value:IPopupContent):void {		
		}
		
		public function setIcon(value:Object):void {
			iconComponent.source = FlexUtilGlobals.getInstance().adjustImageBeforeDisplaying(value);
		}
		
		public function setLabel(value:String):void {
			labelComponent.text = value;	
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
			var elt:IVisualElement = IVisualElement(value);
			elt.includeInLayout = true;
			Group(activePopupContent).addElement(elt);
		}

		/**
		 * Called by <code>refreshActions()</code>. This may be overridden, if the WrapperView
		 * wants to add some actions.
		 */
		protected function getActionsFromPopupContent(popupContent:IPopupContent, selection:IList):Vector.<IAction> {
			// for SplitViewWrapper, popupContent may be null if the current active view (left or right) is
			// not a IPopupContent. However, we want the action logic to execute, so that SplitViewWrapper can
			// add its switch* actions
			return popupContent != null ? popupContent.getActions(selection) : new Vector.<IAction>();
		}
		
		/**
		 * Populates the View Navigator and the OpenMenuAction, with the first level of actions.
		 */
		public function refreshActions(popupContent:IPopupContent):void	{
			if (activePopupContent != popupContent) {
				return;
			}
			// for SplitViewWrapper, popupContent may be null if the current active view (left or right) is
			// not a IPopupContent. However, we want the action logic to execute, so that SplitViewWrapper can
			// add its switch* actions
			selectionForActivePopupContent = popupContent != null ? popupContent.getSelection() : null;
			allActionsForActivePopupContent = getActionsFromPopupContent(popupContent, selectionForActivePopupContent);

			var newActionContent:Array = new Array();
			var newViewMenuItems:Vector.<ViewMenuItem> = new Vector.<ViewMenuItem>();
			ActionUtil.processAndIterateActions(null, allActionsForActivePopupContent, selectionForActivePopupContent, this, function (action:IAction):void {
				if (action.preferShowOnActionBar) {
					var button:ActionButton = new ActionButton();
					populateButtonWithAction(button, action);				
					newActionContent.push(button);
				} else {
					var actionViewMenuItem:ActionViewMenuItem = new ActionViewMenuItem();
					populateButtonWithAction(actionViewMenuItem, action);				
					newViewMenuItems.push(actionViewMenuItem);
				}
			});
			
			// give the viewMenuItems to the actions so that it can calculate it's enablement
			openMenuAction.viewMenuItems = newViewMenuItems;
			viewMenuItems = newViewMenuItems; 

			// TODO CS
//			newActionContent.push(showOpenEditorsCalloutButton);
			
			var menuButton:ActionButton = new ActionButton();
			populateButtonWithAction(menuButton, openMenuAction);
			newActionContent.push(menuButton);
			
			actionContent = newActionContent;
		}
		
		protected function populateButtonWithAction(button:ButtonBase, action:IAction):void {
			button.label = action.label;
			button.setStyle("icon", FlexUtilGlobals.getInstance().adjustImageBeforeDisplaying(action.icon));
			button.enabled = action.enabled;
			if (button is ActionButton) {
				var actionButton:ActionButton = ActionButton(button);
				actionButton.action = action;
				actionButton.view = this;
			} else if (button is ActionViewMenuItem) {
				var actionViewMenuItem:ActionViewMenuItem = ActionViewMenuItem(button);
				actionViewMenuItem.action = action;
				actionViewMenuItem.view = this;
			}
		}
		
		/**
		 * For a ComposedAction, fills the current view menu.
		 */
		protected function populateViewMenuWithActions(composedAction:IComposedAction):void {
			var newViewMenuItems:Vector.<ViewMenuItem> = new Vector.<ViewMenuItem>();
			ActionUtil.processAndIterateActions(composedAction.id, allActionsForActivePopupContent, selectionForActivePopupContent, this, function (action:IAction):void {
				var actionViewMenuItem:ActionViewMenuItem = new ActionViewMenuItem();
				populateButtonWithAction(actionViewMenuItem, action);				
				newViewMenuItems.push(actionViewMenuItem);
			});
			
			viewMenuItems = newViewMenuItems;
		}
		
		/**
		 * The click handler for ActionButton and ActionViewMenuItem.
		 */
		public function actionClickHandler(action:IAction):void {
			if (action is IComposedAction) {
				
				var runnable:Function = function (event:Event):void {
					removeEventListener("viewMenuClose", runnable);
					callLater(function ():void {
						populateViewMenuWithActions(IComposedAction(action));
						FlexGlobals.topLevelApplication.viewMenuOpen = true;
					});
				};			
				if (FlexGlobals.topLevelApplication.viewMenuOpen) {
					// 1) we need to do a hack; the menu doesn't get repopulated until ViewNavigatorApplication.viewMenuClose_handler() is invoked,
					// (i.e. after the close transition of the menu is finished)
					// 2) we need the callLater() too, because if we do the logic directly, the menu instance won't be set to null in the main app
					// and the repopulation won't be done
					//
					// The issue is that this event, cf. the comment, seems a private/internal event "for testing"; so maybe in the future it won't
					// be available?
					//
					// 3) another solution would be to use a timer of about 200ms, which is greater than the transitions defined in the ViewMenuSkin
					// but relying on timers to run after other timers, etc. doesn't seem very robust
					addEventListener("viewMenuClose", runnable);
					
				} else {
					runnable.call(null, null);
				}
			} else {
				try {
					action.selection = selectionForActivePopupContent;
					action.run();				
				} finally {
					action.selection = null;
				}
			}
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function showSpinner(text:String):void {
			if (spinner != null) {
				throw new Error("Spinner is already displayed!");
			}
			spinner = new MobileSpinner();
			spinner.percentWidth = 100;
			spinner.percentHeight = 100;			
			spinner.text = text;			
			addElement(spinner);
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function hideSpinner():void {		
			removeElement(spinner);
			spinner = null;
		}
		
	}
}