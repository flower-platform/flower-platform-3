package org.flowerplatform.flexutil.layout {
	import mx.core.UIComponent;
	
	/**
	 * Interface that needs to be implemented in order to provide graphical support for <code>ViewLayoutData</code>.
	 * Objects that implements this interface must provide functionality for the following methods:
	 * <ul>
	 * 	<li> getId() - gets the id for view provider. Needed when creating views.
	 * 	<li> getView() - creates and returns a graphical component corresponding to a <code>ViewLayoutData</code>;
	 * 					 this graphical component will be inserted in a tab navigator. 
	 * 	<li> geTitle() - returns the title for a <code>ViewLayoutData</code>
	 *  <li> geIcon() - returns the icon for a <code>ViewLayoutData</code>
	 * 	<li> getTabCustomizer() - returns the tab customizer for a <code>ViewLayoutData</code>
	 * 	<li> getViewPopupWindow() - returns the popup window instance to be used when opening a view in a window.
	 * </ul>
	 * 
	 * @author Sorin
	 * @author Cristina
	 * @flowerModelElementId _62RBACuwEeG6vrEjfFek0Q
	 */
	public interface IViewProvider {
		
		
		function getId():String;
		
		/**		  
		 * @flowerModelElementId _BhLysVDFEeGsUPSh9UfXpw
		 */		 
		function createView(viewLayoutData:ViewLayoutData):UIComponent;
		 
		/**
		 * @flowerModelElementId _BhLytVDFEeGsUPSh9UfXpw
		 */
		function getTitle(viewLayoutData:ViewLayoutData = null):String;
		 
		/**
		 * @flowerModelElementId _BhMZwlDFEeGsUPSh9UfXpw
		 */
		function getIcon(viewLayoutData:ViewLayoutData = null):Object;
		 
		 /**
		  * @flowerModelElementId _GEq74OCXEeGdYcOEhSk3ug
		  */
		function getTabCustomizer(viewLayoutData:ViewLayoutData):Object;
		
		function getViewPopupWindow(viewLayoutData:ViewLayoutData):UIComponent;
		 
	}
	
}