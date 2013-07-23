/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.flexdiagram.renderer.connection {

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