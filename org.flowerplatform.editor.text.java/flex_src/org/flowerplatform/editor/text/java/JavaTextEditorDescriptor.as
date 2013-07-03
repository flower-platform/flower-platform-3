package org.flowerplatform.editor.text.java {
	
	import org.flowerplatform.editor.EditorFrontend;
	import org.flowerplatform.editor.remote.EditorStatefulClient;
	import org.flowerplatform.editor.text.TextEditorDescriptor;
	import org.flowerplatform.editor.text.remote.TextEditorStatefulClient;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 * @flowerModelElementId _X3OhgMBsEeG5PP70DrXYIQ
	 */
	public class JavaTextEditorDescriptor extends TextEditorDescriptor {

		override public function getEditorName():String {
			return "java";
		}
		
		override protected function createViewInstance():EditorFrontend	{
			return new JavaTextEditorFrontend();
		}
		
		override protected function createEditorStatefulClient():EditorStatefulClient {
			return new TextEditorStatefulClient("textEditorStatefulService");
		}
		
		override public function getId():String {	
			return "org.flowerplatform.editor.text.java";
		}
		
		override public function getIcon(viewLayoutData:ViewLayoutData=null):Object {
			return null;
		}
		
		override public function getTitle(viewLayoutData:ViewLayoutData=null):String {
			if (viewLayoutData == null) {
				return EditorTextJavaPlugin.getInstance().getMessage("editor.text.java.name");
			} else {
				return super.getTitle(viewLayoutData);
			}
		}
	}
}