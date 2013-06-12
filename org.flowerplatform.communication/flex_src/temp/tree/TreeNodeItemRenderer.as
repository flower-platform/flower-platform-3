package temp.tree {
	import com.crispico.flower.flexdiagram.util.common.BitmapContainer;
	import com.crispico.flower.util.tree.CustomIconTreeItemRenderer;
	
	import mx.core.IFlexDisplayObject;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;

	/**
	 * Renderer implementation that uses <code>TreeNode.iconUrls</code> to
	 * retrieve an image from the factory.
	 * 
	 * @author Cristi
	 * @flowerModelElementId _3ZzosEv6EeCTEPQXKsfiNg
	 */ 
	public class TreeNodeItemRenderer extends CustomIconTreeItemRenderer {
		
		/**
		 * @see Class doc.
		 */ 
		override protected function createIcon():IFlexDisplayObject {
//			if (data == null)
				return null;
//			
//			var treeNode:TreeNode = TreeNode(data);
//			if (icon == null) // First time when populating item renderer
//				return new BitmapContainer(treeNode.iconUrls);
//
//			// Reuse the icon. Although it will be removed, the CustomIconTreeItemRenderer will add it back. 
//			BitmapContainer(icon).retrieveImage(treeNode.iconUrls)
//			return icon;
		}
	}
}