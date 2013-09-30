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

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.team.core.TeamException;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
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
public class SvnTestsProjectActionsGabriela {

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

	public Boolean folderExistsAtDestination(List<PathFragment> destinationPath, String folderName) throws TeamException {
		RemoteFolder destinationFolder;
		if (destinationPath.get(destinationPath.size() - 1).getName().equals("svn://csp1/flower2"))
			destinationFolder = (RemoteFolder) SVNProviderPlugin.getPlugin().getRepository("svn://csp1/flower2").getRootFolder();
		else	
			destinationFolder = (RemoteFolder) GenericTreeStatefulService.getNodeByPathFor(destinationPath, null);
		ISVNRemoteResource[] children = destinationFolder.members(null);
		for (int i = 0; i < children.length; i++) {
			if (children[i].getName().equals(folderName))
				return true;
		}
		return false;
	}
	
	public static void checkoutProjectsInPreparationForTests() {
		// if checkouts fail, then all the other tests would have failed
		// nonetheless if checkouts were made in their methods
		ArrayList<ArrayList<PathFragment>> selectionForCheckout = new ArrayList<>();
		selectionForCheckout.add(getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", "svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository", "testing_do_not_delete", "svnFolder"));
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
	public void branchTagProjectsTest() throws MalformedURLException, TeamException {
		//fail("Not yet implemented");
		boolean resourceSelected;
		List<BranchResource> branchResources = new ArrayList<BranchResource>();
		String destinationURL;
		String comment;
		boolean createMissingFolders;
		boolean preserveFolderStructure;
		boolean actionResult;

		// Test 1 => BranchTag action on a folder from the repository
		BranchResource bRes = new BranchResource();
		ArrayList<PathFragment> resourcePathFragment = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", "workingDirectories", "workingDirectories",
				"commonWD", "workingDirectory", "commonProject", "project",
				"gabriela", "projFile", "branchTagTestResource", "projFile");
		bRes.setImage("image/folder_pending.gif");
		bRes.setName("branchTagTestResource");
		bRes.setPartialPath("");
		bRes.setPath(resourcePathFragment);
		branchResources.add(bRes);
		resourceSelected = true;
		destinationURL = "svn://csp1/flower2/testing_do_not_delete/gabriela/branchTagTestProjects";
		comment = "lala";
		createMissingFolders = false;
		preserveFolderStructure = true;
		actionResult = SvnService.getInstance().branchTagResources(context,
				resourceSelected, branchResources, destinationURL, comment, 7,
				createMissingFolders, preserveFolderStructure);
		assertEquals(true, actionResult);
		// end of Test 1

		// Test 2 => check if the new folder exists after the BranchTag
		ArrayList<PathFragment> destinationPathFragment = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", ".svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository", "testing_do_not_delete",
				"svnFolder", "gabriela", "svnFolder");
		assertEquals(true, folderExistsAtDestination(destinationPathFragment, "branchTagTestProjects"));
		// end of Test 2
		
		// Test 3 => undo the action from Test 1
		List<List<PathFragment>> objectFullPaths = new ArrayList<List<PathFragment>>();
		List<PathFragment> selectedItemToDelete = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", ".svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository", "testing_do_not_delete", "svnFolder",
				"gabriela", "svnFolder", 
				"branchTagTestProjects", "svnFolder");
				objectFullPaths.add(selectedItemToDelete);
				actionResult = SvnService.getInstance().deleteSvnAction(context, objectFullPaths, "");
				assertEquals("The folder was not deleted", true, actionResult);
		// end of Test 3
	}
	
	@Test
	public void switchToBranchTag() {
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
				"gabriela", "projFile", "branchTagTestResource", "projFile");
		bRes.setImage("image/folder_pending.gif");
		bRes.setName("organization_default.PROIECT");
		bRes.setPartialPath("");
		bRes.setPath(resourcePathFragment);
		branchResources.add(bRes);

		location = "svn://csp1/flower2/testing_do_not_delete/gabriela/branchTagTestResource";
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
	
	@Test
	public void cleanupTest() {
		Boolean actionResult;
		List<List<PathFragment>> resourcesSelected = new ArrayList<List<PathFragment>>();
		ArrayList<PathFragment> firstResourcePathFragment = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", "workingDirectories", "workingDirectories",
				"commonWD", "workingDirectory", "commonProject", "project",
				"gabriela", "projFile", "branchTagTestResource", "projFile");
		resourcesSelected.add(firstResourcePathFragment);
		// Test 1 => check if the action returns true (true = success, false =
		// failure)
		actionResult = SvnService.getInstance().cleanUp(context,
				resourcesSelected);
		assertEquals(true, actionResult);
	}
	
/*	@Test
	public void shareProjectTest() {
		ArrayList<PathFragment> projectPath;
		String repositoryUrl;
		String directoryName;
		boolean create;
		String comment;
		boolean actionResult;

		// Test 1 => Share Project (the destination is the root folder
		projectPath = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", "workingDirectories", "workingDirectories",
				"my_workingdir", "workingDirectory", "proj_unversioned", "project", "proj", "projFile");
		repositoryUrl = "svn://csp1/flower2";
		directoryName = "river";
		create = false;
		comment = "lala";
		actionResult = SvnService.getInstance().shareProject(context,
				projectPath, repositoryUrl, directoryName, create, comment);
		assertEquals(true, actionResult);
		// end of Test 1
		
		
		
		
	}*/

}
