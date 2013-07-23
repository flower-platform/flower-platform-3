package org.flowerplatform.editor.model.action {
	
	import org.flowerplatform.editor.model.EditorModelPlugin;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class AddScenarioAction extends TextInputAction {
		
		public function AddScenarioAction() {
			super();
			
			label = "Add Scenario";
			icon = EditorModelPlugin.getInstance().getResourceUrl("images/scenario.png");
		}
		
		override public function get visible():Boolean {
			if (selection == null || selection.length == 0) {
				return true;
			}
			return false;
		}
		
		override public function run():void {
			askForTextInput("Scenario 1", "Add Scenario", "Add", 
				function(name:String):void {
					NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).service_addNewScenario(name);
				});
		}
	}
}