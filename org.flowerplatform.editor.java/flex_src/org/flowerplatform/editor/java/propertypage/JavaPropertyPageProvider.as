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
package org.flowerplatform.editor.java.propertypage {	
	import org.flowerplatform.editor.java.JavaEditorPlugin;
	import org.flowerplatform.web.common.projects.properties.IPropertyPage;
	import org.flowerplatform.web.common.projects.properties.IPropertyPageProvider;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class JavaPropertyPageProvider implements IPropertyPageProvider {
				
		public function getLabel():String{
			return JavaEditorPlugin.getInstance().getMessage("java.propertypage.name");
		}
		
		public function getPage():IPropertyPage {
			return new JavaPropertyPage();
		}
	}
}