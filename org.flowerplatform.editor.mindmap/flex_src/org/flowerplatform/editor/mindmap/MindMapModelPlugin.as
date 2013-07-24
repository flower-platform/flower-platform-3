/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.editor.mindmap {
	
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.mindmap.action.DeleteAction;
	import org.flowerplatform.editor.mindmap.action.MoveDownAction;
	import org.flowerplatform.editor.mindmap.action.MoveUpAction;
	import org.flowerplatform.editor.mindmap.action.NewFolderAction;
	import org.flowerplatform.editor.mindmap.action.NewHeadline1Action;
	import org.flowerplatform.editor.mindmap.action.NewHeadline2Action;
	import org.flowerplatform.editor.mindmap.action.NewHeadline3Action;
	import org.flowerplatform.editor.mindmap.action.NewPageAction;
	import org.flowerplatform.editor.mindmap.action.NewParagraphAction;
	import org.flowerplatform.editor.mindmap.action.RenameAction;
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

			mindmapDiagramClassFactoryActionProvider.actionClasses.push(NewFolderAction);
			mindmapDiagramClassFactoryActionProvider.actionClasses.push(NewPageAction);
			mindmapDiagramClassFactoryActionProvider.actionClasses.push(NewHeadline1Action);
			mindmapDiagramClassFactoryActionProvider.actionClasses.push(NewHeadline2Action);
			mindmapDiagramClassFactoryActionProvider.actionClasses.push(NewHeadline3Action);
			mindmapDiagramClassFactoryActionProvider.actionClasses.push(NewParagraphAction);
			mindmapDiagramClassFactoryActionProvider.actionClasses.push(RenameAction);
			mindmapDiagramClassFactoryActionProvider.actionClasses.push(MoveUpAction);
			mindmapDiagramClassFactoryActionProvider.actionClasses.push(MoveDownAction);
			mindmapDiagramClassFactoryActionProvider.actionClasses.push(DeleteAction);
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