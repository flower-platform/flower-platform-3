package org.flowerplatform.editor.model.action {
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.IDiagramShellAware;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.dialog.IDialogResultHandler;
	
	public class NewModelAction extends ActionBase implements IDialogResultHandler, IDiagramShellAware {
		
		private var _diagramShell:DiagramShell;
		
		public function NewModelAction() {
			
		}
		
		public function get diagramShell():DiagramShell {		
			return _diagramShell;
		}
		
		public function set diagramShell(value:DiagramShell):void {
			_diagramShell = value;
		}
		
		override public function run():void {
			if (diagramShell) {}
		}
		
		public function handleDialogResult(result:Object):void {
		
		}		
	}
	
}