/*
license-start

Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation version 3.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details, at <http://www.gnu.org/licenses/>.

Contributors:
Crispico - Initial API and implementation  The product is licensed under GPL. This is the new version of the license.

license-end
*/
package org.flowerplatform.editor.javascript.propertypage
{
	import org.flowerplatform.editor.javascript.JavaScriptEditorPlugin;
	import org.flowerplatform.web.common.projects.properties.IPropertyPage;
	import org.flowerplatform.web.common.projects.properties.IPropertyPageProvider;
	/**
	 * @author Razvan Tache
	 * @see JavaPropertyPageProvider
	 */	
	public class JavaScriptPropertyPageProvider implements IPropertyPageProvider {
		
		
		public function getLabel():String{
			return JavaScriptEditorPlugin.getInstance().getMessage("javaScript.propertypage.name");
		}
		
		public function getPage():IPropertyPage {
			return new JavaScriptPropertyPage();
		}
		
	}
	
}