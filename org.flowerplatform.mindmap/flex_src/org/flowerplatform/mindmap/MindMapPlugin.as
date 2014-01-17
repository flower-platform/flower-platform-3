package org.flowerplatform.mindmap {
	
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.action.ClassFactoryActionProvider;
	import org.flowerplatform.mindmap.action.RefreshAction;
	import org.flowerplatform.mindmap.action.ReloadAction;
	import org.flowerplatform.mindmap.layout.MindMapEditorProvider;
	import org.flowerplatform.mindmap.remote.Node;

	public class MindMapPlugin extends AbstractFlowerFlexPlugin	{
		
		protected static var INSTANCE:MindMapPlugin;
		
		public var service:MindMapService = new MindMapService();
		
		public static function getInstance():MindMapPlugin {
			return INSTANCE;
		}
				
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
			
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new MindMapEditorProvider());			
		}
			
		override protected function registerClassAliases():void {
			registerClassAliasFromAnnotation(Node);			
		}
		
		override protected function registerMessageBundle():void {
			
		}
	}
}