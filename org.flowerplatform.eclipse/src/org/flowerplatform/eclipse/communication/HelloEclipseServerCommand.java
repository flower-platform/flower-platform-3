package org.flowerplatform.eclipse.communication;

import org.flowerplatform.communication.command.HelloServerCommand;
import org.flowerplatform.eclipse.EclipsePlugin;

public class HelloEclipseServerCommand extends HelloServerCommand {

	public HelloEclipseServerCommand() {
		super();
	}
	
	@Override
	public void executeCommand() {
		super.executeCommand();
		for (IEclipseChannelListener lis : EclipsePlugin.getInstance().getListeners()) {
			lis.newClientInitializationsSent(this.getCommunicationChannel());
		}
	}

}
