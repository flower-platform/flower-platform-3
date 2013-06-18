package org.flowerplatform.communication {
	
	import flash.utils.Dictionary;
	
	import mx.core.FlexGlobals;
	import mx.core.IVisualElementContainer;
	
	import org.flowerplatform.blazeds.BlazeDSBridge;
	import org.flowerplatform.blazeds.BridgeEvent;
	import org.flowerplatform.common.CommonPlugin;
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.communication.callback.InvokeCallbackClientCommand;
	import org.flowerplatform.communication.command.AbstractClientCommand;
	import org.flowerplatform.communication.command.CompoundClientCommand;
	import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
	import org.flowerplatform.communication.command.HelloServerCommand;
	import org.flowerplatform.communication.command.ServerSnapshotClientCommand;
	import org.flowerplatform.communication.command.WelcomeClientCommand;
	import org.flowerplatform.communication.progress_monitor.remote.CreateProgressMonitorStatefulClientCommand;
	import org.flowerplatform.communication.progress_monitor.remote.ProgressMonitorStatefulClient;
	import org.flowerplatform.communication.progress_monitor.remote.ProgressMonitorStatefulLocalClient;
	import org.flowerplatform.communication.sequential_execution.SequentialExecutionServerCommand;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.stateful_service.InvokeStatefulClientMethodClientCommand;
	import org.flowerplatform.communication.stateful_service.InvokeStatefulServiceMethodServerCommand;
	import org.flowerplatform.communication.stateful_service.StatefulClientRegistry;
	import org.flowerplatform.communication.tree.GenericTreeList;
	import org.flowerplatform.communication.tree.remote.GenericTreeStatefulClient;
	import org.flowerplatform.communication.tree.remote.GenericTreeStatefulClientLocalState;
	import org.flowerplatform.communication.tree.remote.PathFragment;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.Utils;
	
	import temp.tree.ProjectExplorerTreeNew;
	
	public class CommunicationPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:CommunicationPlugin;

		public static function getInstance():CommunicationPlugin {
			return INSTANCE;
		}
		
		public var bridge:BlazeDSBridge;
		
		public var lastCallbackId:int = 0;
		
		public var pendingCallbacks:Dictionary = new Dictionary();
		
		public var statefulClientRegistry:StatefulClientRegistry = new StatefulClientRegistry();
		
		public var firstWelcomeWithInitializationsReceived:Boolean;

		override protected function registerClassAliases():void {
			registerClassAliasFromAnnotation(CompoundClientCommand);
			registerClassAliasFromAnnotation(DisplaySimpleMessageClientCommand);
			registerClassAliasFromAnnotation(InvokeCallbackClientCommand);
			registerClassAliasFromAnnotation(InvokeServiceMethodServerCommand);
			registerClassAliasFromAnnotation(SequentialExecutionServerCommand);
			registerClassAliasFromAnnotation(InvokeStatefulServiceMethodServerCommand);
			registerClassAliasFromAnnotation(InvokeStatefulClientMethodClientCommand);
			registerClassAliasFromAnnotation(ServerSnapshotClientCommand);
			registerClassAliasFromAnnotation(HelloServerCommand);
			registerClassAliasFromAnnotation(WelcomeClientCommand);
			
			registerClassAliasFromAnnotation(GenericTreeStatefulClientLocalState);
			registerClassAliasFromAnnotation(TreeNode);
			registerClassAliasFromAnnotation(PathFragment);
			
			registerClassAliasFromAnnotation(ProgressMonitorStatefulLocalClient);			
			registerClassAliasFromAnnotation(CreateProgressMonitorStatefulClientCommand);			
		}
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
		}
		
		override public function start():void {
			super.start();
			
			bridge = new BlazeDSBridge();
			bridge.addEventListener(BridgeEvent.OBJECT_RECEIVED, handleReceivedObject);
//			bridge.connect(); 
//			bridge.addEventListener(BridgeEvent.CONNECTED, function (event:BridgeEvent):void {
////				var tree:ProjectExplorerTreeNew = new ProjectExplorerTreeNew();
////				IVisualElementContainer(FlexGlobals.topLevelApplication).addElement(tree);
//				var treeList:GenericTreeList = new GenericTreeList();
//				var statefulClient:GenericTreeStatefulClient = new GenericTreeStatefulClient();
//				
//				treeList.dispatchEnabled = true;
//				treeList.statefulClient = statefulClient;
//				IVisualElementContainer(FlexGlobals.topLevelApplication).addElement(treeList);
//				
//				statefulClient.statefulServiceId = "ProjectExplorerTreeStatefulService";
//				statefulClient.clientIdPrefix = "Project Explorer";
//				statefulClient.treeList = treeList;
//				
//				CommunicationPlugin.getInstance().statefulClientRegistry.register(statefulClient, null);
//
//			});
		}

		override protected function registerMessageBundle():void {
			// do nothing; this plugin doesn't have a .resources (yet)
		}
		
		protected function handleReceivedObject(event:BridgeEvent):void {
			if (event.receivedObject == null || 
				!(event.receivedObject is AbstractClientCommand)) { // TODO CS/FP2: after connecting with credentials, a plain Object arrives here; why/from where?
				return;
			}
			AbstractClientCommand(event.receivedObject).execute();
		}
	}
}
