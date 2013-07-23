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
package com.crispico.flower.mp.codesync.base.editor {
	import org.flowerplatform.editor.EditorDescriptor;
	import org.flowerplatform.editor.EditorFrontend;
	import org.flowerplatform.editor.remote.EditorStatefulClient;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 * @author Mariana
	 */
	public class CodeSyncEditorDescriptor extends EditorDescriptor {
		
		override public function getEditorName():String {
			return "codeSync";
		}
		
		override protected function createViewInstance():EditorFrontend	{
			return new CodeSyncEditorFrontend();
		}
		
		override protected function createEditorStatefulClient():EditorStatefulClient {
			return new CodeSyncEditorStatefulClient();
		}
		
		public override function getId():String {	
			return "com.crispico.flower.mp.web.codesync";
		}
		
		public override function getIcon(viewLayoutData:ViewLayoutData=null):Object {
			return null;
		}
		
		public override function getTitle(viewLayoutData:ViewLayoutData=null):String {
			return "CodeSync";
		}
		
	}
}