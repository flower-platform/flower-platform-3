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
package org.flowerplatform.editor.editorsupport {
	import flash.events.EventDispatcher;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	
	/*import plugin_migration.SingletonRefsFromPrePluginEra;
	import plugin_migration.SingletonRefsFromPrePluginEra;*/
	
	/**
	 * Support for content types; holds mappings from content types to content types ids
 	 * and compatible editors, and also from editors to {@link EditorEntry}s. 
	 * 
	 * <p>
	 * Also adds Open and Open With menu entries for each openable tree node.
	 * 
	 * <p>
	 * Regarding <code>EditableResource</code>s (a non-exhaustive list):
	 * <ul>
	 * 	<li>holds all <code>EditableResource</code>s
	 * 	<li>holds all <code>EditorFrontendController</code>s (that contain <code>EditorFrontend</code>s)
	 * 	<li>logic for closing editors
	 *  <li>logic related to save, save & close
	 * </ul>
	 * 
	 * <p>
	 * It is a singleton, so it cannot be initialized manually; {@link #INSTANCE} will be used 
 	 * to access this class.
 	 * 
 	 * <p>
	 * <b>NOTE</b>: Although a class with the same name exists in ActionScript, they are 
	 * not "physically related" (i.e. mirrored/serialized from server <->client). However
	 * some data is sent at client startup on AS, by <code>InitializeEditorSupportClientCommand</code>.
	 * 
	 * @author Cristi
	 * @author Mariana
	 * @flowerModelElementId _7Lxd0EyoEeGsUPSh9UfXpw
	 */
	public class EditorSupport extends EventDispatcher {
		
		/**
		 * @flowerModelElementId __7pGQEyoEeGsUPSh9UfXpw
		 */
		public static const INSTANCE:EditorSupport = new EditorSupport();
		
		public static const UNDEFINED_CONTENT_TYPE_ID:int = -1;
		
		/**
		 * @see Getter doc.
		 * 
 		 * @flowerModelElementId _vhuRcE1sEeGsUPSh9UfXpw
		 */
		private var _contentTypeEntries:ArrayCollection;
		
		/**
		 * @see Getter doc.
		 * 
		 * @flowerModelElementId _9DXRgE1sEeGsUPSh9UfXpw
		 */
		private var _contentTypesToIds:Dictionary = new Dictionary();
		
		/**
		 * @see Getter doc.
		 * 
		 * @flowerModelElementId _tpNekE2WEeGsUPSh9UfXpw
		 */
		private var _editorNamesToEditorDescriptors:Dictionary = new Dictionary();
		
		/**
		 * @see Getter doc.
		 * 
		 * @flowerModelElementId _myElALb8EeGlK6b9EKaKdw
		 */
		private var _editorFrontendControllers:Dictionary = new Dictionary();
		
//		/**
//		 * @see Getter doc.
//		 * 
//		 * @flowerModelElementId _LYFDMFvlEeGgHc6ke3rMAg
//		 */
//		private var _editableResources:Dictionary = new Dictionary();
//		
		// TODO CS/STFL de reactivat lockLeaseSeconds trimis de pe server
		/**
		 * Populated at init by the command sent by the server.
		 * 
		 * @flowerModelElementId _TuRRwLoOEeGmmdms_o5fxQ
		 */
		public var lockLeaseSeconds:int;
		
//		/**
//		 * The Save All action, which is enabled only if there is
//		 * at least one dirty resource. Its enabled state can be
//		 * used to determine the global dirty state of the application. 
//		 * 
//		 * @flowerModelElementId _y8pN0FyOEeGwx-0cTKUc5w
//		 */
//		public var saveAllAction:SaveAllAction;
//		
//		/**
//		 * The Save Action; its enabled state changes
//		 * each time the active view changes, to ensure that the
//		 * action is enabled when an element corresponding to a 
//		 * dirty resource has focus and disabled otherwise.
//		 * 
//		 * @flowerModelElementId _z6fWIFyOEeGwx-0cTKUc5w
//		 */
//		public var saveAction:SaveAction;
		
		/**
		 * @see Class/method with same name in Java.
		 * 
		 * @flowerModelElementId _IkxfME1tEeGsUPSh9UfXpw
		 */
		public function get contentTypeEntries():ArrayCollection {
			return _contentTypeEntries;
		}
		
		/**
		 * @flowerModelElementId _Ik1woE1tEeGsUPSh9UfXpw
		 */
		public function set contentTypeEntries(_contentTypeEntries:ArrayCollection):void {
			this._contentTypeEntries = _contentTypeEntries;
		}
		
		/**
		 * @see Class/method with same name in Java.
		 * 
 		 * @flowerModelElementId _Ik6CE01tEeGsUPSh9UfXpw
		 */
		public function get contentTypesToIds():Dictionary {
			return _contentTypesToIds;
		}
		
		/**
		 * @flowerModelElementId _Ik_ho01tEeGsUPSh9UfXpw
		 */
		public function set contentTypesToIds(_contentTypesToIds:Dictionary):void {
			this._contentTypesToIds = _contentTypesToIds;
		}
		
		/**
		 * Client side only mapping between editor strings and editor entries.
		 * 
		 * @see #addEditorEntry()
		 * @flowerModelElementId _SJOM8E2ZEeGsUPSh9UfXpw
		 */
		public function get editorNamesToEditorDescriptors():Dictionary {
			return _editorNamesToEditorDescriptors;
		}
		
		/**
		 * Used by plugins to add <code>EditorEntry</code>s.
		 * 
		 * @see #editorsToEditorEntries
		 * @flowerModelElementId _QxlJcFFbEeGMrNbRkxqlAA
		 */ 
		/*public function addEditorDescriptor(editorDescriptor:BasicEditorDescriptor):void {
			_editorNamesToEditorDescriptors[editorDescriptor.getEditorName()] = editorDescriptor;
			if (editorDescriptor is IViewProvider)
				SingletonRefsFromPrePluginEra.layoutRegistry.addViewProvider(IViewProvider(editorDescriptor));
		}*/
		
		/**
		 * Creates Open and Open With menu entries if all the nodes in the selection are openable
		 * (i.e. it has content type id) and adds them to the menu. If there are more than one 
		 * node selected, then it only adds the Open entry.
		 * 
		 * @flowerModelElementId _MnQgkE2WEeGsUPSh9UfXpw
		 */
		/*public function addOpenActionsInContextMenu(selection:ArrayCollection, 
			menu:FlowerContextMenu):void {
			
			// if there is only one node selected, add Open and OpenWith entries
			if (selection.length == 1) {
//				var node:NavigatorTreeNode = NavigatorTreeNode(selection.getItemAt(0));
				var node:Object = selection.getItemAt(0);
				if (node.contentTypeId != -1) {
					var editableResourcePath:String = SingletonRefsFromPrePluginEra.projectExplorerStatefulClient_getEditableResourcePathFromTreeNodeFunction(node);
					var openWithAction:OpenWithAction = new OpenWithAction(editableResourcePath, menu, node.contentTypeId);
					if (openWithAction.isVisible())
						menu.addChild(openWithAction);
					menu.addActionEntryIfVisible(new OpenAction());
				}
			} else {
				// check if all the nodes in the selection are openable
//				for each (var node:NavigatorTreeNode in selection) {
				for each (var node:Object in selection) {
					if (node.contentTypeId == -1)
						return;
				}
				
				// if there is more than one node selected, add only the Open entry
				menu.addActionEntryIfVisible(new OpenAction());
			}
		}*/
		
//		public function getFirstEditorDescriptorForNode(contentTypeId:int):BasicEditorDescriptor {
//			try {
//				return getFirstEditorDescriptorForNodeUnsafe(contentTypeId);
//			} catch (e:Error) { 
//				/* swallow */ 
//			}
//			return null;
//		}

		/*
		public function getFirstEditorDescriptorForNodeUnsafe(contentTypeId:int):BasicEditorDescriptor {
			if (contentTypeId < 0 || EditorSupport.INSTANCE.contentTypeEntries.length <= contentTypeId)
				throw new Error("The supplied content type id is out of range : " + contentTypeEntry.contentType + "!");

			var contentTypeEntry:ContentTypeEntry = EditorSupport.INSTANCE.contentTypeEntries[contentTypeId];
			if (contentTypeEntry.compatibleEditors.length == 0) 
				throw new Error("There is no compatible editor for this content: " + contentTypeEntry.contentType + "!");
			
			var editorName:String = String(contentTypeEntry.compatibleEditors[0]);
			var editorEntry:BasicEditorDescriptor = EditorSupport.INSTANCE.editorNamesToEditorDescriptors[editorName];
			if (editorEntry == null)
				throw new Error("There is no compatible editor descriptor found for editor: " + editorName + "!");
			
			return editorEntry;
		}*/
		
//		/**
//		 * All the <code>EditorFrontendController</code> from the application
//		 * (each one containint one or several <code>EditorFrontend</code>s).
//		 * Indexed by editorInput.
//		 * 
//		 * @flowerModelElementId _3mj14Lb8EeGlK6b9EKaKdw
//		 */
//		public function get editorFrontendControllers():Dictionary {
//			return _editorFrontendControllers;
//		}
//		
//		/**
//		 * Contains all editable resources opened by this client,
//		 * mapped by their <code>editorInput</code>.
//		 * 
//		 * @flowerModelElementId _hxkNsFvlEeGgHc6ke3rMAg
//		 */
//		public function get editableResources():Dictionary {
//			return _editableResources;
//		}
//
//		/**
//		 * Update the save/save all actions enablement, and refreshes the
//		 * labels of the Workbench.
//		 * 
//		 * @flowerModelElementId _47k7oGnaEeGf2Ze1btT4ow
//		 */ 
//		public function dirtyStateUpdated(editableResource:EditableResource):void {
//			if (editableResource == null) {
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
//				if (saveAction.currentEditorInput == editableResource.editorInput) {
//					saveAction.enabled = editableResource.dirty;
//				}
//				var editorFrontendController:EditorFrontendController = EditorSupport.INSTANCE.editorFrontendControllers[editableResource.editorInput];
//				if (editorFrontendController is TextEditorFrontendController)
//					(editorFrontendController as TextEditorFrontendController).updateDirtyState(editableResource.editorInput, editableResource.dirty);
//			}
//			
//			if (saveAllAction.enabled) {
//				// at least one dirty resource
//				if (editableResource == null || !editableResource.dirty) {
//					// either an ER has dissapeared or an ER has been saved
//					// => refresh the global dirty state, by looking at all ERs
//					
//					var dirtyERFound:Boolean = false;
//					var count:int = 0;
//					for each (var er:EditableResource in editableResources) {
//						if (er.dirty) {
//							dirtyERFound = true;
//							count++;
//							break;
//						}
//					}
//					if (count == 0 || !dirtyERFound) {
//						saveAllAction.enabled = false;
//					}
//				}
//			} else {
//				if (editableResource != null) {
//					// everything is saved
//					saveAllAction.enabled = editableResource.dirty; // the value of this variable should always be true
//				} // otherwise we are not interested; i.e. global dirty = false and an ER has left (which was non dirty for sure)
//			}
//			FlexPlugin1.getInstance().workbench.refreshLabels();		
//			dispatchEvent(new DirtyStateUpdatedEvent(null));
//		}
//		
//		/**
//		 * Updates the target of the Save action to the 
//		 * given component, if it is the current active view.
//		 * 
//		 * @flowerModelElementId _Mrq1QLfrEeGB8_d5lw6TyA
//		 */
//		public function editorInputChangedForComponent(component:IDirtyStateProvider):void {
//			if (FlexPlugin1.getInstance().workbench.activeViewList.getActiveView() == component) {
//				// we are only interested in the active view (i.e. who's "linked" to the save action)
//				var editorInput:Object = component.getEditorInputForCurrentEditableResource();
//				var editableResource:EditableResource = editableResources[editorInput];
//				if (editableResource == null) {
//					saveAction.enabled = false;
//				} else {
//					if (editableResource.masterResourceEditorInput != null) {
//						saveAction.currentEditorInput = editableResource.masterResourceEditorInput;
//					} else {
//						saveAction.currentEditorInput = editorInput;
//					}
//					saveAction.enabled = editableResource.dirty;
//				}
//			}
//		}
//		
//		/**
//		 * Delegates to <code>editorInputChangedForComponent</code>.
//		 * 
//		 * @flowerModelElementId _wexykFmAEeG5zfjR769bxA
//		 */
//		public function activeViewChangedHandler(evt:ActiveViewChangedEvent):void {
//			if (evt.newView is IDirtyStateProvider) {
//				editorInputChangedForComponent(IDirtyStateProvider(evt.newView));
//			} else {
//				saveAction.enabled = false;
//				saveAction.currentEditorInput = null;
//			}
//		}
//		
//		/**
//		 * Invokes the save on the server side. For slave ERs, the action is invoked
//		 * for their master (if there are 2/+ slaves with same master ER, the call is 
//		 * made only once for the master ER).
//		 * 
//		 * @flowerModelElementId _CK1qkLurEeGAt-EVfEFJMA
//		 */
//		public function saveEditorInputs(editorInputs:ArrayCollection):void {
//			var addedEditorInputs:Dictionary = new Dictionary();
//			var editorInputsToSave:ArrayCollection = new ArrayCollection();
//			for each (var editorInput:Object in editorInputs) {
//				var editableResource:EditableResource = EditableResource(EditorSupport.INSTANCE.editableResources[editorInput]);
//				var editorInputToAdd:Object = null;
//				if (editableResource != null && editableResource.masterResourceEditorInput) {
//					// slave ER
//					editorInputToAdd = editableResource.masterResourceEditorInput;
//				} else {
//					// ER null (shouldn't happen) or normal or master ER
//					editorInputToAdd = editorInput;
//				}
//				if (addedEditorInputs[editorInputToAdd] == null) {
//					editorInputsToSave.addItem(editorInputToAdd);
//					addedEditorInputs[editorInputToAdd] = editorInputToAdd;
//				}
//				
//			}
//			
//			// in case we are saving a text file, we need to send all the buffered events to the server; 
//			// otherwise, because of the aggregation interval, some events may arrive on the server after the file has been saved
//			for each (var editorInput:Object in editorInputs) {
//				var controller:EditorFrontendController = EditorSupport.INSTANCE.editorFrontendControllers[editorInput];
//				if (controller is TextEditorFrontendController) {
//					var textEditorFrontendController:TextEditorFrontendController = controller as TextEditorFrontendController;
//					for each (var editorFrontend:TextEditorFrontend in textEditorFrontendController.editorFrontends) {
//						(editorFrontend.editor as SyntaxTextArea).emptyKeystrokeAggregationBuffer();
//					}
//				}
//			}
//			
//			BaseFlowerDiagramEditor.instance.sendObject(
//				new InvokeServiceMethodServerCommand("editorSupport", "saveEditableResources", [editorInputsToSave]));
//		}
//		
//		/**
//		 * Groups the input data into a list of "editorInputAndEditor" dynamic
//		 * object, and calls <code>closeEditorInputsAndEditorsWithSaveDialog()</code>.
//		 * 
//		 * @flowerModelElementId _avnQoLkpEeG4yKAWAH18iQ
//		 */
//		public function closeEditorsWithSaveDialog(editorFrontends:ArrayCollection):void {
//			// sort the editors according to their editorInput
//			var sort:Sort = new Sort();
//			sort.compareFunction = function(a:Object, b:Object, array:Array = null):int {
//				var edA:EditorFrontend = EditorFrontend(a);
//				var edB:EditorFrontend = EditorFrontend(b);
//				if (edA.editorInput < edB.editorInput) {
//					return -1;
//				} else if (edA.editorInput > edB.editorInput) {
//					return 1;
//				} else {
//					return 0;
//				}
//			}
//			editorFrontends.sort = sort;
//			editorFrontends.refresh();
//			
//			// create entries by grouping editors by their editorInputs
//			var previousEditorFrontend:EditorFrontend = null;
//			var editorInputsAndEditors:ArrayCollection = new ArrayCollection();
//			var currentEntry:Object = null;
//			for each (var editorFrontend:EditorFrontend in editorFrontends) {
//				if (previousEditorFrontend != editorFrontend) {
//					// a new (or first) editor in the list; => create an entry
//					currentEntry = {editorInput: editorFrontend.editorInput, editorsToClose: new ArrayCollection()};
//					editorInputsAndEditors.addItem(currentEntry);
//				}
//				previousEditorFrontend = editorFrontend;
//				ArrayCollection(currentEntry.editorsToClose).addItem(editorFrontend);
//			}
//			
//			closeEditorInputsAndEditorsWithSaveDialog(editorInputsAndEditors);
//		}
//		
//		/**
//		 * Brings up the save resources dialog.
//		 * 
//		 * @flowerModelElementId _av_rIbkpEeG4yKAWAH18iQ
//		 */
//		public function closeEditorInputsAndEditorsWithSaveDialog(editorInputsAndEditors:ArrayCollection):void {
//			new SaveResourcesDialog().show(editorInputsAndEditors);
//		}
//		
//		/**
//		 * For each master ER, adds all the slave ERs, avoiding duplication. If slave ERs
//		 * exist already in the list, their "editorsToClose" is forced to null.
//		 * 
//		 * @flowerModelElementId _CYEXUKveEeGeJtJmWA5biQ
//		 */
//		private function expandMasterEditableResources(editorInputsAndEditors:ArrayCollection):void {
//			for each (var entry:Object in editorInputsAndEditors) {
//				var editableResource:EditableResource = EditorSupport.INSTANCE.editableResources[entry.editorInput];
//				if (editableResource != null && editableResource.slaveEditableResources != null) {
//					// only if the ER exists and is a master
//					for each (var slaveEditableResource:EditableResource in editableResource.slaveEditableResources) {
//						var found:Boolean = false;
//						for each (var entry2:Object in editorInputsAndEditors) {
//							if (entry2.editorInput == slaveEditableResource.editorInput) {
//								// the slave ER exists already; force editorsToClose to null
//								entry2.editorsToClose = null;
//								found = true;
//								break;
//							}
//						}
//						if (!found) {
//							// the slave ER doesn't exist, so add it
//							editorInputsAndEditors.addItem({ editorInput: slaveEditableResource.editorInput, editorsToClose: null });
//						}
//					}
//				}
//			}
//		}
//		
//		/**
//		 * For each entry (of type dynamic object), having <code>editorInput</code>,
//		 * closes all editors found in <code>editorsToClose</code>. If this is null, it means
//		 * that all associated <code>EditorFrontend</code>s will be closed.
//		 * 
//		 * <p>
//		 * If there are no <code>EditorFrontend</code>s left for a given <code>editorInput</code>,
//		 * then it unsubscribes from the server.
//		 * 
//		 * <p>
//		 * If an entry points to a master <code>EditableResource</code>, all its slave <code>EditableResource</code>s
//		 * are added to the list and processed.
//		 * 
//		 * @flowerModelElementId _CYZugaveEeGeJtJmWA5biQ
//		 */
//		public function closeEditorInputsAndEditors(editorInputsAndEditors:ArrayCollection):void {
//			expandMasterEditableResources(editorInputsAndEditors);
//			for each (var entry:Object in editorInputsAndEditors) {
//				var editorInput:Object = entry.editorInput;
//				var editorsToClose:ArrayCollection = ArrayCollection(entry.editorsToClose); // contains EditorFrontend instances
//
//				var editableResource:EditableResource = EditorSupport.INSTANCE.editableResources[editorInput];
//
//				if (editableResource == null) {
//					// This may not be normal: editable resource doens't exist but editors
//					// exist. Could happen for example if the user quickly closes the 
//					// editor, before data from server arrives. OR, something wrong happened
//					// with the whole mechanism. 
//					// OR, the call comes from UpdateEditableResourceClientCommand with remove=true
//					
//					// we force closing all editors, even if this was not the caller's intention
//					editorsToClose = null;
//				}
//				
//				var associatedEditors:ArrayCollection = null;
//				var editorFrontendController:EditorFrontendController = EditorFrontendController(editorFrontendControllers[editorInput]);
//				if (editorFrontendController != null) {
//					associatedEditors = editorFrontendController.editorFrontends;
//				}
//				
//				// close editors only if they exist
//				if (associatedEditors != null) {
//					for (var i:int = associatedEditors.length - 1; i >= 0; i--) {
//						// we use a decreasing for loop to optimize for the case
//						// where editorsToClose == null
//						
//						var currentEditor:EditorFrontend = associatedEditors[i];
//						if (editorsToClose == null || editorsToClose.contains(currentEditor)) {
//							// all editors to be closed OR
//							// the editor is in the list of editors to close
//							associatedEditors.removeItemAt(i);
//							if (currentEditor.editor is SyntaxTextArea) {
//								(currentEditor.editor as SyntaxTextArea).emptyKeystrokeAggregationBuffer();
//							}
//							currentEditor.controller = null;
//							
//							//@author Daniela
//							//we need this because even if the editor is closed the stage event listeners
//							//of the diagram tools remains active
//							currentEditor.deactivateDiagramIfOneExists();
//							
//							// layout lib remove view specific code
//							var workbench:Workbench = FlexPlugin1.getInstance().workbench;							
//							workbench.removeView(currentEditor);
//						} // else the editor is not in the list of editors to close; skip it
//					}
//					
//					if (associatedEditors.length == 0) {
//						// all the editors have been removed; remove the entry from the map
//						delete editorFrontendControllers[editorInput];
//						// mark variable with null, to force unsubscribe
//						associatedEditors = null;
//					} // else, there are still existing editors; so unsubscribe...() won't be called
//				}
//				
//				if (associatedEditors == null && editableResource != null) {
//					// either there were no associated editors (i.e. is a master ER)
//					// or all the associated editors were closed => unsubscribe
//					
//					// editableResource can be null, according to the comment at the beginning
//					// of the method (in which case we don't send the command)
//					BaseFlowerDiagramEditor.instance.sendObject(new InvokeServiceMethodServerCommand(
//						"editorSupport", "unsubscribeFromEditableResource", [editorInput]));
//				}
//			}
//		}
//		
//		/**
//		 * Creates and adds a <code>GoToOpenResourcesViewAction</code> to the menu
//		 * if there is only one node selected and this node corresponds to an
//		 * editable resource from the registry.
//		 * @flowerModelElementId _48YM4GnaEeGf2Ze1btT4ow
//		 */		
//		public function addGoToOpenResourcesViewActionInContextMenu(selection:ArrayCollection, menu:FlowerContextMenu):void {
//			if (selection.length == 1) {
//				var navigatorTreeNode:NavigatorTreeNode = NavigatorTreeNode(selection.getItemAt(0));
//				var editableResource:EditableResource = _editableResources[getEditorInputFromTreeNode(navigatorTreeNode)];
//				if (editableResource != null) {
//					menu.addActionEntryIfVisible(new GoToOpenResourcesViewAction(editableResource.editorInput));
//				}
//			}
//		}
		
		
//		public function viewsRemovedHandler(e:ViewsRemovedEvent):void {
//			var editorFrontends:ArrayCollection = new ArrayCollection();
//			for each (var view:Object in e.removedViews) {
//				// TODO CS/STFL coabitare cod vechi & nou
//				if (view is EditorFrontend && EditorFrontend(view).controller != null) {
//					editorFrontends.addItem(view);
//				}
//			}
//			if (editorFrontends.length > 0) {
//				EditorSupport.INSTANCE.closeEditorsWithSaveDialog(editorFrontends);
//			}
//		}

	}
	
}