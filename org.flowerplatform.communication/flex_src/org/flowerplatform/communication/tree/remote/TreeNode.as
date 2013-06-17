package org.flowerplatform.communication.tree.remote {
	import flash.events.Event;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flexutil.tree.HierarchicalModelWrapper;
	import org.flowerplatform.flexutil.tree.TreeList;
	
	/**
	 * Model element used by the tree to display data. Instances come from
	 * Java.
	 * 
	 * @author Cristi
	 * @author Cristina
	 * 
	 * @flowerModelElementId _32g2IDR9EeCGErbqxW555A
	 */
	[Bindable]
	[RemoteClass]
	[SecureSWF(rename="off")]
	public class TreeNode {
		
		private var _label:String;
		
		private var _icon:String;
				
		private var _hasChildren:Boolean;
		
		/**
		 * @flowerModelElementId _9jJo0DR9EeCGErbqxW555A
		 */
		[SecureSWF(rename="off")]
		public var children:ArrayCollection;
				
		/** 
		 * @flowerModelElementId _HlmtYKAQEeGLneHqP7FuFA
		 */
		[SecureSWF(rename="off")]
		public var parent:TreeNode;
		
		/**
		 * @flowerModelElementId _cLrFQKP8EeGeHqktJlHXmA
		 */
		[SecureSWF(rename="off")]
		public var pathFragment:PathFragment;
				 
		[SecureSWF(rename="off")]
		public var customData:Object;
		
		[SecureSWF(rename="off")]
		/**
		 * @flowerModelElementId _5CHGkDR9EeCGErbqxW555A
		 */
		public function get label():String {
			return _label;
		}

		/**
		 * @private
		 */
		public function set label(value:String):void {
			_label = value;
			dispatchEvent(new Event(TreeList.UPDATE_TREE_RENDERER_EVENT));
		}

		public function get icon():String {
			return _icon;
		}

		public function set icon(value:String):void {
			_icon = value;
			dispatchEvent(new Event(TreeList.UPDATE_TREE_RENDERER_EVENT));
		}

		[SecureSWF(rename="off")]
		/**
		 * @flowerModelElementId _bWTdoDSAEeCGErbqxW555A
		 */
		public function get hasChildren():Boolean {
			return _hasChildren;
		}

		/**
		 * @private
		 */
		public function set hasChildren(value:Boolean):void {
			_hasChildren = value;
			dispatchEvent(new Event(TreeList.UPDATE_TREE_RENDERER_EVENT));
		}

        /**
         * @flowerModelElementId _ZFk8kJpwEeGg5ZWNBtAGAA
         */
        public function getPathForNode():ArrayCollection {
        	var node:TreeNode = this;
        	if (node == null || node.parent == null) {
        		return null;
        	}
        	var path:ArrayCollection = new ArrayCollection();
        	while (node.parent != null) {        		
        		path.addItemAt(node.pathFragment, 0);
        		node = node.parent;
        	}			
			return path;
		}
		
		/**
		 * Based on a given <code>delimiter</code>,
		 * returns the full path of this node by concatenating
		 * all path fragments names.
		 * @flowerModelElementId _mz4XgKr2EeG3eZ1Jezjhtw
		 */ 
		public function getPath(delimiter:String="/"):String {
			var paths:ArrayCollection = getPathForNode();
			var path:String = "";
			for each (var pathFragment:PathFragment in paths) {
				if (path != "") {
					path += delimiter;
				}
				path += pathFragment.name;
			}
			return path;
		}
       
        /**
         * Iterates through all tree structure 
         * to find the corresponding node for given <code>path</code>.
         * Note: if problems, this can be optimized by using a hash map of paths.
         *  
         * @flowerModelElementId _WkupEJpzEeGg5ZWNBtAGAA
         */
		 public static function getNodeByPath(path:ArrayCollection, parent:TreeNode = null):TreeNode {							
			if (path == null) {
				return parent;
			}			
			var node:TreeNode = parent;			
			for each (var pathFragment:PathFragment in path) {				
				parent = node;
				node = null;
				for each (var child:TreeNode in parent.children) {
					if (child.pathFragment.name == pathFragment.name &&
						child.pathFragment.type == pathFragment.type) {
						node = child;
						break;
					}
				}
				if (node == null) {
					return null;
				}
			}
			return node;			
		}	
		 
		 public static function getTreeNodeFromTreeListDataProviderItem(object:Object):TreeNode {
			 return TreeNode(HierarchicalModelWrapper(object).treeNode);
		 }
	}
	
}