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
package com.crispico.flower.util.layout.actions
{
	import com.crispico.flower.flexdiagram.action.BaseAction;
	import com.crispico.flower.util.UtilAssets;
	import com.crispico.flower.util.layout.Workbench;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	import mx.collections.ArrayCollection;
	import mx.core.UIComponent;

	/**
	 * @author Cristina
	 */ 
	public class UndockAction extends BaseAction {
			
		private var workbench:Workbench;
		
		public function UndockAction(workbench:Workbench) {
			this.workbench = workbench;
			label = UtilAssets.INSTANCE.getMessage("layout.action.undock");
			image = UtilAssets.INSTANCE._dockIcon;
			sortIndex = 15;
		}
		
		public override function run(selectedEditParts:ArrayCollection):void {	
			var viewLayoutData:ViewLayoutData = selectedEditParts[0];
			
			// get graphic component
			var component:UIComponent = workbench.layoutDataToComponent[viewLayoutData];			
			// close the view from workbench
			workbench.closeView(component, false);
			
			workbench.addViewInPopupWindow(viewLayoutData, NaN, NaN, NaN, NaN, false, component);				
		}
		
	}	
}