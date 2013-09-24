package org.flowerplatform.web.svn.remote;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.web.svn.SvnPlugin;
import org.flowerplatform.web.svn.history.AffectedPathEntry;
import org.flowerplatform.web.svn.history.HistoryEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tigris.subversion.subclipse.core.ISVNRemoteFolder;
import org.tigris.subversion.subclipse.core.ISVNRemoteResource;
import org.tigris.subversion.subclipse.core.ISVNRepositoryLocation;
import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.SVNProviderPlugin;
import org.tigris.subversion.subclipse.core.commands.GetLogsCommand;
import org.tigris.subversion.subclipse.core.history.ILogEntry;
import org.tigris.subversion.subclipse.core.history.LogEntryChangePath;
import org.tigris.subversion.subclipse.core.repo.SVNRepositoryLocation;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.ISVNInfo;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNInfoUnversioned;
import org.tigris.subversion.svnclientadapter.SVNRevision;

/**
 * @author Victor Badila
 */
public class SvnHistoryService {
	
	public static final String SERVICE_ID = "svnHistoryService";
	
	private static Logger logger = LoggerFactory.getLogger(SvnHistoryService.class);
	
	/**
	 * Gets a list of history entries for corresponding {@link ISVNRemoteResource remote resource}
	 * starting from <code>revisionStart</code> and ending when the number of entries (<code>nbEntries</code>) is achieved. <br>
	 * If no remote resource is found, returns <code>null</code>
	 * <p>
	 * 
	 * The {@link GetLogsCommand} is used to get entries from SVN history.
	 * 
	 * @param context - used to have access to the id registry
	 * @param path
	 * @param revisionStart - revision number from where the searching starts; 
	 * 						if <code>-1</code> then the head revision is taken in consideration. 
	 * @param nbEntries - the number of entries to return
	 * 					if <code>-1</code> then all remaining entries are returned.
	 * 
	 */
	public List<HistoryEntry> getEntries(ServiceInvocationContext context, Object path, long revisionStart, long nbEntries, Boolean appendAffectedPathsInfo) {	
		// get remote resource		
		ISVNRemoteResource remoteResource = getRemoteResourceFromPath(context, path);		
		if (remoteResource == null) {
			return null;
		}
		// run command		
		GetLogsCommand logCmd = new GetLogsCommand(remoteResource, remoteResource.getRevision(), revisionStart == -1 ? SVNRevision.HEAD : new SVNRevision.Number(revisionStart - 1), 
												   new SVNRevision.Number(0), false, nbEntries == -1 ? 0 : nbEntries, null, false);
		try {
			logCmd.run(new NullProgressMonitor());
		} catch (SVNException e) { // swallowed	
			return Collections.emptyList();
		}
		// convert to client data
		List<HistoryEntry> historyEntries = new ArrayList<HistoryEntry>();
		for (ILogEntry entry : logCmd.getLogEntries()) {
			historyEntries.add(convertToHistoryEntry(remoteResource, entry, appendAffectedPathsInfo));
		}		
		return historyEntries;
		
	}	
	
	/**
	 * Gets the corresponding {@link ISVNRemoteResource remote resource} starting from an object path.
	 * The object path can correspond to:
	 * <ul>
	 * 	<li> an {@link Pair}, when accessed tree node is of svn project file type , the member {@link File} will be returned}
	 * 	<li> an {@link ISVNRepositoryLocation}, the corresponding {@link ISVNRemoteFolder} will be returned
	 * </ul>
	 * If no remote resource found, returns <code>null</code>.
	 */
	private ISVNRemoteResource getRemoteResourceFromPath(ServiceInvocationContext context, Object path) {
		CommunicationChannel channel = (CommunicationChannel) context.getCommunicationChannel();
		Object object = GenericTreeStatefulService.getNodeByPathFor((List<PathFragment>) path, null);			
		if (object instanceof ISVNRemoteResource) {
			return (ISVNRemoteResource) object;
		}		
		if (object instanceof Pair) {
			File correspondingFile = ((Pair<File, String>) object).a;
			try {				
				ISVNClientAdapter myClientAdapter;
				myClientAdapter = SVNProviderPlugin.getPlugin().getSVNClient();
				ISVNInfo svnInfo = myClientAdapter.getInfo(correspondingFile);
				if (svnInfo instanceof SVNInfoUnversioned) {
					channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand("Error", SvnPlugin.getInstance().getMessage("svnHistoryService.errorMessage.operationNotPossibleForSelection"), DisplaySimpleMessageClientCommand.ICON_ERROR));
					return null;
				}
				String svnUrl =  svnInfo.getUrl().toString();				
				SVNRepositoryLocation repository = SVNRepositoryLocation.fromString(svnUrl);
				object = repository;				
			} catch (SVNException | SVNClientException e) {					
				logger.debug(CommonPlugin.getInstance().getMessage("error"), e);
				channel.appendCommandToCurrentHttpResponse(new DisplaySimpleMessageClientCommand("Error", e.getMessage(), DisplaySimpleMessageClientCommand.ICON_ERROR));
				return null;				
			}			
		}		
		if (object instanceof ISVNRepositoryLocation) {			
	        IAdaptable a = (IAdaptable) object;
	        Object adapter = a.getAdapter(ISVNRemoteFolder.class);
	        return (ISVNRemoteFolder)adapter;
	    }      
		return null;
	}
	
	private HistoryEntry convertToHistoryEntry(ISVNRemoteResource remoteResource, ILogEntry logEntry, Boolean appendAffectedPathsInfo) {
		HistoryEntry historyEntry = new HistoryEntry();
		String revision = logEntry.getRevision().toString();		
		historyEntry.setRevision(revision);		
		historyEntry.setDate(logEntry.getDate());				
		if(logEntry.getAuthor() == null) {
			historyEntry.setAuthor(SvnPlugin.getInstance().getMessage("svnHistory.returnedData.noauthor"));
		} else {
			historyEntry.setAuthor(logEntry.getAuthor());
		}		
		String comment = logEntry.getComment();
		if (comment == null) {
			historyEntry.setComment("");
		} else {
			historyEntry.setComment(comment.replaceAll("\r", " ").replaceAll("\n", " "));
		}		
		if (appendAffectedPathsInfo) {
			List<AffectedPathEntry> affectedPaths = new ArrayList<AffectedPathEntry>();
			for (LogEntryChangePath affectedPath : logEntry.getLogEntryChangePaths()) {
				affectedPaths.add(convertToAffectedPathEntry(affectedPath));
			}
			historyEntry.setAffectedPathEntries(affectedPaths);			
		}		
		return historyEntry;
	}	

	/**
	 * Returns an {@link AffectedPathEntry} filled with data provided by a {@link LogEntryChangePath}.
	 * 
	 * @see #convertToHistoryEntry()
	 */
	private AffectedPathEntry convertToAffectedPathEntry(LogEntryChangePath logEntry) {		
		AffectedPathEntry affectedPathEntry = new AffectedPathEntry();
		affectedPathEntry.setAction("" + logEntry.getAction());
		affectedPathEntry.setAffectedPath(logEntry.getPath());
		
		if (logEntry.getCopySrcPath() != null) {
			affectedPathEntry.setDescription(
					SvnPlugin.getInstance().getMessage("svnHistory.returnedData.copiedFrom") + ' ' +
					logEntry.getCopySrcPath() + ' ' +
					logEntry.getCopySrcRevision().toString());
        }
		
		return affectedPathEntry;
	}
}