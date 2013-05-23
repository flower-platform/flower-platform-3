package com.crispico.flower.flexdiagram.toolbar {
	
	/**
	 * This interface must be implemented by every
	 * class that contributes to the 
	 * <code>Toolbar</code>.
	 * 
	 * @flowerModelElementId _wOFpsMK5Ed6-yKDFMO4rZg
	 */
	public interface IToolProvider {
		
		/**
		 * Called when the received Toolbar needs it's 
		 * content refilled.
		 *
		 * The received toolbar will be empty. 
		 */
		function fillToolbar(toolbar:Toolbar, activeDiagramType:String = null):void;
		
	}
}