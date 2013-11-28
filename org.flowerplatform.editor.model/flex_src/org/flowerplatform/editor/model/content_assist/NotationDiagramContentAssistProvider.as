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
package org.flowerplatform.editor.model.content_assist {
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.editor.model.EditorModelPlugin;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.content_assist.IContentAssistProvider;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class NotationDiagramContentAssistProvider implements IContentAssistProvider {
		
		private var viewId:Object;
		
		private var triggerCharacters:ArrayCollection = new ArrayCollection([":".charCodeAt()]);
		
		public function NotationDiagramContentAssistProvider(viewId:Object = null) {
			this.viewId = viewId;
		}

		public function getContentAssistItems(pattern:String, setContentAssistItems:Function):void {
			NotationDiagramEditorStatefulClient(DiagramEditorStatefulClient.TEMP_INSTANCE).service_contentAssist(viewId, pattern, setContentAssistItems);
		}
		
		public function getTriggerCharacters():ArrayCollection {
			return triggerCharacters;
		}
		
		public function getResource(resource:String):Object {
			return FlexUtilGlobals.getInstance().adjustImageBeforeDisplaying(EditorModelPlugin.getInstance().getComposedImageUrl(resource));
		}
	}
}