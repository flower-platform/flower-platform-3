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
	
	import com.crispico.flower.util.spinner.ModalSpinner;
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.common.CommonPlugin;
	import org.flowerplatform.web.git.GitPlugin;
	import org.flowerplatform.web.git.history.remote.dto.HistoryEntryDto;

	/**
	 * @author Cristina Constantinescu
	 */ 
	public class CherryPickAction extends AbstractHistoryAction {
		
		public function CherryPickAction() {			
			label = GitPlugin.getInstance().getMessage("git.history.action.cherryPick.label");
			icon = GitPlugin.getInstance().getResourceUrl("/images/full/obj16/cherry-pick.png");
			orderIndex = int(GitPlugin.getInstance().getMessage("git.history.action.cherryPick.sortIndex"));
		}
		
		override public function run():void {			
			GitPlugin.getInstance().getHistoryView().statefulClient
				.cherryPick(HistoryEntryDto(selection[0]), context.repository, this, refreshCallbackHandler);							
		}
		
	}	
}