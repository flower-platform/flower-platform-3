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
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.persistence.SashLayoutData;
	import com.crispico.flower.util.layout.persistence.StackLayoutData;
	import com.crispico.flower.util.layout.persistence.WorkbenchLayoutData;
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import org.flowerplatform.web.common.explorer.ExplorerViewProvider;
	import org.flowerplatform.editor.open_resources_view.OpenResourcesViewProvider;
	/**
	 * Flower Modeling Perspective.
	 * Contains : 
	 * <ul>
	 * 	<li> <code>DocumentationViewProvider</code>
	 * 	<li> <code>ModelPropertiesViewProvider</code>
	 *  <li> <code>NavigatorViewProvider</code>
	 * </ul>
	 * 
	 * @author Cristi
	 * @author Cristina
	 */
	public class DefaultPerspective extends Perspective {
		
		public static const ID:String = "defaultPerspective";
		
		public override function get id():String {
			return ID;
		}
		
		public override function get name():String {
			return "Default Perspective";
		}
		
		public override function get iconUrl():String {			
			return "icons/Web/icons/icon_flower.gif";
		}
		
		public override function resetPerspective(workbench:Workbench):void {
			var wld:WorkbenchLayoutData = new WorkbenchLayoutData();
			wld.direction = SashLayoutData.HORIZONTAL;
			wld.ratios = new ArrayCollection([25, 75]);
			wld.mrmRatios = new ArrayCollection([0, 0]);
			
			var navigatorsStackArea:StackLayoutData = new StackLayoutData();
			navigatorsStackArea.parent = wld;
			wld.children.addItem(navigatorsStackArea);
			
			var projectExplorerView:ViewLayoutData = new ViewLayoutData();
			projectExplorerView.viewId = ExplorerViewProvider.ID;
			navigatorsStackArea.children.addItem(projectExplorerView);
			projectExplorerView.parent = navigatorsStackArea;
			
			var view:ViewLayoutData = new ViewLayoutData();
						
			var sash:SashLayoutData = new SashLayoutData();
			sash.direction = SashLayoutData.VERTICAL;
			sash.ratios = new ArrayCollection([70, 30]);
			sash.mrmRatios = new ArrayCollection([0, 0]);
			sash.parent = wld;
			wld.children.addItem(sash);
			
			var sashEditor:SashLayoutData = new SashLayoutData();
			sashEditor.isEditor = true;
			sashEditor.direction = SashLayoutData.HORIZONTAL;
			sashEditor.ratios = new ArrayCollection([100]);
			sashEditor.mrmRatios = new ArrayCollection([0]);
			sashEditor.parent = sash;
			sash.children.addItem(sashEditor);
			
			var stackEditor:StackLayoutData = new StackLayoutData();
			stackEditor.parent = sashEditor;
			sashEditor.children.addItem(stackEditor);
			
			var stack:StackLayoutData = new StackLayoutData();
			stack.parent = sash;
			sash.children.addItem(stack);
			
			view = new ViewLayoutData();			
			view.viewId = OpenResourcesViewProvider.ID;
			stack.children.addItem(view);
			view.parent = stack;
			
//			view = new ViewLayoutData();
//			view.viewId = WebDocumentationViewProvider.ID;
//			stack.children.addItem(view);
//			view.parent = stack;
//			
//			view = new ViewLayoutData();
//			view.viewId = WebPropertiesEditorViewProvider.ID;
//			stack.children.addItem(view);
//			view.parent = stack;
			
			load(workbench, wld, sashEditor);
		}

	}
}