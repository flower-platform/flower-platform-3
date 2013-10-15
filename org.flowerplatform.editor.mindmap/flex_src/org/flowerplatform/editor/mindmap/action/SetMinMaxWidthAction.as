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
	import org.flowerplatform.editor.mindmap.ui.SetNodeWidthLimitsView;
	import org.flowerplatform.emf_model.notation.MindMapNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class SetMinMaxWidthAction extends ActionBase {
		
		public function SetMinMaxWidthAction() {
			super();
			label = MindMapModelPlugin.getInstance().getMessage('setWidthLimits.action.label');
			preferShowOnActionBar = true;
		}		
		
		override public function get visible():Boolean {			
			if (selection != null) {
				return true;	
			}			
			return false;
		}
		
		override public function run():void {
			var view:SetNodeWidthLimitsView = new SetNodeWidthLimitsView();
			view.selection = selection;
			view.currentMinWidth = MindMapNode(selection.getItemAt(0)).viewDetails.minWidth;
			view.currentMaxWidth = MindMapNode(selection.getItemAt(0)).viewDetails.maxWidth;
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setWidth(300)
				.setHeight(150)
				.setViewContent(view)
				.show();
		}
		
	}
}