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
public class SvnTestsRepositoryActionsGabriela {

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
		ISVNRepositoryLocation repository = null;
		String destinationFolder;
		destinationFolder = "svn://csp1/flower2";
		try {
			repository = SVNProviderPlugin.getPlugin().getRepository(
					destinationFolder);
		} catch (SVNException e) {
			e.printStackTrace();
		}
		assertEquals(destinationFolder, repository.getUrl().toString());
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
	
	public Boolean existsNewFolder(String folderName) {
		List<RemoteFolder> foldersOfRepo = getRepositoryFolders("svn://csp1/flower2");
		for (int i = 0; i < foldersOfRepo.size(); i++) {
			if (foldersOfRepo.get(i).getName().equals(folderName))
				return true;
		}
		return false;
	}

	public Boolean existsNewFolder(String folderName, String destinationName)
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
	public void createRemoteFolderTest() throws TeamException {
		ArrayList<PathFragment> parentPathFragment;
		Boolean actionResult;
		// Test 1 => create a new remote folder in the repository; it will fail
		// if a folder with the same name already exists
		parentPathFragment = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", ".svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository");
		actionResult = SvnService.getInstance().createRemoteFolder(context,
				parentPathFragment, "create1", "lala");
		assertEquals("The folder already exists in the repository", true,
				actionResult);
		// end of Test 1

		// Test 2 => check if the new folder created by Test 1 really exists in
		// the repository; it will fail if the folder does not exist
		assertEquals("The folder does not exist.", true,
				folderExistsAtDestination(parentPathFragment, "create1"));
		// end of Test 2

		// Test 3 => create a new remote folder inside an existing folder from
		// the repository
		parentPathFragment = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", ".svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository", "testing_do_not_delete",
				"svnFolder", "gabriela", "svnFolder");
		actionResult = SvnService.getInstance().createRemoteFolder(context,
				parentPathFragment, "create2", "lala");
		assertEquals("The folder already exists in the repository", true,
				actionResult);
		// end of Test 3
		
		// Undo previous actions
		
		// Test 4 => Delete a folder; it will delete the folder created by Test 1
		List<List<PathFragment>> objectFullPaths = new ArrayList<List<PathFragment>>();
		List<PathFragment> selectedItemToDelete = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", ".svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository", "create1", "svnFolder");
		objectFullPaths.add(selectedItemToDelete);
		actionResult = SvnService.getInstance().deleteSvnAction(context, objectFullPaths, "");
		assertEquals("The folder was not deleted", true, actionResult);
		// end of Test 4
		
