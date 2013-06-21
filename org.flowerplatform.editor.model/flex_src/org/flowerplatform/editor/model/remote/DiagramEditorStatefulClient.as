package org.flowerplatform.editor.model.remote {
	import org.flowerplatform.editor.remote.EditorStatefulClient;
	
	public class DiagramEditorStatefulClient extends EditorStatefulClient {
		public override function getStatefulServiceId():String {	
			return "diagramEditorStatefulService";
		}
	
		override protected function areLocalUpdatesAppliedImmediately():Boolean	{
			return false;
		}
	}
}