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
package org.flowerplatform.editor.model.remote.scenario {
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.tree.remote.GenericTreeStatefulClient;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class ScenarioTreeStatefulClient extends GenericTreeStatefulClient {
		
		public var diagramStatefulClient:NotationDiagramEditorStatefulClient;
		
		public function ScenarioTreeStatefulClient() {
			super();
			
			statefulServiceId = "diagramEditorStatefulService";
		}
		
		override public function openNode(node:Object, resultCallbackObject:Object=null, resultCallbackFunction:Function=null):void {
			var path:ArrayCollection;
			if (node is TreeNode && node != null) {
				path = TreeNode(node).getPathForNode();
			} else {
				path = ArrayCollection(node);
			}
			
			context["diagramEditableResourcePath"] = diagramStatefulClient.editableResourcePath;
			diagramStatefulClient.service_openScenarioNode(path, context);
		}
		
	}
}