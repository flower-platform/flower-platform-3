package com.crispico.flower.mp.codesync.base.editor {
	import org.flowerplatform.editor.EditorDescriptor;
	import org.flowerplatform.editor.EditorFrontend;
	import org.flowerplatform.editor.remote.EditorStatefulClient;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 * @author Mariana
	 */
	public class CodeSyncEditorDescriptor extends EditorDescriptor {
		
		override public function getEditorName():String {
			return "codeSync";
		}
		
		override protected function createViewInstance():EditorFrontend	{
			return new CodeSyncEditorFrontend();
		}
		
		override protected function createEditorStatefulClient():EditorStatefulClient {
			return new CodeSyncEditorStatefulClient();
		}
		
		public override function getId():String {	
			return "com.crispico.flower.mp.web.codesync";
		}
		
		public override function getTitle(viewLayoutData:ViewLayoutData=null):String {
			return "CodeSync";
		}
		
	}
}