package com.crispico.flower.util.tree {
	import com.crispico.flower.util.tree.checkboxtree.CheckBoxTreeItemRenderer;
	
	import flash.display.DisplayObject;
	
	import mx.controls.treeClasses.TreeListData;
	import mx.core.IFlexDisplayObject;

	
	/**
	 * Abstract renderer that allows the use of custom icons, mainly
	 * meant to be used with images retrieved from <code>ImageFactory</code>.
	 * 
	 * <p>
	 * Subclasses need to override <code>createIcon()</code> method.
	 * 
	 * @author Cristi 
	 */ 
	public class CustomIconTreeItemRenderer extends CheckBoxTreeItemRenderer {
		
		/**
		 * Cancels the original icon related logic (where icon = Class provided
		 * by tree) and uses our icon
		 * instead.
		 */ 
		override protected function commitProperties():void {
			var newIcon:IFlexDisplayObject = createIcon();
			if (newIcon != null) {
				TreeListData(listData).icon = null;
			}
			super.commitProperties();
			if (newIcon != null) {
				icon = newIcon;
				addChild(DisplayObject(icon));
			}
		} 

		/**
		 * Needs to be overidden and return a new icon instance
		 * (probably based on the model data).
		 */ 
		protected function createIcon():IFlexDisplayObject {
			return null;
		}

	}
	
}