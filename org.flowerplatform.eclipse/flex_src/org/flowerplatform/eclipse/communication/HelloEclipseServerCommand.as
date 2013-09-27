package org.flowerplatform.eclipse.communication
{
	import org.flowerplatform.communication.command.AbstractClientCommand;
	import org.flowerplatform.communication.command.HelloServerCommand;
	import org.flowerplatform.eclipse.EclipsePlugin;
	
	[RemoteClass(alias="org.flowerplatform.eclipse.communication.HelloEclipseServerCommand")]
	public class HelloEclipseServerCommand extends HelloServerCommand
	{
		
	}
}