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
package org.flowerplatform.codesync.code.javascript.model.renderer {
	
	import flash.events.MouseEvent;
	
	import mx.core.IDataRenderer;
	
	import org.flowerplatform.editor.model.EditorModelPlugin;
	import org.flowerplatform.editor.model.NotationDiagramShell;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.renderer.BoxChildIconItemRenderer;
	import org.flowerplatform.editor.model.renderer.BoxRenderer;
	import org.flowerplatform.emf_model.notation.ExpandableNode;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.visual_children.IVisualChildrenController;
	import org.flowerplatform.flexdiagram.renderer.IDiagramShellAware;
	import org.flowerplatform.flexdiagram.renderer.IVisualChildrenRefreshable;
	
	import spark.components.Button;
	import spark.layouts.VerticalLayout;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class ExpandableBoxRenderer extends BoxRenderer {
		
		protected var title:BoxChildIconItemRenderer;
		
		protected var expandButton:Button;
		
		public function ExpandableBoxRenderer():void {
			super();
			
			VerticalLayout(layout).horizontalAlign = "left";
		}
		
		override public function set data(value:Object):void {
			super.data = value;
			title.data = value;
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			
			// no border
			graphics.clear();
			
			var node:ExpandableNode = ExpandableNode(data);
			if (node.hasChildren) {
				if (expandButton == null) {
					expandButton = new Button();
					expandButton.styleName = "borderlessButton";
					expandButton.width = 16;
					expandButton.height = 16;
					expandButton.addEventListener(MouseEvent.CLICK, expandCollapseContainer);
					title.addChildAt(expandButton, 0);
				}
			} else {
				if (expandButton != null) {
					title.removeChild(expandButton);
					expandButton = null;
				}
			}
			if (expandButton != null) {
				expandButton.setStyle("icon", node.expanded 
					? EditorModelPlugin.getInstance().getResourceUrl("images/obj16/collapseall.gif")
					: EditorModelPlugin.getInstance().getResourceUrl("images/obj16/expandall.gif"));
			}
		}
		
		
		override protected function createChildren():void {
			super.createChildren();
			
			title = new BoxChildIconItemRenderer();
			addElementAt(title, 0);
			
			// only for the children of the file
			if (parent is ExpandableBoxRenderer) {
				VerticalLayout(layout).paddingLeft = 20;
			}
		}
		
		protected function expandCollapseContainer(evt:MouseEvent):void {
			var node:ExpandableNode = ExpandableNode(data);
			NotationDiagramEditorStatefulClient(NotationDiagramShell(diagramShell).editorStatefulClient)
					.service_expandCollapseCompartment(node.id, !node.expanded);
		}
	}
}