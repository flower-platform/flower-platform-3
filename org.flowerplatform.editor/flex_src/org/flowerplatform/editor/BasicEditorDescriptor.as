/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package  org.flowerplatform.editor {
	import mx.core.UIComponent;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.editor.remote.EditorStatefulClient;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;

	/**
	 * The methods for name and icon are meant to be compatible to the ones in
	 * <code>EditorDescriptor</code> which implements <code>IViewProvider</code>.
	 * 
	 * 
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
		 * 
		 */
		public function getIcon(viewLayoutData:ViewLayoutData=null):Object {
			throw new Error("This method should be implemented.");
		}
		
		/**
		 * Abstract method. Called with a <code>null</code> parameter, should
		 * return the icon of the editor.
		 * 
		 * 
		 */
		public function getTitle(viewLayoutData:ViewLayoutData=null):String	{
			throw new Error("This method should be implemented.");
		}
	
		/**
		 * Should open the corresponding editor, with the
		 * provided input. 
		 * 
		 * 
		 */
		public function openEditor(editableResourcePath:String, forceNewEditor:Boolean=false, openForcedByServer:Boolean=false, handleAsClientSubscription:Boolean=false):UIComponent {
			throw new Error("This method should be implemented");
		}
		
		/**
		 * Abstract method.
		 * 
		 * <p>
		 * Should create a new instance of the corresponding
		 * <code>EditorStatefulClient</code>.
		 * 
		 */ 
		protected function createEditorStatefulClient():EditorStatefulClient {
			throw new Error("This method should be implemented.");
		}
		
		protected function getOrCreateEditorStatefulClient(editableResourcePath:String):EditorStatefulClient {
			var editorStatefulClient:EditorStatefulClient = EditorStatefulClient(CommunicationPlugin.getInstance().statefulClientRegistry.getStatefulClientById(
				calculateStatefulClientId(editableResourcePath)));
			
			if (editorStatefulClient == null) {
				editorStatefulClient = createEditorStatefulClient();
				editorStatefulClient.editorDescriptor = this;
				editorStatefulClient.editableResourcePath = editableResourcePath;
			}		
			
			return editorStatefulClient;
		}
		
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