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