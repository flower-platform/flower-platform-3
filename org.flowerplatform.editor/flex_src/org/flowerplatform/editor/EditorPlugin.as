package org.flowerplatform.editor {
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.editor.action.EditorTreeActionProvider;
	import org.flowerplatform.editor.remote.ContentTypeDescriptor;
	import org.flowerplatform.editor.remote.InitializeEditorPluginClientCommand;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Cristi
	 */
	public class EditorPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:EditorPlugin;
		
		public static function getInstance():EditorPlugin {
			return INSTANCE;
		}
		
		public static const TREE_NODE_KEY_CONTENT_TYPE:String = "contentType";
		
		public var editorTreeActionProvider:EditorTreeActionProvider = new EditorTreeActionProvider();
		
		public var contentTypeDescriptors:Vector.<ContentTypeDescriptor>;
		
		public var lockLeaseSeconds:int;
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
		}
		
		override protected function registerClassAliases():void	{
			registerClassAliasFromAnnotation(ContentTypeDescriptor);
			registerClassAliasFromAnnotation(InitializeEditorPluginClientCommand);
		}
		
		public function getEditorDescriptorByName(name:String):EditorDescriptor {
			return null;
		}
	}
}