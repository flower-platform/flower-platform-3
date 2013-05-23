package com.crispico.flower.flexdiagram.action {

	import com.crispico.flower.flexdiagram.DiagramViewer;
	
	import mx.collections.ArrayCollection;
	
	/**
	 * The structure for a command/entry in the context menu
	 * Subclasses would normally override the following methods and/or have the following responsabilities:
	 * <ul>	
	 * 		<li>the constructor should pass to the super constructor the label of the command button from the menu 
	 * 		<li><code>getIcon()</code> - should return the class corresponding to the command icon
	 * 		<li><code>isEnabled()</code> - If returns true,it will appear in the <code>ContextMenu</code>
	 * 		<li><code>run()</code> - execute the command corresponding to an Action
	 * </ul>
	 * 
	 * @author Ioana Hagiescu
	 * @flowerModelElementId _buNBcL8REd6XgrpwHbbsYQ
	 */ 
	public class Action {
		
		/**
		 * @flowerModelElementId _dZNf4M9mEd6B9ITMJUGokQ
		 */
		protected var icon:Class;
		
		/**
		 * @flowerModelElementId _buNBdL8REd6XgrpwHbbsYQ
		 */
		protected var label:String;
		
		/**
		 * @flowerModelElementId _buWycL8REd6XgrpwHbbsYQ
		 */
		public function getLabel():String {
			return label;
		} 
		
		/**
		 * @flowerModelElementId _buWydL8REd6XgrpwHbbsYQ
		 */
		public function getIcon():Class {
			return icon;
		}
						
		/**
		 * @flowerModelElementId _buWyeL8REd6XgrpwHbbsYQ
		 */
		public function Action(label:String = null) {
			this.label = label;
		}

		/**
		 * @flowerModelElementId _buWyfL8REd6XgrpwHbbsYQ
		 */
		public function isEnabled(selection:ArrayCollection):Boolean {
			return true;
		}
		
		/**
		 * @flowerModelElementId _buptZb8REd6XgrpwHbbsYQ
		 */
		public function run(selection:ArrayCollection):void {
		}
	}
}