package  org.flowerplatform.flexutil.layout {
	
	import mx.collections.ArrayCollection;
	
	/**
	 * Represents the node data of a tree structure. <br>
	 * Stores information like the node's parent and its children if any.
	 * 
	 * @author Cristina
	 * @flowerModelElementId _Jjl7QEslEeGoH-1QrqBGdw
	 */ 
	public class LayoutData {
		
		/**
		 * Represents the parent node.
		 * @flowerModelElementId _JjnJYEslEeGoH-1QrqBGdw
		 */ 
		public var parent:LayoutData;
		
		/**
		 * Represents the list of children nodes.
		 * It can be null/empty (leaf node). 
		 */ 
		public var children:ArrayCollection = new ArrayCollection(); /* of LayoutData*/
		
	}
	
}