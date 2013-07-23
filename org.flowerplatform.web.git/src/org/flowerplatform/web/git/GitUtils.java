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
package org.flowerplatform.web.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.jgit.api.GitCommand;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.errors.DetachedHeadException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.errors.TransportException;
import org.eclipse.jgit.internal.JGitText;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.RefUpdate.Result;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.lib.RepositoryCache.FileKey;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.TrackingRefUpdate;
import org.eclipse.jgit.util.FS;
import org.eclipse.osgi.framework.internal.core.FrameworkProperties;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.FlowerWebProperties;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.stateful_service.NamedLockPool;
import org.flowerplatform.web.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristina Constantienscu
 */
public class GitUtils {
	
	private static Logger logger = LoggerFactory.getLogger(GitUtils.class);
	
	public static final String MAIN_REPOSITORY = "main";
	public static final String WORKING_DIRECTORY_PREFIX = "wd_";
	
	public static final String GIT_REPOSITORIES_NAME = ".git-repositories";
	
	/**
	 * Flower Web Property.
	 * @see /META-INF/flower-web.properties
	 */
	public static final String GIT_INSTALL_DIR = "git.git-install-dir";
		
	/**
	 * Name of the command file used to create a virtual repository on Windows.
	 * @see /META-INF/git/git-new-workdir_win.cmd
	 */
	private static final String GIT_NEW_WORKDIR_WIN = "git-new-workdir_win.cmd";
	
	/**
	 * Name of the command file used to create a virtual repository on Linux.
	 * @see /META-INF/git/git-new-workdir_linux
	 */
	private static final String GIT_NEW_WORKDIR_LINUX = "git-new-workdir_linux.sh";
	
	static {
		FlowerWebProperties.AddProperty addProperty = new FlowerWebProperties.AddProperty(GIT_INSTALL_DIR, "") {			
			/**
			 * Verify if git.exe exists at given location.
			 */
			@Override			
			protected String validateProperty(String input) {				
				String git = CommonPlugin.getInstance().getFlowerWebProperties().getProperty(GIT_INSTALL_DIR) + "/cmd/git.exe";
				if (!new File(git).exists()) {
					return String.format("Git executable wasn't found at '%s'! Please verify '%s' property!", git, GIT_INSTALL_DIR);				
				}
				return null;
			}
		}.setInputFromFileMandatory(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0);
		
		CommonPlugin.getInstance().getFlowerWebProperties().addProperty(addProperty);
				
		// verify JavaVM version; it must be >= 1.7
		String jvmVersion = System.getProperty("java.vm.specification.version");
		if (jvmVersion.compareTo("1.7") < 0) {
			logger.error("Your current JVM version is {}. In order to use Git properly, the JVM version needs to be at least 1.7!", jvmVersion);		
		}	
		
		/*
		 * Each repository configures this property at creation:
		 * core.fileMode
		 * If false, the executable bit differences between the index and the working copy are ignored; 
		 * useful on broken filesystems like FAT. See git-update-index(1).
		 * 
		 * The default is true, except git-clone(1) or git-init(1) will probe and set core.fileMode false if appropriate when the repository is created.
		 * 
		 * We set it always to false, because we don't want to add "execute" permission on files.
		 */
		try {
			Class<?> fsPosixJava6 = Class.forName("org.eclipse.jgit.util.FS_POSIX_Java6");
			if (fsPosixJava6.isInstance(FS.DETECTED)) {
				Field field = fsPosixJava6.getDeclaredField("canExecute");
				field.setAccessible(true);

				// remove final modifier from field
			    Field modifiersField = Field.class.getDeclaredField("modifiers");
			    modifiersField.setAccessible(true);
			    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			    field.set(FS.DETECTED, null);
			    
				field = fsPosixJava6.getDeclaredField("setExecute");
				field.setAccessible(true);			
				modifiersField.setAccessible(true);
			    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			    field.set(FS.DETECTED, null);	
			    
			    field = FS.class.getDeclaredField("DETECTED");
			    modifiersField.setAccessible(true);
			    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			    field.set(FS.DETECTED, FS.detect());
			
			}
		} catch (Exception e) {
			throw new RuntimeException("Exception thrown while setting 'non-executable' state for git repository files!", e);
		}		
	}
	
	public File getGitRepositoriesFile(File orgFile) {
		return new File(CommonPlugin.getInstance().getWorkspaceRoot(), orgFile.getName() + "/" + GIT_REPOSITORIES_NAME + "/");
	}
	
