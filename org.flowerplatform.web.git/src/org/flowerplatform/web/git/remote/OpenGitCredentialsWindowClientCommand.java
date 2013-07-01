package org.flowerplatform.web.git.remote;

import org.flowerplatform.communication.command.AbstractClientCommand;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;

/**
 * @author Cristina Constantinescu
 */
public class OpenGitCredentialsWindowClientCommand extends AbstractClientCommand {
	
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
	
	public OpenGitCredentialsWindowClientCommand(String repositoryURI, String user) {
		this.logging = false;
		this.repositoryURI = repositoryURI;
		this.user = user;
	}

	public OpenGitCredentialsWindowClientCommand(String repositoryURI, InvokeServiceMethodServerCommand command) {
		this.logging = true;
		this.repositoryURI = repositoryURI;
		this.command = command;
	}	
	
}
