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
package org.flowerplatform.web.git.layout {
	
	import com.crispico.flower.util.layout.PopupHostViewWrapper;
	import com.crispico.flower.util.spinner.ModalSpinner;
	import com.crispico.flower.util.spinner.ModalSpinnerSupport;
	
	import mx.collections.IList;
	import mx.containers.VBox;
	import mx.core.UIComponent;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.layout.IWorkbench;
	import org.flowerplatform.web.git.GitPlugin;
	import org.flowerplatform.web.git.remote.dto.ViewInfoDto;

	/**
	 *	@author Cristina Constantinescu
	 */
	public class GitAbstractView extends VBox implements ModalSpinnerSupport {
		
		public function getInfo():ViewInfoDto {
			throw "Must implement get info()";
		}
		
		protected function getSelectedObjectFromExplorer():Object {	
			var workbench:IWorkbench = FlexUtilGlobals.getInstance().workbench;
			
			var explorer:UIComponent = workbench.getComponent("explorer");
		
			if (explorer is PopupHostViewWrapper) {
				var selection:IList = PopupHostViewWrapper(explorer).activePopupContent.getSelection();
				if (selection != null && selection.length == 1 && selection.getItemAt(0) is TreeNode) {
					return TreeNode(selection.getItemAt(0)).getPathForNode(true);
				}
			}
			return null;				
		}
		
		protected function getMessage(messageId:String, params:Array=null):String {
			return GitPlugin.getInstance().getMessage(messageId, params);
		}
				
		protected function getResourceUrl(resource:String):String {
			return GitPlugin.getInstance().getResourceUrl(resource);				
		}
		///////////////////////////////////////////////////////////////
		// Modal Spinner Support
		///////////////////////////////////////////////////////////////
		
		private var _modalSpinner:ModalSpinner;
		
		public function get modalSpinner():ModalSpinner	{
			return _modalSpinner;
		}
		
		public function set modalSpinner(value:ModalSpinner):void {
			_modalSpinner = value;
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			if (modalSpinner != null) {
				modalSpinner.setActualSize(unscaledWidth, unscaledHeight);
			}
		}
	}
}