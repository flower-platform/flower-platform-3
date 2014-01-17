package org.flowerplatform.mindmap.layout {
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.layout.IViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import org.flowerplatform.mindmap.MindMapEditorFrontend;
	
	public class MindMapEditorProvider implements IViewProvider {
				
		public static const ID:String = "mindMapEditor";
		
		public function getId():String {
			return ID;
		}
		
		public function createView(viewLayoutData:ViewLayoutData):UIComponent {
			return new MindMapEditorFrontend();
		}
		
		public function getTitle(viewLayoutData:ViewLayoutData=null):String {
			return "mindmap";
		}
		
		public function getIcon(viewLayoutData:ViewLayoutData=null):Object {
			return null;
		}
		
		public function getTabCustomizer(viewLayoutData:ViewLayoutData):Object {
			return null;
		}
		
		public function getViewPopupWindow(viewLayoutData:ViewLayoutData):UIComponent {
			return null;
		}
	}
}