	public Repository getRepository(File repoFile) {
		File gitDir = getGitDir(repoFile);
		if (gitDir != null) {
			try {				
				Repository repository = RepositoryCache.open(FileKey.exact(gitDir, FS.DETECTED));				
				return repository;
			}  catch (IOException e) {
				// TODO CC: log
			}
		}
		return null;		
	}
	
	public File getMainRepositoryFile(File repoFile, boolean createIfNecessary) {	
		File mainRepoFile = new File(repoFile, MAIN_REPOSITORY);
		if (createIfNecessary && !mainRepoFile.exists()) {
			if (!mainRepoFile.mkdirs()) {
				logger.error("Cannot create main repository file for {}", repoFile);
				return null;
			}
		}
		return mainRepoFile;
	}
	
	public Repository getMainRepository(File repoFile) {	
		return getRepository(getMainRepositoryFile(repoFile, false));
	}
	
	public File getGitDir(File file) {
		if (file.exists()) {
			while (file != null) {
				if (GIT_REPOSITORIES_NAME.equals(file.getName())) {
					return null;
				}
				if (RepositoryCache.FileKey.isGitRepository(file, FS.DETECTED)) {
					return file;
				} else if (RepositoryCache.FileKey.isGitRepository(new File(file, Constants.DOT_GIT), FS.DETECTED)) {
					return new File(file, Constants.DOT_GIT);
				}
				file = file.getParentFile();
			}
		}
		return null;
	}
	
	public boolean isRepository(File file) {
		return getGitDir(file) != null;
	}
	
	public String getRepositoryName(Repository repo) {
		return repo.getDirectory().getParentFile().getParentFile().getName();
	}
	
	/**
	 * Deletes the given file and its content.
	 * <p>
	 * If the file is a symbolic link, deletes only the file.
	 * Otherwise, deletes also the content from the original location
	 * (file.listFiles() returns the children files from original location).
	 */
	public void delete(File f) {	
		if (f.isDirectory() && !Files.isSymbolicLink(Paths.get(f.toURI()))) {		
			for (File c : f.listFiles()) {
				delete(c);
			}
		}
		f.delete();
	}
	
	public boolean isAuthentificationException(Exception e) {		
		TransportException cause = null;
		if (e.getCause() instanceof TransportException) {
			cause = (TransportException) e.getCause();
		} else if (e instanceof TransportException) {
			cause = (TransportException) e;
		}
		
		if (cause != null && (matchMessage(JGitText.get().notAuthorized, cause.getMessage()) ||
			cause.getMessage().endsWith("username must not be null.") ||
			cause.getMessage().endsWith("host must not be null."))) {
			return true;
		}		
		return false;
	}
	
