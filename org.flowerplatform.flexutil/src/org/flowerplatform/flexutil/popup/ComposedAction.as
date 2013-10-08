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
package org.flowerplatform.flexutil.popup {
	public class ComposedAction extends ActionBase implements IComposedAction {

		private var _childActions:Vector.<IAction>;

		public function get childActions():Vector.<IAction>
		{
			return _childActions;
		}

		public function set childActions(value:Vector.<IAction>):void
		{
			_childActions = value;
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		override public function get visible():Boolean {
			if (childActions == null) {
				return false;
			} else {
				for (var i:int = 0; i < childActions.length; i++) {
					var childAction:IAction = childActions[i];
					childAction.selection = selection;
					if (childActions[i].visible) {
						// at least one visible => the composed action is visible
						return true;
					}
				}
				return false;
			}
		}
		
		
	}
}