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
package org.flowerplatform.editor.model.action {
	
	import org.flowerplatform.editor.model.content_assist.NotationDiagramContentAssistProvider;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.search.SearchView;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class SearchAction extends ActionBase {
		
		public function SearchAction() {
			super();
			
			label = "Search";
			preferShowOnActionBar = true;
		}
		
		override public function get visible():Boolean {
			return selection.length == 1;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var view:SearchView = new SearchView();
			view.contentAssistProvider = new NotationDiagramContentAssistProvider(node.id);
			view.setResultHandler(new TestDialogResultHandler());
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setTitle("Search")
				.setWidth(400)
				.setHeight(600)
				.setViewContent(view)
				.show();
		}
	}
}
import mx.controls.Alert;

import org.flowerplatform.flexutil.dialog.IDialogResultHandler;

class TestDialogResultHandler implements IDialogResultHandler {
	
	public function handleDialogResult(result:Object):void {
		Alert.show("Selected item - " + result); 
	}
}