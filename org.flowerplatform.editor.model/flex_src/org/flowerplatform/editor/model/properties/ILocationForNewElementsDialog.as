package org.flowerplatform.editor.model.properties {
	
	import org.flowerplatform.flexutil.dialog.IDialog;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;

	/**
	 * @author Cristina Constantinescu
	 */
	public interface ILocationForNewElementsDialog extends IDialog, IViewContent {
		
		function set diagramEditableResourcePath(value:String):void;
		
		function set currentLocationForNewElements(value:String):void;
		
		function set currentShowNewElementsPathDialog(value:Boolean):void;
	}
}