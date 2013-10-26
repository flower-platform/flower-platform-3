package org.flowerplatform.editor.model.action {
	
	import mx.collections.IList;
	
	import org.flowerplatform.editor.model.DiagramEditorFrontend;
	import org.flowerplatform.editor.model.EditorModelPlugin;
	import org.flowerplatform.editor.model.NotationDiagramShell;
	import org.flowerplatform.editor.model.location_new_elements.LocationForNewElementsDialog;
	import org.flowerplatform.editor.model.properties.ILocationForNewElementsDialog;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.Diagram;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.renderer.IDiagramShellAware;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.action.ComposedAction;
	import org.flowerplatform.flexutil.dialog.IDialogResultHandler;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class AddNewElementAction extends ComposedAction implements IDialogResultHandler, IDiagramShellAware {
		
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
		
		public function AddNewElementAction() {
			actAsNormalAction = true;
		}
		
		protected function createNewModelElement(location:String, selection:IList, context:Object):void {
			throw new Error("This method must be implemented!");
		}
		
		protected function showLocationForNewElementsDialog():Boolean {
			return true;
		}
		
		override public function run():void {		
			if (showLocationForNewElementsDialog() && diagram.viewDetails.showLocationForNewElementsDialog) {
				var dialog:LocationForNewElementsDialog = new LocationForNewElementsDialog();
				dialog.selectionOfItems = NotationDiagramShell(diagramShell).diagramFrontend.convertSelectionToSelectionForServer(selection);
				dialog.currentLocationForNewElements = diagram.viewDetails.locationForNewElements;
				dialog.currentShowNewElementsPathDialog = diagram.viewDetails.showLocationForNewElementsDialog;
			
				var options:Object = new Object();
				options.selection = selection;
				options.context = context;				
				dialog.options = options;
				
				dialog.setResultHandler(this);
				
				FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(dialog)				
				.setWidth(470)
				.setHeight(450)
				.show();
			} else {
				createNewModelElement(diagram.viewDetails.locationForNewElements, selection, context);
			}
		}
		
		public function handleDialogResult(result:Object):void {	
			var storedOptions:Object = result.options;
			createNewModelElement(String(result.location), IList(storedOptions.selection), storedOptions.context);
		}		 
	}
	
}
