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
	import mx.collections.IList;

	public interface IAction {
		function get id():String;
		function get parentId():String;
		function get orderIndex():int;
		function get preferShowOnActionBar():Boolean;
		function get icon():Object;
		function get label():String;
		
		/**
		 * The system sets this before invoking: visible, enabled, run(). After
		 * these invocations, the selection is removed in a try/finally block
		 */
		function set selection(value:IList):void;

		/**
		 * Before this method is invoked, the <code>selection</code> property is populated.
		 * After this method call, the selection is removed.
		 */
		function get visible():Boolean;

		/**
		 * Before this method is invoked, the <code>selection</code> property is populated.
		 * After this method call, the selection is removed.
		 */
		function get enabled():Boolean;
		
		/**
		 * Before this method is invoked, the <code>selection</code> property is populated.
		 * After this method call, the selection is removed.
		 */
		function run():void;
	}
}