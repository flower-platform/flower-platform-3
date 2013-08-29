package org.flowerplatform.web.svn.operation;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.tigris.subversion.subclipse.core.client.OperationProgressNotifyListener; // TODO merge copiata toata clasa pe care o extinde intrucat aceasta din urma importa doar librarii din svnclientadapter si nu foloseste nimic .subclipse.core sau .ui
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.SVNNodeKind;
import org.tigris.subversion.svnclientadapter.SVNProgressEvent;

/**
 * Sends progress monitor notifications received from native code to current client.
 * 
 * @author Victor Badila
 */
public class SVNOperationProgressNotifyListener extends OperationProgressNotifyListener {

	private CommunicationChannel channel;
	
	public SVNOperationProgressNotifyListener(IProgressMonitor monitor,	ISVNClientAdapter svnClient, CommunicationChannel channel) {
		super(monitor, svnClient);
		this.channel = channel;
	}

	@Override
	public void onNotify(File path, SVNNodeKind kind) {
		if (CommunicationPlugin.getInstance().tlCurrentChannel.get().equals(channel)) {
			super.onNotify(path, kind);
		}
	}

	@Override
	public void logCompleted(String message) {
		if (CommunicationPlugin.getInstance().tlCurrentChannel.get().equals(channel)) {
			super.logCompleted(message);
		}
	}

	@Override
	public void onProgress(SVNProgressEvent progressEvent) {
		if (CommunicationPlugin.getInstance().tlCurrentChannel.get().equals(channel)) {
			super.onProgress(progressEvent);
		}
	}	

	
}
