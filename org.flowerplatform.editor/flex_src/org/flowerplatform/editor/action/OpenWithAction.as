package  org.flowerplatform.editor.action {
	import mx.collections.ArrayCollection;

	/**
	 * Opens an editor. It's a submenu that reuses <code>OpenAction</code>
	 * as it's children.
	 * 
	 * @author Cristi
	 * @author Mariana
	 * @flowerModelElementId _fagGsE2iEeGsUPSh9UfXpw
	 */
	public class OpenWithAction {
	
//		private var resourceAlreadyOpen:Boolean;
//		
//		/**
//		 * @flowerModelElementId _wcB_wlJdEeGnQ71Q1-0lCg
//		 */
//		public function OpenWithAction(editableResourcePath:String, parentContextMenu:FlowerContextMenu, contentTypeId:int) {
//			var existingEditorStatefulClients:ArrayCollection = GlobalEditorOperationsManager.INSTANCE.getEditorStatefulClientsForEditableResourcePath(editableResourcePath);
//			resourceAlreadyOpen = existingEditorStatefulClients != null;
//			var label:String;
//			if (!resourceAlreadyOpen) {
//				label = "Open With";
//			} else {
//				// resource already open
//				label = "Open With (in New Editor)"; 
//			}
//			
//			super(new SubMenuEntryModel(OpenAction.ICON_URL, label, 151), parentContextMenu);
//			
//			var contentTypeEntry:ContentTypeEntry = EditorSupport.INSTANCE.contentTypeEntries[contentTypeId];
//			// add an open entry for all the compatible editors
//			if (contentTypeEntry != null) {
//				for each (var editor:String in contentTypeEntry.compatibleEditors) {
//					var editorEntry:BasicEditorDescriptor = EditorSupport.INSTANCE.editorNamesToEditorDescriptors[editor];
//					if (editorEntry != null) {
//						var entry:ActionEntry = new ActionEntry(new OpenAction(editorEntry, true));
//						getSubMenu().addChild(entry);
//					}
//				}
//			}
//		}
//
//		/**
//		 * Visible only if multiple actions are available.
//		 */ 
//		public function isVisible():Boolean {
//			return (getSubMenu().numChildren > 1) || 
//				resourceAlreadyOpen; // for plain text, we display this action if the editor is open, to let the user open a new editor
//		}
	}
}