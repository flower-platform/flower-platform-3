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
package org.flowerplatform.web {
	import com.crispico.flower.util.HTMLToolTip;
	import com.crispico.flower.util.layout.Perspective;
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.event.ActiveViewChangedEvent;
	
	import flash.events.MouseEvent;
	import flash.external.ExternalInterface;
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	
	import mx.containers.HBox;
	import mx.core.FlexGlobals;
	import mx.core.IVisualElementContainer;
	import mx.managers.ToolTipManager;
	
	import org.flowerplatform.blazeds.BridgeEvent;
	import org.flowerplatform.codesync.regex.RegexIdePerspective;
	import org.flowerplatform.codesync.regex.ide.RegexActionsViewProvider;
	import org.flowerplatform.codesync.regex.ide.RegexConfigsViewProvider;
	import org.flowerplatform.codesync.regex.ide.RegexMacrosViewProvider;
	import org.flowerplatform.codesync.regex.ide.RegexMatchesViewProvider;
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.open_resources_view.OpenResourcesViewProvider;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.action.ComposedAction;
	import org.flowerplatform.flexutil.action.IActionProvider;
	import org.flowerplatform.flexutil.action.VectorActionProvider;
	import org.flowerplatform.flexutil.global_menu.WebMenuBar;
	import org.flowerplatform.flexutil.layout.event.ViewsRemovedEvent;
	import org.flowerplatform.flexutil.popup.IPopupHandler;
	import org.flowerplatform.flexutil.selection.SelectionChangedEvent;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	import org.flowerplatform.properties.PropertiesViewProvider;
	import org.flowerplatform.web.action.ShowViewAction;
	import org.flowerplatform.web.action.SwitchPerspectiveAction;
	import org.flowerplatform.web.common.WebCommonPlugin;
	import org.flowerplatform.web.common.explorer.ExplorerViewProvider;
	import org.flowerplatform.web.layout.DefaultPerspective;
	import org.flowerplatform.web.security.ui.GroupsScreen;
	import org.flowerplatform.web.security.ui.OrganizationsScreen;
	import org.flowerplatform.web.security.ui.PermissionsScreen;
	import org.flowerplatform.web.security.ui.UserForm;
	import org.flowerplatform.web.security.ui.UserFormViewProvider;
	import org.flowerplatform.web.security.ui.UsersScreen;
	
	import spark.components.Button;
	
	/**
	 * @author Cristi
	 */
	public class WebPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:WebPlugin;
		
		public static function getInstance():WebPlugin {
			return INSTANCE;
		}
		
		protected var webCommonPlugin:WebCommonPlugin = new WebCommonPlugin();
		
		public var currentPerspective:Perspective;
		
		public var perspectives:Vector.<Perspective> = new Vector.<Perspective>();
		
		override public function preStart():void {
			super.preStart();
			webCommonPlugin.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
			
			perspectives.push(new DefaultPerspective());
			perspectives.push(new RegexIdePerspective());
			
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new UserFormViewProvider());			
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new OpenResourcesViewProvider());
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new RegexActionsViewProvider());
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new RegexMatchesViewProvider());
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new RegexMacrosViewProvider());
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new RegexConfigsViewProvider());
		}
		
		/**
		 * @author Cristian Spiescu
		 * @author Mircea Negreanu
		 */
		override public function start():void {
			super.start();
			// pass the same descriptor; to be used for images (that need the descriptor for the URL)
			webCommonPlugin.flexPluginDescriptor = flexPluginDescriptor;	
			webCommonPlugin.start();
			
			ToolTipManager.showDelay = 0;
			ToolTipManager.toolTipClass = HTMLToolTip;
			
			Workbench(FlexUtilGlobals.getInstance().workbench).addEventListener(ViewsRemovedEvent.VIEWS_REMOVED, 
				EditorPlugin.getInstance().globalEditorOperationsManager.viewsRemovedHandler);
			Workbench(FlexUtilGlobals.getInstance().workbench).addEventListener(ActiveViewChangedEvent.ACTIVE_VIEW_CHANGED, 
				EditorPlugin.getInstance().globalEditorOperationsManager.activeViewChangedHandler);
						
			// is needed from other plugins
			CommunicationPlugin.getInstance().bridge.addEventListener(BridgeEvent.WELCOME_RECEIVED_FROM_SERVER, welcomeReceivedFromServerHandler);

			// Init for EditorSupport
			//GlobalEditorOperationsManager.INSTANCE = new GlobalEditorOperationsManager(Workbench(FlexUtilGlobals.getInstance().workbench));

			FlexUtilGlobals.getInstance().selectionManager.addEventListener(SelectionChangedEvent.SELECTION_CHANGED, function (event:SelectionChangedEvent):void {
				trace("Selection changed: " + event.selection);
			});
			
			if (ExternalInterface.available) { 
				ExternalInterface.addCallback("invokeSaveResourcesDialog", invokeSaveResourcesDialog); 
			}
		}
		
		/**
		 * Accessed by JavaScript code in order to prevent closing the browser if there
		 * are resources that are not saved.
		 * 
		 * @return true if the close of the browser window needs to be prevented.
		 */
		public function invokeSaveResourcesDialog():Boolean {
			if (!WebCommonPlugin.getInstance().authenticationManager.bridge.connectionEstablished){
				// this happens when the user was loggout by server (for inactivity); in this
				// case, no need to care about dirty resources any more
				return false;
			}
			
			// we invoke the dialog, so that the user has it open already when returning to the app
			// (from the JS Alert that he has just closed)
			EditorPlugin.getInstance().globalEditorOperationsManager.invokeSaveResourcesDialogAndInvoke(null, null, null);
			return EditorPlugin.getInstance().globalEditorOperationsManager.getGlobalDirtyState();
		}
		
		/**
		 * Creates and adds an action to the the actionProvider
		 * 
		 * @author Mircea Negreanu
		 */
		private function createAndAddAction(label:String, id:String, parentId:String, icon:String, functionDelegate:Function, actionProvider:IActionProvider):void {
			var action:ActionBase;
			if (id != null) {
				action = new ComposedAction();				
			} else {
				action = new ActionBase();
			}
			
			action.label = label;
			action.id = id;
			action.parentId = parentId;
			action.icon = icon;
			action.functionDelegate = functionDelegate;
			actionProvider.getActions(null).push(action);
		}

		/**
		 * @author Mariana
		 * @author Mircea Negreanu
		 */
		private function showScreen(cls:Class):void {
			var handler:IPopupHandler = FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler();
			var content:IViewContent = new cls();
			handler.setViewContent(content).show();
			if (Object(content).hasOwnProperty("entityId")) {
				Object(content).entityId = WebCommonPlugin.getInstance().authenticationManager.currentUserLoggedIn.id;
			}
		}
		
		protected function welcomeReceivedFromServerHandler(event:BridgeEvent):void {
			// create the actionProvider from menu
			var menuActionProvider:VectorActionProvider = new VectorActionProvider();
			
			createAndAddAction("Administration", "administration", null, null, null,
				menuActionProvider);
			
			createAndAddAction("Organizations", null, "administration", 
				WebPlugin.getInstance().getResourceUrl("images/usr_admin/organization.png"), function():void {
					showScreen(OrganizationsScreen);
				}, menuActionProvider);
			
			createAndAddAction("Groups", null, "administration", 
				WebPlugin.getInstance().getResourceUrl("images/usr_admin/group.png"), function():void {
					showScreen(GroupsScreen);
				}, menuActionProvider);
			
			createAndAddAction("Users", null, "administration", 
				WebPlugin.getInstance().getResourceUrl("images/usr_admin/user.png"), function():void {
					showScreen(UsersScreen);
				}, menuActionProvider);
			
			createAndAddAction("Permissions", null, "administration", 
				WebPlugin.getInstance().getResourceUrl("images/usr_admin/permission.png"), function():void {
					showScreen(PermissionsScreen);
				}, menuActionProvider);
			
			createAndAddAction("Window", "window", null, null, null, menuActionProvider);
			
			if (perspectives.length > 0) {
				createAndAddAction("Open Perspective", "show_perspective", "window", null, null, menuActionProvider);
				
				for each (var perspective:Perspective in perspectives) {
					menuActionProvider.getActions(null).push(new SwitchPerspectiveAction(perspective));
				}
			}
			
			createAndAddAction("Show View", "show_view", "window", null, null, menuActionProvider);
			
			menuActionProvider.getActions(null).push(
				new ShowViewAction(FlexUtilGlobals.getInstance().composedViewProvider.getViewProvider(ExplorerViewProvider.ID)));
			menuActionProvider.getActions(null).push(
				new ShowViewAction(FlexUtilGlobals.getInstance().composedViewProvider.getViewProvider(PropertiesViewProvider.ID)));
			menuActionProvider.getActions(null).push(
				new ShowViewAction(FlexUtilGlobals.getInstance().composedViewProvider.getViewProvider(OpenResourcesViewProvider.ID)));
			menuActionProvider.getActions(null).push(
				new ShowViewAction(FlexUtilGlobals.getInstance().composedViewProvider.getViewProvider(RegexActionsViewProvider.ID)));
			menuActionProvider.getActions(null).push(
				new ShowViewAction(FlexUtilGlobals.getInstance().composedViewProvider.getViewProvider(RegexMatchesViewProvider.ID)));
			
			
			createAndAddAction("User", "user", null,  
				WebPlugin.getInstance().getResourceUrl("images/usr_admin/user.png"), 
				null, menuActionProvider);
			
			createAndAddAction("My Account", null, "user", 
				WebPlugin.getInstance().getResourceUrl("images/usr_admin/user.png"), function():void {
					showScreen(UserForm);
				},menuActionProvider);
			
			createAndAddAction("Switch User", null, "user", null, function():void {
				WebCommonPlugin.getInstance().authenticationManager.showAuthenticationView(true);
			}, menuActionProvider);
			
			createAndAddAction("Logout", null, "user", null, function():void {
				CommunicationPlugin.getInstance().bridge.disconnectBecauseUserLoggedOut();
			}, menuActionProvider);
			
			createAndAddAction("Help", "help", null, null, null, menuActionProvider);
			
			createAndAddAction("Lean and Discuss (opens a new window)", null, "help",  null, function():void {
				navigateToURL(new URLRequest("http://learn-discuss.flower-platform.com/flower_dev_center"), "_blank");
			}, menuActionProvider);
			
			var hBox:HBox = new HBox();
			hBox.percentWidth = 100;
			
			// create the menu
			var menuBar:WebMenuBar = new WebMenuBar(menuActionProvider);
			menuBar.percentWidth = 100;
			hBox.addChild(menuBar);
			
			// removed all the other buttons (were replaced by the menu)
			// this is the only one left
			var btn:Button = new Button();
			btn.label = "Get Current User";
			btn.addEventListener(MouseEvent.CLICK, function(evt:MouseEvent):void {
				btn.label = "Logged in as: " + WebCommonPlugin.getInstance().authenticationManager.currentUserLoggedIn.name;
			});
			hBox.addChild(btn);
			
			IVisualElementContainer(FlexGlobals.topLevelApplication).addElementAt(hBox, 0);
						
			perspectives[0].resetPerspective(Workbench(FlexUtilGlobals.getInstance().workbench));
		}
			
		override protected function registerMessageBundle():void {
			super.registerMessageBundle();
		}
		
		public function getPerspective(id:String):Perspective {
			for (var i:int = 0; i < perspectives.length; i++) {
				if (perspectives[i].id == id) {
					return perspectives[i];
				}
			}
			return null;
		}
		
		/**
		 * @author Tache Razvan Mihai
		 */
		override protected function registerClassAliases():void {
		}
	}
}
