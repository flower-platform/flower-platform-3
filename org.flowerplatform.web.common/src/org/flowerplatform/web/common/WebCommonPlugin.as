package org.flowerplatform.web.common {
	
	import mx.core.FlexGlobals;
	import mx.core.IVisualElementContainer;
	
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.popup.IActionProvider;
	import org.flowerplatform.web.common.communication.AuthenticationManager;
	import org.flowerplatform.web.common.explorer.ExplorerViewProvider;
	
	/**
	 * @author Cristi
	 */
	public class WebCommonPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:WebCommonPlugin;
		
		public static function getInstance():WebCommonPlugin {
			return INSTANCE;
		}
		
		public static const NODE_TYPE_ORGANIZATION:String = "or";
		
		public static const NODE_TYPE_FILE:String = "f";
		
		public var authenticationManager:AuthenticationManager;
		
		public var explorerTreeActionProviders:Vector.<IActionProvider> = new Vector.<IActionProvider>();
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
			
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new ExplorerViewProvider());
			explorerTreeActionProviders.push(EditorPlugin.getInstance().editorTreeActionProvider);
			explorerTreeActionProviders.push(new TestSampleExplorerTreeActionProvider());
			
			EditorPlugin.getInstance().addPathFragmentToEditableResourcePathCallback = function (treeNode:TreeNode):String {
				if (treeNode.pathFragment == null) {
					return null;
				}
				if (treeNode.pathFragment.type == NODE_TYPE_ORGANIZATION || treeNode.pathFragment.type == NODE_TYPE_FILE) {
					return treeNode.pathFragment.name;
				} else {
					return null;
				}
			}
		}
		
		override public function start():void {
			super.start();
			authenticationManager = new AuthenticationManager();
		}
		
		override protected function registerMessageBundle():void {
			// do nothing; this plugin doesn't have a .resources (yet)
		}
		
	}
}