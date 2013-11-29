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
	import flash.desktop.Clipboard;
	import flash.desktop.ClipboardFormats;
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.web.git.GitPlugin;
	import org.flowerplatform.web.git.history.remote.dto.HistoryEntryDto;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class CopyIdToClipboardAction extends AbstractHistoryAction {
		
		public function CopyIdToClipboardAction() {
			super();
			label = GitPlugin.getInstance().getMessage("git.history.action.copy.label");
			icon = GitPlugin.getInstance().getResourceUrl("images/copy.gif");
			orderIndex = int(GitPlugin.getInstance().getMessage("git.history.action.copy.sortIndex"));
		}
						
		override public function run():void {			
			var entry:HistoryEntryDto = selection[0];						
			Clipboard.generalClipboard.setData(ClipboardFormats.TEXT_FORMAT, entry.id);				
		}
	}
	
}