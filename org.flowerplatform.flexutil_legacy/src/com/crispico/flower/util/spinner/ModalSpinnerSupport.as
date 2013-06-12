package com.crispico.flower.util.spinner {
	
	/**
	 * Should be implemented by <code>Container</code> classes that don't have
	 * absolute layout, and that want to be able to host a <code>ModalSpinner</code>.
	 * 
	 * <p>
	 * Responsabilities:
	 * <ul>
	 * 	<li>have a backing field for the <code>modalSpinner</code> property
	 * 	<li>should override <code>updateDisplayList</code> and call <code>setActualSize()</code>
	 * 		for the <code>modalSpinner</code> property (if present). Other logic may be executed
	 * 		there as well (e.g. reposition the component to not cover the tile bar).
	 * <ul>
	 *
	 * @author Cristi
	 */
	public interface ModalSpinnerSupport {
		
		function get modalSpinner():ModalSpinner;
		
		function set modalSpinner(value:ModalSpinner):void;
	}
}