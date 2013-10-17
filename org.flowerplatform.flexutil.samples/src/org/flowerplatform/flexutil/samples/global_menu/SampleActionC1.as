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
package org.flowerplatform.flexutil.samples.global_menu {
	import org.flowerplatform.flexutil.action.ComposedAction;
	
	/**
	 * Composed action sample that reacts to selection.
	 * 
	 * @author Mircea Negreanu
	 */
	public class SampleActionC1 extends ComposedAction {
		public function SampleActionC1() {
			super();
		}
		
		override public function get label():String {
			if (selection == null || selection.length == 0) {
				return "Action for: empty";
			} else {
				return "Action for: " + selection.getItemAt(0);
			}
		}
		
		override public function get enabled():Boolean {
			if (selection == null || selection.length == 0) {
				return true;
			} else {
				if (selection.getItemAt(0) == "Flash") {
					return false;
				}
			}
			
			return true;
		}
	}
}