	/**
	 * Creates a string message to be displayed on client side
	 * to inform the user about push result operation.
	 */
	public String handlePushResult(PushResult pushResult) {
		StringBuilder sb = new StringBuilder();		
		
		sb.append(pushResult.getMessages());
		sb.append("\n");
		
		for (RemoteRefUpdate rru : pushResult.getRemoteUpdates()) {
			String rm = rru.getRemoteName();
			RemoteRefUpdate.Status status = rru.getStatus();
			sb.append(rm);
			sb.append(" -> ");
			sb.append(status.name());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public String handleMergeResult(MergeResult mergeResult) {
		StringBuilder sb = new StringBuilder();		
		if (mergeResult == null) {
			return sb.toString();
		}
		sb.append("Status: ");
		sb.append(mergeResult.getMergeStatus());
		sb.append("\n");
		
		if (mergeResult.getMergedCommits() != null) {
			sb.append("\nMerged commits: ");
			sb.append("\n");
			for (ObjectId id : mergeResult.getMergedCommits()) {
				sb.append(id.getName());
				sb.append("\n");
			}
		}
		if (mergeResult.getCheckoutConflicts() != null) {
			sb.append("\nConflicts: ");
			sb.append("\n");
			for (String conflict : mergeResult.getCheckoutConflicts()) {
				sb.append(conflict);
				sb.append("\n");
			}
		}
				
		if (mergeResult.getFailingPaths() != null) {
			sb.append("\nFailing paths: ");
			sb.append("\n");
			for (String path : mergeResult.getFailingPaths().keySet()) {
				sb.append(path);
				sb.append(" -> ");
				sb.append(mergeResult.getFailingPaths().get(path).toString());
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	public boolean matchMessage(String pattern, String message) {
		if (message == null) {
			return false;
		}
		int argsNum = 0;
		for (int i = 0; i < pattern.length(); i++) {
			if (pattern.charAt(i) == '{') {
				argsNum++;
			}
		}
		Object[] args = new Object[argsNum];
		for (int i = 0; i < args.length; i++) {
			args[i] = ".*"; //$NON-NLS-1$
		}

		return Pattern.matches(".*" + MessageFormat.format(pattern, args) + ".*", message); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * Creates a string message to be displayed on client side
	 * to inform the user about fetch result operation.
	 */
	public String handleFetchResult(FetchResult fetchResult) {
		StringBuilder sb = new StringBuilder();		
		if (fetchResult.getTrackingRefUpdates().size() > 0) {
			// handle result
			for (TrackingRefUpdate updateRes : fetchResult.getTrackingRefUpdates()) {
				sb.append(updateRes.getRemoteName());
				sb.append(" -> ");
				sb.append(updateRes.getLocalName());			
				sb.append(" ");
				sb.append(updateRes.getOldObjectId() == null ? "" : updateRes.getOldObjectId().abbreviate(7).name());
				sb.append("..");
				sb.append(updateRes.getNewObjectId() == null ? "" : updateRes.getNewObjectId().abbreviate(7).name());
				sb.append(" ");
				Result res = updateRes.getResult();
				switch (res) {
					case NOT_ATTEMPTED :
					case NO_CHANGE :
					case NEW :
					case FORCED :
					case FAST_FORWARD :
					case RENAMED :
						sb.append("OK.");
						break;
					case REJECTED :
						sb.append("Fetch rejected, not a fast-forward.");					
					case REJECTED_CURRENT_BRANCH :
						sb.append("Rejected because trying to delete the current branch.");					
					default :
						sb.append(res.name());				
				}
				sb.append("\n");	
			}
		} else {
			sb.append("OK.");
		}
		return sb.toString();
	}
	
	public boolean findProjectFiles(final Collection<File> files, File directory, Set<String> visistedDirs) {
		if (directory == null)
			return false;

		if (directory.getName().equals(Constants.DOT_GIT) && FileKey.isGitRepository(directory, FS.DETECTED)) {
			return false;
		}
		File[] contents = directory.listFiles();
		if (contents == null || contents.length == 0) {
			return false;
		}
		Set<String> directoriesVisited;
		// Initialize recursion guard for recursive symbolic links
		if (visistedDirs == null) {
			directoriesVisited = new HashSet<String>();
			try {
				directoriesVisited.add(directory.getCanonicalPath());
			} catch (IOException exception) {
				return false;
			}
		} else {
			directoriesVisited = visistedDirs;
		}
		// first look for project description files
		String dotProject = IProjectDescription.DESCRIPTION_FILE_NAME;
		for (int i = 0; i < contents.length; i++) {
			File file = contents[i];
			if (file.isFile() && file.getName().equals(dotProject) && !files.contains(file)) {
				files.add(file);
			}
		}
		// recurse into sub-directories (even when project was found above, for nested projects)
		for (int i = 0; i < contents.length; i++) {
			// Skip non-directories
			if (!contents[i].isDirectory()) {
				continue;
			}
			// Skip .metadata folders
			if (contents[i].getName().equals(".metadata")) {
				continue;
			}
			try {
				String canonicalPath = contents[i].getCanonicalPath();
				if (!directoriesVisited.add(canonicalPath)) {
					// already been here --> do not recurse
					continue;
				}
			} catch (IOException exception) {
				return false;
			}
			findProjectFiles(files, contents[i], directoriesVisited);
		}
		return true;
	}
	
	public RevCommit getHeadCommit(Repository repository) {
		RevCommit headCommit = null;
		try {
			ObjectId parentId = repository.resolve(Constants.HEAD);
			if (parentId != null) {
				headCommit = new RevWalk(repository).parseCommit(parentId);
			}
		} catch (IOException e) {
			return null;
		}
		return headCommit;
	}
	
	@SuppressWarnings("restriction")
	public Object[] getFetchPushUpstreamDataRefSpecAndRemote(Repository repository) 
			throws InvalidConfigurationException, NoHeadException, DetachedHeadException {		
		
		String branchName;
		String fullBranch;
		try {
			fullBranch = repository.getFullBranch();
			if (fullBranch == null) {
				throw new NoHeadException(JGitText.get().pullOnRepoWithoutHEADCurrentlyNotSupported);
			}
			if (!fullBranch.startsWith(Constants.R_HEADS)) {
				// we can not pull if HEAD is detached and branch is not
				// specified explicitly
				throw new DetachedHeadException();
			}
			branchName = fullBranch.substring(Constants.R_HEADS.length());
		} catch (IOException e) {
			throw new JGitInternalException(JGitText.get().exceptionCaughtDuringExecutionOfPullCommand, e);
		}
		// get the configured remote for the currently checked out branch
		// stored in configuration key branch.<branch name>.remote
		Config repoConfig = repository.getConfig();
		String remote = repoConfig.getString(
				ConfigConstants.CONFIG_BRANCH_SECTION, branchName,
				ConfigConstants.CONFIG_KEY_REMOTE);
		if (remote == null) {
			// fall back to default remote
			remote = Constants.DEFAULT_REMOTE_NAME;
		}

		// get the name of the branch in the remote repository
		// stored in configuration key branch.<branch name>.merge
		String remoteBranchName = repoConfig.getString(
				ConfigConstants.CONFIG_BRANCH_SECTION, branchName,
				ConfigConstants.CONFIG_KEY_MERGE);

		if (remoteBranchName == null) {
			String missingKey = ConfigConstants.CONFIG_BRANCH_SECTION + "."
					+ branchName + "." + ConfigConstants.CONFIG_KEY_MERGE;
			throw new InvalidConfigurationException(MessageFormat.format(
					JGitText.get().missingConfigurationForKey, missingKey));
		}
		
        // check if the branch is configured for pull-rebase
        boolean doRebase = repoConfig.getBoolean(
                        ConfigConstants.CONFIG_BRANCH_SECTION, branchName,
                        ConfigConstants.CONFIG_KEY_REBASE, false);
          
		final boolean isRemote = !remote.equals("."); //$NON-NLS-1$
		String remoteUri;	
		if (isRemote) {
			remoteUri = repoConfig.getString(
					ConfigConstants.CONFIG_REMOTE_SECTION, remote,
					ConfigConstants.CONFIG_KEY_URL);
			if (remoteUri == null) {
				String missingKey = ConfigConstants.CONFIG_REMOTE_SECTION + "."
						+ remote + "." + ConfigConstants.CONFIG_KEY_URL;
				throw new InvalidConfigurationException(MessageFormat.format(
						JGitText.get().missingConfigurationForKey, missingKey));
			}

			return new Object[] {fullBranch, remoteBranchName, remoteUri, doRebase};			
		}
		return null;
	}
	
	public void listenForChanges(File file) throws IOException {
        Path path = file.toPath();
        if (file.isDirectory()) {
            WatchService ws = path.getFileSystem().newWatchService();
            path.register(ws, StandardWatchEventKinds.ENTRY_CREATE, 
              StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
            WatchKey watch = null;
            while (true) {
                System.out.println("Watching directory: " + file.getPath());
                try {
                    watch = ws.take();
                } catch (InterruptedException ex) {
                    System.err.println("Interrupted");
                }
                List<WatchEvent<?>> events = watch.pollEvents();
                if (!watch.reset()) {
                	break;
                }
                for (WatchEvent<?> event : events) {
                    Kind<Path> kind = (Kind<Path>) event.kind();
                    Path context = (Path) event.context();
                    if (kind.equals(StandardWatchEventKinds.OVERFLOW)) {
                        System.out.println("OVERFLOW");
                    } else if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {
                        System.out.println("Created: " + context.getFileName());
                    } else if (kind.equals(StandardWatchEventKinds.ENTRY_DELETE)) {
                        System.out.println("Deleted: " + context.getFileName());
                    } else if (kind.equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
                        System.out.println("Modified: " + context.getFileName());
                    }
                }
            }
        } else {
            System.err.println("Not a directory. Will exit.");
        }
    }
	
	private NamedLockPool namedLockPool = new NamedLockPool();
	
	/**
	 * This method must be used to set user configuration before running
	 * some GIT commands that uses it.
	 * 
	 * <p>
	 * A lock/unlock on repository is done before/after the command is executed
	 * because the configuration modifies the same file and this will not be
	 * thread safe any more.
	 */
	public Object runGitCommandInUserRepoConfig(Repository repo, GitCommand<?> command) throws Exception {
		namedLockPool.lock(repo.getDirectory().getPath());
		
		try {
			StoredConfig c = repo.getConfig();
			c.load();			
			User user = (User) CommunicationPlugin.tlCurrentPrincipal.get().getUser();
			
			c.setString(ConfigConstants.CONFIG_USER_SECTION, null, ConfigConstants.CONFIG_KEY_NAME, user.getName());
			c.setString(ConfigConstants.CONFIG_USER_SECTION, null, ConfigConstants.CONFIG_KEY_EMAIL, user.getEmail());
			
			c.save();
			
			return command.call();
		} catch (Exception e) {
			throw e;
		} finally {
			namedLockPool.unlock(repo.getDirectory().getPath());
		}
	}
	
	/**
	 * Executes the corresponding Windows/Linux script to create a virtual repository.
	 */
	@SuppressWarnings("restriction")
	public String run_git_workdir_cmd(String source, String destination) {		
		File file = null;
		try {
			String OS = System.getProperty("os.name").toLowerCase();			
			boolean isWindows = true;
			if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0) {
				isWindows = false;
			} else if (!(OS.indexOf("win") >= 0)) {			
				return "git-new-workdir command only supports format for Windows/Linux!";				
			}
			
			String cmdName = isWindows ? GIT_NEW_WORKDIR_WIN : GIT_NEW_WORKDIR_LINUX;			
			file = File.createTempFile("git", isWindows ? ".cmd" : ".sh", new File(FrameworkProperties.getProperty("flower.server.tmpdir")));			
			InputStream is = getClass().getClassLoader().getResourceAsStream("META-INF/git/" + cmdName);
			OutputStream out = new FileOutputStream(file);
			IOUtils.copy(is, out);
			
			is.close();
			out.close();
			
			if (!file.exists()) {
				return String.format("%s wasn't found at '%s'!", cmdName, file.getAbsolutePath());						
			}		
			
			file.setExecutable(true);
			
			List<String> cmd = new ArrayList<String>();
			cmd.add(file.getAbsolutePath());
			cmd.add(source);
			cmd.add(destination);
			if (isWindows) {
				String git = CommonPlugin.getInstance().getFlowerWebProperties().getProperty(GIT_INSTALL_DIR) + "/cmd/git.exe";
				if (!new File(git).exists()) {
					return String.format("Git executable wasn't found at '%s'! Please verify '%s' property!", git, GIT_INSTALL_DIR);				
				}
				cmd.add(git);
			}
			
	        Process proc = Runtime.getRuntime().exec(cmd.toArray(new String[cmd.size()]));
	            			
			if (logger.isDebugEnabled()) {
				// any error message?
				StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
				errorGobbler.start();
				// any output?
				StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");
				outputGobbler.start();
			}
							      
	        // any error???
	        int exitVal = proc.waitFor();
	        switch (exitVal) {
	        	case 0: return null; // OK
	        	case 1: return String.format("Usage: %s ^<repository^> ^<new_workdir^> %s[^<branch^>]", 
	        									cmdName, isWindows ? "^<git_exe_location^> " : "");
	        	case 2: return String.format("Directory not found: '%s'!", source);
	        	case 3: return String.format("Not a git repository: '%s'!", source);
	        	case 4: return String.format("'%s' is a bare repository!", source);
	        	case 5: return String.format("Destination directory '%s' already exists!", destination);
	        	case 6: return String.format("Unable to create '%s'!", destination);	        	
	        }
		} catch (Exception e) {
			logger.error("Exception thrown while running git-new-workdir command!", e); 
			return "Exception thrown while creating working directory!";				
		} finally {
			if (file != null) {
				file.delete();
			}
		}
		return null;
	}
	
	/**
	 * Class used to get the output data while executing a runtime process.
	 * 
	 * @see GitService#run_git_workdir_cmd(String, String)
	 */
	class StreamGobbler extends Thread {
		private InputStream is;
		private String type;

		private String message;
		public StreamGobbler(InputStream is, String type) {
			this.is = is;
			this.type = type;
		}
		
		public String getMessage() {
			return message;
		}

		public void run() {
			try {
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				while ((line = br.readLine()) != null) {
					logger.debug(type + ">" + line);
				}
			} catch (IOException ioe) {
				logger.error("Exception thrown while writing command line text", ioe);
			}
		}
	}
	
}