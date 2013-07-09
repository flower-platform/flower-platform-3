package com.crispico.flower.mp.codesync.base.editor {
	import org.flowerplatform.editor.remote.EditorStatefulClient;
	
	/**
	 * @author Mariana
	 */
	public class CodeSyncEditorStatefulClient extends org.flowerplatform.editor.remote.EditorStatefulClient {
	
		override public function getStatefulServiceId():String {
			return "CodeSyncEditorStatefulService";
		}
		
		///////////////////////////////////////////////////////////////
		// Wrappers for service methods
		///////////////////////////////////////////////////////////////
		
		public function synchronize():void {
			invokeServiceMethod("synchronize", [editableResourcePath]);
		}
		
		public function applySelectedActions():void {
			invokeServiceMethod("applySelectedActions", [editableResourcePath]);
		}
		
		public function cancelSelectedActions():void {
			invokeServiceMethod("cancelSelectedActions", [editableResourcePath]);
		}
		
	}
}