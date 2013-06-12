package  com.crispico.flower.util.layout.persistence {
	import mx.collections.ArrayCollection;
	
	/**
	 * Represents a <code>SashLayoutData</code> entity that stores informations about
	 * the position (left/right/bottom) of all its <code>StackLayoutData</code> 
	 * children found in minimized state.
	 * 
	 * @autor Cristina
	 * @flowerModelElementId __hOqYCunEeG6vrEjfFek0Q
	 */
	public class WorkbenchLayoutData extends SashLayoutData  {
		
		public var minimizedStacks:ArrayCollection = new ArrayCollection();
		
		/**
		 * Keeps a list with all undocked views.
		 */ 
		public var undockedViews:ArrayCollection = new ArrayCollection();
	}
}