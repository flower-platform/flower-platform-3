package org.flowerplatform.web.layout {
	
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.persistence.SashLayoutData;
	import com.crispico.flower.util.layout.persistence.StackLayoutData;
	import com.crispico.flower.util.layout.persistence.WorkbenchLayoutData;
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import org.flowerplatform.mindmap.layout.MindMapEditorProvider;

	public class MindMapPerspective extends Perspective	{
		
		public static const ID:String = "mindmapPerspective";
		
		public override function get id():String {
			return ID;
		}
		
		public override function get name():String {
			return "MindMap Perspective";
		}
		
		public override function get iconUrl():String {			
			return "icons/Web/icons/icon_flower.gif";
		}
		
		public override function resetPerspective(workbench:Workbench):void {
			var wld:WorkbenchLayoutData = new WorkbenchLayoutData();
			wld.direction = SashLayoutData.HORIZONTAL;
			wld.ratios = new ArrayCollection([100]);
			wld.mrmRatios = new ArrayCollection([0]);
						
			var sashEditor:SashLayoutData = new SashLayoutData();
			sashEditor.isEditor = true;
			sashEditor.direction = SashLayoutData.HORIZONTAL;
			sashEditor.ratios = new ArrayCollection([100]);
			sashEditor.mrmRatios = new ArrayCollection([0]);
			sashEditor.parent = wld;
			wld.children.addItem(sashEditor);
			
			var stackEditor:StackLayoutData = new StackLayoutData();
			stackEditor.parent = sashEditor;
			sashEditor.children.addItem(stackEditor);
			
//			var view:ViewLayoutData = new ViewLayoutData;
//			view.isEditor = true;
//			view.viewId = MindMapEditorProvider.ID;
//			view.parent = stackEditor;
//			stackEditor.children.addItem(view);
			
			load(workbench, wld, sashEditor);
		}
	}
}