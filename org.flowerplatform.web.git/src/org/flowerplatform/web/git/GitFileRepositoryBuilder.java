package org.flowerplatform.web.git;

import java.io.IOException;

import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.BaseRepositoryBuilder;

/**
 * @author Cristina Constantinescu
 */
public class GitFileRepositoryBuilder extends BaseRepositoryBuilder<GitFileRepositoryBuilder, GitFileRepository> {

	@Override
	public GitFileRepository build() throws IOException {
		GitFileRepository repo = new GitFileRepository(setup());
		if (isMustExist() && !repo.getObjectDatabase().exists())
			throw new RepositoryNotFoundException(getGitDir());
		return repo;
	}
}
