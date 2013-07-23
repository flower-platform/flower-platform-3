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
	import flash.utils.Dictionary;
	
	import mx.collections.IList;

	public class ActionUtil {

		public static function processAndIterateActions(rootActionId:String, actions:Vector.<IAction>, selection:IList, forEachCallbackObject:Object, forEachCallbackFunction:Function):void {
			if (actions == null) {
				return;
			}
			var actionsInMap:Vector.<IAction>;
			// process the main list of subactions; i.e. display the first level of actions
			var parentActionIdToActions:Dictionary = new Dictionary();
			for (var i:int = 0; i < actions.length; i++) {
				var action:IAction = actions[i];
				actionsInMap = parentActionIdToActions[action.parentId];
				if (actionsInMap == null) {
					actionsInMap = new Vector.<IAction>();
					parentActionIdToActions[action.parentId] = actionsInMap;
				}
				actionsInMap.push(action);
			}
			actionsInMap = parentActionIdToActions[rootActionId];	
			if (actionsInMap == null) {
				return;
			}
			
			for (i = 0; i < actionsInMap.length; i++) {
				action = actionsInMap[i];
				if (action is IComposedAction) {
					// we do this only when processing the main list of actions
					IComposedAction(action).childActions = parentActionIdToActions[action.id];
				}
				try {
					action.selection = selection;
					if (action.visible) {
						forEachCallbackFunction.call(forEachCallbackObject, action);
					}
				} finally {
					action.selection = null;
				}
			}

		}
		
	}
}