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
package org.flowerplatform.editor.remote;

import java.util.List;

import org.flowerplatform.communication.command.AbstractClientCommand;
import org.flowerplatform.editor.EditorPlugin;

public class InitializeEditorPluginClientCommand extends AbstractClientCommand {

	private List<ContentTypeDescriptor> contentTypeDescriptors;

	private int lockLeaseSeconds = 10;

	public List<ContentTypeDescriptor> getContentTypeDescriptors() {
		return contentTypeDescriptors;
	}

	public void setContentTypeDescriptors(List<ContentTypeDescriptor> contentTypeDescriptors) {
		this.contentTypeDescriptors = contentTypeDescriptors;
	}

	public int getLockLeaseSeconds() {
		return lockLeaseSeconds;
	}

	public void setLockLeaseSeconds(int lockLeaseSeconds) {
		this.lockLeaseSeconds = lockLeaseSeconds;
	}

	public InitializeEditorPluginClientCommand() {
		super();
		contentTypeDescriptors = EditorPlugin.getInstance().getContentTypeDescriptorsList();
	}

}