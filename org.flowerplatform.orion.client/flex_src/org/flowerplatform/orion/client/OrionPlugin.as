package org.flowerplatform.orion.client {
	
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.persistence.SashLayoutData;
	import com.crispico.flower.util.layout.persistence.StackLayoutData;
	import com.crispico.flower.util.layout.persistence.WorkbenchLayoutData;
	
	import flash.external.ExternalInterface;
	import flash.geom.Utils3D;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.core.FlexGlobals;
	
	import org.flowerplatform.blazeds.BridgeEvent;
	import org.flowerplatform.common.CommonPlugin;
	import org.flowerplatform.common.link.ILinkHandler;
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.command.HelloServerCommand;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NewJavaClassDiagramAction;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import org.flowerplatform.flexutil.layout.event.ViewsRemovedEvent;

	public class OrionPlugin extends AbstractFlowerFlexPlugin implements ILinkHandler {
		
		protected static var INSTANCE:OrionPlugin;
		
		public static function getInstance():OrionPlugin {
			return INSTANCE;
		}
		
		public static const CREATE_DIAGRAM:String = "orionCreateDiagram";
		public static const ADD_TO_DIAGRAM:String = "orionAddToDiagram";
		
		override public function preStart():void {
			super.preStart();
			
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
//			CommonPlugin.getInstance().linkHandlers[OPEN_RESOURCES] = this;
			CommonPlugin.getInstance().linkHandlers[CREATE_DIAGRAM] = this;
			CommonPlugin.getInstance().linkHandlers[ADD_TO_DIAGRAM] = this;
		}
		
		override public function start():void {
			super.start();			
			Workbench(FlexUtilGlobals.getInstance().workbench).addEventListener(ViewsRemovedEvent.VIEWS_REMOVED, EditorPlugin.getInstance().viewsRemoved);
			
			CommunicationPlugin.getInstance().bridge.addEventListener(BridgeEvent.CONNECTED, handleConnected);
			
			CommunicationPlugin.getInstance().bridge.connect();
			CommunicationPlugin.getInstance().bridge.addEventListener(BridgeEvent.WELCOME_RECEIVED_FROM_SERVER, welcomeReceivedFromServerHandler);
		}
		
		public function handleConnected(event:BridgeEvent):void {			
			var hello:HelloServerCommand = new HelloServerCommand();
			hello.clientApplicationVersion = CommonPlugin.VERSION;
			hello.firstWelcomeWithInitializationsReceived = CommunicationPlugin.getInstance().firstWelcomeWithInitializationsReceived;
			CommunicationPlugin.getInstance().bridge.sendObject(hello);			
		}
		
		protected function welcomeReceivedFromServerHandler(event:BridgeEvent):void {
			loadWorkbenchLayoutData();
			
			// handle browser url
			CommonPlugin.getInstance().handleLinkWithQueryStringDecoded(FlexGlobals.topLevelApplication.parameters);
		}
				
		private function loadWorkbenchLayoutData():void {		
			var wld:WorkbenchLayoutData = new WorkbenchLayoutData();
			wld.direction = SashLayoutData.HORIZONTAL;
			wld.ratios = new ArrayCollection([25, 75]);
			wld.mrmRatios = new ArrayCollection([0, 0]);
					
			var sashEditor:SashLayoutData = new SashLayoutData();
			sashEditor.isEditor = true;
			sashEditor.direction = SashLayoutData.HORIZONTAL;
			sashEditor.ratios = new ArrayCollection([100]);
			sashEditor.mrmRatios = new ArrayCollection([0]);
			sashEditor.parent = wld;
			wld.children.addItem(sashEditor);
			
			var stackEditor:StackLayoutData = new StackLayoutData();
			stackEditor.parent = sashEditor;
			sashEditor.children.addItem(stackEditor);
			
			Workbench(FlexUtilGlobals.getInstance().workbench).load(wld, true, true);
		}
		
		public function handleLink(command:String, parameters:String):void {
			if (command == CREATE_DIAGRAM) {
				var cmd:NewJavaClassDiagramAction = new NewJavaClassDiagramAction();
				cmd.parentPath = parameters;
				CommunicationPlugin.getInstance().bridge.sendObject(cmd);				
			} else if (command == ADD_TO_DIAGRAM) {
				CommunicationPlugin.getInstance().bridge.sendObject(
					new InvokeServiceMethodServerCommand(
						"orionOperationsService", "getPaths", 
						[parameters], this, addToDiagram));
			}
		}
		
		public function addToDiagram(paths:ArrayCollection):void {
			NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).service_handleDragOnDiagram(paths);
		}
	}
}