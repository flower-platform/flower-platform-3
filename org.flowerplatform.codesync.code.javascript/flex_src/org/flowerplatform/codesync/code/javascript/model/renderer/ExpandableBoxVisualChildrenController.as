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
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.visual_children.SequentialLayoutVisualChildrenController;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class ExpandableBoxVisualChildrenController extends SequentialLayoutVisualChildrenController {

		public function ExpandableBoxVisualChildrenController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		override protected function getVisualElementsToSkip(model:Object):int {
			// skip the title renderer
			return 1;
		}
		
	}
}