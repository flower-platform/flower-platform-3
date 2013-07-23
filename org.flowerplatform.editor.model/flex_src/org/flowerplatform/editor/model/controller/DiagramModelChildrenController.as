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
	import mx.collections.IList;
	
	import org.flowerplatform.communication.transferable_object.ReferenceHolderList;
	import org.flowerplatform.emf_model.notation.Diagram;
	import org.flowerplatform.emf_model.notation.View;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexutil.ComposedList;
	
	public class DiagramModelChildrenController extends ViewModelChildrenController {
		public function DiagramModelChildrenController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		override public function getChildren(model:Object):IList {
			return new ComposedList([
				new ReferenceHolderList(View(model).persistentChildren_RH),
				new ReferenceHolderList(Diagram(model).persistentEdges_RH)
			]);
		}
		
		
	}
}