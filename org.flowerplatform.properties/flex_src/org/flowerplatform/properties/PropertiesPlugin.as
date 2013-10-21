package org.flowerplatform.properties {
	import flash.utils.Dictionary;
	
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.properties.ui.PropertiesList;
	import org.flowerplatform.properties.ui.PropertiesViewProvider;
	import org.flowerplatform.properties.ui.Property;
	import org.flowerplatform.properties.ui.property_renderer.BooleanPropertyRenderer;
	import org.flowerplatform.properties.ui.property_renderer.StringPropertyRenderer;
	/**
	 * @author Razvan Tache
	 */
	public class PropertiesPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:PropertiesPlugin;
		
		public var propertyList:PropertiesList;

		public var propertyRendererClasses:Dictionary = new Dictionary();
		
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
		
		override protected function registerMessageBundle():void {
			// do nothing, as we don't have a .resources(yet)
		}
		
		override protected function registerClassAliases():void {
			registerClassAliasFromAnnotation(Property);
		}
		
		private function registerPropertyProviders():void {
			propertyRendererClasses["String"] = StringPropertyRenderer;
			propertyRendererClasses["Boolean"] = BooleanPropertyRenderer;
		}
	}
}