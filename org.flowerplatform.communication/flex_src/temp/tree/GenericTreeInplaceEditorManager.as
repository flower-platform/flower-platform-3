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
package  temp.tree {
	
	import flash.events.KeyboardEvent;
	import flash.ui.Keyboard;
	import flash.utils.Dictionary;
	
	import mx.controls.TextInput;
	import mx.events.CloseEvent;
	import mx.events.ListEvent;
	import mx.events.ListEventReason;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	
	/**
	 * The class provides ied functionality for <code>GenericTree</code>s.
	 * 
	 * @author Cristina
	 * @flowerModelElementId _D9B6QFYjEeCukY0DWzslHg
	 */
	public class GenericTreeInplaceEditorManager {
		
		/**
		 * Global repository that holds all the active trees and
		 * their corresponding ied manager.		
		 */ 		
		 public static var activeTrees:Dictionary = new Dictionary(); 
		 
		 /**
		  * @flowerModelElementId _OFjYCFw6EeCMd4YreAKWkA
		  */
		 public var tree:GenericTree;
				 
		 /**
		  * @flowerModelElementId _Dk0l4FY0EeCukY0DWzslHg
		  */
		 private var currentEditedTreeNode:TreeNode;
		 		
		 /**
		  * @flowerModelElementId _lB3kB1kfEeCu5cp3IGqniw
		  */
		 private static const EDITING:int = 1;
		 		
		 /**
		  * @flowerModelElementId _lBtzDlkfEeCu5cp3IGqniw
		  */
		 private static const NOT_EDITING:int = 2;
					 
		 /**
		  * @flowerModelElementId _OFjYGFw6EeCMd4YreAKWkA
		  */
		 private static const DISABLED:int = 3;

		/**
		  * Stores the state for current editor.
		  * Three states are available :
		  * <ul>
		  * 	<li> EDITING - the editor is open for editing
		  * 	<li> NOT_EDITING - the editor is closing/closed
		  * 	<li> DISABLED - the editor waits for server response
		  * </ul>
		  * @flowerModelElementId _awa3cFawEeCGFegCku6Qjg
		  */ 
		 private var editingState:int = NOT_EDITING;
		 
		 /**
		  * @flowerModelElementId _OFtJBFw6EeCMd4YreAKWkA
		  */
		 private static const NO_KEY:int = 0;
		 
		 /**
		  * @flowerModelElementId _OFtJBlw6EeCMd4YreAKWkA
		  */
		 private static const CTRL_ENTER:int = 1;
		 
		 /**
		  * @flowerModelElementId _OFtJCFw6EeCMd4YreAKWkA
		  */
		 private static const ESCAPE:int = 2;
		 		 		
		/**
		  * Stores the key used when closing the editor.
		  * There are three exit keys used :
		  * <ul>
		  * 	<li> NO_KEY - the key pressed isn't important
		  * 	<li> CTRL+ENTER - the editor was closed by pressing CTRL + ENTER keys
		  * 	<li> ESCAPE - the editor was closed by pressing ESCAPE key
		  * </ul>
		  * @flowerModelElementId _KlWSEFYjEeCukY0DWzslHg
		  */
		 private var lastExitType:int = NO_KEY;		
	 
		 /**
		  * If the auto create after editing preference is enabled and the editor
		  * was closed by pressing CTRL+ENTER, this function will be executed.
		  * <blockquote>
		  * 	<code>itemEditEndFunction():void</code>
		  * </blockquote>
		  * @flowerModelElementId _aO_pwFk_EeCUr69IUjNRtg
		  */
		 public var itemEditEndFunction:Function;
		
		 private var oldLabel:String;
		 
		 public function GenericTreeInplaceEditorManager(tree:GenericTree) {
		 	this.tree = tree;
		 }
		
		 public static function registerTree(tree:GenericTree):void {
		 	var instance:GenericTreeInplaceEditorManager = new GenericTreeInplaceEditorManager(tree);
			activeTrees[tree.statefulClient.getStatefulClientId()] = instance;
			instance.activate(tree);
		 }
		 
		 public static function unregisterTree(tree:GenericTree):void {	
		 	if (activeTrees[tree.statefulClient.getStatefulClientId()] == null) {
		 		return;	
		 	}	 		 	
			GenericTreeInplaceEditorManager(activeTrees[tree.statefulClient.getStatefulClientId()]).deactivate();
			delete activeTrees[tree.statefulClient.getStatefulClientId()];
		 }
			 
		 /**
		  * Initializes this manager to work with the tree given as parameter.
		  * @flowerModelElementId _HYVjgFYjEeCukY0DWzslHg
		  */ 
		 private function activate(tree:GenericTree):void {			
			this.tree.addEventListener(KeyboardEvent.KEY_DOWN, treeNodeKeyDownHandler);
			// the priority is higer because 
			// we want first to execute our behavior when closing the editor
			this.tree.addEventListener(ListEvent.ITEM_EDIT_END, treeNodeEditEndHandler, false, 1);
		 }
		 
		 private function deactivate():void {			
			tree.removeEventListener(KeyboardEvent.KEY_DOWN, treeNodeKeyDownHandler);			
			tree.removeEventListener(ListEvent.ITEM_EDIT_END, treeNodeEditEndHandler);			
		 }
		
		/**
		 * If F2 is pressed, starts the editor for the selected tree node.
		 * @flowerModelElementId _YQi5gFkSEeC5PuYnMMFxJw
		 */
		private function treeNodeKeyDownHandler(event:KeyboardEvent):void {					
			if (event.keyCode == Keyboard.F2) {
				var tree:GenericTree = getTreeFromEventTarget(event.target);
				if (tree != null) {
					var node:TreeNode = TreeNode(tree.selectedItem);
					if (node != null && tree.canEditNode(node)) {
						startEditing(node);
					}
				}
			}										
		}
		
		private function getTreeFromEventTarget(target:Object):GenericTree {
			if (target == null || target is GenericTree)
				return GenericTree(target);
			if (target is TreeNodeItemRenderer)
				return getTreeFromEventTarget(TreeNodeItemRenderer(target).automationOwner);
			else
				return getTreeFromEventTarget(target.parent);
		}
		
		/**
		 * Handles the editor KEY_DOWN event.
		 * Stores the exit key in order to be used after closing the editor.
		 * @flowerModelElementId _OFtJFlw6EeCMd4YreAKWkA
		 */ 		
		private function editorKeyDownHandler(event:KeyboardEvent):void {
			lastExitType = NO_KEY;					
			if (event.keyCode == Keyboard.ENTER && event.ctrlKey) {
				lastExitType = CTRL_ENTER;
				return;
			}
			if (event.keyCode == Keyboard.ESCAPE) {
				lastExitType = ESCAPE;
			}								
		}
		
		/**
		 * Handles the editor ITEM_EDIT_END event. 
		 * The event is dispached by Tree.endEdit(reason) method.
		 * <p>
		 * The NON_EDITING state porpose is to not let this handler to execute it's behavior.
		 * The DISABLED state means that the editor waits for a server response, so other handlers
		 * must not be executed on this waiting time.
		 * <p>
		 * This handler behavior isn't executed along with the other handlers, so the propagation
		 * is stoped.
		 * <p>
		 * There is a flex bug when the editor is closed by pressing ESCAPE key :
		 * the data is saved always.
		 * So a hack was made : if the user pressed ESCAPE, then consider the editor's text to be the node's label. 
		 * @flowerModelElementId _vLcOgFk_EeCUr69IUjNRtg
		 */
		private function treeNodeEditEndHandler(event:ListEvent):void {			
			if (editingState == NOT_EDITING) {
				if (lastExitType == ESCAPE && tree.itemEditorInstance != null) {
					TextInput(tree.itemEditorInstance).text = currentEditedTreeNode.label;
				}
				TextInput(tree.itemEditorInstance).removeEventListener(KeyboardEvent.KEY_DOWN, editorKeyDownHandler);																	
				return;
			}
			if (editingState == DISABLED) {
				event.stopImmediatePropagation();				
				return;
			}
			if (event.reason == ListEventReason.CANCELLED) {
				editingState = NOT_EDITING;								
				tree.closeEditor();
				afterEndEditing();
			} else {								
				endEditing();									
			}	
			event.stopImmediatePropagation();						
		}
		
		/**
		 * Starts openning the editor for <code>treeNode</code> by sending a 
		 * request to server to provide the editor's text to show.
		 * 
		 * @flowerModelElementId _WUjEIFYjEeCukY0DWzslHg
		 */
		public function startEditing(treeNode:TreeNode):void {
			currentEditedTreeNode = treeNode;				
			editingState = EDITING;
			
			tree.statefulClient.getInplaceEditorText(treeNode, this, handleServerTextToEdit);					
		}
		
		private var text:String;
		
		/**
		 * The method has the server response and opens the editor.
		 * If the response is null, then the editor must not open.
		 * @flowerModelElementId _vOUKUFY0EeCukY0DWzslHg
		 */ 
		private function handleServerTextToEdit(text:String):void {
			if (text == null) {
				return;
			}	
			this.text = text;
			tree.openEditor(currentEditedTreeNode);
			tree.addEventListener(ListEvent.ITEM_EDIT_BEGIN, canEditHandler, false, - 50);		
		}
		
		/**
		 * Sets the <code>text</code> to opened editor.
		 * @flowerModelElementId _OFtJKFw6EeCMd4YreAKWkA
		 */ 
		private function canEditHandler(event:ListEvent):void {
			tree.removeEventListener(ListEvent.ITEM_EDIT_BEGIN, canEditHandler);		
			TextInput(tree.itemEditorInstance).text = text;
			TextInput(tree.itemEditorInstance).addEventListener(KeyboardEvent.KEY_DOWN, editorKeyDownHandler, false, 1);
		}
			
		/**
		 * The method sends a request to server in order to save and validate the editor's text.
		 * In this time, the editor is "seen" in DISABLED state and the original ITEM_EDIT_END behavior
		 * will be stoped from executing.
		 * @flowerModelElementId _egPTMFYjEeCukY0DWzslHg
		 */
		public function endEditing():void {
			editingState = DISABLED;	
			oldLabel = currentEditedTreeNode.label;
			
			tree.statefulClient.setInplaceEditorText(currentEditedTreeNode, TextInput(tree.itemEditorInstance).text, this, handleServerResponse);
		}
		
		/**
		 * If the text was saved succesfully, then the editor is closed.
		 * Otherwise, show the validation error.
		 * @flowerModelElementId _KNlCkFY0EeCukY0DWzslHg
		 */
		private function handleServerResponse(response:Boolean):void {
			editingState = NOT_EDITING;					
			tree.closeEditor();
			if (response) {				
				afterEndEditing();			
			} else {
				currentEditedTreeNode.label = oldLabel;
			}
		}
		
		/**
		 * After closing the validation error dialog, reestablish the editor's state.
		 * @flowerModelElementId _OFtJNVw6EeCMd4YreAKWkA
		 */ 
		private function enableEditing(event:CloseEvent):void {
			editingState = EDITING;
		}
		
		/**
		 * The method is executed after the editor is closed.
		 * If the auto create element after editing preference is enabled and
		 * a CTRL+ENTER was pressed to close the editor, then function is executed to 
		 * create another element.
		 * It's code is provided by {@link StartTreeNodeIEDCommand#runAction()}.
		 * @flowerModelElementId _OF2S81w6EeCMd4YreAKWkA
		 */ 
		private function afterEndEditing():void {			
//			if (!SingletonRefsFromPrePluginEra.preferenceStore.autoCreateElementAfterEditing) {
//				return;
//			}
			if (itemEditEndFunction != null) {
				if (lastExitType == CTRL_ENTER) {
					itemEditEndFunction();
				}
				itemEditEndFunction = null;					
			}
			lastExitType = NO_KEY;	
		}
		
		public function isEditing():Boolean {
			return editingState != NOT_EDITING;
		}
	}
}