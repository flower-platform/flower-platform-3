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
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.popup.IPopupContent;
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
		
		override public function preStart():void {
			super.preStart();
			webCommonPlugin.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
			
			perspectives.push(new DefaultPerspective());
		}
		
		override public function start():void {
			super.start();
			// pass the same descriptor; to be used for images (that need the descriptor for the URL)
			webCommonPlugin.flexPluginDescriptor = flexPluginDescriptor;	
			webCommonPlugin.start();
			
			var hBox:HBox = new HBox();
			test_addButton("User Form", new UserForm(), hBox, 6);
			test_addButton("Users Screen", new UsersScreen(), hBox);
			test_addButton("Organizations Screen", new OrganizationsScreen(), hBox);
			test_addButton("Groups Screen", new GroupsScreen(), hBox);
			test_addButton("Permissions Screen", new PermissionsScreen(), hBox);
			IVisualElementContainer(FlexGlobals.topLevelApplication).addElement(hBox);
			
			var workbench:Workbench = new Workbench();
			FlexUtilGlobals.getInstance().workbench = workbench;
			workbench.viewProvider = FlexUtilGlobals.getInstance().composedViewProvider;
			workbench.percentHeight = 100;
			workbench.percentWidth = 100;
			IVisualElementContainer(FlexGlobals.topLevelApplication).addElement(workbench);
			
			CommunicationPlugin.getInstance().bridge.addEventListener(BridgeEvent.WELCOME_RECEIVED_FROM_SERVER, welcomeReceivedFromServerHandler);
		}
		
		/**
		 * @author Mariana
		 */
		private function test_addButton(label:String, content:IPopupContent, hBox:HBox, entityId:int = -1):void {
			var btn:Button = new Button();
			btn.label = label;
			btn.addEventListener(MouseEvent.CLICK, function(evt:MouseEvent):void {
				var handler:IPopupHandler = FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler();
				handler.setPopupContent(content).show();
				if (Object(content).hasOwnProperty("entityId")) {
					Object(content).entityId = entityId;
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
	}
}
