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
package org.flowerplatform.editor.text.remote {
	
	import org.flowerplatform.editor.EditorFrontend;
	import org.flowerplatform.editor.text.CodeMirrorEditorFrontend;
	import org.flowerplatform.editor.text.codemirror_editor.ICodeMirrorEditor;

	/**
	 * @author Cristina Constantinescu
	 */ 
	public class CodeMirrorEditorStatefulClient extends TextEditorStatefulClient {
		
		public function CodeMirrorEditorStatefulClient(statefulServiceId:String) {
			super(statefulServiceId);
		}
		
		override protected function copyLocalDataFromExistingEditorToNewEditor(existingEditor:EditorFrontend, newEditor:EditorFrontend):void	{
			if (editableResourceStatus) {
				newEditor.editableResourceStatusUpdated();
			}
			CodeMirrorEditorFrontend(existingEditor).getContent(function(value:String):void {
				CodeMirrorEditorFrontend(newEditor).setContent(value);
			});
		}
		
		override protected function unsubscribeFromStatefulService_removeEditorFrontend(editorFrontend:EditorFrontend, editorFrontendIndex:int):void {			
			super.unsubscribeFromStatefulService_removeEditorFrontend(editorFrontend, editorFrontendIndex);
			ICodeMirrorEditor(editorFrontend.editor).dispose();
		}
	
	}
}
