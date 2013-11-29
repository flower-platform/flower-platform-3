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
package org.flowerplatform.web.git.history.action {
	import com.crispico.flower.flexdiagram.action.BaseAction;
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.web.git.GitPlugin;
	import org.flowerplatform.web.git.history.remote.dto.HistoryEntryDto;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class AbstractHistoryAction extends ActionBase {

		override public function get visible():Boolean {
			if (selection.length > 1) {
				return false;
			} 
			if (!(selection[0] is HistoryEntryDto)) {
				return false;
			}
			return true;
		}
		
		protected function refreshCallbackHandler(result:Boolean):void {
			GitPlugin.getInstance().getHistoryView().refreshClickHandler();
		}
		
	}
}