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
package org.flowerplatform.editor.remote {
	import org.flowerplatform.communication.command.AbstractClientCommand;
	import org.flowerplatform.editor.BasicEditorDescriptor;
	import org.flowerplatform.editor.EditorPlugin;
	
	
	[RemoteClass]
	public class CreateEditorStatefulClientCommand extends AbstractClientCommand { 
		
		public var editableResourcePath:String;
		
		public var editor:String;
		
		public var handleAsClientSubscription:Boolean;
		
		override public function execute():void	{
			var editorDescriptor:BasicEditorDescriptor = EditorPlugin.getInstance().getEditorDescriptorByName(editor);
			if (editorDescriptor == null) {
				throw "Cannot find EditorDescriptor for editor = " + editor;
			}
			
			editorDescriptor.openEditor(editableResourcePath, false, true, handleAsClientSubscription);
		}
		
	}
}