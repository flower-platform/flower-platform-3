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
package org.flowerplatform.editor.model
{
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	
	import org.flowerplatform.communication.tree.GenericTreeList;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.scenario.ScenarioTreeStatefulClient;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.IDiagramShellAware;
	import org.flowerplatform.flexdiagram.tool.DragToCreateRelationTool;
	import org.flowerplatform.flexdiagram.tool.DragTool;
	import org.flowerplatform.flexdiagram.tool.InplaceEditorTool;
	import org.flowerplatform.flexdiagram.tool.ResizeTool;
	import org.flowerplatform.flexdiagram.tool.ScrollTool;
	import org.flowerplatform.flexdiagram.tool.SelectOnClickTool;
	import org.flowerplatform.flexdiagram.tool.ZoomTool;
	import org.flowerplatform.flexutil.action.IAction;

	/**
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */
	public class NotationDiagramEditorFrontend extends DiagramEditorFrontend {
				
		protected var scenarioTree:GenericTreeList;
		
		protected var activeView:Object;
		
		override protected function createChildren():void {			
			super.createChildren();
			
			scenarioTree = new GenericTreeList();
			scenarioTree.percentHeight = 100;
			scenarioTree.right = 0;
			var treeClient:ScenarioTreeStatefulClient = new ScenarioTreeStatefulClient();
			treeClient.diagramStatefulClient = NotationDiagramEditorStatefulClient(editorStatefulClient);
			treeClient.diagramStatefulClient.scenarioTreeStatefulClient = treeClient;
			scenarioTree.statefulClient = treeClient;
			treeClient.treeList = scenarioTree;
			editor.addChild(scenarioTree);
			
			var root:TreeNode = new TreeNode();
			root.children = new ArrayCollection();
			root.hasChildren = true;
			scenarioTree.rootNode = root;
			scenarioTree.statefulClient.openNode(root);
			scenarioTree.addEventListener(Event.CHANGE, selectionChangedHandler);
		}
		
		override protected function getDiagramShellInstance():DiagramShell {
			var diagramShell:NotationDiagramShell = new NotationDiagramShell();
			diagramShell.editorStatefulClient = DiagramEditorStatefulClient(editorStatefulClient);
			
			diagramShell.registerTools([
				ScrollTool, SelectOnClickTool, InplaceEditorTool, ResizeTool, 
				DragToCreateRelationTool, DragTool/*, SelectOrDragToCreateElementTool*/, ZoomTool]);
			return diagramShell;
		}
		
		/**
		 * @author Mariana Gheorghe
		 */
		override protected function selectionChangedHandler(e:Event):void {
			if (e.target == scenarioTree) {
				activeView = scenarioTree;
			} else {
				activeView = diagramShell;
				scenarioTree.selectedItems = null;
			}
			super.selectionChangedHandler(e);
		}
		
		/**
		 * @author Mariana Gheorghe
		 */
		override public function getSelection():IList {		
			if (activeView is GenericTreeList) {
				return GenericTreeList(activeView).getSelection();
			}
			return super.getSelection();
		}
		
		/**
		 * @author Mariana Gheorghe
		 */
		override public function getActions(selection:IList):Vector.<IAction> {
			var result:Vector.<IAction> = new Vector.<IAction>();
			
			var actions:Vector.<IAction> = EditorModelPlugin.getInstance().notationDiagramClassFactoryActionProvider.getActions(selection);
			if (actions != null) {
				for each (var action:IAction in actions) {
					if (action is IDiagramShellAware) {
						IDiagramShellAware(action).diagramShell = diagramShell;
					}
					result.push(action);
				}
			}		
			return result;
		}
		
	}
}