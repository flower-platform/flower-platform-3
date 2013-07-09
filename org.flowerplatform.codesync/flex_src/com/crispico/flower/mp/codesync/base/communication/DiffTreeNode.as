package com.crispico.flower.mp.codesync.base.communication {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;

	/**
	 * The "Bindable" annotation is here in order to have the variables transformaed into accessors
	 * by the Flex compiler (needed by the merge/copy logic <code>UpdateTreeNodeCommand</code>).
	 * 
	 * @flowerModelElementId _A62hcLOTEeCa6MHp-4L_Cw
	 */ 
	[Bindable]
	[RemoteClass(alias="com.crispico.flower.mp.codesync.base.communication.DiffTreeNode")]
	public class DiffTreeNode extends TreeNode {
		public var topLeftColor:uint = 0xFFFFFF;
		public var topRightColor:uint = 0xFFFFFF;
		public var bottomLeftColor:uint = 0xFFFFFF;
		public var bottomRightColor:uint = 0xFFFFFF;
		public var crossColor:uint = 0xFFFFFF;
		public var toolTip:String;
		public var contextMenuEntries:ArrayCollection;
	}
}