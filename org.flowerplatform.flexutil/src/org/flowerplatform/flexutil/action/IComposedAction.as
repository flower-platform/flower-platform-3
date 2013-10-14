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
package org.flowerplatform.flexutil.action {

	/**
	 * Child actions don't need to be added manually. Instead, set the <code>parentId</code> of the
	 * child, pointing towards the id of the composed action. The logic from
	 * <code>ActionUtil.processAndIterateActions()</code> adds the child actions, during
	 * interaction from UI (e.g. click on context menu, etc.). 
	 * 
	 * @author Cristian Spiescu
	 */
	public interface IComposedAction extends IAction {
		function get childActions():Vector.<IAction>;
		function set childActions(value:Vector.<IAction>):void;
	}
}