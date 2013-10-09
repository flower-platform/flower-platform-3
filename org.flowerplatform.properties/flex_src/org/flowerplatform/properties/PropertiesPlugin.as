package org.flowerplatform.properties {
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	
	import org.flowerplatform.properties.ui.PropertiesList;
	import org.flowerplatform.properties.ui.PropertiesViewProvider;

	public class PropertiesPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:PropertiesPlugin;
		
		public var propertyList:PropertiesList;

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
		}
		
		override protected function registerMessageBundle():void {
			// do nothing, as we don't have a .resources(yet)
		}
		
		override protected function registerClassAliases():void {
			
		}
		
	}
}