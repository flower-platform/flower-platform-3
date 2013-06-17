package  org.flowerplatform.editor {
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.layout.ViewLayoutData;

	/**
	 * The methods for name and icon are meant to be compatible to the ones in
	 * <code>EditorDescriptor</code> which implements <code>IViewProvider</code>.
	 * 
	 * @flowerModelElementId _F1yocKWpEeGAT8h2VXeJdg
	 */
	public class BasicEditorDescriptor {
		
		/**
		 * Should return the same value as the corresponding Java <code>EditorStatefulService</code>. 
		 */
		public function getEditorName():String {
			throw new Error("This method should be implemented");
		}
		
		/**
		 * Abstract method. Called with a <code>null</code> parameter, should
		 * return the icon of the editor.
		 * 
		 * @flowerModelElementId _TRQXsKjNEeG5F5Y4p-wnrg
		 */
		public function getIcon(viewLayoutData:ViewLayoutData=null):Object {
			throw new Error("This method should be implemented.");
		}
		
		/**
		 * Abstract method. Called with a <code>null</code> parameter, should
		 * return the icon of the editor.
		 * 
		 * @flowerModelElementId _TRXFYqjNEeG5F5Y4p-wnrg
		 */
		public function getTitle(viewLayoutData:ViewLayoutData=null):String	{
			throw new Error("This method should be implemented.");
		}
	
		/**
		 * Should open the corresponding editor, with the
		 * provided input. 
		 * 
		 * @flowerModelElementId _1UHNYE2ZEeGsUPSh9UfXpw
		 */
		public function openEditor(editableResourcePath:String, forceNewEditor:Boolean=false, openForcedByServer:Boolean=false, handleAsClientSubscription:Boolean=false):UIComponent {
			throw new Error("This method should be implemented");
		}
		
//		/**
//		 * Abstract method.
//		 * 
//		 * <p>
//		 * Should create a new instance of the corresponding
//		 * <code>EditorStatefulClient</code>.
//		 * @flowerModelElementId _NJ-x8AcIEeK49485S7r3Vw
//		 */ 
//		protected function createEditorStatefulClient():EditorStatefulClient {
//			throw new Error("This method should be implemented.");
//		}
//		
//		protected function getOrCreateEditorStatefulClient(editableResourcePath:String):EditorStatefulClient {
//			var editorStatefulClient:EditorStatefulClient = EditorStatefulClient(StatefulClientRegistry.INSTANCE.getStatefulClientById(
//				calculateStatefulClientId(editableResourcePath)));
//			
//			if (editorStatefulClient == null) {
//				editorStatefulClient = createEditorStatefulClient();
//				editorStatefulClient.editorDescriptor = this;
//				editorStatefulClient.editableResourcePath = editableResourcePath;
//			}		
//			
//			return editorStatefulClient;
//		}
		
		/**
		 * Has the same behavior like the similar method from Java <code>EditorStatefulService</code>. 
		 */
		public function calculateStatefulClientId(editableResourcePath:String):String {
			// only one / because the path already contains a /
			return getEditorName() + ":/" + editableResourcePath;
		}
		
		/**
		 * By default all editor descriptors can give a friendly editableResourcePath to be navigated from a link.
		 * Some type of editor descriptors are more general and are not openable in an Editor View (like a model).
		 * In this case, the overriding method should return false.
		 */ 
		public function canCalculateFriendlyEditableResourcePath():Boolean {
			return true;
		}
	}
	
}