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
	
	import org.flowerplatform.editor.model.NotationDiagramShell;
	import org.flowerplatform.editor.model.content_assist.NotationDiagramContentAssistProvider;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.Diagram;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.IDiagramShellAware;
	import org.flowerplatform.flexutil.FlexUtilAssets;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.content_assist.SearchView;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class SearchAction extends ActionBase implements IDiagramShellAware {
		
		public function SearchAction() {
			super();
			
			label = FlexUtilAssets.INSTANCE.getMessage("search");
			preferShowOnActionBar = true;
		}
		
		private var _diagramShell:DiagramShell;
		
		public function get diagramShell():DiagramShell {		
			return _diagramShell;
		}
		
		public function set diagramShell(value:DiagramShell):void {
			_diagramShell = value;
		}
		
		override public function get visible():Boolean {
			return selection.length == 1 && selection.getItemAt(0) is Diagram;
		}
		
		override public function run():void {
			var view:SearchView = new SearchView();
			view.contentAssistProvider = new NotationDiagramContentAssistProvider();
			var client:NotationDiagramEditorStatefulClient = NotationDiagramEditorStatefulClient(
				NotationDiagramShell(diagramShell).editorStatefulClient);
			view.setResultHandler(new DragOnDiagramHandler(client));
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setTitle(label)
				.setWidth(400)
				.setHeight(600)
				.setViewContent(view)
				.show();
		}
	}
}

import mx.collections.ArrayList;

import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
import org.flowerplatform.flexutil.dialog.IDialogResultHandler;

class DragOnDiagramHandler implements IDialogResultHandler {
	
	private var client:NotationDiagramEditorStatefulClient;
	
	public function DragOnDiagramHandler(client:NotationDiagramEditorStatefulClient) {
		this.client = client;
	}
	
	public function handleDialogResult(result:Object):void {
		client.service_handleDragOnDiagram(new ArrayList([result]));
	}
}