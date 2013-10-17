package org.flowerplatform.editor.text.remote {
	import org.flowerplatform.editor.EditorFrontend;
	import org.flowerplatform.editor.remote.EditorStatefulClient;

	/**
	 * @author Cristina Constatinescu
	 */
	public class CodeMirrorEditorStatefulClient extends EditorStatefulClient {
			
		protected var statefulServiceId:String;
		
		public override function getStatefulServiceId():String {	
			return statefulServiceId;
		}
		
		public function CodeMirrorEditorStatefulClient(statefulServiceId:String) {
			this.statefulServiceId = statefulServiceId;
		}
		
		override protected function copyLocalDataFromExistingEditorToNewEditor(existingEditor:EditorFrontend, newEditor:EditorFrontend):void	{
//			super.copyLocalDataFromExistingEditorToNewEditor(existingEditor, newEditor);
//			OrionEditorFrontend(newEditor).setContent(
//				OrionEditorFrontend(existingEditor).orionEditor.);
		}
		
		override protected function areLocalUpdatesAppliedImmediately():Boolean	{
			return true;
		}
		
		///////////////////////////////////////////////////////////////
		// Wrappers for service methods
		///////////////////////////////////////////////////////////////
		
		/**
		 * 
		 * @author Daniela
		 */
		public function service_selectRangeFor(category:String, searchString:String):void {
			invokeServiceMethod("selectRangeFor", [editableResourcePath, category, searchString]);
		}
		
	}
}