package org.flowerplatform.codesync {
	import org.flowerplatform.codesync.remote.CodeSyncAction;
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.web.common.WebCommonPlugin;
	
	/**
	 * @author Cristi
	 */
	public class CodeSyncPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:CodeSyncPlugin;
		
		public static function getInstance():CodeSyncPlugin {
			return INSTANCE;
		}
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
			
			WebCommonPlugin.getInstance().explorerTreeClassFactoryActionProvider.actionClasses.push(CodeSyncAction);
		}
		
		override protected function registerClassAliases():void {
			registerClassAliasFromAnnotation(CodeSyncAction);
		}
		
		override protected function registerMessageBundle():void {
		}
		
		
	}
}