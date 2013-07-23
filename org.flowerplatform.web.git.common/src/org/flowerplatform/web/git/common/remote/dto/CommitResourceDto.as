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
package  org.flowerplatform.web.git.common.remote.dto {
		
	/**
	 *	@author Cristina Constantinescu
	 */ 
	[RemoteClass]
	[Bindable]
	public class CommitResourceDto {
		
		public static const ADDED:int = 0;
		public static const CHANGED:int = 1;
		public static const REMOVED:int = 2;
		public static const CONFLICTING:int = 3;
		public static const MODIFIED:int = 4;
		public static const UNTRACKED:int = 5;
		public static const MISSING:int = 6;
		
		public var path:String;
		
		public var label:String;
		
		public var image:String;
		
		public var state:int;
		
		private var _selected:Boolean;
		
		public function getSelected():Boolean {
			return _selected;
		}
		
		public function setSelected(value:Boolean):void {
			this._selected = value;
		}
				
	}
}