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
package org.flowerplatform.editor.model {
	
	import org.flowerplatform.editor.EditorDescriptor;
	import org.flowerplatform.editor.EditorFrontend;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.editor.remote.EditorStatefulClient;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;

	/**
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */
	public class NotationDiagramEditorDescriptor extends EditorDescriptor {
		
		override public function getEditorName():String {
			return "diagram";
		}
		
		override protected function createViewInstance():EditorFrontend	{
			return new NotationDiagramEditorFrontend();
		}
		
		override protected function createEditorStatefulClient():EditorStatefulClient {
			return new NotationDiagramEditorStatefulClient();
		}
		
		public override function getId():String {	
			return "org.flowerplatform.editor.diagram";
		}
		
		public override function getIcon(viewLayoutData:ViewLayoutData=null):Object {	
			//			var result:Object = super.getIcon(viewLayoutData);
			//			if (result != null) {
			//				return result;
			//			} else {
			//				return EditorTextPlugin.getInstance().getResourceUrl("images/file.gif");
			//			}
			return null;
		}
		
		public override function getTitle(viewLayoutData:ViewLayoutData=null):String {	
			//			if (viewLayoutData == null) {
			//				return EditorTextPlugin.getInstance().getMessage("editor.text.name");
			//			} else {
			//				return viewLayoutData.customData.slice(viewLayoutData.customData.lastIndexOf("/") + 1);
			//			}
			//			return null;
			return "diagram ...";
		}
	}
}