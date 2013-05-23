package com.crispico.flower.flexdiagram.util.tabNavigator {
	import flexlib.containers.SuperTabNavigator;
	import flexlib.controls.SuperTabBar;
		
	/**
	 * This graphical component knows how to show tabs with icons that were set using their URLs.
	 * The URLs can be set to each child, when it is added to tabNavigator, by using the "iconURL" style.
	 * If a child has set this style and also the embed icon, the style has bigger priority.
	 * 
	 * @author Cristina
	 * @flowerModelElementId _KP2xffDREd-yKrmCiYZiog
	 */
	public class FlowerSuperTabNavigator extends SuperTabNavigator {

	    /**	 
	     * The tab navigator will work with a <code>CustomSuperTabBar</code> 
	     * instead of a <code>SuperTabBar</code>
	     * @flowerModelElementId _KP2xgPDREd-yKrmCiYZiog
	     */
	    override protected function createTabBar():SuperTabBar {
	      return new FlowerSuperTabBar();
	    }
	}
	
}