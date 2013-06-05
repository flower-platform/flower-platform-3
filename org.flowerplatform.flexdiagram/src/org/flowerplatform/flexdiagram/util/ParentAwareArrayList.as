package org.flowerplatform.flexdiagram.util {
	import mx.collections.ArrayList;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class ParentAwareArrayList extends ArrayList {
		
		public var parent:Object;
		
		public function ParentAwareArrayList(parent:Object, source:Array=null) {
			super(source);
			this.parent = parent;
		}
	}
}