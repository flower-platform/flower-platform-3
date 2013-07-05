package org.flowerplatform.codesync {
	import com.crispico.flower.mp.codesync.base.action.DiffActionEntry;
	import com.crispico.flower.mp.codesync.base.action.DiffContextMenuEntry;
	import com.crispico.flower.mp.codesync.base.communication.DiffTreeNode;
	import com.crispico.flower.mp.codesync.base.editor.CodeSyncEditorDescriptor;
	
	import org.flowerplatform.codesync.remote.CodeSyncAction;
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.editor.EditorDescriptor;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
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
			
			var editorDescriptor:EditorDescriptor = new CodeSyncEditorDescriptor();
			EditorPlugin.getInstance().editorDescriptors.push(editorDescriptor);
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(editorDescriptor);
			
			WebCommonPlugin.getInstance().explorerTreeClassFactoryActionProvider.actionClasses.push(CodeSyncAction);
		}
		
		override protected function registerClassAliases():void {
			registerClassAliasFromAnnotation(CodeSyncAction);
			registerClassAliasFromAnnotation(DiffTreeNode);
			registerClassAliasFromAnnotation(DiffContextMenuEntry);
			registerClassAliasFromAnnotation(DiffActionEntry);
		}
		
		override protected function registerMessageBundle():void {
		}
		
		
	}
}