		// Test 5 => Delete a folder; it will delete the folder created by Test 3
		objectFullPaths = new ArrayList<List<PathFragment>>();
		selectedItemToDelete = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", ".svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository", "testing_do_not_delete", "svnFolder", "gabriela", "svnFolder",
				"create2", "svnFolder");
		objectFullPaths.add(selectedItemToDelete);
		actionResult = SvnService.getInstance().deleteSvnAction(context, objectFullPaths, "");
		assertEquals("The folder was not deleted", true, actionResult);
		// end of Test 5
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
				"svn://csp1/flower2", "svnRepository", "testing_do_not_delete",
				"svnFolder", "gabriela", "svnFolder", "rename2", "svnFolder", "rename7", "svnFolder");
		destinationPath = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", ".svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository", "testing_do_not_delete",
				"svnFolder", "gabriela", "svnFolder");
		remoteResourceName = "rename7";
		comment = "lala";
		actionResult = SvnService.getInstance().renameMove(context,
				remoteResourcePath, destinationPath, remoteResourceName,
				comment);
		assertEquals(true, actionResult);
		// end of Test 1
		
		// // Test 2 => check if the folder from Test 1 exists at the destination
		assertEquals(true, folderExistsAtDestination(destinationPath, "rename7"));
		// end of Test 2

		// Test 3 => move a folder from a folder to the repository
		remoteResourcePath = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", ".svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository", "testing_do_not_delete",
				"svnFolder", "gabriela", "svnFolder", "rename3", "svnFolder");
		destinationPath = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", ".svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository");
		remoteResourceName = "rename3";
		comment = "lala";
		actionResult = SvnService.getInstance().renameMove(context,
				remoteResourcePath, destinationPath, remoteResourceName,
				comment);
		assertEquals(true, actionResult);
		// end of Test 3

		 // Test 4 => move a folder to a folder that already contains a folder
		 //with the same name; 
		 remoteResourcePath = getArrayOfPathFragmentsFromStringArgs(
		 "explorerTreeStatefulService|Explorer1", "r", "hibernate",
		 "organization", ".svn-repositories", "svnRepositories",
		 "svn://csp1/flower2", "svnRepository", "testing_do_not_delete", "svnFolder",
		 "gabriela", "svnFolder", "rename7", "svnFolder");
		 destinationPath = getArrayOfPathFragmentsFromStringArgs(
		 "explorerTreeStatefulService|Explorer1", "r", "hibernate",
		 "organization", ".svn-repositories", "svnRepositories",
		 "svn://csp1/flower2", "svnRepository", "testing_do_not_delete", "svnFolder",
		 "gabriela", "svnFolder", "rename1", "svnFolder");
		 remoteResourceName = "rename7";
		 comment = "lala";
		 actionResult = SvnService.getInstance().renameMove(context,
		 remoteResourcePath, destinationPath, remoteResourceName, comment);
		 assertEquals(false, actionResult);
		 // end of Test 4
		 
		 // Test 5 => undo action from Test 1
		 remoteResourcePath = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", ".svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository", "testing_do_not_delete",
				"svnFolder", "gabriela", "svnFolder", "rename7", "svnFolder");
		destinationPath = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", ".svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository", "testing_do_not_delete",
				"svnFolder", "gabriela", "svnFolder", "rename2", "svnFolder");
		remoteResourceName = "rename7";
		comment = "lala";
		actionResult = SvnService.getInstance().renameMove(context,
				remoteResourcePath, destinationPath, remoteResourceName,
				comment);
		assertEquals(true, actionResult);
		// end of Test 5
		
		// Test 6 => undo action from Test 3
		remoteResourcePath = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", ".svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository", "rename3", "svnFolder");
		destinationPath = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", ".svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository", "testing_do_not_delete",
				"svnFolder", "gabriela", "svnFolder");
		remoteResourceName = "rename3";
		comment = "lala";
		actionResult = SvnService.getInstance().renameMove(context,
				remoteResourcePath, destinationPath, remoteResourceName,
				comment);
		assertEquals(true, actionResult);
		// end of Test 6
	}
	
	@Test
	public void branchTagTest() throws MalformedURLException, TeamException {
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
				"organization", ".svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository", "testing_do_not_delete",
				"svnFolder", "gabriela", "svnFolder", "branchTagTestResource",
				"svnFolder");
		bRes.setImage("image/folder_pending.gif");
		bRes.setName("tst");
		bRes.setPartialPath("");
		bRes.setPath(resourcePathFragment);
		branchResources.add(bRes);
		resourceSelected = false;
		destinationURL = "svn://csp1/flower2/testing_do_not_delete/gabriela/branchTagTestSingleSelection";
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
		assertEquals(true, folderExistsAtDestination(destinationPathFragment, "branchTagTestSingleSelection"));
		// end of Test 2
		
		// Test 3 => undo the previous action
		List<List<PathFragment>> objectFullPaths = new ArrayList<List<PathFragment>>();
		List<PathFragment> selectedItemToDelete = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", ".svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository", "testing_do_not_delete", "svnFolder",
				"gabriela", "svnFolder", "branchTagTestSingleSelection", "svnFolder");
				objectFullPaths.add(selectedItemToDelete);
				actionResult = SvnService.getInstance().deleteSvnAction(context, objectFullPaths, "");
				assertEquals("The folder was not deleted", true, actionResult);
		// end of Test 3
				
		// Test 4 => multiple selection; 
		// the application works but the test does not work;
		// Error:
		//org.tigris.subversion.svnclientadapter.SVNClientException: org.apache.subversion.javahl.ClientException: Filesystem is corrupt
		//svn: Invalid change ordering: new node revision ID without delete
		/*ArrayList<PathFragment> secondResourcePathFragment = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", ".svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository", "testing_do_not_delete",
				"svnFolder", "gabriela", "svnFolder", "branchTagTestDestination",
				"svnFolder");
		bRes.setImage("image/folder_pending.gif");
		bRes.setName("hey");
		bRes.setPartialPath("");
		bRes.setPath(secondResourcePathFragment);
		branchResources.add(bRes);
		destinationURL = "svn://csp1/flower2/testing_do_not_delete/gabriela/branchTagTestMultipleSelection";
		actionResult = SvnService.getInstance().branchTagResources(context,
				resourceSelected, branchResources, destinationURL, comment, 7,
				createMissingFolders, preserveFolderStructure);
		assertEquals(true, actionResult);*/
		// end of Test 3
	}

}
