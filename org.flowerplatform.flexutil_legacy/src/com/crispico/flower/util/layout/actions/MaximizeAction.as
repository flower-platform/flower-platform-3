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
package  com.crispico.flower.util.layout.actions {
	import com.crispico.flower.flexdiagram.action.BaseAction;
	import com.crispico.flower.util.UtilAssets;
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.persistence.StackLayoutData;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	import mx.collections.ArrayCollection;

	/**
	 * @flowerModelElementId _WdiV0OCZEeGdYcOEhSk3ug
	 */
	public class MaximizeAction extends BaseAction  {
		
		/**
		 * @flowerModelElementId _thr_gOtxEeGb_JdgRgmL9A
		 */
		private var workbench:Workbench;
		
		/**
		 * @flowerModelElementId _thsmkOtxEeGb_JdgRgmL9A
		 */
		public function MaximizeAction(workbench:Workbench) {
			this.workbench = workbench;
			label = UtilAssets.INSTANCE.getMessage("layout.action.maximize");
			image = UtilAssets.INSTANCE._maximizeViewIcon;
			sortIndex = 40;
		}
		
		/**
		 * @flowerModelElementId _uuFFIOE0EeGM87_2dAj_1Q
		 */
		public override function isVisible(selectedEditParts:ArrayCollection):Boolean {	
			var viewLayoutData:ViewLayoutData = selectedEditParts[0];
			if (StackLayoutData(viewLayoutData.parent).mrmState == StackLayoutData.MAXIMIZED) {
				return false;
			}
			return true;
		}
		
		/**
		 * @flowerModelElementId _cE-fcOCZEeGdYcOEhSk3ug
		 */
		public override function run(selectedEditParts:ArrayCollection):void {	
			var viewLayoutData:ViewLayoutData = selectedEditParts[0];
			workbench.maximize(StackLayoutData(viewLayoutData.parent));			
		}
	}
}