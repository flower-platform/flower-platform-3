package org.flowerplatform.editor.java {
	import mx.collections.ArrayList;
	
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.editor.java.propertypage.JavaPropertyPageProvider;
	import org.flowerplatform.editor.java.propertypage.remote.JavaProjectPropertyPageService;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.web.common.WebCommonPlugin;
	
	public class JavaEditorPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:JavaEditorPlugin;
		
		public var javaProjectPropertyPageService:JavaProjectPropertyPageService = new JavaProjectPropertyPageService();
		
		public static function getInstance():JavaEditorPlugin {
			return INSTANCE;
		}
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
			
			WebCommonPlugin.getInstance().projectPropertyProviders.addItem(new JavaPropertyPageProvider());
		}
	}
}