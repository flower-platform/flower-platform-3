package org.flowerplatform.web.git.staging.communication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.dircache.DirCacheIterator;
import org.eclipse.jgit.events.IndexChangedEvent;
import org.eclipse.jgit.events.IndexChangedListener;
import org.eclipse.jgit.events.ListenerHandle;
import org.eclipse.jgit.events.RefsChangedEvent;
import org.eclipse.jgit.events.RefsChangedListener;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.IndexDiff;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.InterIndexDiffFilter;
import org.eclipse.jgit.treewalk.filter.PathFilterGroup;

/**
 * @author Cristina
 */
public class GitStagingData {

//	private Repository repository;
//	
//	private IndexDiffData indexDiff;
//	
//	private String repositoryLocation;
//	
//	private DirCache lastIndex;
//	
//	private ListenerHandle indexChangedListenerHandle;	
//	private ListenerHandle refsChangedListenerHandle;
//	private IResourceChangeListener resourceChangeListener;
//		
//	public GitStagingData() {
//	}
//
//	public Repository getRepository() {
//		return repository;
//	}
//
//	public void setRepository(Repository repository) {
//		if (this.repository != null && this.repository.getDirectory().equals(repository.getDirectory())) {
//			return;
//		}
//		if (indexChangedListenerHandle != null) {
//			indexChangedListenerHandle.remove();
//		}
//		if (refsChangedListenerHandle != null) {
//			refsChangedListenerHandle.remove();
//		}
//		if (resourceChangeListener != null) {
//			ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceChangeListener);
//		}
//		this.repository = repository;	
//		this.indexDiff = null;
//		
//		indexChangedListenerHandle = this.repository.getListenerList().addIndexChangedListener(
//			new IndexChangedListener() {
//				public void onIndexChanged(IndexChangedEvent event) {
//					refreshIndexDelta();
//				}
//		});
//		
//		refsChangedListenerHandle = this.repository.getListenerList().addRefsChangedListener(
//			new RefsChangedListener() {
//				public void onRefsChanged(RefsChangedEvent event) {
//					refresh(null);
//				}
//		});
//		
//		resourceChangeListener = new IResourceChangeListener() {			
//			public void resourceChanged(IResourceChangeEvent event) {
//				GitResourceDeltaVisitor visitor = new GitResourceDeltaVisitor(GitStagingData.this.repository);
//				try {
//					event.getDelta().accept(visitor);
//				} catch (CoreException e) {					
//					return;
//				}
//				Collection<String> filesToUpdate = visitor.getFilesToUpdate();
//				if (visitor.getGitIgnoreChanged()) {
//					refresh(null);
//				} else if (indexDiff == null) {
//					refresh(null);
//				} else if (!filesToUpdate.isEmpty()) {
//					if (filesToUpdate.size() < 1000) {
//						refresh(Arrays.asList(filesToUpdate.toArray(new String[filesToUpdate.size()])));
//					} else {
//						// Calculate new IndexDiff if too many resources changed
//						// This happens e.g. when a project is opened
//						refresh(null);
//					}
//				}
//			}
//		};
//		
//		ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListener, IResourceChangeEvent.POST_CHANGE);
//		
//		if (!this.repository.isBare()) {
//			try {
//				lastIndex = repository.readDirCache();
//			} catch (Exception e) {
//			}
//		}
//	}
//
//	public IndexDiffData getIndexDiff() {
//		if (indexDiff == null) {
//			refresh(null);
//		}
//		return indexDiff;
//	}
//	
//	/**
//	 * Refreshes all resources that changed in the index since the last call to
//	 * this method. This is suitable for incremental updates on index changed
//	 * events
//	 *
//	 * For bare repositories this does nothing.
//	 */
//	private void refreshIndexDelta() {
//		if (repository.isBare()) {
//			return;
//		}
//
//		try {
//			DirCache currentIndex = repository.readDirCache();
//			DirCache oldIndex = lastIndex;
//
//			lastIndex = currentIndex;
//
//			if (oldIndex == null) {
//				refresh(null); // full refresh in case we have no data to compare.
//				return;
//			}
//
//			Set<String> paths = new TreeSet<String>();
//			TreeWalk walk = new TreeWalk(repository);
//
//			try {
//				walk.addTree(new DirCacheIterator(oldIndex));
//				walk.addTree(new DirCacheIterator(currentIndex));
//				walk.setFilter(new InterIndexDiffFilter());
//
//				while (walk.next()) {
//					if (walk.isSubtree()) {
//						walk.enterSubtree();
//					} else {
//						paths.add(walk.getPathString());
//					}
//				}
//			} finally {
//				walk.release();
//			}
//
//			if (!paths.isEmpty()) {
//				List<String> files = calcTreeFilterPaths(paths);
//				refresh(files);
//			}
//
//		} catch (IOException ex) {
//			refresh(null);
//		}
//	}
//	
//	private void refresh(List<String> files) {
//		try {
//			IndexDiff indexDiff = new IndexDiff(repository, Constants.HEAD, IteratorService.createInitialIterator(repository));
//			if (files != null) {
//				indexDiff.setFilter(PathFilterGroup.createFromStrings(files));
//			}
//			indexDiff.diff();	
//			
//			if (files != null) {
//				this.indexDiff = new IndexDiffData(getIndexDiff(), files, indexDiff);
//			} else {
//				this.indexDiff = new IndexDiffData(indexDiff);
//			}
//		} catch (IOException e) {
//		}
//	}
//	
//	/**
//	 * In the case when a file to update was in a folder that was untracked
//	 * before, we need to visit more that just the file. E.g. when the file is
//	 * now tracked, the folder is no longer untracked but maybe some sub folders
//	 * have become newly untracked.
//	 */
//	private List<String> calcTreeFilterPaths(Collection<String> filesToUpdate) {
//		List<String> paths = new ArrayList<String>();
//		for (String fileToUpdate : filesToUpdate) {
//			if (indexDiff != null) {
//				for (String untrackedFolder : indexDiff.getUntrackedFolders()) {
//					if (fileToUpdate.startsWith(untrackedFolder)) {
//						paths.add(untrackedFolder);
//					}
//				}
//			}
//			paths.add(fileToUpdate);
//		}
//		return paths;
//	}	
//	
//	public String getRepositoryLocation() {
//		return repositoryLocation;
//	}
//
//	public GitStagingData(String repositoryLocation) {	
//		this();
//		this.repositoryLocation = repositoryLocation;
//	}		
	
}
