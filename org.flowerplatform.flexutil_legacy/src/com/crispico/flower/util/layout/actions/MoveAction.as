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
	import com.crispico.flower.util.layout.ArrangeTool;
	import com.crispico.flower.util.layout.Workbench;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import flexlib.containers.SuperTabNavigator;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Button;
	import mx.core.UIComponent;
	
	/**
	 * @flowerModelElementId _OvQQ8OCYEeGdYcOEhSk3ug
	 */
	public class MoveAction extends BaseAction  {
		
		/**
		 * @flowerModelElementId _gfkQ4OCYEeGdYcOEhSk3ug
		 */
		public static const VIEW:int=0;
		
		/**
		 * @flowerModelElementId _l5C3AOCYEeGdYcOEhSk3ug
		 */
		public static const GROUP:int=1;
		
		/**
		 * @flowerModelElementId _e_ZeoOCYEeGdYcOEhSk3ug
		 */
		private var type:int;
		
		/**
		 * @flowerModelElementId _thzUQOtxEeGb_JdgRgmL9A
		 */
		private var workbench:Workbench;
		
		/**
		 * @flowerModelElementId _thz7UOtxEeGb_JdgRgmL9A
		 */
		public function MoveAction(workbench:Workbench, type:int) {
			this.workbench = workbench;
			this.type = type;
			switch(type) {
				case VIEW: {
					label = UtilAssets.INSTANCE.getMessage("layout.action.move.view");
					image = UtilAssets.INSTANCE._viewIcon;
					sortIndex = 10;
					break;
				}					
				case GROUP: {
					label = UtilAssets.INSTANCE.getMessage("layout.action.move.group");
					image = UtilAssets.INSTANCE._viewsIcon;
					sortIndex = 20;
					break;
				}	
			}
		}
		
		/**
		 * @flowerModelElementId _dlRC8OCYEeGdYcOEhSk3ug
		 */
		public override function run(selectedEditParts:ArrayCollection):void {	
			var viewLayoutData:ViewLayoutData = selectedEditParts[0];
			
			var tabNavigator:SuperTabNavigator = SuperTabNavigator(workbench.layoutDataToComponent[viewLayoutData.parent]);			
			var point:Point;
			var tab:Button;
			if (type == VIEW) {
				tab = Button(tabNavigator.getTabAt(viewLayoutData.parent.children.getItemIndex(viewLayoutData)));
				point = tab.localToGlobal(new Point(tab.stage.x + tab.width/2, tab.stage.y));
			} else {
				tab = Button(tabNavigator.getTabAt(viewLayoutData.parent.children.length - 1));
				point = tab.localToGlobal(new Point(tab.stage.x + tab.width, tab.stage.y));
				point.x += 1;
			}
			workbench.arrangeTool.startDragging(point, ArrangeTool.MOVE_BY_CLICKING);
		}
	}
}