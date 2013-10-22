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
package org.flowerplatform.editor {
	
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.event.ActiveViewChangedEvent;
	
	import flash.events.EventDispatcher;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.core.mx_internal;
	
	import org.flowerplatform.common.CommonPlugin;
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.command.flextojava.FlexToJavaCompoundCommand;
	import org.flowerplatform.communication.stateful_service.StatefulClient;
	import org.flowerplatform.communication.stateful_service.StatefulClientRegistry;
	import org.flowerplatform.editor.EditorFrontend;
	import org.flowerplatform.editor.action.SaveAction;
	import org.flowerplatform.editor.action.SaveAllAction;
	import org.flowerplatform.editor.open_resources_view.OpenResourcesView;
	import org.flowerplatform.editor.remote.EditableResource;
	import org.flowerplatform.editor.remote.EditorStatefulClient;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.layout.IDirtyStateProvider;
	import org.flowerplatform.flexutil.layout.event.*;

	public class GlobalEditorOperationsManager extends EventDispatcher {
		
		//use EditorPlugin.getInstance.globalEditorOperationsManager
		public static var INSTANCE:GlobalEditorOperationsManager; 

		private var workbench:Workbench;
		
		public var saveAction:SaveAction;
		
		public var saveAllAction:SaveAllAction;
		
		/**
		 * Maintained in order to quickly retrieve, for a given path, the list of EditorStatefulClients (which will have 1 element in general).
		 */
		private var editableResourcePathToEditorStatefulClients:Dictionary = new Dictionary();
		
		public function editorStatefulClientRegistered(editorStatefulClient:EditorStatefulClient):void {
			var editorStatefulClients:ArrayCollection = editableResourcePathToEditorStatefulClients[editorStatefulClient.editableResourcePath];
			if (editorStatefulClients == null) {
				editorStatefulClients = new ArrayCollection();
				editableResourcePathToEditorStatefulClients[editorStatefulClient.editableResourcePath] = editorStatefulClients;
			}
			editorStatefulClients.addItem(editorStatefulClient);
		}
		
		public function editorStatefulClientUnregistered(editorStatefulClient:EditorStatefulClient):void {
			var editorStatefulClients:ArrayCollection = editableResourcePathToEditorStatefulClients[editorStatefulClient.editableResourcePath];
			editorStatefulClients.removeItemAt(editorStatefulClients.getItemIndex(editorStatefulClient));
			if (editorStatefulClients.length == 0) {
				delete editableResourcePathToEditorStatefulClients[editorStatefulClient.editableResourcePath];
			}
		}
		
		public function getEditorStatefulClientsForEditableResourcePath(editableResourcePath:String):ArrayCollection {
			return editableResourcePathToEditorStatefulClients[editableResourcePath];
		}
		
		public function getDirtyStateForEditableResourcePath(editableResourcePath:String):Boolean {
			var editorStatefulClients:ArrayCollection = editableResourcePathToEditorStatefulClients[editableResourcePath];
			if (editorStatefulClients == null) {
				return false;
			}
			
			for each (var editorStatefulClient:EditorStatefulClient in editorStatefulClients) {
				if (editorStatefulClient.editableResourceStatus != null && editorStatefulClient.editableResourceStatus.dirty) {
					return true;
				}
			}
			
			return false;
		}
		
		/**
		 * Returns first dirty EditorStatefulClient. If no one is dirty, the first EditorStatefulClient is returned.
		 * 
		 * This is used by trees to return the current EditorStatefulClient for the selection. Actually the implementation is not 
		 * 100% correct, because in theory, we could have 2 editors for same path, and both dirty. The save
		 * operation will handle only one. However in practice, we have 1 editor per path. But if the case described happens,
		 * this method tries to be fair (i.e. returning the first that is dirty).
		 */
		public function getInterestingEditorStatefulClientForEditableResourcePath(editableResourcePath:String):EditorStatefulClient {
			var editorStatefulClients:ArrayCollection = GlobalEditorOperationsManager.INSTANCE.getEditorStatefulClientsForEditableResourcePath(editableResourcePath);
			if (editorStatefulClients == null || editorStatefulClients.length == 0) {
				// second test is useless, in theory; I hope in practice to;
				// I put it here to be sure that the .get(0) won't fail
				return null;
			}
			
			var result:EditorStatefulClient = editorStatefulClients[0];
			for each (var current:EditorStatefulClient in editorStatefulClients) {
				if (current.editableResourceStatus != null && current.editableResourceStatus.dirty) {
					// editableResourceStatus may be null in case of EditorStatefulService.subscribe() exception or
					// if the data hasn't arrived yet from server
					result = current;
					break;
				}
			}
			
			return result;
		}
		
		
		public function GlobalEditorOperationsManager(workbench:Workbench) {
			workbench.addEventListener(ViewsRemovedEvent.VIEWS_REMOVED, viewsRemovedHandler);
			workbench.addEventListener(ActiveViewChangedEvent.ACTIVE_VIEW_CHANGED, activeViewChangedHandler);

			this.workbench = workbench;
		}
		
		
		private function viewsRemovedHandler(e:ViewsRemovedEvent):void {
			var editorFrontends:ArrayCollection = new ArrayCollection();
			for each (var view:Object in e.removedViews) {
				if (view is EditorFrontend) {
					editorFrontends.addItem(view);
					
					e.dontRemoveViews.addItem(view);
				}
			}
			if (editorFrontends.length > 0) {
				// TODO 
				new SaveResourcesDialog().show(editorFrontends, editorFrontends, null);
			}

		}
		
		/**
		 * Delegates to <code>editorInputChangedForComponent</code>.
		 * 
		 */
		private function activeViewChangedHandler(evt:ActiveViewChangedEvent):void {
			if (evt.newView is IDirtyStateProvider) {
				editorInputChangedForComponent(IDirtyStateProvider(evt.newView));
			} else {
				/*saveAction.enabled = false;
				saveAction.currentEditorStatefulClient = null;*/
			}
		}

		/**
		 * Updates the target of the Save action to the 
		 * given component, if it is the current active view.
		 * 
		 */
		
		public function editorInputChangedForComponent(component:IDirtyStateProvider):void {
			/*FlexUtilGlobals.getInstance().workbench*/
		if(true){	//if (workbench.activeViewList.getActiveView() == component) {
				// we are only interested in the active view (i.e. who's "linked" to the save action)
				var editorStatefulClient:EditorStatefulClient = EditorStatefulClient(component.getEditorStatefulClientForSelectedElement());
				if (editorStatefulClient == null) {
					saveAction.currentEditorStatefulClient = null;
					saveAction.enabled = false;
				} else {
//					if (editorStatefulClient.editableResourceStatus.masterResourceEditorInput != null) {
//						saveAction.currentEditorInput = editableResource.masterResourceEditorInput;
//					} else {
						saveAction.currentEditorStatefulClient = editorStatefulClient;
//					}
					// when the editor is just opening, the ER status is not yet on the client; that's why we check for null
					/*saveAction.enabled = editorStatefulClient.editableResourceStatus != null && editorStatefulClient.editableResourceStatus.dirty;*/
				}
			}
		}
		
		/**
		 * Update the save/save all actions enablement, and refreshes the
		 * labels of the Workbench.
		 */ 
		public function dirtyStateUpdated(editorStatefulClient:EditorStatefulClient):void {
			
//		if (editorStatefulClient == null) {
//				// i.e. freshly unsubscribed from an ER
//				if (saveAction.enabled) {
//					if (editableResources[saveAction.currentEditorInput] == null) {
//						// means that the save action was pointing to the freshly unsubscribed ER
//						saveAction.currentEditorInput = null;
//						saveAction.enabled = false;
//					}
//				}
//			} else {
//				// ER not null
//				if (saveAction.currentEditorStatefulClient == editorStatefulClient) {
//					saveAction.enabled = editorStatefulClient.editableResourceStatus.dirty;
//				}
//				var editorFrontendController:EditorFrontendController = EditorSupport.INSTANCE.editorFrontendControllers[editableResource.editorInput];
//				if (editorFrontendController is TextEditorFrontendController)
//					(editorFrontendController as TextEditorFrontendController).updateDirtyState(editableResource.editorInput, editableResource.dirty);
//			}
			
			if (saveAllAction.enabled) {
				// at least one dirty resource
				if (editorStatefulClient == null || !editorStatefulClient.editableResourceStatus.dirty) {
					// either an ER has dissapeared or an ER has been saved
					// => refresh the global dirty state, by looking at all ERs
					
					var dirtyERFound:Boolean = false;
					var count:int = 0;
					var statefulClientsList:ArrayCollection= CommunicationPlugin.getInstance().statefulClientRegistry.mx_internal::statefulClientsList;
				  //for each (var sc:StatefulClient in StatefulClientRegistry.INSTANCE.mx_internal::statefulClientsList) {
					for each (var sc:StatefulClient in statefulClientsList) {
						if (!(sc is EditorStatefulClient)) {
							continue;
						}
						var currentESC:EditorStatefulClient = EditorStatefulClient(sc);
						if (currentESC.editableResourceStatus != null && currentESC.editableResourceStatus.dirty) {
							dirtyERFound = true;
							count++;
							break;
						}
					}
//					
					if (count == 0 || !dirtyERFound) {
						saveAllAction.enabled = false;
					}
				}
			} else {
				if (editorStatefulClient != null) {
					// everything is saved
					saveAllAction.enabled = editorStatefulClient.editableResourceStatus.dirty; // the value of this variable should always be true
				} // otherwise we are not interested; i.e. global dirty = false and an ER has left (which was non dirty for sure)
			}
			Workbench(FlexUtilGlobals.getInstance().workbench).refreshLabels();			
			dispatchEvent(new DirtyStateUpdatedEvent(editorStatefulClient));
		}

		/**
		 * Returns a list of dynamic objects {editorStatefulClient, selected}.
		 * Slave editor clients are replaced with their master. Duplicates are removed.
		 * 
		 */ 
	public function createEntriesToSave(editorsOrStatefulClientsToSave:ArrayCollection, ignoreDirtyFlag:Boolean = false, useMasterFlag:Boolean = true):ArrayCollection {
			// used to eliminate duplicates; key = editableResourcePath; value = editorStatefulClient, but not used
			var map:Dictionary = new Dictionary();
			var result:ArrayCollection = new ArrayCollection();
			
			for each (var object:Object in editorsOrStatefulClientsToSave) {
				var editorStatefulClient:EditorStatefulClient;
				if (object is EditorStatefulClient) {
					editorStatefulClient = EditorStatefulClient(object);
				} else if (object is EditorFrontend) {
					// is editor frontend
					editorStatefulClient = EditorFrontend(object).editorStatefulClient;
				} else {
					continue;
				}
				
				if (editorStatefulClient.editableResourceStatus == null) {
					// Shouldn't usually happen. However, happens if something went wrong and data never came from server
					// in which case we want to just close the editor. I think it can happen as well if the user quickly closes
					// an editor before the data arrived from server
					continue;
				}
				
				if (ignoreDirtyFlag || editorStatefulClient.editableResourceStatus.dirty) {
					// process this item only if dirty
					
					/*if (useMasterFlag && editorStatefulClient.editableResourceStatus.masterEditorStatefulClientId != null) {
						// a slave => we use the master
						editorStatefulClient = EditorStatefulClient(StatefulClientRegistry.INSTANCE.getStatefulClientById(editorStatefulClient.editableResourceStatus.masterEditorStatefulClientId));
					}*/
					
					if (map[editorStatefulClient.editableResourcePath] == null) {
						// element not yet seen
						map[editorStatefulClient.editableResourcePath] = editorStatefulClient;
						result.addItem({
							editorStatefulClient: editorStatefulClient,
							selected: true
						});
					}
				}
			}
			
			return result;
		}
		
		public function getSaveCommandForSelectedEntries(entriesToSave:ArrayCollection):FlexToJavaCompoundCommand {
			var commandToSend:FlexToJavaCompoundCommand = new FlexToJavaCompoundCommand(false, true);
			for each (var entry:Object in entriesToSave) {
				if (entry.selected) {
					commandToSend.append(EditorStatefulClient(entry.editorStatefulClient).save(true));
				}
			}

			return commandToSend;
		}
//
//		/**
//		 * Shows the label for an <code>EditableResource</code>, including the dirty sign, its
//		 * slave ERs (if exist) and number of other clients.
//		 * 
//		 */
		public function getEditableResourceLabel(editableResource:EditableResource, showDirtyStatus:Boolean):String {
			var result:String = String(editableResource.label);
			
			if (showDirtyStatus && editableResource.dirty) {
				result += " *";
			}
			
			if (editableResource.slaveEditableResources != null) {
				// master ER
				for (var i:int = 0; i < editableResource.slaveEditableResources.length; i++) {
					if (i == 0) {
						result += " (includes ";
					}
					result += EditableResource(editableResource.slaveEditableResources[i]).label;
					if (i != editableResource.slaveEditableResources.length - 1) {
						result += ", ";
					} else {
						result += ")";
					}
				}
			}
			
			// TODO CS/STFL coabitare; editableResource.clients == null nu cred ca mai e necesar
			if (editableResource.clients != null && editableResource.clients.length > 0) {
				result += ' [Open by ' + editableResource.clients.length + ' other client(s)]';
			}
			return result;
		}
//
//		public function getGlobalDirtyState():Boolean {
//			// currently this is used by the Project Explorer view; I saw that when the app is started with this view
//			// minimized, this code is invoked earlier then the initialization of the menu bar (i.e. saveAll action as well) 
//			return GlobalEditorOperationsManager.INSTANCE.saveAllAction != null ? GlobalEditorOperationsManager.INSTANCE.saveAllAction.enabled : false;
//		}
//		
		public function invokeSaveResourcesDialogAndInvoke(callbackObject:Object, callbackFunction:Function, callbackArguments:Array):void {
			// it's safe to pass the whole list, because it's copied; so the fact that ESCs
			// dissapear (i.e. list is modified) won't affect
			new SaveResourcesDialog().show(CommunicationPlugin.getInstance().statefulClientRegistry.mx_internal::statefulClientsList, null, null, callbackObject, callbackFunction, callbackArguments);
		}
	}
	
}