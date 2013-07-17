package org.flowerplatform.editor.mindmap {
	
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.emf_model.notation.MindMapNode;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapModelPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:MindMapModelPlugin;
		
		public static function getInstance():MindMapModelPlugin {
			return INSTANCE;
		}
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;			
		}
		
		override protected function registerClassAliases():void {
			registerClassAliasFromAnnotation(MindMapNode);			
		}
		
		override protected function registerMessageBundle():void {
			// do nothing; this plugin doesn't have a .resources (yet)
		}
		
	}
}