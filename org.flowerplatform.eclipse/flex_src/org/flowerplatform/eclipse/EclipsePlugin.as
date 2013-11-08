package org.flowerplatform.eclipse
{
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.persistence.SashLayoutData;
	import com.crispico.flower.util.layout.persistence.WorkbenchLayoutData;
	
	import flash.display.DisplayObject;
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
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.stateful_service.StatefulClient;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.remote.EditorStatefulClient;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.layout.event.ViewsRemovedEvent;
	
	use namespace mx_internal;
	
	
	public class EclipsePlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:EclipsePlugin;
		
		public static function getInstance():EclipsePlugin {
			return INSTANCE;
		}
		
//		private var workbench:Workbench;
		
		override public function start():void{
			
			super.start();
			
			INSTANCE = this;
				
			CommunicationPlugin.getInstance().bridge.addEventListener(BridgeEvent.CONNECTED, connectedHandler);
			CommunicationPlugin.getInstance().bridge.connect();
			
			CommunicationPlugin.getInstance().bridge.addEventListener(BridgeEvent.WELCOME_RECEIVED_FROM_SERVER, 
				welcomeReceivedFromServerHandler);
			ExternalInterface.addCallback("isEditableResoucesOpened", isEditableResoucesOpened);
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
			var wld:WorkbenchLayoutData = new WorkbenchLayoutData();
			wld.direction = SashLayoutData.HORIZONTAL;
			wld.ratios = new ArrayCollection([100]);
			wld.mrmRatios = new ArrayCollection([0]);
			
			var sashEditor:SashLayoutData = new SashLayoutData();
			sashEditor.isEditor = true;
			sashEditor.direction = SashLayoutData.HORIZONTAL;
			sashEditor.ratios = new ArrayCollection([100]);
			sashEditor.mrmRatios = new ArrayCollection([0]);
			sashEditor.parent = wld;
			wld.children.addItem(sashEditor);
			
			Workbench(FlexUtilGlobals.getInstance().workbench).addEventListener(ViewsRemovedEvent.VIEWS_REMOVED, EditorPlugin.getInstance().viewsRemoved);			
			Workbench(FlexUtilGlobals.getInstance().workbench).load(wld);

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