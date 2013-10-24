package org.flowerplatform.editor.model.properties {
	
	import mx.collections.IList;
	
	import org.flowerplatform.flexutil.dialog.IDialog;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;

	/**
	 * @author Cristina Constantinescu
	 */
	public interface ILocationForNewElementsDialog extends IDialog, IViewContent {
		
		function set selectionOfItems(value:IList):void;
		
		function set currentLocationForNewElements(value:String):void;
		
		function set currentShowNewElementsPathDialog(value:Boolean):void;
		
		// these options will be returned to result handler when ok pressed
		function set options(value:Object):void;
	}
}