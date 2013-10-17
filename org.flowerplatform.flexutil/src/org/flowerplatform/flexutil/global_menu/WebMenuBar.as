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
package org.flowerplatform.flexutil.global_menu {
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import flash.geom.Point;
	import flash.utils.Dictionary;
	import flash.utils.clearTimeout;
	import flash.utils.setTimeout;
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.controls.Menu;
	import mx.controls.MenuBar;
	import mx.controls.menuClasses.IMenuBarItemRenderer;
	import mx.events.MenuEvent;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionUtil;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.action.IActionProvider;
	
	/**
	 * Extends the existing MenuBar so it can use an actionProvider.
	 * 
	 * @author Mircea Negreanu
	 */
	public class WebMenuBar extends MenuBar {
		
		protected var _actionProvider:IActionProvider;
		
		/**
		 *  currently shown menu 
		 */
		protected var menu:Menu;
		
		/**
		 *  after the submenu is closed the menuBar needs to be reset
		 */
		protected var resetMenuBarOnMenuClosingTimout:int = 0;
		
		public function WebMenuBar(ap:IActionProvider = null):void {
			super();
			
			if (ap != null) {
				_actionProvider = ap;
			}
			
			// Since we do this dynamically, we need the menuBar to tell us
			// when it thinks the menu should be shown/hidden
			addEventListener(MenuEvent.MENU_SHOW, menu_showHandler);
			addEventListener(MenuEvent.MENU_HIDE, menu_hideHandler);
		}
		
		public function get actionProvider():IActionProvider {
			return _actionProvider;
		}
		
		public function set actionProvider(ap:IActionProvider):void {
			_actionProvider = ap;
		
			// also build the menuBar
			if (actionProvider != null) {
				var xmlList:XMLList = new XMLList();
				
				var selection:IList = FlexUtilGlobals.getInstance().selectionManager.activeSelectionProvider.getSelection();
				
				dataProvider = getXMLListActions(selection, null);
			}
		}
		
		/**
		 * Utility function to build the xmlList for menubar/menus from the actionProvider
		 */ 
		protected function getXMLListActions(selection:IList, parentId:String = null):ArrayCollection {
			if (actionProvider != null) {
				var index:int = 0;
				
				var listActions:ArrayCollection = new ArrayCollection();
				
				ActionUtil.processAndIterateActions(parentId, 
					actionProvider.getActions(selection),
					selection,
					this,
					function(action:IAction):void {
						listActions.addItem(action);
					}
				);
				
				return listActions;
			}
			
			return null;
		}
		
		/**
		 * Compose the childMenu (based on the action provider and the selected menuBarItem) and shows it
		 */
		protected function menu_showHandler(event:MenuEvent):void {
			// callLater because we need the handler to bring 
			// back control to the caller
			callLater(buildAndShowMenu);
		}
		
		/**
		 * Time for the menu to be hidden
		 */
		protected function menu_hideHandler(event:MenuEvent):void {
			hideMenu();
		}
		
		/**
		 * Builds the child menu based on the current selected menubar
		 */
		protected function buildAndShowMenu():void {
			if (menu != null) {
				// we have a menu displayed -> hide it
				hideMenu();
			}
			
			if (actionProvider != null) {
				// get the current selected action
				var item:IMenuBarItemRenderer = menuBarItems[selectedIndex];
				var menuBarAction:IAction = item.data as IAction;
				
				// get the current selection
				var selection:IList = FlexUtilGlobals.getInstance().selectionManager.activeSelectionProvider.getSelection();
				
				// get the list of actions of the current action
				var xmlList:ArrayCollection = getXMLListActions(selection, menuBarAction.id);
				
				// create and show the menu
				menu = Menu.createMenu(this, xmlList, false);
				menu.labelField = this.labelField;
				menu.iconField = this.iconField;
				
				var menuStyle:Object = getStyle("menuStyleName");
				if (menuStyle != null) {
					menu.styleName = menuStyle;
				}
					
				menu.addEventListener(MenuEvent.MENU_HIDE, childMenu_eventHandler);
				menu.addEventListener(MenuEvent.MENU_SHOW, childMenu_eventHandler);
				menu.addEventListener(KeyboardEvent.KEY_DOWN, childMenu_eventHandler);
				menu.dataDescriptor = new WebMenuDataDescriptor(actionProvider, selection);
				
				var pt:Point = new Point(0, 0);
				pt = (menuBarItems[selectedIndex] as DisplayObject).localToGlobal(pt);
				pt.y += menuBarItems[selectedIndex].height + 1;
				// TODO: take ltr and screen boundaries into account
				
				menu.show(pt.x, pt.y);
				
				menuBarItems[selectedIndex].menuBarItemState = "itemDownSkin";
			}
		}
		
		/**
		 * Hides the currently shown menu (if any)
		 */
		protected function hideMenu():void {
			if (menu != null) {
				menu.hide();
				menu = null;
			}
		}
		
		/**
		 * Process events sent by the childMenu:
		 * <ul>
		 * 	<li>MENU_HIDE - start a timeout to reset the look of the menuBar 
		 * 		and the selectedIndex (otherwise it will look selected forever)</li> 
		 * 	<li>MENU_SHOW - cancel the timeout if the user selected another 
		 * 		menu at a short time after the previouse was closed</li>
		 * 	<li>KEY_DOWN - need to be sent to the parent menuBar (to implement left 
		 * 		and right)</li>
		 * </ul>
		 */
		protected function childMenu_eventHandler(event:Event):void {
			if (event is MenuEvent) {
				var menuEvent:MenuEvent = event as MenuEvent;
				if (menuEvent != null && menuEvent.menu != null && menuEvent.menu.parentMenu == null) {
					if (menuEvent.type == MenuEvent.MENU_HIDE) {
						// if the timeout is already in process, clean it
						if (resetMenuBarOnMenuClosingTimout != 0) {
							clearTimeout(resetMenuBarOnMenuClosingTimout);
						}
						
						// start the timeout to reset the look of the selected menuBar
						resetMenuBarOnMenuClosingTimout = setTimeout(function():void {
							if (selectedIndex != -1) {
								menuBarItems[selectedIndex].menuBarItemState = "itemUpSkin";
								selectedIndex = -1;
							}
						}, 50);
					} else if (menuEvent.type == MenuEvent.MENU_SHOW) {
						// we have another menu that just opened, immediatly after the first
						// one was closed -> stop the closing timeout
						if (resetMenuBarOnMenuClosingTimout != 0) {
							clearTimeout(resetMenuBarOnMenuClosingTimout);
						}
					}
				}
				
			} else if (event is KeyboardEvent) {
				// TODO: Take the submenu into account too
				dispatchEvent(event);
			}
		}
	}
}