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
package org.flowerplatform.editor.model.controller {
	
	import org.flowerplatform.editor.model.renderer.ConnectionRenderer;
	import org.flowerplatform.emf_model.notation.Edge;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.renderer.ConnectionRendererController;
	
	public class EdgeRendererController extends ConnectionRendererController {
		public function EdgeRendererController(diagramShell:DiagramShell) {
			super(diagramShell, ConnectionRenderer);
		}
		
		override public function getSourceModel(connectionModel:Object):Object {
			return Edge(connectionModel).source_RH.referencedObject;
		}
		
		override public function getTargetModel(connectionModel:Object):Object {
			return Edge(connectionModel).target_RH.referencedObject;
		}
		
		override public function hasMiddleLabel(connectionModel:Object):Boolean {
			return true;
		}
		
	}
}