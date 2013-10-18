package org.flowerplatform.editor.model.action {
	
	import org.flowerplatform.editor.model.EditorModelPlugin;
	import org.flowerplatform.flexutil.action.ComposedAction;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NewModelComposedAction extends ComposedAction {
		
		public function NewModelComposedAction() {
			super();
			id = "new";
			label = EditorModelPlugin.getInstance().getMessage("action.new");
			icon = EditorModelPlugin.getInstance().getResourceUrl("/images/add.png");
			orderIndex = 100;
		}
	}
}