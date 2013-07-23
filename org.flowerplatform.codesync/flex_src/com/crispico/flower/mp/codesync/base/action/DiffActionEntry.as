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
package com.crispico.flower.mp.codesync.base.action {
	/**
	 * @flowerModelElementId _AuBpkLOXEeCa6MHp-4L_Cw
	 */
	[Bindable]
	[RemoteClass(alias="com.crispico.flower.mp.codesync.base.action.DiffActionEntry")]
	public class DiffActionEntry {
		public var actionType:int;
		public var label:String;
		public var diffIndex:int;
		public var enabled:Boolean;
		public var default1:Boolean;
	}
}