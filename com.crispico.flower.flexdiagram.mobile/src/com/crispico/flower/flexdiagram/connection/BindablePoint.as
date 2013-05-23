package com.crispico.flower.flexdiagram.connection {

	/**
	 * 
	 * A point data structure that has its 2 properties bindable.
	 * 
	 * @author Crist
	 * @flowerModelElementId _bwlnFL8REd6XgrpwHbbsYQ
	 */
	public class BindablePoint {

		/**
		 * @flowerModelElementId _bwlnGL8REd6XgrpwHbbsYQ
		 */
		[Bindable]
		public var x:int;
		
		/**
		 * @flowerModelElementId _bwlnG78REd6XgrpwHbbsYQ
		 */
		[Bindable]
		public var y:int;
		
		/**
		 * @flowerModelElementId _bwlnHr8REd6XgrpwHbbsYQ
		 */
		public function BindablePoint(x:int, y:int) {
			this.x = x;
			this.y = y;
		}
		
	}
}