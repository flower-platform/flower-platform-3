package com.crispico.flower.flexdiagram.tree {
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	
	public class TreeListDataProvider extends ArrayCollection {
		
		//the root MyObj object
		private var _root:TreeNode;
					
		//list of open nodes
		private var _openList:Array;
		
		//pre-computed depth map, so we can give each node the correct visual treatment
		private var _depthMap:Object;
		
		public function TreeListDataProvider(root:TreeNode) {
			_root = root;
			_openList = [];
			_depthMap = {};
			
			computeDepthMap(_root, -1);
			reset();
		}
			
		public function reset(openList:Array = null):void {
			_openList = (openList == null ? [] : openList);
			var a:Array = [];
			addItemToList(_root, a);
			this.source = a;
		}
		
		private function addItemToList(obj:TreeNode, a:Array):void {
			if (obj.children == null) return;
			
			var N:int = obj.children.length;
			for (var i:int = 0; i < N; i++) {
				var child:TreeNode = obj.children[i] as TreeNode;
				a.push(child);
				if (isItemOpen(child)) {
					addItemToList(child, a);
				}
			}
		}
				
		public function isItemOpen(obj:TreeNode):Boolean {
			return (_openList.indexOf(obj.id) != -1);
		}
		
		public function openItem(obj:TreeNode):void {
			if (!obj.hasChildren) {
				return;
			}
			_openList.push(obj.id);
			var a:Array = [];
			addItemToList(obj, a);
			
			var idx:int = this.getItemIndex(obj);
			while (a.length) {
				this.addItemAt(a.shift(), ++idx);
			}
			
		}
		
		public function closeItem(obj:TreeNode):void {
			var idx:int = _openList.indexOf(obj.id);
			if (idx == -1) return;
			
			_openList.splice(idx, 1);
			
			var a:Array = [];
			addItemToList(obj, a);
			idx = this.getItemIndex(obj) + 1;
			while (a.length) {
				this.removeItemAt(idx);
				a.shift();
			}
		}
		
		/**
		 * When this class is initialized, we recursively compute a depth map
		 * for all objects in the tree.  This makes future depth lookups fast.
		 * Root node has depth of zero, its children have depth of 1, etc.
		 */
		public function computeDepthMap(obj:TreeNode, depth:int):void {
			_depthMap[obj.id] = depth;
			if (obj.children == null) return;
			
			var N:int = obj.children.length;
			for (var i:int = 0; i < N; i++) {
				var child:TreeNode = obj.children[i] as TreeNode;
				computeDepthMap(child, depth + 1);
			}
		}
		
		public function getItemDepth(obj:TreeNode):int {			
			if (_depthMap.hasOwnProperty(obj.id)) {
				return _depthMap[obj.id];
			} else {
				return 0;
			}
		}
		
		public function updateNode(path:ArrayCollection, newNode:TreeNode):void {
			var oldNode:TreeNode = TreeNode.getNodeByPath(path, _root);
			if (oldNode == null) {
				return;
			}
			updateNodeInternal(oldNode, newNode);
			
			refresh();
		}
		
		private function updateNodeInternal(oldNode:TreeNode, newNode:TreeNode):void {			
			if (oldNode.hasChildren && !newNode.hasChildren) {
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
					for each (var oldChildNode:TreeNode in oldNode.children) {
						oldChildrenMap[oldChildNode.id] = oldChildNode;
					}
				} else {
					oldNode.children = new ArrayCollection();
				}						
				// iterate the new children list
				for each (var newChildNode:TreeNode in newNode.children) {
					oldChildNode = oldChildrenMap[newChildNode.id];
					if (oldChildNode == null) {						
						// no corresponding element found; creating a new one						
						oldChildNode = newChildNode;
						if (oldChildNode.children == null) {
							oldChildNode.children = new ArrayCollection();	
						}
					} else {
						// a corresponding element was found; we'll use it						
						delete oldChildrenMap[oldChildNode.id];						
					}					
					
					// recurse to (re)initialize the node
					updateNodeInternal(oldChildNode, newChildNode);
					oldChildNode.parent = oldNode;
					// and add it as to the new list
					newChildrenList.addItem(oldChildNode);				
				}
				
				if (isItemOpen(oldNode)) {
					var a:Array = [];
					addItemToList(oldNode, a);
					idx = this.getItemIndex(oldNode) + 1;
					while (a.length) {
						this.removeItemAt(idx);
						a.shift();
					}
				}
				oldNode.children.removeAll();			
				oldNode.children.addAll(newChildrenList);
						
				if (isItemOpen(oldNode)) {
					a = [];
					addItemToList(oldNode, a);				
					var idx:int = this.getItemIndex(oldNode);
					while (a.length) {
						this.addItemAt(a.shift(), ++idx);
					}
				}
			}
			
			computeDepthMap(oldNode, getItemDepth(oldNode));			
		}	
				
	}		
}

