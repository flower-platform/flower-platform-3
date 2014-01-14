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
package org.flowerplatform.web.layout {
	import com.crispico.flower.util.layout.Perspective;
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.persistence.SashLayoutData;
	import com.crispico.flower.util.layout.persistence.StackLayoutData;
	import com.crispico.flower.util.layout.persistence.WorkbenchLayoutData;
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.editor.open_resources_view.OpenResourcesViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import org.flowerplatform.properties.PropertiesViewProvider;
	import org.flowerplatform.web.WebPlugin;
	import org.flowerplatform.web.common.explorer.ExplorerViewProvider;

	/**
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */
	public class DefaultPerspective extends Perspective {
		
		public static const ID:String = "defaultPerspective";
		
		public override function get id():String {
			return ID;
		}
		
		public override function get name():String {
			return WebPlugin.getInstance().getMessage("default.perspective");
		}
		
		public override function get iconUrl():String {			
			return WebPlugin.getInstance().getResourceUrl("images/icon_flower.gif");
		}
		
		public override function resetPerspective(workbench:Workbench):void {
			var wld:WorkbenchLayoutData = new WorkbenchLayoutData();
			wld.direction = SashLayoutData.HORIZONTAL;
			wld.ratios = new ArrayCollection([25, 75]);
			wld.mrmRatios = new ArrayCollection([0, 0]);
			
			addViewsInSash([ExplorerViewProvider.ID], wld);

			var sash:SashLayoutData = addSash(wld, SashLayoutData.VERTICAL, [70, 30], [0, 0]);			
			var sashEditor:SashLayoutData = addSash(sash, SashLayoutData.HORIZONTAL, [100], [0], true);	
			
			addViewsInSash([OpenResourcesViewProvider.ID], sash);

			load(workbench, wld, sashEditor);
		}

	}
}
