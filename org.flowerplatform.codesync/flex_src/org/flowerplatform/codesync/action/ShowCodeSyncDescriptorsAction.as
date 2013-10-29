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
package org.flowerplatform.codesync.action {
	import mx.collections.IList;
	
	import org.flowerplatform.codesync.CodeSyncPlugin;
	import org.flowerplatform.codesync.views.loaded_descriptors.LoadedDescriptorsViewProvider;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.action.IAction;
	
	/**
	 * Opens the "Loaded Descriptors" view when run.
	 * 
	 * @author Mircea Negreanu
	 */
	public class ShowCodeSyncDescriptorsAction extends ActionBase implements IAction {
		public function ShowCodeSyncDescriptorsAction() {
			super();
			
			preferShowOnActionBar = true;
			label = CodeSyncPlugin.getInstance().getMessage("loadDesc.action.title");
			icon = CodeSyncPlugin.getInstance().getResourceUrl("images/Folder.gif");
		}

		override public function run():void {
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewIdInWorkbench(LoadedDescriptorsViewProvider.ID)
				.setWidth(500)
				.setHeight(300)
				.show();
		}
	}
}