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
package org.flowerplatform.web.git.dto {
	import mx.collections.ArrayCollection;
	import mx.controls.List;
	
	/**
	 *	@author Cristina Constantinescu
	 */   
	[RemoteClass]
	[Bindable]
	public class GitRef {
		
		public var name:String;
		
		public var shortName:String;
		
		public var label:String;
		
		public var upstream:String;
		
		public var image:String;
			
		private var _selected:Boolean = false;
		
		public function getSelected():Boolean {
			return _selected;
		}
		
		public function setSelected(value:Boolean):void {
			this._selected = value;
		}
	}
}