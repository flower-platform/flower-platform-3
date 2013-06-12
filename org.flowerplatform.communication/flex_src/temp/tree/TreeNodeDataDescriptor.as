package temp.tree {
	import mx.collections.ArrayCollection;
	import mx.collections.ICollectionView;
	import mx.controls.treeClasses.ITreeDataDescriptor;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	
	/**
	 * <code>ITreeDataDescriptor</code> that knows to describe a 
	 * <code>TreeNode</code>.
	 * 
	 * @author Cristi
	 * @flowerModelElementId _NEPYsDTtEeCWJOrqWwArag
	 */
	public class TreeNodeDataDescriptor implements ITreeDataDescriptor {
		
		public function getChildren(node:Object, model:Object=null):ICollectionView {
			return TreeNode(node).children;
		}
		
		public function hasChildren(node:Object, model:Object=null):Boolean {
			return TreeNode(node).hasChildren;
		}
		
		public function isBranch(node:Object, model:Object=null):Boolean {
			return hasChildren(node);
		}
		
		public function getData(node:Object, model:Object=null):Object {
			return node;
		}
		
		public function addChildAt(parent:Object, newChild:Object, index:int, model:Object=null):Boolean {
			throw "not implemented";
		}
		
		public function removeChildAt(parent:Object, child:Object, index:int, model:Object=null):Boolean {
			throw "not implemented";
		}
	}
}