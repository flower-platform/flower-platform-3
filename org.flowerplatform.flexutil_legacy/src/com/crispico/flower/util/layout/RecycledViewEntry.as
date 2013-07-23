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
package  com.crispico.flower.util.layout {
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	import mx.core.UIComponent;
	
	/**
	 * @author Cristina
	 * @flowerModelElementId _pp59wOI-EeGF46ujw3kLCA
	 */
	public class RecycledViewEntry {
		
		/**
		 * @flowerModelElementId _r5FLEOI-EeGF46ujw3kLCA
		 */
		public var oldViewLayoutData:ViewLayoutData;
		
		/**
		 * @flowerModelElementId _uyscgOI-EeGF46ujw3kLCA
		 */
		public var existingView:UIComponent;
				
		public function RecycledViewEntry(oldViewLayoutData:ViewLayoutData, existingView:UIComponent) {
			this.oldViewLayoutData = oldViewLayoutData;
			this.existingView = existingView;			
		}
	}
	
}