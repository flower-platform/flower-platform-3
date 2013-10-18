package org.flowerplatform.eclipse
{
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.persistence.SashLayoutData;
	import com.crispico.flower.util.layout.persistence.WorkbenchLayoutData;
	
	import mx.collections.ArrayCollection;
	import mx.core.FlexGlobals;
	import mx.core.IVisualElementContainer;
	
	import org.flowerplatform.blazeds.BridgeEvent;
	import org.flowerplatform.common.CommonPlugin;
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.command.HelloServerCommand;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.layout.event.ViewsRemovedEvent;
	
	
	public class EclipsePlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:EclipsePlugin;
		
		public static function getInstance():EclipsePlugin {
			return INSTANCE;
		}
		
		private var workbench:Workbench;
		
		override public function start():void{
			
			super.start();
			
			INSTANCE = this;
			
			workbench = new Workbench();
			var wld:WorkbenchLayoutData = new WorkbenchLayoutData();
			var sashEditor:SashLayoutData = new SashLayoutData();
			var application:IVisualElementContainer = IVisualElementContainer(FlexGlobals.topLevelApplication);
			
			application.addElement(workbench);

			FlexUtilGlobals.getInstance().workbench = workbench;
			workbench.viewProvider = FlexUtilGlobals.getInstance().composedViewProvider;
			workbench.percentWidth = 100;
			workbench.percentHeight = 100;
//			IVisualElementContainer(FlexGlobals.topLevelApplication).addElement(workbench);
			workbench.addEventListener(ViewsRemovedEvent.VIEWS_REMOVED, EditorPlugin.getInstance().viewsRemoved);
			
			sashEditor.isEditor = true;
			sashEditor.direction = SashLayoutData.HORIZONTAL;
			sashEditor.ratios = new ArrayCollection([100]);
			sashEditor.mrmRatios = new ArrayCollection([0]);
			sashEditor.parent = wld;
			wld.children.addItem(sashEditor);
			
			workbench.load(wld);
//			IVisualElementContainer(FlexGlobals.topLevelApplication).addElement(workbench);
			
			CommunicationPlugin.getInstance().bridge.addEventListener(BridgeEvent.CONNECTED, connectedHandler);
			CommunicationPlugin.getInstance().bridge.connect();
			
			CommunicationPlugin.getInstance().bridge.addEventListener(BridgeEvent.WELCOME_RECEIVED_FROM_SERVER, 
				welcomeReceivedFromServerHandler);
		}
		
		public function execute():void{
			EclipsePlugin.getInstance().execute();
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
	}
}