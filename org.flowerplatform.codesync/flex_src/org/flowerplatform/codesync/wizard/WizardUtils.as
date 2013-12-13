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
package org.flowerplatform.codesync.wizard {
	import flash.events.Event;
	import flash.events.EventDispatcher;
	
	import mx.core.FlexGlobals;
	
	import org.flowerplatform.communication.tree.remote.PathFragment;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class WizardUtils extends EventDispatcher {
		
		public var selectedEditorStatefulClient:NotationDiagramEditorStatefulClient;	
	
		private var _selectedMDAElementInfo:PathFragment;
		
		[Bindable]
		public function get selectedMDAElementInfo():PathFragment {
			return _selectedMDAElementInfo;
		}

		public function set selectedMDAElementInfo(value:PathFragment):void {
			_selectedMDAElementInfo = value;
			
			FlexGlobals.topLevelApplication.dispatchEvent(new WizardEvent(WizardEvent.SELECTED_WIZARD_ELEMENT_CHANGED));
		}
		
	}
}