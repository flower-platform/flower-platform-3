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
package org.flowerplatform.flexutil.popup {
	import mx.collections.IList;
	
	/**
	 * Can wrap a single IViewContent (web), or multiple <code>IViewContent</code>s (mobile, where 2 VCs are visible and 
	 * others (e.g. editors) may exist, but are currently hidden).
	 * 
	 * <p>
	 * The VH should detect when the active VH is changed. 
	 * When such a thing happens => invoke <code>setActivePopupContent()</code>. E.g.: 
	 * 
	 * <ul>
	 * <li>on first display</li>
	 * <li>if there are multiple VCs (e.g. mobile) => on focus in or when the current VC is changed 
	 * (e.g. editor switch, open resource from tree, switch to right view)</li>
	 * <li>if there are several <code>IViewHost</code> on screen, catch the corresponding "activated" event 
	 * (e.g. for web/Workbench)</li>
	 * </ul>
	 * 
	 * @author Cristian Spiescu
	 */
	public interface IPopupHost {
		
		/**
		 * Should not be called directly. This method is
		 * invoked by the <code>ISelectionManager</code>, when the selection changes, or by when
		 * the view is activated (including during init).
		 * 
		 * <p>
		 * This method should get the selection (and probably actions as well), and should return
		 * the selection.
		 * 
		 * <p>
		 * It may probably cache the selection and actions (needed by the context menu framework for web/mobile).
		 */
		function selectionChanged():IList;	

		/**
		 * Should hold the VC (if there is only one), or the active
		 * one (if there are multiple VCs).
		 */
		function get activePopupContent():IPopupContent;
		
		/**
		 * Besides setting the corresponding attribute, should invoke 
		 * <code>FlexUtilGlobals.getInstance().selectionManager.viewContentActivated()</code>. If the caller
		 * is a focus in style handler, then <code>viaFocusIn</code> should be set to <code>true</code>.
		 */
		function setActivePopupContent(value:IPopupContent, viaFocusIn:Boolean = false):void;
		
		function setLabel(value:String):void;
		function setIcon(value:Object):void;
		
		function displayCloseButton(value:Boolean):void;
		function addToControlBar(value:Object):void;
		
		/**
		 * @author Cristina Constantinescu
		 */ 
		function showSpinner(text:String):void;
		function hideSpinner():void;
	}
}