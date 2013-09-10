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
package org.flowerplatform.editor.mindmap {
	
	import mx.collections.IList;
	
	import org.flowerplatform.editor.mindmap.remote.MindMapDiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.DiagramEditorFrontend;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.IDiagramShellAware;
	import org.flowerplatform.flexutil.popup.IAction;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapDiagramEditorFrontend extends DiagramEditorFrontend {
				
		override protected function getDiagramShellInstance():DiagramShell {
			var diagram:NotationMindMapDiagramShell = new NotationMindMapDiagramShell();
			diagram.editorStatefulClient = MindMapDiagramEditorStatefulClient(editorStatefulClient);
			return diagram;			
		}
		
		override public function getActions(selection:IList):Vector.<IAction> {
			var result:Vector.<IAction> = new Vector.<IAction>();
			
			var actions:Vector.<IAction> = MindMapModelPlugin.getInstance().mindmapDiagramClassFactoryActionProvider.getActions(selection);
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