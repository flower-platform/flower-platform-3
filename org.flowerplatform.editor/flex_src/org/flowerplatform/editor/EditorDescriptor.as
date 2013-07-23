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
	import com.crispico.flower.util.layout.Workbench;
	
	import mx.collections.ArrayCollection;
	import mx.core.UIComponent;
	import mx.core.mx_internal;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.stateful_service.StatefulClient;
	import org.flowerplatform.editor.remote.EditorStatefulClient;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.layout.IViewProvider;
	import org.flowerplatform.flexutil.layout.IWorkbench;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;

	/**
	 * Abstract class; should be subclassed.
	 * 
	 * <p>
	 * Acts as a <code>BasicEditorDescriptor</code> and as
	 * a <code>IViewProvider</code>.
	 * 
	 * <p>
	 * Holds specific information for an editor (e.g.
	 * name and icon URL that will appear in the 
	 * context menu item) and triggers the editor open,
	 * based on an editor input.
	 * 
	 * @author Cristi
	 * @author Mariana
	 * @flowerModelElementId __b0vAQirEeK2eaWSbd_Jmg
	 */
	public class EditorDescriptor extends BasicEditorDescriptor implements IViewProvider {
		
		private static const OPEN_FORCED_BY_SERVER_EDITOR_INPUT_MARKER:String = "???___open_forced_by_server";
		
		/**
		 * Abstract method.
		 * 
		 * @flowerModelElementId _TRTbAajNEeG5F5Y4p-wnrg
		 */
		public function getId():String {
			throw new Error("This method should be implemented.");
		}
		
		/**
		 * Abstract method.
		 * 
		 * <p>
		 * Should create a new (unpopulated) instance of the corresponding
		 * <code>EditorFrontend</code>.
		 * 
		 * @flowerModelElementId _ROn7MKgVEeGLHLPNLE28hw
		 */
		protected function createViewInstance():EditorFrontend {
			throw new Error("This method should be implemented.");
		}
		
		/**
		 * Adds a new view in the global <code>Workbench</code>, which will then
		 * trigger the creation of the view (i.e. <code>EditorFrontend</code>).
		 * 
		 * @flowerModelElementId _Fk6KAKseEeGkXYK9TZw9sA
		 */
		override public function openEditor(editableResourcePath:String, forceNewEditor:Boolean = false, openForcedByServer:Boolean = false, handleAsClientSubscription:Boolean = false):UIComponent {
			var viewLayoutData:ViewLayoutData = new ViewLayoutData();
			viewLayoutData.viewId = getId();
			viewLayoutData.customData = editableResourcePath;
			
			// we need to pass this info to .createView(), so using a suffix is the only way
			if (openForcedByServer) {
				viewLayoutData.customData += OPEN_FORCED_BY_SERVER_EDITOR_INPUT_MARKER;
			}
			viewLayoutData.isEditor = true;
			
			if (!forceNewEditor) {
				// we want to see if the resource is already open by this type of editor
				// if force = true => it doesn't matter
//				var existingEditorStatefulClients:ArrayCollection = GlobalEditorOperationsManager.INSTANCE.getEditorStatefulClientsForEditableResourcePath(editableResourcePath);
				var existingStatefulClients:ArrayCollection = CommunicationPlugin.getInstance().statefulClientRegistry.mx_internal::statefulClientsList;
				if (existingStatefulClients != null) {
					for each (var existingStatefulClient:StatefulClient in existingStatefulClients) {
						if (existingStatefulClient is EditorStatefulClient) {
							var existingEditorStatefulClient:EditorStatefulClient = EditorStatefulClient(existingStatefulClient);
							if (existingEditorStatefulClient.editorDescriptor == this && existingEditorStatefulClient.editableResourcePath == editableResourcePath) {
								if (existingEditorStatefulClient.editorFrontends.length > 0) {
									// the resource is already opened by this type of editor
									
									// bring it to front
									var workbench:IWorkbench = FlexUtilGlobals.getInstance().workbench;
									
									var editorFrontend:EditorFrontend = EditorFrontend(existingEditorStatefulClient.editorFrontends[0]);
									// normally if the ESC exists => it has at least 1 EditorFrontend; if abnormally this is not true => exception here
									workbench.setActiveView(editorFrontend);
									// and exit
									return editorFrontend;
								}
							}
						}
					}
				}
			}
			
			return FlexUtilGlobals.getInstance().workbench.addEditorView(viewLayoutData, true);		
		}
		
		public function getTabCustomizer(viewLayoutData:ViewLayoutData):Object {			
			return null;
		}
		
		public function getViewPopupWindow(viewLayoutData:ViewLayoutData):UIComponent {
			return null;
		}

//		override public function getIcon(viewLayoutData:ViewLayoutData=null):Object	{
//			if (viewLayoutData == null) {
//				return null;
//			}
//			
//			var editor:EditorFrontend = EditorFrontend(SingletonRefsFromPrePluginEra.workbench.layoutDataToComponent[viewLayoutData]);
//			if (editor == null || editor.editorStatefulClient == null) {
//				return null;
//			}
//			
//			var editableResourceStatus:EditableResource = editor.editorStatefulClient.editableResourceStatus;
//			if (editableResourceStatus == null) {
//				return null;
//			}
//
//			return editableResourceStatus.iconUrl;
//		}
		
		/**
		 * Creates the view (i.e. <code>EditorFrontend</code>) and delegates to the
		 * <code>EditorFrontendController</code> for opening (creates one if none exists for the current
		 * editorInput).
		 * 
		 * @flowerModelElementId _cBMwJqWoEeGAT8h2VXeJdg
		 */
		public function createView(viewLayoutData:ViewLayoutData):UIComponent {	
			var editor:EditorFrontend = createViewInstance();

			var openForcedByServer:Boolean = false;
			if (Utils.endsWith(viewLayoutData.customData, OPEN_FORCED_BY_SERVER_EDITOR_INPUT_MARKER)) {
				// an open forced by server => cleanup the editorInput
				viewLayoutData.customData = viewLayoutData.customData.replace(OPEN_FORCED_BY_SERVER_EDITOR_INPUT_MARKER, "");
				openForcedByServer = true;
			}
				
			var editableResourcePath:String = viewLayoutData.customData;
			
			var editorStatefulClient:EditorStatefulClient = getOrCreateEditorStatefulClient(editableResourcePath);
			
			CommunicationPlugin.getInstance().statefulClientRegistry.register(editorStatefulClient, { editorFrontend: editor, openForcedByServer: openForcedByServer });
			
			return editor;
		}
		
	}
}