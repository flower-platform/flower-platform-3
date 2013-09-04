package org.flowerplatform.web.svn.operation;

import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.channel.ICommunicationChannelLifecycleListener;
import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.SVNProviderPlugin;
import org.tigris.subversion.svnclientadapter.ISVNNotifyListener;
import org.tigris.subversion.svnclientadapter.ISVNProgressListener;
import org.tigris.subversion.svnclientadapter.SVNNodeKind;
import org.tigris.subversion.svnclientadapter.SVNProgressEvent;

/**
 * Manager for {@link ISVNNotifyListener}s and {@link ISVNProgressListener}s 
 * used by our SVN operations.
 * 
 * <p>
 * This was implemented for {@link ISVNProgressListener} also 
 * because the JHL accepts only one instance of it and we cannot use it
 * in a multithreading environment.
 *  
 * @author Victor Badila
 */
public class SvnOperationManager implements ISVNNotifyListener, ISVNProgressListener, ICommunicationChannelLifecycleListener {
	
	protected Set<SvnOperationNotifyListener> notifyListeners = new CopyOnWriteArraySet<SvnOperationNotifyListener>();
			
	protected Set<ISVNProgressListener> progressListeners = new CopyOnWriteArraySet<ISVNProgressListener>();
	
	public static SvnOperationManager INSTANCE = new SvnOperationManager();
		
	private SvnOperationManager() {
		try {
			SVNProviderPlugin.getPlugin().getSVNClient().addNotifyListener(this);
		} catch (SVNException e) {		
		}
	}

	public void add(SvnOperationNotifyListener listener) {
		notifyListeners.add(listener);
	}

	public void remove(SvnOperationNotifyListener listener) {
		notifyListeners.remove(listener);
	}
	
	public void add(ISVNProgressListener listener) {
		progressListeners.add(listener);
	}

	public void remove(ISVNProgressListener listener) {
		progressListeners.remove(listener);
	}
	
	public void onNotify(File path, SVNNodeKind kind) {
		for (Iterator<SvnOperationNotifyListener> it = notifyListeners.iterator(); it.hasNext();) {
			ISVNNotifyListener listener = (ISVNNotifyListener) it.next();
			listener.onNotify(path, kind);
		}
	}	

	public void logCompleted(String message) {
		for (Iterator<SvnOperationNotifyListener> it = notifyListeners.iterator(); it.hasNext();) {
			ISVNNotifyListener listener = (ISVNNotifyListener) it.next();
			listener.logCompleted(message);
		}
	}

	public void logMessage(String message) {
		for (Iterator<SvnOperationNotifyListener> it = notifyListeners.iterator(); it.hasNext();) {
			ISVNNotifyListener listener = (ISVNNotifyListener) it.next();
			listener.logMessage(message);
		}				
	}

	@Override
	public void communicationChannelDestroyed(CommunicationChannel webCommunicationChannel) {
		for (Iterator<SvnOperationNotifyListener> it = notifyListeners.iterator(); it.hasNext();) {
			SvnOperationNotifyListener listener = (SvnOperationNotifyListener) it.next();
			if (listener.getChannel().equals(webCommunicationChannel)) {
				it.remove();
			}
		}
	}
		    
	@Override
	public void onProgress(SVNProgressEvent progressEvent) {
		for (Iterator<ISVNProgressListener> it = progressListeners.iterator(); it.hasNext();) {
			ISVNProgressListener listener = (ISVNProgressListener) it.next();
			listener.onProgress(progressEvent);
		}
	}
	
	public void logCommandLine(String commandLine) {
	}

	public void logRevision(long revision, String path) {
	}
	
	public void logError(String message) {
	}
	
	public void setCommand(int command) {
	}

	@Override
	public void communicationChannelCreated(CommunicationChannel webCommunicationChannel) {
		// TODO Auto-generated method stub
		
	}
	
}

