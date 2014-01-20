package org.flowerplatform.idea
{
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.persistence.SashLayoutData;
	import com.crispico.flower.util.layout.persistence.StackLayoutData;
	import com.crispico.flower.util.layout.persistence.WorkbenchLayoutData;
	
	import flash.external.ExternalInterface;
	
	import mx.collections.ArrayCollection;
	import mx.core.FlexGlobals;
	import mx.core.IVisualElementContainer;
	import mx.core.mx_internal;
	
	import org.flowerplatform.blazeds.BridgeEvent;
	import org.flowerplatform.common.CommonPlugin;
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.command.HelloServerCommand;
	import org.flowerplatform.communication.stateful_service.StatefulClient;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.remote.EditorStatefulClient;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.layout.event.ViewsRemovedEvent;
	
	import spark.components.Button;
	
	use namespace mx_internal;
	
	
	public class IdeaPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:IdeaPlugin;
		
		public static function getInstance():IdeaPlugin {
			return INSTANCE;
		}
		
		
		override public function start():void{
			
			super.start();
			
			INSTANCE = this;
			
			var workbench:Workbench = Workbench(FlexUtilGlobals.getInstance().workbench);
			var wld:WorkbenchLayoutData = new WorkbenchLayoutData();
			var sashEditor:SashLayoutData = new SashLayoutData();
			var application:IVisualElementContainer = IVisualElementContainer(FlexGlobals.topLevelApplication);
			
			//application.addElement(workbench);
//			application.addElement(new Button());

			//FlexUtilGlobals.getInstance().workbench = workbench;
			//workbench.viewProvider = FlexUtilGlobals.getInstance().composedViewProvider;
//			workbench.percentHeight = 100;
////			IVisualElementContainer(FlexGlobals.topLevelApplication).addElement(workbench);
			workbench.addEventListener(ViewsRemovedEvent.VIEWS_REMOVED, EditorPlugin.getInstance().viewsRemoved);
			
			sashEditor.isEditor = true;
			sashEditor.direction = SashLayoutData.HORIZONTAL;
			sashEditor.ratios = new ArrayCollection([100]);
			sashEditor.mrmRatios = new ArrayCollection([0]);
			sashEditor.parent = wld;
			wld.children.addItem(sashEditor);
			
			var stackEditor:StackLayoutData = new StackLayoutData();
			stackEditor.parent = sashEditor;
			sashEditor.children.addItem(stackEditor);
			
			workbench.load(wld);
//			IVisualElementContainer(FlexGlobals.topLevelApplication).addElement(workbench);
			
			CommunicationPlugin.getInstance().bridge.addEventListener(BridgeEvent.CONNECTED, connectedHandler);
			CommunicationPlugin.getInstance().bridge.connect();
			
			CommunicationPlugin.getInstance().bridge.addEventListener(BridgeEvent.WELCOME_RECEIVED_FROM_SERVER, 
				welcomeReceivedFromServerHandler);
			ExternalInterface.addCallback("isEditableResoucesOpened", isEditableResoucesOpened);
		}
		
		public function execute():void{
			IdeaPlugin.getInstance().execute();
		}
		
		public function connectedHandler(event:BridgeEvent):void {
//			ModalSpinner.addGlobalModalSpinner("Initializing...");
			
			// We send always the Hello to the server, regardless of app init or not 
			var helloServerCommand:HelloServerCommand = new HelloServerCommand();
			helloServerCommand.clientApplicationVersion = CommonPlugin.VERSION;
//			BaseFlowerDiagramEditor.instance.sendObject(helloServerCommand);
			helloServerCommand.firstWelcomeWithInitializationsReceived = CommunicationPlugin.getInstance().firstWelcomeWithInitializationsReceived;
			CommunicationPlugin.getInstance().bridge.sendObject(helloServerCommand);
		}
		
		protected function welcomeReceivedFromServerHandler(event:BridgeEvent):void {
//			return (Workbench(FlexUtilGlobals.getInstance().workbench));
//			ModalSpinner.removeGlobalModalSpinner();
			CommonPlugin.getInstance().handleLinkWithQueryStringDecoded(FlexGlobals.topLevelApplication.parameters);
		}
		
		override protected function registerMessageBundle():void {
			super.registerMessageBundle();
		}
		
		override protected function registerClassAliases():void {			
		}
		
		public function isEditableResoucesOpened( path:String ):Boolean {
			for each (var sc:StatefulClient in CommunicationPlugin.getInstance().statefulClientRegistry.mx_internal::statefulClientsList) {
				if ( EditorStatefulClient(sc).editableResourcePath == path) {
					//if it's opened, set focus on it
					EditorStatefulClient(sc).revealEditor();
					return true;	
				}
			}
			return false;
		}
		
	}
}