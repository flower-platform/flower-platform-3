package org.flowerplatform.editor.mindmap {
	
	import org.flowerplatform.editor.EditorDescriptor;
	import org.flowerplatform.editor.EditorFrontend;
	import org.flowerplatform.editor.mindmap.remote.MindMapDiagramEditorStatefulClient;
	import org.flowerplatform.editor.remote.EditorStatefulClient;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapDiagramEditorDescriptor extends EditorDescriptor {
		
		override public function getEditorName():String {
			return "mindmap";
		}
		
		override protected function createViewInstance():EditorFrontend	{
			return new MindMapDiagramEditorFrontend();
		}
		
		override protected function createEditorStatefulClient():EditorStatefulClient {
			return new MindMapDiagramEditorStatefulClient();
		}
		
		public override function getId():String {	
			return "org.flowerplatform.editor.mindmap";
		}
		
		public override function getIcon(viewLayoutData:ViewLayoutData=null):Object {				
			return null;
		}
		
		public override function getTitle(viewLayoutData:ViewLayoutData=null):String {		
			return "mindmap ...";
		}
	}
}