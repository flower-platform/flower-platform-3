package org.flowerplatform.properties {
	import flash.utils.Dictionary;
	
	import flexunit.utils.ArrayList;
	
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.FactoryWithInitialization;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.dialog.IDialog;
	import org.flowerplatform.flexutil.dialog.IDialogResultHandler;
	import org.flowerplatform.properties.PropertiesViewProvider;
	import org.flowerplatform.properties.property_renderer.BooleanPropertyRenderer;
	import org.flowerplatform.properties.property_renderer.StringPropertyRenderer;
	import org.flowerplatform.properties.property_renderer.StringWithButtonPropertyRenderer;
	import org.flowerplatform.properties.remote.Properties;
	import org.flowerplatform.properties.remote.Property;

	/**
	 * @author Razvan Tache
	 */
	public class PropertiesPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:PropertiesPlugin;
		
		public var propertiesView:PropertiesView;
		public var propertyRendererClasses:Dictionary = new Dictionary();
		
		/**
		 * @author Cristina Constantinescu
		 */ 
		public var propertiesProviders:ArrayList = new ArrayList();
		
		public static function getInstance():PropertiesPlugin {
			return INSTANCE;
		}
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
			
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new PropertiesViewProvider());
		}
		
		override public function start():void {
			super.start();
			registerPropertyProviders();
		}
				
		override protected function registerClassAliases():void {
			registerClassAliasFromAnnotation(Property);
		}
		
		private function registerPropertyProviders():void {
			propertyRendererClasses[null] = new FactoryWithInitialization
				(StringPropertyRenderer);
			propertyRendererClasses["String"] = new FactoryWithInitialization
				(StringPropertyRenderer);
			propertyRendererClasses["Boolean"] = new FactoryWithInitialization
				(BooleanPropertyRenderer);
			
		}

	}
}
