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
package org.flowerplatform.communication {
	
	import flash.utils.Dictionary;
	
	import org.flowerplatform.blazeds.BlazeDSBridge;
	import org.flowerplatform.blazeds.BridgeEvent;
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.communication.callback.InvokeCallbackClientCommand;
	import org.flowerplatform.communication.command.AbstractClientCommand;
	import org.flowerplatform.communication.command.CompoundClientCommand;
	import org.flowerplatform.communication.command.CompoundServerCommand;
	import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
	import org.flowerplatform.communication.command.HelloServerCommand;
	import org.flowerplatform.communication.command.ServerSnapshotClientCommand;
	import org.flowerplatform.communication.command.WelcomeClientCommand;
	import org.flowerplatform.communication.progress_monitor.remote.CreateProgressMonitorStatefulClientCommand;
	import org.flowerplatform.communication.progress_monitor.remote.ProgressMonitorStatefulLocalClient;
	import org.flowerplatform.communication.sequential_execution.SequentialExecutionServerCommand;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.stateful_service.InvokeStatefulClientMethodClientCommand;
	import org.flowerplatform.communication.stateful_service.InvokeStatefulServiceMethodServerCommand;
	import org.flowerplatform.communication.stateful_service.StatefulClientRegistry;
	import org.flowerplatform.communication.transferable_object.ReferenceHolder;
	import org.flowerplatform.communication.tree.remote.GenericTreeStatefulClientLocalState;
	import org.flowerplatform.communication.tree.remote.PathFragment;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.plugin.FlexPluginDescriptor;
		
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
			registerClassAliasFromAnnotation(CompoundServerCommand);
			registerClassAliasFromAnnotation(DisplaySimpleMessageClientCommand);
			registerClassAliasFromAnnotation(InvokeCallbackClientCommand);
			registerClassAliasFromAnnotation(InvokeServiceMethodServerCommand);
			registerClassAliasFromAnnotation(SequentialExecutionServerCommand);
			registerClassAliasFromAnnotation(InvokeStatefulServiceMethodServerCommand);
			registerClassAliasFromAnnotation(InvokeStatefulClientMethodClientCommand);
			registerClassAliasFromAnnotation(ServerSnapshotClientCommand);
			registerClassAliasFromAnnotation(HelloServerCommand);
			registerClassAliasFromAnnotation(WelcomeClientCommand);
			registerClassAliasFromAnnotation(ReferenceHolder);
			
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
			bridge.addEventListener(BridgeEvent.CONNECTED, handleConnected);
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
		
		/**
		 * Notifies plugins that the client has connected to the server.
		 * 
		 * @author Mariana Gheorghe
		 */
		protected function handleConnected(event:BridgeEvent):void {
			for each (var flexPluginDescriptor:FlexPluginDescriptor in FlexUtilGlobals.getInstance().flexPluginManager.flexPluginEntries) {
				flexPluginDescriptor.flexPlugin.handleConnectedToServer();
			}
		}
	}
}