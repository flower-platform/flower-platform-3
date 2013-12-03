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
	import org.flowerplatform.editor.model.EditorModelPlugin;
	
	public class AddRelatedElementsAction extends DisplayMissingRelationsAction {

		public function AddRelatedElementsAction() {
			super();
			
			addMissingElementsToDiagram = true;
			label = EditorModelPlugin.getInstance().getMessage("action.addRelatedElements");
			icon = EditorModelPlugin.getInstance().getResourceUrl("images/action/addMissEltsDisplayRelationsIcon.gif");
			orderIndex = 801;
		}
		
	}
}