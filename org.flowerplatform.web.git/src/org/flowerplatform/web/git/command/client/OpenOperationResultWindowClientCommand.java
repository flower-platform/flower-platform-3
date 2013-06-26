package org.flowerplatform.web.git.command.client;

import org.flowerplatform.communication.command.AbstractClientCommand;

/**
 * @author Cristina
 */
public class OpenOperationResultWindowClientCommand extends AbstractClientCommand {

	private String title;
	
	private String message;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public OpenOperationResultWindowClientCommand(String title, String message) {		
		this.title = title;
		this.message = message;
	}	
	
}
