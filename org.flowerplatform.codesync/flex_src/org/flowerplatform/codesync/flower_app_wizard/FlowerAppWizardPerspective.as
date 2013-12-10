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
package org.flowerplatform.codesync.flower_app_wizard {
	
	import com.crispico.flower.util.layout.Perspective;
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.persistence.SashLayoutData;
	import com.crispico.flower.util.layout.persistence.StackLayoutData;
	import com.crispico.flower.util.layout.persistence.WorkbenchLayoutData;
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.codesync.CodeSyncPlugin;
	import org.flowerplatform.codesync.regex.MacrosRegexViewProvider;
	import org.flowerplatform.codesync.regex.RegexConfigsViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import org.flowerplatform.properties.PropertiesViewProvider;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class FlowerAppWizardPerspective extends Perspective {
		
		public static const ID:String = "flowerAppWizardPerspective";
		
		public override function get id():String {
			return ID;
		}
		
		public override function get name():String {
			return CodeSyncPlugin.getInstance().getMessage("flowerAppWiz.perspective");
		}
		
		public override function get iconUrl():String {			
			return CodeSyncPlugin.getInstance().getResourceUrl("images/wizard/wand-hat.png");
		}
		
		public override function resetPerspective(workbench:Workbench):void {
			var wld:WorkbenchLayoutData = new WorkbenchLayoutData();
			wld.direction = SashLayoutData.VERTICAL;
			wld.ratios = new ArrayCollection([70, 30]);
			wld.mrmRatios = new ArrayCollection([0, 0]);
			
			var sashEditor:SashLayoutData = addSash(wld, SashLayoutData.HORIZONTAL, [100], [0], true);
			
			var bottomSash:SashLayoutData = addSash(wld, SashLayoutData.HORIZONTAL, [20, 40, 40], [0, 0, 0]);					
			addViewsInSash([RegexConfigsViewProvider.ID], bottomSash);
			addViewsInSash([MacrosRegexViewProvider.ID], bottomSash);
			addViewsInSash([PropertiesViewProvider.ID], bottomSash);
			
			load(workbench, wld, sashEditor);
		}		
	}
}