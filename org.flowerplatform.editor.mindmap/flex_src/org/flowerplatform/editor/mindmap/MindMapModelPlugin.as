package org.flowerplatform.editor.mindmap {
	
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.mindmap.remote.NewMindMapDiagramAction;
	import org.flowerplatform.emf_model.notation.MindMapNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;

	import org.flowerplatform.flexutil.popup.ClassFactoryActionProvider;

	import org.flowerplatform.web.common.WebCommonPlugin;

	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapModelPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:MindMapModelPlugin;
		
		public var mindmapDiagramClassFactoryActionProvider:ClassFactoryActionProvider = new ClassFactoryActionProvider();
				
		public static function getInstance():MindMapModelPlugin {
			return INSTANCE;
		}
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
			
			var editorDescriptor:MindMapDiagramEditorDescriptor = new MindMapDiagramEditorDescriptor();
			EditorPlugin.getInstance().editorDescriptors.push(editorDescriptor);
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(editorDescriptor);

//			mindmapDiagramClassFactoryActionProvider.actionClasses.push(TestAction);
		}
		
		override public function start():void {
			super.start();
			WebCommonPlugin.getInstance().explorerTreeClassFactoryActionProvider.actionClasses.push(NewMindMapDiagramAction);
		}
		
		override protected function registerClassAliases():void {
			registerClassAliasFromAnnotation(MindMapNode);	
			registerClassAliasFromAnnotation(NewMindMapDiagramAction);
		}
		
		override protected function registerMessageBundle():void {
			// do nothing; this plugin doesn't have a .resources (yet)
		}
		
	}
}
