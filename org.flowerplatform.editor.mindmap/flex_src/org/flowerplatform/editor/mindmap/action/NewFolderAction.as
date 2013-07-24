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
package org.flowerplatform.editor.mindmap.action {
	import flash.profiler.showRedrawRegions;
	
	import mx.collections.ArrayList;
	
	import org.flowerplatform.editor.mindmap.MindMapModelPlugin;
	import org.flowerplatform.editor.mindmap.remote.MindMapDiagramEditorStatefulClient;
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.MindMapNode;

	/**
	 * @author Cristina Constantinescu
	 */ 
	public class NewFolderAction extends NewMindMapNodeAction {
		
		public function NewFolderAction() {
			super();
			label = "New Folder";
			icon = MindMapModelPlugin.getInstance().getResourceUrl("images/folder.gif");
			viewType = "folder";
		}
		
	}
}