package org.flowerplatform.editor.text {
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.remote.ContentTypeDescriptor;
	import org.flowerplatform.editor.text.remote.TextEditorUpdate;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Cristi
	 */
	public class EditorTextPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:EditorTextPlugin;
		
		public static function getInstance():EditorTextPlugin {
			return INSTANCE;
		}
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
			
			var editorDescriptor:TextEditorDescriptor = new TextEditorDescriptor();
			EditorPlugin.getInstance().editorDescriptors.push(editorDescriptor);
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(editorDescriptor);
		}
		
		override protected function registerClassAliases():void	{
			registerClassAliasFromAnnotation(TextEditorUpdate);
		}
		
		
	}
}