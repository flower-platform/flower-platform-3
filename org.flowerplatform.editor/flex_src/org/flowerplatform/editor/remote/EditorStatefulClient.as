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
package  org.flowerplatform.editor.remote {
	
	import flash.events.Event;
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayCollection;
	import mx.core.Application;
	import mx.core.FlexGlobals;
	import mx.events.FlexEvent;
	import mx.utils.DescribeTypeCache;
	import mx.utils.ObjectUtil;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.sequential_execution.SequentialExecutionQueue;
	import org.flowerplatform.communication.stateful_service.IStatefulClientLocalState;
	import org.flowerplatform.communication.stateful_service.ServiceInvocationOptions;
	import org.flowerplatform.communication.stateful_service.StatefulClient;
	import org.flowerplatform.editor.BasicEditorDescriptor;
	import org.flowerplatform.editor.EditorFrontend;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.open_resources_view.OpenResourcesView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	/**
	 * @flowerModelElementId _Z2ZWYAJ3EeKGLqam5SXwYg
	 */
	public class EditorStatefulClient extends StatefulClient {
		
		/**
		 * @flowerModelElementId _NJb_YAcIEeK49485S7r3Vw
		 */
		public var editorDescriptor:BasicEditorDescriptor;
		
		/**
		 * @flowerModelElementId _94DU0AJ5EeKGLqam5SXwYg
		 */
		public var editableResourcePath:String;
		
		/**
		 * @flowerModelElementId _NJcmcAcIEeK49485S7r3Vw
		 */
		public var editableResourceStatus:EditableResource;
		
		/**
		 * The associated <code>EditorFrontend</code>s.
		 * @flowerModelElementId _NJcmcQcIEeK49485S7r3Vw
		 */
		public var editorFrontends:ArrayCollection = new ArrayCollection();
		
		private var seq:SequentialExecutionQueue = new SequentialExecutionQueue();
		
		public var collaborativeFigureModels:ArrayCollection = new ArrayCollection();

		/**
		 * @flowerModelElementId _byXMcAJ6EeKGLqam5SXwYg
		 */
		public override function getStatefulClientId():String {	
			return editorDescriptor.calculateStatefulClientId(editableResourcePath);
		}
		
		public override function getCurrentStatefulClientLocalState(dataFromRegistrator:Object = null):IStatefulClientLocalState {	
			var state:EditorStatefulClientLocalState = new EditorStatefulClientLocalState();
			state.editableResourcePath = editableResourcePath;
			return state;
		}

		/**
		 * Abstract method.
		 * 
		 * <p>
		 * Should return <code>true</code> if the client applies the modification
		 * immediately without waiting for server's response (e.g. text files). 
		 * <code>false</code> otherwise (e.g. diagrams).
		 * 
		 * @see Method with same name from Java Class <code>EditorBackend</code>.
		 * @flowerModelElementId _NJdNgQcIEeK49485S7r3Vw
		 */
		protected function areLocalUpdatesAppliedImmediately():Boolean {
			throw new Error("This method should be implemented.");
		}
		
		/**
		 * Should be overridden (don't forget super!), in order to initialize
		 * the new editor with local data from the existing editor.
		 * 
		 * <p>
		 * This implementation forces the resource status bar to refresh.
		 * 
		 * @flowerModelElementId _QiTtIAJ9EeKGLqam5SXwYg
		 */
		protected function copyLocalDataFromExistingEditorToNewEditor(existingEditor:EditorFrontend, newEditor:EditorFrontend):void {
			// if the resource is open in more than one editor when the app is started/refreshed, the resource has not yet arrived from the server
			if (editableResourceStatus)
				newEditor.editableResourceStatusUpdated();
		}
		/**
		 * If <code>areLocalUpdatesAppliedImmediately()</code> dispatches the updates
		 * to other opened <code>EditorFrontend</code>s.
		 * 
		 * <p>
		 * Updates the lock expire time of the <code>EditableResource</code>. We do this
		 * here locally to avoid to receive data from the server (which may be quite verbose
		 * for text editors for example).
		 * 
		 * <p>
		 * Sends the updates to the server (i.e. attempt an update, which may fail if the
		 * lock is acquired by someone else).
		 * 
		 * @flowerModelElementId _NJfCsgcIEeK49485S7r3Vw
		 */
		public function attemptUpdateContent(editor:EditorFrontend, content:Object, serviceInvocationOptions:ServiceInvocationOptions = null):Object {
			if (areLocalUpdatesAppliedImmediately()) {
				for each (var editorFrontend:EditorFrontend in editorFrontends) {
					if (editorFrontend != editor) {
						editorFrontend.executeContentUpdateLogic(content, false);
					}
				}
			}
			
			// update the lock expire time, because the server won't contact us to update this time
			if (editableResourceStatus != null && editableResourceStatus.locked && editableResourceStatus.lockOwner.communicationChannelId == CommunicationPlugin.getInstance().bridge.communicationChannelId) {
				editableResourceStatus.lockExpireTime = new Date(new Date().time + EditorPlugin.getInstance().lockLeaseSeconds * 1000);
			}
			
			if (serviceInvocationOptions == null) {
				serviceInvocationOptions = new ServiceInvocationOptions();
			}
			serviceInvocationOptions.setSequentialExecutionQueue(seq);
			
			return invokeServiceMethod("attemptUpdateEditableResourceContent",	[editableResourcePath, content], serviceInvocationOptions);
		}
		
		/**
		 * @param dataFromRegistrator By convention (from the parent class/system), this is <code>null</code>
		 * 		when the system reconnects (i.e. after a communication failure). By convention (from this class/system), this method
		 * 		is only invoked by the <code>EditorDescriptor</code>, when the view (i.e. <code>EditorFrontend</code>) is created.
		 * 		<code>dataFromRegistrator</code> is a dynamic object { editorFrontend, openForcedByServer }
		 * @flowerModelElementId _CSYDsAJ9EeKGLqam5SXwYg
		 */ 
		override public function subscribeToStatefulService(dataFromRegistrator:Object):void {
			hasBeenUnsubscribedForcefully = false;
			
			if (dataFromRegistrator != null) {
				// a new view was added; init these fields
				var newEditorFrontend:EditorFrontend = EditorFrontend(dataFromRegistrator.editorFrontend);
				newEditorFrontend.editorInput = editableResourcePath;
				newEditorFrontend.editorStatefulClient = this;
				editorFrontends.addItem(newEditorFrontend);	
			}
			
			if (dataFromRegistrator != null && editorFrontends.length > 1) {
				// a 2nd, 3rd, etc view has been opened for the editorInput
				// i.e. at least one editor already open on client
				// no comm to the server; init the editor locally
				newEditorFrontend.addEventListener(FlexEvent.CREATION_COMPLETE, 
					function(e:Event):void {
						// we call from this callback because all the initializations will probably
						// need the visual component to be fully created (e.g. mxml items which are not
						// created at construction)
						copyLocalDataFromExistingEditorToNewEditor(EditorFrontend(editorFrontends[0]), newEditorFrontend);
					}
				);
			} else if (dataFromRegistrator == null || !dataFromRegistrator.openForcedByServer) {
				// if subscribe is because reconnect, or because the first view has been opened
				// do the original logic, i.e. subscribe to server. If the open was forced by the
				// server, we don't subscribe, because the server already did the subscriptions
				super.subscribeToStatefulService(dataFromRegistrator);
			}
		}
		
		/**
		 * @flowerModelElementId _NJgQ0QcIEeK49485S7r3Vw
		 */
		override public function unsubscribeFromStatefulService(dataFromUnregistrator:Object):Boolean {
			// we use a for loop even for the "remove only one" use case, because
			// calling .getItemIndex() would do a loop as well
			for (var i:int = editorFrontends.length - 1; i >= 0; i--) {
				var editorFrontend:EditorFrontend = EditorFrontend(editorFrontends[i]);
				if (dataFromUnregistrator == editorFrontend || dataFromUnregistrator == null || hasBeenUnsubscribedForcefully) {
					// all editors need to be closed or the current editor is among the selected editors
					unsubscribeFromStatefulService_removeEditorFrontend(editorFrontend, i);
				}
			}

			if (editorFrontends.length == 0) {
				// no more open editors
				unsubscribeFromStatefulService_beforeUnsubscribeFromStatefulService(dataFromUnregistrator);
				
				return super.unsubscribeFromStatefulService(dataFromUnregistrator);
				
				// TODO/STFL aici va veni cod pentru ER slave
				// nu stiu la ce m-am referit, dar nu e bine; caci ar putea scoate un SC care e inainte in lista, deci ar strica iteratia de 
				// SCR.allClUnsubFor...
			} else {
				// nothing communicated to server;
				return false;
			}
			
		}
		
		protected function unsubscribeFromStatefulService_removeEditorFrontend(editorFrontend:EditorFrontend, editorFrontendIndex:int):void {
			editorFrontends.removeItemAt(editorFrontendIndex);
//			editorFrontend.resourceStatusBar.collaborativeDiagramViewer.setRootModel(null);
			FlexUtilGlobals.getInstance().workbench.closeView(editorFrontend, false);			
		}
		
		protected function unsubscribeFromStatefulService_beforeUnsubscribeFromStatefulService(dataFromUnregistrator:Object):void {

			if (OpenResourcesView.INSTANCE != null) {
				OpenResourcesView.INSTANCE.editableResourceStatusRemoved(editableResourceStatus);
			}			
			
			if (editableResourceStatus != null && editableResourceStatus.masterEditorStatefulClientId != null) {
				// slave ER; unregister from its master (i.e. slaveEditableResources)
				
				var masterESC:EditorStatefulClient = EditorStatefulClient(CommunicationPlugin.getInstance().statefulClientRegistry.getStatefulClientById(editableResourceStatus.masterEditorStatefulClientId));
				if (masterESC == null) {
					// master not here any more, so there is nothing to do. This happens when the master is unsubscribed
					// and the server logic forces unsubscription for all slaves. But on the client, the master is already gone, so
					// no need to do anything
					return;
				}
				
				var masterER:EditableResource = masterESC.editableResourceStatus;
				if (masterER == null) {
					throw "Slave Editable Resource " + editableResourcePath + " is being removed, but its master Editable Resource is not here, although it EditorStatefulClient exists, for id =  " + editableResourceStatus.masterEditorStatefulClientId;
				}
				
				var index:int = masterER.slaveEditableResources.getItemIndex(editableResourceStatus);
				masterER.slaveEditableResources.removeItemAt(index);
			}
			
		}
		
		override protected function removeUIAndRelatedElementsAndStatefulClientBecauseUnsubscribedForcefully():void {
			unsubscribeFromStatefulService(null);
		}
		
		override public function afterAddInStatefulClientRegistry():void {
			// do nothing
//			GlobalEditorOperationsManager.INSTANCE.editorStatefulClientRegistered(this);
		}
		
		/**
		 * Notify so that the actions enablement is recalculated. We do this here,
		 * because we want the current StatefulClient to be out from the list, because
		 * it might have been the last dirty editor, in which case we need to disable Save All.
		 */
		override public function afterRemoveFromStatefulClientRegistry():void {
//			GlobalEditorOperationsManager.INSTANCE.editorStatefulClientUnregistered(this);
//			GlobalEditorOperationsManager.INSTANCE.dirtyStateUpdated(null);
		}
			
		/**
		 * Merges fields from new -> existing, except the transient ones.
		 * TODO CS/STFL de unificat cu celelalte locuri de merge, cf. intrarii mindmap (WP-ME)
		 * @flowerModelElementId _NJg34QcIEeK49485S7r3Vw
		 */ 
		private function mergeEditableResource(existingER:EditableResource, newER:EditableResource):void {
			var classInfo:XML = DescribeTypeCache.describeType(existingER).typeDescription;
			for each (var v:XML in classInfo..accessor) {
				if (v.@name != null && v.@access != 'readonly' &&
					v.@name != "clients" && v.@name != "slaveEditableResources") {
					existingER[v.@name] = newER[v.@name];
				}
			}			
		}

		///////////////////////////////////////////////////////////////
		// Wrappers for service methods
		///////////////////////////////////////////////////////////////

		/**
		 * @flowerModelElementId _NJhe9QcIEeK49485S7r3Vw
		 */
		public function tryLockFromButton():void {
			invokeServiceMethod("tryLockFromButton", [editableResourcePath]);
		}
		
		/**
		 * @flowerModelElementId _NJiGAQcIEeK49485S7r3Vw
		 */
		public function unlockFromButton():void {
			invokeServiceMethod("unlockFromButton", [editableResourcePath]);
		}
		
		/**
		 * @flowerModelElementId _NJiGAwcIEeK49485S7r3Vw
		 */
		public function save(returnCommandWithoutSending:Boolean=false):Object {
			var target:EditorStatefulClient;
			
			if (editableResourceStatus.masterEditorStatefulClientId != null) {
				// a slave => we use the master
				target = EditorStatefulClient(CommunicationPlugin.getInstance().statefulClientRegistry.getStatefulClientById(editableResourceStatus.masterEditorStatefulClientId));
			} else {
				target = this;
			}
			
			return target.invokeServiceMethod("save", [target.editableResourcePath], new ServiceInvocationOptions().setReturnCommandWithoutSending(returnCommandWithoutSending));
		}
		
//		public function service_addCollaborativeFigureModel(model:CollaborativeFigureModel):void {
//			invokeServiceMethod("addCollaborativeFigureModel", [editableResourcePath, model], new ServiceInvocationOptions().setResultCallbackFunction(
//				function (result:Number):void {
//					model.id = result;
//				}
//			));
//		}
//				
//		public function service_updateCollaborativeFigureModels(models:ArrayCollection):void {
//			invokeServiceMethod("updateCollaborativeFigureModels", [editableResourcePath, models]);
//		}
//		
//		public function service_removeCollaborativeFigureModels(modelIds:ArrayCollection):void {
//			invokeServiceMethod("removeCollaborativeFigureModels", [editableResourcePath, modelIds]);
//		}
//
//		override protected function removeUIAndRelatedElementsAndStatefulClientBecauseUnsubscribedForcefully():void {
//			StatefulClientRegistry.INSTANCE.unregister(this, null);
//		}
		
		///////////////////////////////////////////////////////////////
		// @RemoteInvocation methods
		///////////////////////////////////////////////////////////////
		
		/**
		 * Dispatches the content received from the server to all the
		 * <code>EditorFrontend</code>s.
		 * 
		 * If <code>isFullContent</code> is <code>true</code>, dispatches
		 * an <code>EditableResourceContentEvent</code> for each editor frontend.
		 * 
		 * @flowerModelElementId _NJebogcIEeK49485S7r3Vw
		 */
		[RemoteInvocation]
		public function updateContent(content:Object, isFullContent:Boolean):void {
			for each (var editorFrontend:EditorFrontend in editorFrontends) {
				editorFrontend.executeContentUpdateLogic(content, isFullContent);
//				if (isFullContent) {
//					dispatchEvent(new EditableResourceContentEvent(editorFrontend.editorInput));
//				}
			}			
		}
		
		/**
		 * Regarding master/slave dirty state, there is the following convention. Server: if a slave modifies the state,
		 * an update is propagated to the client, but for the master. Client (i.e. here): if a dirty state update comes for a master, it
		 * propagates for all slaves.
		 * 
		 * @flowerModelElementId _NJhe8gcIEeK49485S7r3Vw
		 */
		[RemoteInvocation]
		public function updateEditableResourceStatus(newEditableResourceStatus:EditableResource):void {
			if (newEditableResourceStatus == null) {
				// This should never happen. However, we halt here to avoid NPEs 
				throw "EditorStatefulClient.updateEditableEditableResource() called with null argument; statefulClientId = " + getStatefulClientId();
			}

			var dirtyStateChanged:Boolean = false;

			if (editableResourceStatus == null) {
				// resource just opened; add use case

				editableResourceStatus = newEditableResourceStatus;
				dirtyStateChanged = editableResourceStatus.dirty;
				
				if (editableResourceStatus.masterEditorStatefulClientId != null) {
					// this is a slave ER; we register it to the master ER

					var masterESC:EditorStatefulClient = EditorStatefulClient(CommunicationPlugin.getInstance().statefulClientRegistry.getStatefulClientById(editableResourceStatus.masterEditorStatefulClientId));
					if (masterESC == null) {
						throw "Master EditorStatefulClient should be available for id = " + editableResourceStatus.masterEditorStatefulClientId;
					}
					
					var masterER:EditableResource = masterESC.editableResourceStatus;
					if (masterER == null) {
						throw "Slave Editable Resource " + editableResourcePath + " arrived, but its master Editable Resource is not here yet for id =  " + editableResourceStatus.masterEditorStatefulClientId;
					}

					if (masterER.slaveEditableResources == null) {
						masterER.slaveEditableResources = new ArrayCollection();
					}
					masterER.slaveEditableResources.addItem(editableResourceStatus);
				}

				// notify the OpenResView				
				if (OpenResourcesView.INSTANCE != null) {
					OpenResourcesView.INSTANCE.editableResourceStatusAdded(editableResourceStatus);
					Application(FlexGlobals.topLevelApplication).dispatchEvent(new Event("resourceOpened"));
				}
			} else {
				// resource updated; update use case
				
				dirtyStateChanged = editableResourceStatus.dirty != newEditableResourceStatus.dirty;
				mergeEditableResource(editableResourceStatus, newEditableResourceStatus);
				
				if (editableResourceStatus.slaveEditableResources != null) {
					// for a master: copy the dirty status and force an update,
					// so that the slave editors may do interesting things: update the enablement of the save button,
					// show/hide the synchronizing spinner based on info form master
					for each (var slaveER:EditableResource in editableResourceStatus.slaveEditableResources) {
						var newSlaveER:EditableResource = EditableResource(ObjectUtil.clone(slaveER));
						newSlaveER.dirty = editableResourceStatus.dirty;
						// we need to call this here, in order to let the EditorFrontEnds reeval
						// the enablement of their Save button
						var slaveESC:EditorStatefulClient = EditorStatefulClient(CommunicationPlugin.getInstance().statefulClientRegistry.getStatefulClientById(slaveER.editorStatefulClientId));
						slaveESC.updateEditableResourceStatus(newSlaveER);
					}
				}
				if (dirtyStateChanged && !newEditableResourceStatus.dirty) {
					IEventDispatcher(FlexGlobals.topLevelApplication).dispatchEvent(new Event("resourceSaved"));
				}
			}
			
			// refresh the lock & dirty info
			for each (var editorFrontend:EditorFrontend in editorFrontends) {
				editorFrontend.editableResourceStatusUpdated();
			}
			
			// will update actions, etc
//			if (dirtyStateChanged) {
//				GlobalEditorOperationsManager.INSTANCE.dirtyStateUpdated(this);
//			}
		}
		
		[RemoteInvocation]
		public function newClientsAdded(newClients:ArrayCollection, clearExistingClients:Boolean = false):void {
			if (clearExistingClients) {
				editableResourceStatus.clients.removeAll();
			}
			editableResourceStatus.clients.addAll(newClients);
			if (OpenResourcesView.INSTANCE != null) {
				OpenResourcesView.INSTANCE.editableResourceClientAddedOrRemoved();
			}
		}
		
		[RemoteInvocation]
		public function clientRemoved(removedClientId:String):void {
			for (var i:int = 0; i < editableResourceStatus.clients.length; i++) {
				// search for the entry with the proper clientId
				if (EditableResourceClient(editableResourceStatus.clients[i]).communicationChannelId == removedClientId) {
					break;
				}
			}
			if (i < editableResourceStatus.clients.length) {
				// means that the loop was existed preamaturely => the element was found
				editableResourceStatus.clients.removeItemAt(i);
				if (OpenResourcesView.INSTANCE != null) {
					OpenResourcesView.INSTANCE.editableResourceClientAddedOrRemoved();
				}
			} // otherwise, no element found => don't do anything
		}
		
		//TODO : Temporary code (see #6777)
		[RemoteInvocation]
		public function disableEditing():void {
			for each (var editorFrontend:EditorFrontend in editorFrontends) {
				editorFrontend.disableEditing();
			}	
		}
		
//		private function findCollaborativeFigureModel(id:int, models:ArrayCollection):Object {
//			var i:int = 0;
//			for each (var model:CollaborativeFigureModel in models) {
//				if (model.id == id) {
//					return { model: model, index: i};
//				}
//				i++;
//			} 
//			return { model: null, index: -1};
//		}
//		
//		[RemoteInvocation]
//		public function updateCollaborativeFigureModels(models:ArrayCollection, clearAllModelsBefore:Boolean):void {
//			if (clearAllModelsBefore) {
//				collaborativeFigureModels.removeAll();
//			}
//			
//			var additionsMade:Boolean = false;
//			for each (var model:CollaborativeFigureModel in models) {
//				var existingModel:CollaborativeFigureModel = findCollaborativeFigureModel(model.id, collaborativeFigureModels).model;
//				if (existingModel == null) {
//					additionsMade = true;
//					collaborativeFigureModels.addItem(model);
//				} else {
//					existingModel.copyFrom(model);
//					existingModel.dispatchEvent(new CollaborativeFigureModelsUpdateEvent());
//				}
//			}
//			
//			if (additionsMade) {
//				collaborativeFigureModels.dispatchEvent(new CollaborativeFigureModelsUpdateEvent());
//			}
//		}
//		
//		[RemoteInvocation]
//		public function removeCollaborativeFigureModels(modelIds:ArrayCollection):void {
//			var additionsMade:Boolean = false;
//			for each (var id:Number in modelIds) {
//				var existingModelIndex:int = findCollaborativeFigureModel(id, collaborativeFigureModels).index;
//				if (existingModelIndex >= 0) {
//					collaborativeFigureModels.removeItemAt(existingModelIndex);
//				}
//			}
//			
//			collaborativeFigureModels.dispatchEvent(new CollaborativeFigureModelsUpdateEvent());
//		}
//		
//		/**
//		 * @author Mariana - check if there are any editors open; this may happen if the open resources 
//		 * cannot be open in editors (e.g. models)
//		 * 
//		 * @flowerModelElementId _aM7-F0hHEeKn-dlTSOkszw
//		 */
//		[RemoteInvocation]
//		public function revealEditor():void {
//			if (editorFrontends.length > 0) {
//				var workbench:Workbench = SingletonRefsFromPrePluginEra.workbench;
//				var lastEditorFrontend:EditorFrontend = editorFrontends[editorFrontends.length - 1];
//				
//				workbench.callLater(workbench.activeViewList.setActiveView, [lastEditorFrontend]);
//			}
//		}
	}
}