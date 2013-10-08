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
package org.flowerplatform.editor.model.remote {
	import org.flowerplatform.codesync.code.java.CodeSyncCodeJavaPlugin;

	/**
	 * @author Mariana Gheorghe
	 */
	[RemoteClass(alias="org.flowerplatform.editor.model.java.remote.NewJavaClassDiagramAction")]
	public class NewJavaClassDiagramAction extends NewDiagramAction {
		
		public function NewJavaClassDiagramAction() {
			super();
			
			label = CodeSyncCodeJavaPlugin.getInstance().getMessage("action.newClassDiagram");
			name = "NewDiagram.notation";
			icon = CodeSyncCodeJavaPlugin.getInstance().getResourceUrl("images/ClassDiagram.gif");
		}
	}
}