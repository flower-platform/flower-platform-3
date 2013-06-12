package org.flowerplatform.communication;

import java.security.Principal;

import javax.security.auth.Subject;

public interface IPrincipal extends Principal {
	public Subject getSubject();
}
