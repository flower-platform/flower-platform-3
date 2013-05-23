package com.crispico.flower.flexdiagram.toolbar {
	
	import flash.events.MouseEvent;
	
	/**
	 * ToolbarButton with no Tool assigned. 
	 * <p>
	 * The Toolbar will call "startJob()" on this kind of buttons only if "canStart()" returns <code>true</code>;
	 * Meaning that this button finished execution.
	 * <p>
	 * Subclasses must override "executeAction()" to implement the work done by this button
	 * and make sure they call endJob() after the button has finished work.
	 * 
	 * @author Luiza
	 * @author Cristi
	 * @flowerModelElementId _yTihEDUSEeCTrKdImkKvZg
	 */ 
	public class StandAloneToolbarButton extends ToolbarButton {
		
		/**
		 * A StandAloneToolbarButton can't be locked.
		 */ 
		override public function setLocked(locked:Boolean):void {
			// do nothing
		}
		
		/**
		 *  
		 * Subclasses should write their particular actions here.
		 * @flowerModelElementId _yTk9UzUSEeCTrKdImkKvZg
		 */
		public function executeAction():void {
			throw "StandAloneToolbarButtons must implement executeAction()";
		}
		
	}
}