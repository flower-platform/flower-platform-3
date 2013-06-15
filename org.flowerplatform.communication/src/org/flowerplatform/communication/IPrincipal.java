package org.flowerplatform.communication;

import java.security.Principal;

import javax.security.auth.Subject;

public interface IPrincipal extends Principal {
	public ICurrentUserLoggedIn getUser(); 
	public long getUserId();
	public void clearCachedUser();
	public Subject getSubject();
}
