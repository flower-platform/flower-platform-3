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
	import com.crispico.flower.util.layout.Workbench;
	
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.containers.HBox;
	import mx.core.FlexGlobals;
	import mx.core.IVisualElementContainer;
	import mx.managers.ToolTipManager;
	
	import org.flowerplatform.blazeds.BridgeEvent;
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.tree.remote.PathFragment;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.layout.event.ViewsRemovedEvent;
	import org.flowerplatform.flexutil.popup.IPopupHandler;
	import org.flowerplatform.flexutil.selection.SelectionChangedEvent;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	import org.flowerplatform.web.common.WebCommonPlugin;
	import org.flowerplatform.web.layout.DefaultPerspective;
	import org.flowerplatform.web.layout.Perspective;
	import org.flowerplatform.web.security.ui.GroupsScreen;
	import org.flowerplatform.web.security.ui.OrganizationsScreen;
	import org.flowerplatform.web.security.ui.PermissionsScreen;
	import org.flowerplatform.web.security.ui.UserForm;
	import org.flowerplatform.web.security.ui.UserFormViewProvider;
	import org.flowerplatform.web.security.ui.UsersScreen;
	
	import org.flowerplatform.properties.PropertiesPlugin;
	
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
			
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new UserFormViewProvider());			
		}
		
		override public function start():void {
			super.start();
			// pass the same descriptor; to be used for images (that need the descriptor for the URL)
			webCommonPlugin.flexPluginDescriptor = flexPluginDescriptor;	
			webCommonPlugin.start();
			
			ToolTipManager.showDelay = 0;
			ToolTipManager.toolTipClass = HTMLToolTip;
			
			var workbench:Workbench = new Workbench();
			FlexUtilGlobals.getInstance().workbench = workbench;
			workbench.viewProvider = FlexUtilGlobals.getInstance().composedViewProvider;
			workbench.percentHeight = 100;
			workbench.percentWidth = 100;
			IVisualElementContainer(FlexGlobals.topLevelApplication).addElement(workbench);
			workbench.addEventListener(ViewsRemovedEvent.VIEWS_REMOVED, EditorPlugin.getInstance().viewsRemoved);
			
			var hBox:HBox = new HBox();
			test_addButton("User Form", UserForm, hBox);
			test_addButton("Users Screen", UsersScreen, hBox);
			test_addButton("Organizations Screen", OrganizationsScreen, hBox);
			test_addButton("Groups Screen", GroupsScreen, hBox);
			test_addButton("Permissions Screen", PermissionsScreen, hBox);

			var btn:Button = new Button();
			btn.label = "Logout";
			btn.addEventListener(MouseEvent.CLICK, function(evt:MouseEvent):void {
				CommunicationPlugin.getInstance().bridge.disconnectBecauseUserLoggedOut();
			});
			hBox.addChild(btn);
			
			btn = new Button();
			btn.label = "Switch";
			btn.addEventListener(MouseEvent.CLICK, function(evt:MouseEvent):void {
				WebCommonPlugin.getInstance().authenticationManager.showAuthenticationView(true);
			});
			hBox.addChild(btn);
			
			btn = new Button();
			btn.label = "Get Current User";
			btn.addEventListener(MouseEvent.CLICK, function(evt:MouseEvent):void {
				btn.label = "Logged in as: " + WebCommonPlugin.getInstance().authenticationManager.currentUserLoggedIn.name;
			});
			hBox.addChild(btn);
						
			IVisualElementContainer(FlexGlobals.topLevelApplication).addElementAt(hBox, 0);
					
			
			CommunicationPlugin.getInstance().bridge.addEventListener(BridgeEvent.WELCOME_RECEIVED_FROM_SERVER, welcomeReceivedFromServerHandler);
			FlexUtilGlobals.getInstance().selectionManager.addEventListener(SelectionChangedEvent.SELECTION_CHANGED, function (event:SelectionChangedEvent):void {
				trace("Selection changed: " + event.selection);
			});
		}
		
		/**
		 * @author Mariana
		 */
		private function test_addButton(label:String, cls:Class, hBox:HBox):void {
			var btn:Button = new Button();
			btn.label = label;
			btn.addEventListener(MouseEvent.CLICK, function(evt:MouseEvent):void {
				var handler:IPopupHandler = FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler();
				var content:IViewContent = new cls();
				handler.setViewContent(content).show();
				if (Object(content).hasOwnProperty("entityId")) {
					Object(content).entityId = WebCommonPlugin.getInstance().authenticationManager.currentUserLoggedIn.id;
				}
			});
			hBox.addElement(btn);
		}
		
		protected function welcomeReceivedFromServerHandler(event:BridgeEvent):void {
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
