package org.flowerplatform.web {
	import com.crispico.flower.util.layout.Workbench;
	
	import flash.events.MouseEvent;
	
	import mx.containers.HBox;
	import mx.core.FlexGlobals;
	import mx.core.IVisualElementContainer;
	
	import spark.components.Button;
	
	import org.flowerplatform.blazeds.BridgeEvent;
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.popup.IPopupHandler;
	import org.flowerplatform.web.common.WebCommonPlugin;
	import org.flowerplatform.web.layout.DefaultPerspective;
	import org.flowerplatform.web.layout.Perspective;
	import org.flowerplatform.web.security.ui.GroupsScreen;
	import org.flowerplatform.web.security.ui.OrganizationsScreen;
	import org.flowerplatform.web.security.ui.PermissionsScreen;
	import org.flowerplatform.web.security.ui.UserForm;
	import org.flowerplatform.web.security.ui.UsersScreen;
	
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
		
		public var workbench:Workbench;
		
		override public function start():void {
			super.start();
			if (INSTANCE != null) {
				throw new Error("Plugin " + Utils.getClassNameForObject(this, true) + " has already been started");
			}
			INSTANCE = this;

			// pass the same descriptor; to be used for images (that need the descriptor for the URL)
			webCommonPlugin.flexPluginDescriptor = flexPluginDescriptor;	
			webCommonPlugin.start();
			
			var hBox:HBox = new HBox();
			test_addButton("User Form", UserForm, hBox, 6);
			test_addButton("Users Screen", UsersScreen, hBox);
			test_addButton("Organizations Screen", OrganizationsScreen, hBox);
			test_addButton("Groups Screen", GroupsScreen, hBox);
			test_addButton("Permissions Screen", PermissionsScreen, hBox);
			IVisualElementContainer(FlexGlobals.topLevelApplication).addElement(hBox);
			
			workbench= new Workbench();
			workbench.viewProvider = FlexUtilGlobals.getInstance().composedViewProvider;
			workbench.percentHeight = 100;
			workbench.percentWidth = 100;
			IVisualElementContainer(FlexGlobals.topLevelApplication).addElement(workbench);
			
			CommunicationPlugin.getInstance().bridge.addEventListener(BridgeEvent.WELCOME_RECEIVED_FROM_SERVER, welcomeReceivedFromServerHandler);
		}
		
		/**
		 * @author Mariana
		 */
		private function test_addButton(label:String, cls:Class, hBox:HBox, entityId:int = -1):void {
			var btn:Button = new Button();
			btn.label = label;
			btn.addEventListener(MouseEvent.CLICK, function(evt:MouseEvent):void {
				var handler:IPopupHandler = FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler();
				handler.setPopupContentClass(cls).show();
				var content:Object = handler.getPopupContent();
				if (content.hasOwnProperty("entityId")) {
					content.entityId = entityId;
				}
			});
			hBox.addElement(btn);
		}
		
		protected function welcomeReceivedFromServerHandler(event:BridgeEvent):void {
			perspectives[0].resetPerspective(workbench);
		}
		
		override public function setupExtensionPointsAndExtensions():void {
			super.setupExtensionPointsAndExtensions();
			webCommonPlugin.setupExtensionPointsAndExtensions();
			
			perspectives.push(new DefaultPerspective());
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
	}
}