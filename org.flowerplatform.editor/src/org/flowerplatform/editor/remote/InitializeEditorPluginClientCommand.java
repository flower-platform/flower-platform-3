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
