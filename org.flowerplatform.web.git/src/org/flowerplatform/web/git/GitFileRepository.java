package org.flowerplatform.web.git;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import org.eclipse.jgit.internal.JGitText;
import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.storage.file.FileBasedConfig;
import org.eclipse.jgit.storage.file.FileRepository;
import org.eclipse.jgit.util.FileUtils;
import org.eclipse.jgit.util.SystemReader;

/**
 * Repository object.
 * 
 * <p>
 * Because we don't keep a map with all repositories created
 * and because each time we need a {@link FileRepository} we create a new instance,
 * equals() and hashCode() have been overridden to get info
 * from repo's directory file.
 * 
 * @author Cristina Constantinescu
 */
public class GitFileRepository extends FileRepository {

	public GitFileRepository(File gitDir) throws IOException {
		this(new GitFileRepositoryBuilder().setGitDir(gitDir).setup());
	}
		
	public GitFileRepository(String gitDir) throws IOException {
		super(gitDir);		
	}

	public GitFileRepository(final GitFileRepositoryBuilder options) throws IOException {
		super(options);
	}

	public void create(boolean bare) throws IOException {
		final FileBasedConfig cfg = getConfig();
		if (cfg.getFile().exists()) {
			throw new IllegalStateException(MessageFormat.format(
					JGitText.get().repositoryAlreadyExists, getDirectory()));
		}
		FileUtils.mkdirs(getDirectory(), true);
		getRefDatabase().create();
		getObjectDatabase().create();

		FileUtils.mkdir(new File(getDirectory(), "branches")); //$NON-NLS-1$
		FileUtils.mkdir(new File(getDirectory(), "hooks")); //$NON-NLS-1$

		RefUpdate head = updateRef(Constants.HEAD);
		head.disableRefLog();
		head.link(Constants.R_HEADS + Constants.MASTER);

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
		final boolean fileMode = false;
		
		cfg.setInt(ConfigConstants.CONFIG_CORE_SECTION, null,
				ConfigConstants.CONFIG_KEY_REPO_FORMAT_VERSION, 0);
		cfg.setBoolean(ConfigConstants.CONFIG_CORE_SECTION, null,
				ConfigConstants.CONFIG_KEY_FILEMODE, fileMode);
		if (bare)
			cfg.setBoolean(ConfigConstants.CONFIG_CORE_SECTION, null,
					ConfigConstants.CONFIG_KEY_BARE, true);
		cfg.setBoolean(ConfigConstants.CONFIG_CORE_SECTION, null,
				ConfigConstants.CONFIG_KEY_LOGALLREFUPDATES, !bare);
		if (SystemReader.getInstance().isMacOS())
			// Java has no other way
			cfg.setBoolean(ConfigConstants.CONFIG_CORE_SECTION, null,
					ConfigConstants.CONFIG_KEY_PRECOMPOSEUNICODE, true);
		cfg.save();
	}
	
	@Override
	public int hashCode() {
		return getDirectory().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		GitFileRepository other = (GitFileRepository) obj;

		return (getDirectory().equals(other.getDirectory()));		
	}
	
}
