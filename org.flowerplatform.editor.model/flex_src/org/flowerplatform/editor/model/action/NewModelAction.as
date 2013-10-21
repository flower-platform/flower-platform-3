package org.flowerplatform.editor.model.action {
	
	import org.flowerplatform.editor.model.EditorModelPlugin;
	import org.flowerplatform.editor.model.NotationDiagramShell;
	import org.flowerplatform.editor.model.properties.ILocationForNewElementsDialog;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.Diagram;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.IDiagramShellAware;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.dialog.IDialogResultHandler;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NewModelAction extends ActionBase implements IDialogResultHandler, IDiagramShellAware {
		
		private var _diagramShell:DiagramShell;
				
		public function get diagramShell():DiagramShell {		
			return _diagramShell;
		}
		
		public function set diagramShell(value:DiagramShell):void {
			_diagramShell = value;
		}
		
		private function get diagram():Diagram {
			return Diagram(NotationDiagramShell(diagramShell).rootModel);
		}
		
		protected function createNewModelElement(location:String):void {
			throw new Error("This method must be implemented!");
		}
		
		override public function run():void {
			if (diagram.viewDetails.showNewElementsPathDialog) {
				var dialog:ILocationForNewElementsDialog = EditorModelPlugin.getInstance().getLocationForNewElementsDialogNewInstance();
				dialog.diagramEditableResourcePath = NotationDiagramShell(diagramShell).editorStatefulClient.editableResourcePath;
				dialog.currentLocationForNewElements = diagram.viewDetails.newElementsPath;
				dialog.setResultHandler(this);
				
				FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(dialog)
				.setTitle("Location For new Elements")
				.show();
			} else {				
				createNewModelElement(diagram.viewDetails.newElementsPath);
			}
		}
		
		public function handleDialogResult(result:Object):void {
			NotationDiagramEditorStatefulClient(NotationDiagramShell(diagramShell).editorStatefulClient).service_updateNewElementsPathProperties(String(result.newLocation), Boolean(result.showDialog));
			
			createNewModelElement(result.newLocation);
		}		
	}
	
}