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
package org.flowerplatform.codesync.regex {
	import com.crispico.flower.util.layout.Perspective;
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.persistence.SashLayoutData;
	import com.crispico.flower.util.layout.persistence.StackLayoutData;
	import com.crispico.flower.util.layout.persistence.WorkbenchLayoutData;
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.codesync.CodeSyncPlugin;
	import org.flowerplatform.codesync.regex.ide.RegexActionsViewProvider;
	import org.flowerplatform.codesync.regex.ide.RegexConfigsViewProvider;
	import org.flowerplatform.codesync.regex.ide.RegexMacrosViewProvider;
	import org.flowerplatform.codesync.regex.ide.RegexMatchesViewProvider;
	import org.flowerplatform.flexutil.layout.LayoutData;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import org.flowerplatform.properties.PropertiesViewProvider;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class RegexIdePerspective extends Perspective {
		
		public static const ID:String = "regexIDEPerspective";
		
		public override function get id():String {
			return ID;
		}
		
		public override function get name():String {
			return "Regex Modeling";
		}
		
		public override function get iconUrl():String {			
			return CodeSyncPlugin.getInstance().getResourceUrl("images/wand.png");
		}
		
		public override function resetPerspective(workbench:Workbench):void {
			var wld:WorkbenchLayoutData = new WorkbenchLayoutData();
			wld.direction = SashLayoutData.HORIZONTAL;
			wld.ratios = new ArrayCollection([70, 30]);
			wld.mrmRatios = new ArrayCollection([0, 0]);
										
			var sash:SashLayoutData = addSash(wld, SashLayoutData.VERTICAL,	[70, 30], [0, 0]);
			var sashEditor:SashLayoutData = addSash(sash, SashLayoutData.HORIZONTAL, [100], [0], true);
									
			var bottomSash:SashLayoutData = addSash(sash, SashLayoutData.HORIZONTAL, [40, 20, 40], [0, 0, 0]);
			addSingleViewInSash(PropertiesViewProvider.ID, bottomSash);
			addSingleViewInSash(RegexConfigsViewProvider.ID, bottomSash);
			addSingleViewInSash(RegexMacrosViewProvider.ID, bottomSash);
			
			var rightSash:SashLayoutData = addSash(wld, SashLayoutData.VERTICAL, [50, 50], [0, 0]);
			addSingleViewInSash(RegexActionsViewProvider.ID, rightSash);
			addSingleViewInSash(RegexMatchesViewProvider.ID, rightSash);
								
			load(workbench, wld, sashEditor);
		}
		
		private function addSash(parent:SashLayoutData, direction:Number, ratios:Array, mrmRatios:Array, isEditor:Boolean = false):SashLayoutData {
			var sash:SashLayoutData = new SashLayoutData();
			sash.isEditor = isEditor;
			sash.direction = direction;
			sash.ratios = new ArrayCollection(ratios);
			sash.mrmRatios = new ArrayCollection(mrmRatios);
			sash.parent = parent;
			parent.children.addItem(sash);
			
			if (isEditor) { // add stack editor
				var stack:StackLayoutData = new StackLayoutData();
				stack.parent = sash;
				sash.children.addItem(stack);
			}
			return sash;
		}
		
		private function addSingleViewInSash(id:String, parent:SashLayoutData):void {
			var stack:StackLayoutData = new StackLayoutData();
			stack.parent = parent;
			parent.children.addItem(stack);
			
			var view:ViewLayoutData = new ViewLayoutData();
			view.viewId = id;
			stack.children.addItem(view);
			view.parent = stack;
		}
		
	}
}