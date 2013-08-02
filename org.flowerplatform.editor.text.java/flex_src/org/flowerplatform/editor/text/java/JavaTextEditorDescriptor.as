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
package org.flowerplatform.editor.text.java {
	
	import org.flowerplatform.editor.EditorFrontend;
	import org.flowerplatform.editor.remote.EditorStatefulClient;
	import org.flowerplatform.editor.text.TextEditorDescriptor;
	import org.flowerplatform.editor.text.remote.TextEditorStatefulClient;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 * 
	 */
	public class JavaTextEditorDescriptor extends TextEditorDescriptor {

		override public function getEditorName():String {
			return "java";
		}
		
		override protected function createViewInstance():EditorFrontend	{
			return new JavaTextEditorFrontend();
		}
		
		override protected function createEditorStatefulClient():EditorStatefulClient {
			return new TextEditorStatefulClient("textEditorStatefulService");
		}
		
		override public function getId():String {	
			return "org.flowerplatform.editor.text.java";
		}
		
		override public function getIcon(viewLayoutData:ViewLayoutData=null):Object {
			return EditorTextJavaPlugin.getInstance().getResourceUrl("images/java_file.gif");
		}
		
		override public function getTitle(viewLayoutData:ViewLayoutData=null):String {
			if (viewLayoutData == null) {
				return EditorTextJavaPlugin.getInstance().getMessage("editor.text.java.name");
			} else {
				return super.getTitle(viewLayoutData);
			}
		}
	}
}