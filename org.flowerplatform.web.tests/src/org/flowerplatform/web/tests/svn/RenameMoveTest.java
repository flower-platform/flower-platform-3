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

package org.flowerplatform.web.tests.svn;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.team.core.TeamException;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.web.communication.RecordingTestWebCommunicationChannel;
import org.flowerplatform.web.communication.TestServiceInvocationContext;
import org.flowerplatform.web.svn.remote.SvnService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tigris.subversion.subclipse.core.ISVNRemoteResource;
import org.tigris.subversion.subclipse.core.ISVNRepositoryLocation;
import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.SVNProviderPlugin;
import org.tigris.subversion.subclipse.core.resources.RemoteFolder;

/**
 * 
 * @author Gabriela Murgoci
 */
public class RenameMoveTest {

	private static final CommunicationChannel communicationChannel = new RecordingTestWebCommunicationChannel();

	private static TestServiceInvocationContext context;

	public static ArrayList<PathFragment> getArrayOfPathFragmentsFromStringArgs(
			String... strings) {
		ArrayList<PathFragment> result = new ArrayList<>();
		for (int i = 0; i < strings.length; i++) {
			PathFragment pf = new PathFragment();
			pf.setName(strings[i]);
			i++;
			pf.setType(strings[i]);
			result.add(pf);
		}
		return result;
	}

	public void getRepositoriesUrls() {
		@SuppressWarnings("unused")
		ISVNRepositoryLocation repository = null;
		String destinationFolder;
		destinationFolder = "svn://csp1/flower2";
		try {
			repository = SVNProviderPlugin.getPlugin().getRepository(
					destinationFolder);
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<RemoteFolder> getRepositoryFolders(String repositoryUrl) {
		ISVNRemoteResource[] membersOfRepo;
		List<RemoteFolder> folders = new ArrayList<RemoteFolder>();
		ISVNRepositoryLocation repository = null;
		try {
			repository = SVNProviderPlugin.getPlugin().getRepository(
					repositoryUrl);
			membersOfRepo = repository.members(null);
			for (int i = 0; i < membersOfRepo.length; i++) {
				folders.add((RemoteFolder) membersOfRepo[i]);
			}
		} catch (SVNException e) {
			e.printStackTrace();
		}
		return folders;
	}

	public Boolean existsFolder(String folderName, String destinationName)
			throws TeamException {
		List<RemoteFolder> foldersOfRepo = getRepositoryFolders("svn://csp1/flower2");
		for (int i = 0; i < foldersOfRepo.size(); i++) {
			if (foldersOfRepo.get(i).getName().equals(destinationName)) {
				ISVNRemoteResource[] resources = foldersOfRepo.get(i).members(
						null);
				for (int j = 0; j < resources.length; j++) {
					if (resources[j].getName().equals(folderName))
						return true;
				}
			}
		}
		return false;
	}

	@BeforeClass
	public static void setUp() {
		context = new TestServiceInvocationContext(communicationChannel,
				new InvokeServiceMethodServerCommand());
	}

	@Test
	public void renameMoveTest() throws TeamException {
		Boolean actionResult;
		ArrayList<PathFragment> remoteResourcePath;
		ArrayList<PathFragment> destinationPath;
		String remoteResourceName;
		String comment;

		// Test 1 => move a folder from the repository to another folder that
		// exists in the repository
		remoteResourcePath = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", ".svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository", "testCreate12",
				"svnFolder");
		destinationPath = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", ".svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository", "testCreate11",
				"svnFolder");
		remoteResourceName = "testCreate12";
		comment = "lala";
		actionResult = SvnService.getInstance().renameMove(context,
				remoteResourcePath, destinationPath, remoteResourceName,
				comment);
		assertEquals(true, actionResult);
		// end of Test 1

		// Test 2 => move a folder from a folder to the repository
		remoteResourcePath = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", ".svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository", "testCreate11",
				"svnFolder", "testCreate12", "svnFolder");
		destinationPath = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", ".svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository");
		remoteResourceName = "testCreate12";
		comment = "lala";
		actionResult = SvnService.getInstance().renameMove(context,
				remoteResourcePath, destinationPath, remoteResourceName,
				comment);
		assertEquals(true, actionResult);
		// end of Test 2

		// Test 3 => move a resource from a folder to a different folder
		remoteResourcePath = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", ".svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository", "testCreate11",
				"svnFolder", "testCreate5", "svnFolder");
		destinationPath = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", ".svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository", "testCreate2",
				"svnFolder");
		remoteResourceName = "testCreate5";
		comment = "lala";
		actionResult = SvnService.getInstance().renameMove(context,
				remoteResourcePath, destinationPath, remoteResourceName,
				comment);
		assertEquals(true, actionResult);
		// end of Test 3

		// Test 4 => move a folder to a folder that already contains a folder
		// with the same name; it will fail
		// remoteResourcePath = getArrayOfPathFragmentsFromStringArgs(
		// "explorerTreeStatefulService|Explorer1", "r", "hibernate",
		// "organization", ".svn-repositories", "svnRepositories",
		// "svn://csp1/flower2", "svnRepository", "testCreate7", "svnFolder");
		// destinationPath = getArrayOfPathFragmentsFromStringArgs(
		// "explorerTreeStatefulService|Explorer1", "r", "hibernate",
		// "organization", ".svn-repositories", "svnRepositories",
		// "svn://csp1/flower2", "svnRepository", "testCreate11", "svnFolder");
		// remoteResourceName = "testCreate7";
		// comment = "lala";
		// actionResult = SvnService.getInstance().renameMove(context,
		// remoteResourcePath, destinationPath, remoteResourceName, comment);
		// assertEquals(true, actionResult);
		// end of Test 4

		// Test 5 => check if the folder is one of the destination's children
		// after the actions
		assertEquals(true, existsFolder("testCreate5", "testCreate2"));
		// end of Test 5
	}

}
