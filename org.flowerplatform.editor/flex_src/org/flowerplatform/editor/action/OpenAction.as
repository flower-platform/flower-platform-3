package  org.flowerplatform.editor.action {
	import mx.collections.ArrayCollection;
	import mx.core.UIComponent;
	
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.flexutil.popup.ActionBase;
	
	/**
	 * Opens an editor. Exists directly in the context menu or
	 * as a child of "Open with" menu.
	 * 
	 * @author Cristi
	 * @author Mariana
     * @author Sorin
	 * @flowerModelElementId _c1aTwE2iEeGsUPSh9UfXpw
	 */
	public class OpenAction extends ActionBase {
		
		public static const ICON_URL:String = EditorPlugin.getInstance().getResourceUrl("images/open_resource.png");
		
//		private var editorEntry:BasicEditorDescriptor;
		
		public var forceNewEditor:Boolean;
		
		/**
		 * There will be an editorEntry only when this action is added in the Open With submenu
		 * @flowerModelElementId _wb413FJdEeGnQ71Q1-0lCg
		 */ 
		public function OpenAction(/*editorEntry:BasicEditorDescriptor=null, forceNewEditor:Boolean = false*/):void {
			label = EditorPlugin.getInstance().getMessage("editor.open");
			icon = ICON_URL;
//			this.editorEntry = editorEntry;
//			this.forceNewEditor = forceNewEditor;
//			if (editorEntry == null) {
//				label = "Open";
//				image = ICON_URL;
//			} else {
//				label = editorEntry.getTitle();
//				image = editorEntry.getIcon();
//			}
//			sortIndex = 150;
		}
//		
//		public function openEditor(editorInput:String, editorContentTypeId:int):UIComponent {
//			var editorEntry:BasicEditorDescriptor = EditorSupport.INSTANCE.getFirstEditorDescriptorForNodeUnsafe(editorContentTypeId);
//			return editorEntry.openEditor(editorInput, forceNewEditor);
//		}
//		
//		/**
//		 * @flowerModelElementId _wb414FJdEeGnQ71Q1-0lCg
//		 */
//		override public function run(selectedEditParts:ArrayCollection):void {
//			if (editorEntry != null) { 
////				editorEntry.openEditor(ProjectExplorerStatefulClient.getEditableResourcePathFromTreeNode(NavigatorTreeNode(selectedEditParts.getItemAt(0))), forceNewEditor);
//				editorEntry.openEditor(SingletonRefsFromPrePluginEra.projectExplorerStatefulClient_getEditableResourcePathFromTreeNodeFunction(selectedEditParts.getItemAt(0)), forceNewEditor);
//			} else {
//				// if there is no set editor entry, open the first compatible editor, if there are any
////				for each (var node:NavigatorTreeNode in selectedEditParts) {
//				for each (var node:Object in selectedEditParts) {
//					var editorInput:String = SingletonRefsFromPrePluginEra.projectExplorerStatefulClient_getEditableResourcePathFromTreeNodeFunction(node); 
//					openEditor(editorInput, node.contentTypeId);
//				}	
//			}
//		}
	}
}