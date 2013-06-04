package com.crispico.flower.flexdiagram.tree
{
	import mx.controls.treeClasses.TreeItemRenderer;
	import mx.core.ClassFactory;
	
	import spark.components.List;
	
	public class TreeList extends List {
		
		public function TreeList() {
			super();
			
			setStyle("skinClass", TreeListSkin);
			itemRenderer = new ClassFactory(TreeListItemRenderer);
		}
	}
}