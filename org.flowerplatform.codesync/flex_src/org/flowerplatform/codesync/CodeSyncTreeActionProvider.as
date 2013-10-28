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
package org.flowerplatform.codesync {
	
	import mx.collections.IList;
	
	import org.flowerplatform.codesync.action.ShowCodeSyncDescriptorsAction;
	import org.flowerplatform.codesync.remote.CodeSyncAction;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.action.IActionProvider;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class CodeSyncTreeActionProvider implements IActionProvider {
		
		/**
		 * @author Mariana Gheorghe
		 * @author Mircea Negreanu
		 */
		public function getActions(selection:IList):Vector.<IAction> {
			var result:Vector.<IAction> = new Vector.<IAction>();
			// TODO temp; reactivate other technologies at a later time
//			result.push(new CodeSyncAction("Code Sync - java", "java"));
//			result.push(new CodeSyncAction("Code Sync - js", "js"));
//			result.push(new CodeSyncAction("Wiki Sync", "github"));
			result.push(new CodeSyncAction("Synchronize", "js"));
			// also add the show descriptor list action
			result.push(new ShowCodeSyncDescriptorsAction());
			return result;
		}
	}
}
