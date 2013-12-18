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
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.WorkbenchViewHost;
	
	import flash.events.Event;
	import flash.events.EventDispatcher;
	
	import mx.collections.ArrayCollection;
	import mx.core.FlexGlobals;
	import mx.core.UIComponent;
	
	import org.flowerplatform.codesync.CodeSyncPlugin;
	import org.flowerplatform.common.CommonPlugin;
	import org.flowerplatform.communication.tree.remote.PathFragment;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.model.DiagramEditorFrontend;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class WizardUtils extends EventDispatcher {
		
		private var _selectedWizardElement:TreeNode;
		
		private var _selectedWizardElementInfo:PathFragment;
		
		public function getSelectedEditorStatefulClient():NotationDiagramEditorStatefulClient {			
			var editorFrontend:Object = null;
			var editors:ArrayCollection = Workbench(FlexUtilGlobals.getInstance().workbench).getAllVisibleViewLayoutData(true);			
			for each (var editor:ViewLayoutData in editors) {
				editorFrontend = Workbench(FlexUtilGlobals.getInstance().workbench).layoutDataToComponent[editor];
				if (editorFrontend is WorkbenchViewHost) {
					editorFrontend = UIComponent(WorkbenchViewHost(editorFrontend).activeViewContent);
				}
				if (editorFrontend is DiagramEditorFrontend) {
					break;
				}
			}		
			
			if (editorFrontend != null) {
				return NotationDiagramEditorStatefulClient(DiagramEditorFrontend(editorFrontend).editorStatefulClient);
			}
			return null;
		}
		
		public function get selectedWizardElement():TreeNode {
			return _selectedWizardElement;
		}

		public function set selectedWizardElement(value:TreeNode):void {
			_selectedWizardElement = value;
						
			selectedWizardElementInfo = _selectedWizardElement != null ? _selectedWizardElement.pathFragment : null;			
		}

		[Bindable]
		public function get selectedWizardElementInfo():PathFragment {
			return _selectedWizardElementInfo;
		}

		public function set selectedWizardElementInfo(value:PathFragment):void {
			_selectedWizardElementInfo = value;
			
			FlexGlobals.topLevelApplication.dispatchEvent(new WizardEvent(WizardEvent.SELECTED_WIZARD_ELEMENT_CHANGED));
		}
		
		public function isSelectedEditorStatefulClientValid(statefulClient:NotationDiagramEditorStatefulClient):Boolean {
			if (statefulClient == null) {
				FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
					.setText(CodeSyncPlugin.getInstance().getMessage('wizard.noDiagramEditorAvailable'))
					.setTitle(CommonPlugin.getInstance().getMessage('info'))
					.setWidth(300)
					.setHeight(150)
					.showMessageBox();
				return false;
			}
			return true;
		}
		
	}
}