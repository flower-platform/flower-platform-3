package org.flowerplatform.web.security.sandbox;

/**
 * This exception is thrown by our custom security policy to stop the protection
 * domain to check if its permissions implies the requested permission. This
 * action is performed when a permission should be denied.
 * 
 * @author Florin
 * 
 * @flowerModelElementId _f1NbgGnXEeGiEKNiPvCvPw
 */
public class FlowerWebSecurityException extends SecurityException {

	private static final long serialVersionUID = 1L;

	public FlowerWebSecurityException(String message) {
		super(message);
	}
}
