package org.flowerplatform.editor.text {
	
	import org.flowerplatform.editor.EditorDescriptor;
	import org.flowerplatform.editor.EditorFrontend;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.remote.EditorStatefulClient;
	import org.flowerplatform.editor.text.remote.CodeMirrorEditorStatefulClient;
	import org.flowerplatform.editor.text.remote.TextEditorStatefulClient;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;

	/**
	 * @author Cristina Constatinescu
	 */
	public class CodeMirrorEditorDescriptor extends EditorDescriptor {
		
		override public function getEditorName():String {
			return "codeMirrorEditor";
		}
		
		override protected function createViewInstance():EditorFrontend	{
			return new CodeMirrorEditorFrontend();
		}
		
		override protected function createEditorStatefulClient():EditorStatefulClient {
			return new CodeMirrorEditorStatefulClient("codeMirrorEditorStatefulService");
		}
		
		public override function getId():String {	
			return "org.flowerplatform.editor.codeMirror";
		}
		
		public override function getIcon(viewLayoutData:ViewLayoutData = null):Object {	
			return EditorTextPlugin.getInstance().getResourceUrl("images/file.gif");			
		}
		
		public override function getTitle(viewLayoutData:ViewLayoutData = null):String {	
			if (viewLayoutData == null) {
				return EditorTextPlugin.getInstance().getMessage("editor.codeMirror.name");
			} else {
				return viewLayoutData.customData.slice(viewLayoutData.customData.lastIndexOf("/") + 1);
			}
			return null;
		}
	}
}