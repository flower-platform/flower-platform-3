package org.flowerplatform.web.git.command.client;

import org.flowerplatform.communication.command.AbstractClientCommand;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;

/**
 * @author Cristina Constantinescu
 */
public class OpenGitCredentialsWindowClientCommand extends AbstractClientCommand {

	private static final int LOGIN_STATE = 0;
	
	private static final int CREDENTIALS_STATE = 1;
	
	private int state;
	
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

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public OpenGitCredentialsWindowClientCommand(String repositoryURI, String user) {
		this.state = CREDENTIALS_STATE;
		this.repositoryURI = repositoryURI;
		this.user = user;
	}

	public OpenGitCredentialsWindowClientCommand(String repositoryURI, InvokeServiceMethodServerCommand command) {
		this.state = LOGIN_STATE;
		this.repositoryURI = repositoryURI;
		this.command = command;
	}	
	
}
