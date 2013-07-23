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