package org.flowerplatform.editor.text.java {
	
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Mariana
	 */
	public class EditorTextJavaPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:EditorTextJavaPlugin;
		
		public static function getInstance():EditorTextJavaPlugin {
			return INSTANCE;
		}
	
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
			
			var editorDescriptor:JavaTextEditorDescriptor = new JavaTextEditorDescriptor();
			EditorPlugin.getInstance().editorDescriptors.push(editorDescriptor);
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(editorDescriptor);
		}
		
	}
}