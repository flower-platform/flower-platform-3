package org.flowerplatform.editor.remote {
	import org.flowerplatform.communication.command.AbstractClientCommand;
	import org.flowerplatform.editor.BasicEditorDescriptor;
	import org.flowerplatform.editor.EditorPlugin;
	
	
	[RemoteClass]
	public class CreateEditorStatefulClientCommand extends AbstractClientCommand { 
		
		public var editableResourcePath:String;
		
		public var editor:String;
		
		public var handleAsClientSubscription:Boolean;
		
		override public function execute():void	{
			var editorDescriptor:BasicEditorDescriptor = EditorPlugin.getInstance().getEditorDescriptorByName(editor);
			if (editorDescriptor == null) {
				throw "Cannot find EditorDescriptor for editor = " + editor;
			}
			
			editorDescriptor.openEditor(editableResourcePath, false, true, handleAsClientSubscription);
		}
		
	}
}