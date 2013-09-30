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
import org.flowerplatform.web.svn.remote.BranchResource;
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
public class SwitchToBranchTest {

	public static ArrayList<PathFragment> workingDirectoryPartialPath;

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

	public static void checkoutProjectsInPreparationForTests() {
		// if checkouts fail, then all the other tests would have failed
		// nonetheless if checkouts were made in their methods
		List<List<PathFragment>> selectionForCheckout = new ArrayList<>();
		selectionForCheckout.add(getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", "svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository", "tst", "svnFile"));
		// commonProject is common for update, commit, revert and ignore tests
		SvnService.getInstance().checkout(context, selectionForCheckout,
				workingDirectoryPartialPath, "\\commonWD", "7000", 0, true,
				false, false, "commonProject");
		SvnService.getInstance().checkout(context, selectionForCheckout,
				workingDirectoryPartialPath, "\\markResolvedWD", "20", 0, true,
				false, false, "markResolvedProject1");
		SvnService.getInstance().checkout(context, selectionForCheckout,
				workingDirectoryPartialPath, "\\markResolvedWD", "20", 0, true,
				false, false, "markResolvedProject2");
	}

	@BeforeClass
	public static void beforeClassMethods() {
		context = new TestServiceInvocationContext(communicationChannel,
				new InvokeServiceMethodServerCommand());
		workingDirectoryPartialPath = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", "workingDirectories", "workingDirectories");
		checkoutProjectsInPreparationForTests();
	}

	@Test
	public void test() {
		List<BranchResource> branchResources = new ArrayList<BranchResource>();
		String location;
		long revision;
		int depth;
		boolean setDepth;
		boolean ignoreExternals;
		boolean force;

		// Test 1
		BranchResource bRes = new BranchResource();
		ArrayList<PathFragment> resourcePathFragment = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", "workingDirectories", "workingDirectories",
				"commonWD", "workingDirectory", "commonProject", "project",
				"organization_default.PROIECT", "projFile");
		bRes.setImage("image/folder_pending.gif");
		bRes.setName("organization_default.PROIECT");
		bRes.setPartialPath("");
		bRes.setPath(resourcePathFragment);
		branchResources.add(bRes);

		location = "svn://csp1/flower2/test2/organization_default.PROIECT";
		revision = -1;
		depth = 0;
		setDepth = false;
		ignoreExternals = false;
		force = true;

		assertEquals(
				true,
				SvnService.getInstance().switchTo(context, branchResources,
						location, revision, depth, setDepth, ignoreExternals,
						force));
		// end of Test 1
	}

}
