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
package org.flowerplatform.web.security.sandbox;

/**
 * This exception is thrown by our custom security policy to stop the protection
 * domain to check if its permissions implies the requested permission. This
 * action is performed when a permission should be denied.
 * 
 * @author Florin
 * 
 * 
 */
public class FlowerWebSecurityException extends SecurityException {

	private static final long serialVersionUID = 1L;

	public FlowerWebSecurityException(String message) {
		super(message);
	}
}