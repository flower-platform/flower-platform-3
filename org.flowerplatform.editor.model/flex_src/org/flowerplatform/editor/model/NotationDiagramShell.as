package org.flowerplatform.editor.model {
	import flash.utils.Dictionary;
	
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
		
		public function NotationDiagramShell() {
			super();
		}
		
		override public function getControllerProvider(model:Object):IControllerProvider {
			var viewType:String = View(model).viewType;
			var result:ComposedControllerProvider = composedControllerProviders[viewType];
			
			if (result == null) {
				var factory:ComposedControllerProviderFactory = EditorModelPlugin.getInstance().composedControllerProviderFactories[viewType];
				if (factory == null) {
					throw new Error("Couldn't find ComposedControllerProviderFactory for viewType = ", viewType);
				}
				result = factory.createComposedControllerProvider(this);
				composedControllerProviders[viewType] = result;
			}
			
			return result;
		}
		
	}
}