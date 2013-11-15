package org.flowerplatform.web.layout {
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.persistence.SashLayoutData;
	import com.crispico.flower.util.layout.persistence.StackLayoutData;
	import com.crispico.flower.util.layout.persistence.WorkbenchLayoutData;
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.codesync.regex.ide.RegexActionsViewProvider;
	import org.flowerplatform.codesync.regex.ide.RegexMatchesViewProvider;
	import org.flowerplatform.editor.open_resources_view.OpenResourcesViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import org.flowerplatform.properties.PropertiesViewProvider;
	import org.flowerplatform.web.WebPlugin;
	import org.flowerplatform.web.common.explorer.ExplorerViewProvider;
	
	public class RegexIdePerspective extends Perspective {
		
		public static const ID:String = "regexIDEPerspective";
		
		public override function get id():String {
			return ID;
		}
		
		public override function get name():String {
			return "Regex Modeling";
		}
		
		public override function get iconUrl():String {			
			return WebPlugin.getInstance().getResourceUrl("images/wand.png");
		}
		
		public override function resetPerspective(workbench:Workbench):void {
			var wld:WorkbenchLayoutData = new WorkbenchLayoutData();
			wld.direction = SashLayoutData.HORIZONTAL;
			wld.ratios = new ArrayCollection([70, 30]);
			wld.mrmRatios = new ArrayCollection([0, 0]);
										
			var sash1:SashLayoutData = new SashLayoutData();			
			sash1.direction = SashLayoutData.VERTICAL;
			sash1.ratios = new ArrayCollection([70, 30]);
			sash1.mrmRatios = new ArrayCollection([0, 0]);
			sash1.parent = wld;
			wld.children.addItem(sash1);
			
			var sashEditor:SashLayoutData = new SashLayoutData();
			sashEditor.isEditor = true;
			sashEditor.direction = SashLayoutData.HORIZONTAL;
			sashEditor.ratios = new ArrayCollection([100]);
			sashEditor.mrmRatios = new ArrayCollection([0]);
			sashEditor.parent = sash1;
			sash1.children.addItem(sashEditor);
			
			var stackEditor:StackLayoutData = new StackLayoutData();
			stackEditor.parent = sashEditor;
			sashEditor.children.addItem(stackEditor);
			
			var stack:StackLayoutData = new StackLayoutData();
			stack.parent = sash1;
			sash1.children.addItem(stack);
			
			var view:ViewLayoutData = new ViewLayoutData();
			view = new ViewLayoutData();
			view.viewId = PropertiesViewProvider.ID;
			stack.children.addItem(view);
			view.parent = stack;
			
			var sash2:SashLayoutData = new SashLayoutData();	
			sash2 = new SashLayoutData();			
			sash2.direction = SashLayoutData.VERTICAL;
			sash2.ratios = new ArrayCollection([50, 50]);
			sash2.mrmRatios = new ArrayCollection([0, 0]);
			sash2.parent = wld;
			wld.children.addItem(sash2);
			
			var stackArea:StackLayoutData = new StackLayoutData();
			stackArea.parent = sash2;
			sash2.children.addItem(stackArea);
			
			view = new ViewLayoutData();
			view.viewId = RegexActionsViewProvider.ID;
			stackArea.children.addItem(view);
			view.parent = stackArea;
			
			stackArea = new StackLayoutData();
			stackArea.parent = sash2;
			sash2.children.addItem(stackArea);
			
			view = new ViewLayoutData();			
			view.viewId = RegexMatchesViewProvider.ID;
			stackArea.children.addItem(view);
			view.parent = stackArea;
					
			load(workbench, wld, sashEditor);
		}
	}
}