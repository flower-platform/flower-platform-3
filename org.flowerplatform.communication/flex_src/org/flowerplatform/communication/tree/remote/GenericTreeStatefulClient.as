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
package org.flowerplatform.communication.tree.remote {
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.utils.DescribeTypeCache;
	import mx.utils.ObjectUtil;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.command.CompoundServerCommand;
	import org.flowerplatform.communication.stateful_service.IStatefulClientLocalState;
	import org.flowerplatform.communication.stateful_service.ServiceInvocationOptions;
	import org.flowerplatform.communication.stateful_service.StatefulClient;
	import org.flowerplatform.communication.tree.remote.GenericTreeStatefulClientLocalState;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.tree.HierarchicalModelWrapper;
	import org.flowerplatform.flexutil.tree.TreeList;
	
	import temp.tree.GenericTree;
	import temp.tree.GenericTreeInplaceEditorManager;
		
	/**
	 * @author Cristina
	 * @flowerModelElementId _SLLCYA70EeKbvNML8mcTuA
	 */
	public class GenericTreeStatefulClient extends StatefulClient {
		
//		/**
//		 * @flowerModelElementId _OZLrABN0EeKR8sYuzDGiDQ
//		 */
//		public static const EXPAND_NODE_KEY:String = "expandNode";
//		
//		public static const EXPAND_ALL_NODES_KEY:String = "expandAllNodes"
//			
		public static const WHOLE_TREE_KEY:String = "wholeTree";	
		
		public static const DONT_UPDATE_MAP_KEY:String = "dontUpdateMap";	
//
//		/**
//		 * @flowerModelElementId _OZRKkBN0EeKR8sYuzDGiDQ
//		 */
//		public static const SELECT_NODE_KEY:String = "selectNode";
		
		/**
		 * @see Getter doc.
		 * @flowerModelElementId _50n5oBE1EeKNlYFNXVVOOw
		 */
		private var _treeNumber:int = -1;
		
		public var clientIdPrefix:String;
		
		public var statefulServiceId:String;
		
		public var context:Object = new Object();
		
		public var treeList:TreeList;
		
		public var requestDataOnSubscribe:Boolean = true;
		
		public var requestDataOnServer:Boolean = true;
		
		///////////////////////////////////////////////////////////////
		// Normal methods
		///////////////////////////////////////////////////////////////
		
		/**
		 * The number is calculated by choosing the smallest number 
		 * that isn't used by other stateful client ids that
		 * starts with <code>genericTree.clientIdPrefix</code>.
		 * 
		 * @return the tree number used to create the structure of stateful client id.
		 * @see getStatefulClientId()
		 * @flowerModelElementId _Oa9zsBN0EeKR8sYuzDGiDQ
		 */ 
		public function get treeNumber():int {	
			if (_treeNumber == -1) {
				var number:int = 1;
				while (CommunicationPlugin.getInstance().statefulClientRegistry.getStatefulClientById(clientIdPrefix + " " + number) != null) {
					number++;
				}
				_treeNumber = number;
			}
			return _treeNumber;
		}
		
		/**
		 * @flowerModelElementId _gJdiMA70EeKbvNML8mcTuA
		 */
		public override function getCurrentStatefulClientLocalState(dataFromRegistrator:Object = null):IStatefulClientLocalState {			
			var openNodes:ArrayCollection = new ArrayCollection();
//			for each (var openNode:TreeNode in genericTree.openItems) {
//				var path:ArrayCollection = openNode.getPathForNode();
//				if (path != null) { // don't add the root node
//					openNodes.addItem(path);
//				}
//			}
			var clientLocalState:GenericTreeStatefulClientLocalState = new GenericTreeStatefulClientLocalState();
			clientLocalState.clientContext = context;
			clientLocalState.openNodes = openNodes;
			
			return clientLocalState;
			return null;
		}
				
		/**
		 * @flowerModelElementId _gKyX4A70EeKbvNML8mcTuA
		 */
		public override function getStatefulClientId():String {			
			return clientIdPrefix + " " + treeNumber;
		}
		
		/**
		 * @flowerModelElementId _gK2CQA70EeKbvNML8mcTuA
		 */
		public override function getStatefulServiceId():String {		
			return statefulServiceId;
		}		
		
		/**
		 * @flowerModelElementId __opRQBE2EeKNlYFNXVVOOw
		 */
		public override function subscribeToStatefulService(dataFromRegistrator:Object):void {		
			super.subscribeToStatefulService(dataFromRegistrator);
			
			TreeNode(treeList.rootNode).pathFragment = new PathFragment(statefulServiceId, "r");
			
			// request data from server and expand root node
			if (requestDataOnSubscribe) {
//				var oldContext:Object = genericTree.context;
//				genericTree.context[EXPAND_NODE_KEY] = true;
				openNode(null);
//				genericTree.context = oldContext;
			}			
		}
		
//		/**
//		 * @flowerModelElementId __ov-8BE2EeKNlYFNXVVOOw
//		 */
//		public override function unsubscribeFromStatefulService(dataFromUnregistrator:Object):Boolean {			
//			// remove listeners added and unregister from managers
//			if (genericTree.inplaceEditorEnabled) {
//				genericTree.inplaceEditorEnabled = false;
//			}	
//			if (genericTree.contextMenuEnabled) {
//				genericTree.contextMenuEnabled = false;			
//			}						
//			return super.unsubscribeFromStatefulService(dataFromUnregistrator);
//		}
//		
//		/**
//		 * @flowerModelElementId _dHZ1UBE3EeKNlYFNXVVOOw
//		 */
//		protected override function invokeServiceMethod(methodName:String, parameters:Array, serviceInvocationOptions:ServiceInvocationOptions=null):Object {	
//			if (genericTree.wrapServiceInvocationCommandWithCompoundCommand) {				
//				if (serviceInvocationOptions == null) {
//					serviceInvocationOptions = new ServiceInvocationOptions();
//				}
//				serviceInvocationOptions.setReturnCommandWithoutSending(true);				
//				var command:Object = super.invokeServiceMethod(methodName, parameters, serviceInvocationOptions);
//				var compoundCommand:CompoundServerCommand = new CompoundServerCommand();
//				command = compoundCommand.append(command);
//				
//				CommunicationPlugin.getInstance().bridge.sendObject(command);
//				
//				return command;
//			}
//			return super.invokeServiceMethod(methodName, parameters, serviceInvocationOptions);
//		}
//		
//		/**
//		 * Overrided to use the function provided if any.
//		 * 
//		 * <p>
//		 * For dispatched trees, 
//		 * a <code>removeUIAndRelatedElementsAndStatefulClientBecauseUnsubscribedForcefullyFunction</code>
//		 * MUST BE PROVIDED.
//		 * Otherwise, the exception from super will be thrown.
//		 * @flowerModelElementId _yb8YgC5YEeKuRNhqLBp7Og
//		 */ 
//		protected override function removeUIAndRelatedElementsAndStatefulClientBecauseUnsubscribedForcefully():void {
//			if (genericTree.removeUIAndRelatedElementsAndStatefulClientBecauseUnsubscribedForcefullyFunction == null) {
//				if (genericTree.dispatchEnabled) {
//					super.removeUIAndRelatedElementsAndStatefulClientBecauseUnsubscribedForcefully();
//				}				
//			} else {
//				genericTree.removeUIAndRelatedElementsAndStatefulClientBecauseUnsubscribedForcefullyFunction();
//			}
//		}		
//		
//		private var rvl_pathToOpen:ArrayCollection;				
//		private var rvl_index:Number;
//		private var rvl_path:ArrayCollection;
//		
//		/**
//		 * @flowerModelElementId _OcKtkBN0EeKR8sYuzDGiDQ
//		 */
//		private function revealNodeInternal(value:Object=null):void {
//			// increase the index to process the next node
//			rvl_index++;
//			if (rvl_index == rvl_path.length) { // stop if end of list
//				return;
//			}
//			// create path for current node
//			rvl_pathToOpen.addItem(rvl_path.getItemAt(rvl_index));
//			
//			// get node from tree
//			var existingNode:TreeNode = TreeNode.getNodeByPath(rvl_pathToOpen, TreeNode(genericTree.dataProvider[0]));			
//			if (existingNode != null && !(genericTree.isItemOpen(existingNode) && rvl_pathToOpen.length < rvl_path.length - 1)) {
//				// if node isn't opened and isn't the one to reveal
//				if (rvl_index == rvl_path.length - 1) { // if is the one to reveal, just select it
//					genericTree.selectedItem = existingNode;
//					genericTree.scrollToIndex(genericTree.selectedIndex);
//				} else { // otherwise open node and expand it
//					genericTree.context[EXPAND_NODE_KEY] = true;		
//					genericTree.context[SELECT_NODE_KEY] = true; // FIXME : Probably it should be unset after ther result arrives? 
//					openNode(rvl_pathToOpen, this, revealNodeInternal);						
//				}
//			} else { // node already opened, process the next one				
//				revealNodeInternal();
//			}					
//		}
//		
//		private var ied_contributionId:String;
//		private var ied_path:ArrayCollection;
//		private var ied_parentNode:TreeNode;			
//		private var ied_autoCreateElementAfterEditing:Boolean;
//		
//		/**
//		 * @flowerModelElementId _OcjvIRN0EeKR8sYuzDGiDQ
//		 */
//		private function ied_openNodeCallback(value:Object):void {
//			var treeNode:TreeNode = TreeNode.getNodeByPath(ied_path, TreeNode(genericTree.dataProvider[0]));
//			var iedManager:GenericTreeInplaceEditorManager = GenericTreeInplaceEditorManager.activeTrees[getStatefulClientId()];
//			iedManager.itemEditEndFunction = ied_runCreateAction;			
//			
//			// start editing only if the preference is enabled
//			if (ied_autoCreateElementAfterEditing && treeNode != null) {
//				iedManager.startEditing(treeNode);
//			}
//		}
//		
//		/**
//		 * @flowerModelElementId _OcyYoRN0EeKR8sYuzDGiDQ
//		 */
//		private function ied_runCreateAction():void {
//			var selection:ArrayCollection = new ArrayCollection();
//			selection.addItem(ied_parentNode);
//			var action:Object = SingletonRefsFromPrePluginEra.flowerContributionRepository.getFlowerActionById(ied_contributionId);
//			
//			var context:ActionContext = new ActionContext();
//			context.statefulServiceId = genericTree.serviceId;	
//			context.statefulClientId = getStatefulClientId();	
//			action.context = context;
//			
//			action.run(selection);			
//		}	
		
		/**
		 * @flowerModelElementId _Ob3LkBN0EeKR8sYuzDGiDQ
		 */
		private function updateNodeInternal(oldNode:TreeNode, newNode:TreeNode):void {			
			var classInfo:XML = DescribeTypeCache.describeType(oldNode).typeDescription;
			for each (var v:XML in classInfo..accessor) {
				if (v.@name != null && v.@access != 'readonly' && newNode[v.@name] != null
					&& v.@name != "children" && v.@name != "hasChildren") {
					/**
					 * Don't merge label or iconUrls in case they are not changed; otherwise
					 * it will trigger a display update that wouldn't be necessary, and sometimes
					 * may cause lag.
					 * 
					 * @author Mariana
					 */ 
					var update:Boolean = true;
					if (v.@name == "label") { 
						if (oldNode.label == newNode.label)
							update = false;
					} else {
						if (v.@name == "icon") { 
							if (oldNode.icon == newNode.icon)
								update = false;
//						if (v.@name == "iconUrls") {
//							update = false;
//							for each (var newUrl:Object in newNode.iconUrls) {
//								if (oldNode.iconUrls && !oldNode.iconUrls.contains(newUrl)) {
//									update = true;
//									break;
//								}
//							}
//							for each (var oldUrl:Object in oldNode.iconUrls) {
//								if (newNode.iconUrls && !newNode.iconUrls.contains(oldUrl)) {
//									update = true;
//									break;
//								}
//							}
						}
					}
					if (update) {
						oldNode[v.@name] = newNode[v.@name];
					}
				}
			}
			
			// hasChildren
			if (oldNode.hasChildren && !newNode.hasChildren) {
				// if childNode changed from true -> false, we force 
				// unregistering existing child nodes
				newNode.children = new ArrayCollection();
			}			
			oldNode.hasChildren = newNode.hasChildren;
			
			// children
			if (newNode.children != null) {
				// this list will replace the current children list of this node;
				// the tree implementation doesn't mind if we replace the list; if
				// there are branches opened, they will be kept opened
				var newChildrenList:ArrayCollection = new ArrayCollection();			
				
				// fill a map with existing child in the old node (left)
				var oldChildrenMap:Dictionary = new Dictionary();
				if (oldNode.children != null) {
					for each (var oldChildNode:TreeNode in oldNode.children)
					oldChildrenMap[oldChildNode.pathFragment.name + oldChildNode.pathFragment.type] = oldChildNode;
				} else {
					oldNode.children = new ArrayCollection();
				}						
				// iterate the new children list
				for each (var newChildNode:TreeNode in newNode.children) {
					oldChildNode = oldChildrenMap[newChildNode.pathFragment.name + newChildNode.pathFragment.type];
					if (oldChildNode == null) {						
						// no corresponding element found; creating a new one						
						oldChildNode = newChildNode;
						if (oldChildNode.children == null) {
							oldChildNode.children = new ArrayCollection();	
						}
					} else {
						// a corresponding element was found; we'll use it						
						delete oldChildrenMap[oldChildNode.pathFragment.name + oldChildNode.pathFragment.type];						
					}					
					
					// recurse to (re)initialize the node
					updateNodeInternal(oldChildNode, newChildNode);
					oldChildNode.parent = oldNode;
					// and add it as to the new list
					newChildrenList.addItem(oldChildNode);				
				}					
				oldNode.children.removeAll();			
				oldNode.children.addAll(newChildrenList);	
			}				
		}	
		
		///////////////////////////////////////////////////////////////
		// Proxies to service methods
		///////////////////////////////////////////////////////////////
		
		/**
		 * Sends command to server to request content for given node.
		 * If <code>node</code> is <code>null</code>, then the root
		 * content is requested.
		 * 
		 * @flowerModelElementId _6HHgUJpvEeGg5ZWNBtAGAA
		 */
		public function openNode(node:Object, resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
			var path:ArrayCollection;
			if (node is TreeNode && node != null) {
				path = TreeNode(node).getPathForNode();
			} else {
				path = ArrayCollection(node);
			}
			
			invokeServiceMethod(
				"openNode", 
				[path, context], 
				new ServiceInvocationOptions().setResultCallbackObject(resultCallbackObject).setResultCallbackFunction(resultCallbackFunction));			
		}
			
		/**
		 * Sends command to server to cleanup the data used for old opened node.
		 *  If <code>node</code> is <code>null</code>, then all data is cleanup.
		 * 
		 * @flowerModelElementId _RhY4wJp4EeGg5ZWNBtAGAA
		 */
		public function closeNode(node:Object):void {
			var path:ArrayCollection;
			if (node is TreeNode && node != null) {
				path = TreeNode(node).getPathForNode();
			} else {
				path = ArrayCollection(node);
			}
			invokeServiceMethod("closeNode", [path, context]);
		}
		
		public function updateTreeStatefulContext(key:String, value:Object, resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
			invokeServiceMethod(
				"updateTreeStatefulContext", 
				[key, value],
				new ServiceInvocationOptions().setResultCallbackObject(resultCallbackObject).setResultCallbackFunction(resultCallbackFunction));
		}
		
		/**
		 * @flowerModelElementId _ObTK4RN0EeKR8sYuzDGiDQ
		 */
		public function getInplaceEditorText(node:TreeNode, resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
			invokeServiceMethod(
				"getInplaceEditorText", 
				[node.getPathForNode()], 
				new ServiceInvocationOptions().setResultCallbackObject(resultCallbackObject).setResultCallbackFunction(resultCallbackFunction));
		}
		
		/**
		 * @flowerModelElementId _ObYDYhN0EeKR8sYuzDGiDQ
		 */
		public function setInplaceEditorText(node:TreeNode, text:String, resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
			invokeServiceMethod(
				"setInplaceEditorText", 
				[node.getPathForNode(), text], 
				new ServiceInvocationOptions().setResultCallbackObject(resultCallbackObject).setResultCallbackFunction(resultCallbackFunction));			
		}
		
		/**
		 * @flowerModelElementId _ObnT8RN0EeKR8sYuzDGiDQ
		 */
		public function performDrop(dropNode:Object, draggedNodes:Array):void {
			var draggedNodesPaths:ArrayCollection = new ArrayCollection();
			
			for (var i:int = 0; i < draggedNodes.length; i ++) {
				draggedNodesPaths.addItem((draggedNodes[i] as TreeNode).getPathForNode());
			}
			
			invokeServiceMethod("performDrop", [dropNode.getPathForNode(), draggedNodesPaths]);			
		}
		
		///////////////////////////////////////////////////////////////
		//@RemoteInvocation methods
		///////////////////////////////////////////////////////////////
		
		/**
		 * Sent from Java: 
		 * <ul>
		 * 	<li>as a result of a <code>InvokeServiceMethodServerCommand</code>, 
		 * 		when a node is opening or,
		 * 	<li>as a result of a change notification received on Java (optional).
		 * </ul>
		 *  
		 * Retrieves the corresponding <code>GenericTree</code> and the corresponding 
		 * <code>TreeNode</code> and recursively merges the existing nodes with the new ones. 
		 * 
		 * <p>
		 * The following strategy is employed: no data is provided => we don't update. E.g. for
		 * tree label, tree children. It is up to the sender to decide how much data sends. In 
		 * theory a whole tree could be sent with one command, or just a node, or just a branch.
		 * In practice 1 level of children are requested when a tree node opens.
		 * 	
		 * @author Cristi
		 * @author Cristina
		 * 		
		 * @flowerModelElementId __GlaUA73EeKbvNML8mcTuA
		 */
		[RemoteInvocation]
		public function updateNode(path:ArrayCollection, newNode:TreeNode, expandNode:Boolean = false, collapseNode:Boolean = false, selectNode:Boolean = false):void {			
			var existingNode:TreeNode = TreeNode.getNodeByPath(path, TreeNode(treeList.rootNode));
			if (existingNode == null) {
				return; // this client doesn't currently display the given node
			}
			// update node
			updateNodeInternal(existingNode, newNode);
			
//			// sets new selection if necessary
//			var rootTreeNode:TreeNode = TreeNode(genericTree.dataProvider[0]);
//			var newSelection:Array = [];
//			var updateSelection:Boolean = false;
//			for each(var selectedTreeNode:TreeNode in genericTree.selectedItems) {
//				var selectedPath:ArrayCollection /* of PathFragment */ = selectedTreeNode.getPathForNode();
//				var selectedNode:TreeNode = TreeNode.getNodeByPath(selectedPath, rootTreeNode);
//				if (selectedNode != null) // If still in tree add it to the selection.
//					newSelection.push(selectedNode);
//				else 
//					updateSelection = true;
//			}
//			
//			/**
//			 * Only set selection if it has actually changed, otherwise a display update will 
//			 * be triggered (see comment above on updating labels and icons).
//			 * 
//			 * @author Mariana
//			 */ 
//			if (updateSelection) {
//				genericTree.selectedItems = newSelection;
//			}
//			
//			if (expandNode) {					
//				genericTree.expandItem(existingNode, true);
//			}
//			
//			if (genericTree.context[EXPAND_ALL_NODES_KEY] == true) {
//				genericTree.expandChildrenOf(existingNode, true /* open */);
//			}
//			
//			if (collapseNode) {
//				genericTree.expandItem(existingNode, false);
//				// Mariana: in case of collapsing, we also need to clear the children list, in case they are invalid
//				// i.e. if a model file has become corrupt, the children shouldn't show anymore
//				existingNode.children = new ArrayCollection();
//			}
//			if (selectNode) {
//				genericTree.selectedItem = existingNode;
//				genericTree.scrollToIndex(genericTree.selectedIndex);
//			}
		}
				
		/**
		 * @flowerModelElementId _AmkYQBEaEeKYjqFAQECmkA
		 */
		[RemoteInvocation]
		public function revealNode(path:ArrayCollection):void {
//			this.rvl_path = path;
//			this.rvl_index = -1;
//			this.rvl_pathToOpen = new ArrayCollection();
//			
//			revealNodeInternal();
		}	
			
		/**
		 * @flowerModelElementId _OcWTwRN0EeKR8sYuzDGiDQ
		 */
		[RemoteInvocation]
		public function startInplaceEditor(contributionId:String, path:ArrayCollection, autoCreateElementAfterEditing:Boolean):void {
//			// properties initialization
//			this.ied_contributionId = contributionId;
//			this.ied_path = path;
//			this.ied_autoCreateElementAfterEditing = autoCreateElementAfterEditing;
//			
//			// get parent path
//			var parentPath:ArrayCollection = ArrayCollection(ObjectUtil.clone(ied_path));
//			parentPath.removeItemAt(parentPath.length - 1);				
//			// expand parent
//			ied_parentNode = TreeNode.getNodeByPath(parentPath, TreeNode(genericTree.dataProvider[0]));			
//			if (ied_parentNode != null) {
//				genericTree.context[EXPAND_NODE_KEY] = true;
//				openNode(ied_parentNode, this, ied_openNodeCallback);		
//			}	
		}		
	}
	
}