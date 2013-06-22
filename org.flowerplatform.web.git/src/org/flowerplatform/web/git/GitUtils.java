package org.flowerplatform.web.git;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.regex.Pattern;

import org.eclipse.jgit.api.GitCommand;
import org.eclipse.jgit.errors.TransportException;
import org.eclipse.jgit.internal.JGitText;
import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.lib.RepositoryCache.FileKey;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.util.FS;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.stateful_service.NamedLockPool;
import org.flowerplatform.web.FlowerWebProperties;
import org.flowerplatform.web.entity.User;
import org.flowerplatform.web.explorer.RootChildrenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristina Constantienscu
 */
public class GitUtils {
	
	private static Logger logger = LoggerFactory.getLogger(GitUtils.class);
	
	public static final String MAIN_REPOSITORY = "main";
		
	public static final String GIT_REPOSITORIES_NAME = ".git-repositories";
	
	static {	
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
		return new File(RootChildrenProvider.getWorkspaceRoot(), orgFile.getName() + "/" + GIT_REPOSITORIES_NAME + "/");
	}
	
	public Repository getRepository(File repoFile) {
		File gitDir = getGitDir(repoFile);
		if (gitDir != null) {
			try {
				return RepositoryCache.open(FileKey.exact(gitDir, FS.DETECTED));
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
		return repo.getDirectory().getParent();
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
		
		if (cause != null && matchMessage(JGitText.get().notAuthorized, cause.getMessage()) ||
			cause.getMessage().endsWith("username must not be null.") ||
			cause.getMessage().endsWith("host must not be null.")) {
			return true;
		}		
		return false;
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
	
}
