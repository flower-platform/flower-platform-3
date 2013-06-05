package com.crispico.flower.flexdiagram.tree {
	import mx.collections.ArrayCollection;
	import mx.utils.UIDUtil;
	
	[Bindable]
	public class TreeNode {
		
		[Embed(source="/../icons/defaultIcon.gif")]			
		public static const INFO:Class;
		
		public var id:String;
		public var name:String;
		public var image:Object;
		
		public var parent:TreeNode;
		public var children:ArrayCollection;
		public var hasChildren:Boolean;
				
		public function TreeNode(name:String = '') {
			id = name;
			this.name = name;
			this.children = new ArrayCollection();
			this.image = INFO;
		}
		
		public static function getNodeByPath(path:ArrayCollection, parent:TreeNode = null):TreeNode {							
			if (path == null) {
				return parent;
			}			
			var node:TreeNode = parent;			
			for each (var id:String in path) {				
				parent = node;
				node = null;
				for each (var child:TreeNode in parent.children) {
					if (child.id == id) {
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
		
		public function getPathForNode():ArrayCollection {
			var node:TreeNode = this;
			if (node == null || node.parent == null) {
				return null;
			}
			var path:ArrayCollection = new ArrayCollection();
			while (node.parent != null) {        		
				path.addItemAt(node.id, 0);
				node = node.parent;
			}			
			return path;
		}
	}
}