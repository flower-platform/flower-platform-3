package com.crispico.flower.flexdiagram.util.treegrid {

	import mx.controls.dataGridClasses.DataGridColumn;
	import mx.core.IFactory;
	import mx.core.ClassFactory;

	/**
	 * This class was created by making a copy/paste from flexLib <code>mx.controls.treeGridClasses.TreeGridColumn</code>.
	 * The action was done in order to provide functionality for icons to work with URLs, 
	 * not only with Classes.
	 * @author Cristina
	 */ 
	public class FlowerTreeGridColumn extends DataGridColumn {
		
		public function FlowerTreeGridColumn() {
			super();			
			itemRenderer = new ClassFactory(FlowerTreeGridItemRenderer);
		}	
	} 
	
}