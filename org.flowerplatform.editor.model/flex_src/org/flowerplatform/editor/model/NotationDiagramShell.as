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
	import flash.utils.Dictionary;
	
	import org.flowerplatform.editor.model.remote.DiagramEditorStatefulClient;
	import org.flowerplatform.emf_model.notation.View;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ComposedControllerProvider;
	import org.flowerplatform.flexdiagram.controller.ComposedControllerProviderFactory;
	import org.flowerplatform.flexdiagram.controller.IControllerProvider;
	
	public class NotationDiagramShell extends DiagramShell {
		
		/**
		 * key = viewType
		 */
		protected var composedControllerProviders:Dictionary = new Dictionary();
		
		public var editorStatefulClient:DiagramEditorStatefulClient;
		
		/**
		 * @author Cristina Constantinescu
		 */ 
		public var diagramFrontend:DiagramEditorFrontend;
		
		override public function getControllerProvider(model:Object):IControllerProvider {
			var viewType:String = View(model).viewType;
			var result:ComposedControllerProvider = composedControllerProviders[viewType];
			
			if (result == null) {
				var factory:ComposedControllerProviderFactory = EditorModelPlugin.getInstance().composedControllerProviderFactories[viewType];
				if (factory == null) {
					throw new Error("Couldn't find ComposedControllerProviderFactory for viewType = " + viewType);
				}
				result = factory.createComposedControllerProvider(this);
				composedControllerProviders[viewType] = result;
			}
			
			return result;
		}
		
	}
}