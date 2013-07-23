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
package org.flowerplatform.communication.command;


/**
 * This method purpose is to send a simple message to flex.
 * The message can contain a title, a body and an icon. 
 * 
 * @author Cristina
 * @author Sorin
 */
public class DisplaySimpleMessageClientCommand extends AbstractClientCommand {
	
	public static final int ICON_ERROR = 1;
	
	public static final int ICON_INFORMATION = 2;
	
	public static final int ICON_WARNING = 8;
	
	private String title;
	
	private String message;
	
	private int icon;
	
	private String details;

	public DisplaySimpleMessageClientCommand(String title, String message, int icon) {
		this(title, message, null, icon);
	}
	
	public DisplaySimpleMessageClientCommand(String title, String message, String details, int icon) {
		this.title = title;
		this.message = message;
		this.icon = icon;
		this.details = details; 
	}

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

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}
	
	public String getDetails() {
		return details;
	}
	
	public void setDetails(String details) {
		this.details = details;
	}
	
	@Override
	public String toString() {
		return super.toString() + String.format(" title = %s message = %s details = %s icon = %s", title, message, details, icon);
	}
	
}