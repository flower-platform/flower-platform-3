package org.flowerplatform.editor.model.action {
	
	import mx.collections.IList;
	
	import org.flowerplatform.editor.model.DiagramEditorFrontend;
	import org.flowerplatform.editor.model.EditorModelPlugin;
	import org.flowerplatform.editor.model.NotationDiagramShell;
	import org.flowerplatform.editor.model.properties.ILocationForNewElementsDialog;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.Diagram;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.emf_model.notation.View;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.renderer.IDiagramShellAware;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.dialog.IDialogResultHandler;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NewModelAction extends ActionBase implements IDialogResultHandler, IDiagramShellAware {
		
		protected var storedSelection:IList;
		protected var storedContext:Object;
		
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
			storedSelection = selection;
			storedContext = context;
			
			if (diagram.viewDetails.showNewElementsPathDialog && (!(storedSelection.getItemAt(0) is Node))) {
				var dialog:ILocationForNewElementsDialog = EditorModelPlugin.getInstance().getLocationForNewElementsDialogNewInstance();
				dialog.selectionOfItems = NotationDiagramShell(diagramShell).diagramFrontend.convertSelectionToSelectionForServer(selection);
				dialog.currentLocationForNewElements = diagram.viewDetails.newElementsPath;
				dialog.currentShowNewElementsPathDialog = diagram.viewDetails.showNewElementsPathDialog;
				dialog.setResultHandler(this);
				
				FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(dialog)
				.setTitle(EditorModelPlugin.getInstance().getMessage("newElementsPath.title"))
				.setWidth(400)
				.setHeight(450)
				.show();
			} else {				
				createNewModelElement(diagram.viewDetails.newElementsPath);
			}
		}
		
		public function handleDialogResult(result:Object):void {		
			createNewModelElement(String(result));
		}		
	}
	
}