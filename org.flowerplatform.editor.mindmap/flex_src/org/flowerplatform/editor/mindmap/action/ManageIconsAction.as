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
package org.flowerplatform.editor.mindmap.action {
	
	import org.flowerplatform.editor.mindmap.MindMapModelPlugin;
	import org.flowerplatform.editor.mindmap.ui.ManageIconsView;
	import org.flowerplatform.emf_model.notation.MindMapNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class ManageIconsAction extends ActionBase {

		public function ManageIconsAction() {
			label = MindMapModelPlugin.getInstance().getMessage("manageIcons.action.label");
			icon = MindMapModelPlugin.getInstance().getResourceUrl("images/images.png");		
			preferShowOnActionBar = true;
		}
		
		override public function get visible():Boolean {			
			if (selection != null) {
				return true;	
			}			
			return false;
		}
		
		override public function run():void {
			var view:ManageIconsView = new ManageIconsView();
			view.selection = selection;
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setWidth(290)
				.setHeight(320)
				.setPopupContent(view)
				.show();
		}
		
	}
}