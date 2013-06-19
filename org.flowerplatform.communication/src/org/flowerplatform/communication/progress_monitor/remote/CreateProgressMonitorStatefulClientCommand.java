package org.flowerplatform.communication.progress_monitor.remote;

import org.flowerplatform.communication.command.AbstractClientCommand;

/**
 * Forces client to create and register a ProgressMonitorStatefulClient 
 * having the given {@link #statefulClientId} and {@link #title}.
 * @author Sorin
 */
public class CreateProgressMonitorStatefulClientCommand extends AbstractClientCommand {
	
	public String title;
	
	public String statefulClientId;

	/**
	 * If <code>true</code>, a cancel button will be displayed on client side 
	 * to allow cancellation.
	 * 
	 * @author Cristina
	 */
	public boolean allowCancellation;
	
	public CreateProgressMonitorStatefulClientCommand(String title, String statefulClientId, boolean allowCancellation) {
		this.title = title;
		this.statefulClientId = statefulClientId;
		this.allowCancellation = allowCancellation;
	}
}
