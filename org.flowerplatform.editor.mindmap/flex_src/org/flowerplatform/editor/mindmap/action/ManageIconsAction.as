package org.flowerplatform.editor.mindmap.action {
	
	import org.flowerplatform.editor.mindmap.MindMapModelPlugin;
	import org.flowerplatform.editor.mindmap.ui.ManageIconsView;
	import org.flowerplatform.emf_model.notation.MindMapNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class ManageIconsAction extends ActionBase {

		public function ManageIconsAction() {
			label = MindMapModelPlugin.getInstance().getMessage("manageIcons.action.label");
			icon = MindMapModelPlugin.getInstance().getResourceUrl("images/images.png");		
			preferShowOnActionBar = true;
		}
		
		override public function run():void {
			var view:ManageIconsView = new ManageIconsView();
			view.selection = selection;
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setWidth(290)
				.setHeight(320)
				.setPopupContent(view)
				.show();
		}
		
	}
}