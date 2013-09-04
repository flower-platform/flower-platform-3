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
package org.flowerplatform.web.svn.remote;

import org.flowerplatform.communication.command.AbstractClientCommand;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;

/**
 * @author Cristina Necula
 */

public class OpenSvnCredentialsWindowClientCommand extends AbstractClientCommand{

private boolean logging;
	
	private String repositoryURI;	
	
	// FOR LOGIN
	private InvokeServiceMethodServerCommand command;

	// FOR CREDENTIALS
	private String user;
		
	public String getRepositoryURI() {
		return repositoryURI;
	}

	public void setRepositoryURI(String repositoryURI) {
		this.repositoryURI = repositoryURI;
	}
			
	public InvokeServiceMethodServerCommand getCommand() {
		return command;
	}

	public void setCommand(InvokeServiceMethodServerCommand command) {
		this.command = command;
	}
	
	public boolean isLogging() {
		return logging;
	}

	public void setLogging(boolean logging) {
		this.logging = logging;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public OpenSvnCredentialsWindowClientCommand(String repositoryURI, String user) {
		this.logging = false;
		this.repositoryURI = repositoryURI;
		this.user = user;
	}

	public OpenSvnCredentialsWindowClientCommand(String repositoryURI, InvokeServiceMethodServerCommand command) {
		this.logging = true;
		this.repositoryURI = repositoryURI;
		this.command = command;
	}
	
}
