package org.flowerplatform.web.security.service;

import java.util.Observable;

/**
 * Used to send notification about entities.
 * 
 * @author Florin
 */
public class ServiceObservable {

	public static final String DELETE = "delete";
	
	/**
	 * Used to notify about entity updates.
	 * 
	 * @author Mariana
	 */
	public static final String UPDATE = "update";
	
	protected Observable observable = new Observable() {
		
		/**
		 * Parameter arg should be a list containing the action performed (like DELETE)
		 * and other relevant objects.
		 */
		public void notifyObservers(Object arg) {
			setChanged();
			super.notifyObservers(arg);
		}
	};
	
	public Observable getObservable() {
		return observable;
	}
}
