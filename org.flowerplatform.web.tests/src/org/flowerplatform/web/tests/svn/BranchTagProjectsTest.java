package org.flowerplatform.web.tests.svn;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
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

public class BranchTagProjectsTest {
	
	public static ArrayList<PathFragment> workingDirectoryPartialPath;	

	private static final CommunicationChannel communicationChannel = new RecordingTestWebCommunicationChannel();

	private static TestServiceInvocationContext context;
	
	public static ArrayList<PathFragment> getArrayOfPathFragmentsFromStringArgs(String... strings) {
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
			repository = SVNProviderPlugin.getPlugin().getRepository(repositoryUrl);
			membersOfRepo = repository.members(null);
			for (int i = 0; i < membersOfRepo.length; i++) {
				folders.add((RemoteFolder)membersOfRepo[i]);
			}
		} catch (SVNException e) {
			e.printStackTrace();
		}
		return folders;
	}
	
	public Boolean existsFolder(String folderName, String destinationName) throws TeamException {
		List<RemoteFolder> foldersOfRepo = getRepositoryFolders("svn://csp1/flower2");
		for (int i = 0; i < foldersOfRepo.size(); i++) {
			if (foldersOfRepo.get(i).getName().equals(destinationName)) {
				ISVNRemoteResource[] resources = foldersOfRepo.get(i).members(null);
				for (int j = 0; j < resources.length; j++) {
					if (resources[j].getName().equals(folderName))
						return true;
				}
			}
		}
		return false;
	}
	
	public static void checkoutProjectsInPreparationForTests() {
		// if checkouts fail, then all the other tests would have failed nonetheless if checkouts were made in their methods
		ArrayList<ArrayList<PathFragment>> selectionForCheckout = new ArrayList<>();
		selectionForCheckout.add(getArrayOfPathFragmentsFromStringArgs("explorerTreeStatefulService|Explorer1", "r", 
				"hibernate", "organization",
				"svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository",
				"tst", "svnFile"));
		//commonProject is common for update, commit, revert and ignore tests
		SvnService.getInstance().checkout(context, selectionForCheckout, workingDirectoryPartialPath, 
				"\\commonWD", "7000", 0, true, false, false, "commonProject");	
		SvnService.getInstance().checkout(context, selectionForCheckout, workingDirectoryPartialPath, 
				"\\markResolvedWD", "20", 0, true, false, false, "markResolvedProject1");
		SvnService.getInstance().checkout(context, selectionForCheckout, workingDirectoryPartialPath, 
				"\\markResolvedWD", "20", 0, true, false, false, "markResolvedProject2");
	}
	
	@BeforeClass
	public static void beforeClassMethods() {
		context = new TestServiceInvocationContext(communicationChannel, new InvokeServiceMethodServerCommand());
		workingDirectoryPartialPath = getArrayOfPathFragmentsFromStringArgs("explorerTreeStatefulService|Explorer1", "r", 
												"hibernate", "organization",
												"workingDirectories", "workingDirectories");
		checkoutProjectsInPreparationForTests();
	}

	@Test
	public void test() throws MalformedURLException, TeamException {
		boolean resourceSelected;
		List<BranchResource> branchResources = new ArrayList<BranchResource>();
		String destinationURL;
		String comment;
		//Number revision;
		boolean createMissingFolders;
		boolean preserveFolderStructure;
		boolean actionResult;
		
		// Test 1 => BranchTag action on a folder from the repository
		BranchResource bRes = new BranchResource();
		ArrayList<PathFragment> resourcePathFragment = getArrayOfPathFragmentsFromStringArgs(
					    "explorerTreeStatefulService|Explorer1", "r", "hibernate",
					    "organization", "workingDirectories", "workingDirectories", "commonWD", "workingDirectory", "commonProject", "project", "organization_default.PROIECT", "projFile");
		bRes.setImage("image/folder_pending.gif");
		bRes.setName("lala3");
		bRes.setPartialPath("");
		bRes.setPath(resourcePathFragment);
		branchResources.add(bRes);
		resourceSelected = true;
		destinationURL = "svn://csp1/flower2/lala2/lala3";
		comment = "lala";
		createMissingFolders = false;
		preserveFolderStructure = true;
		actionResult = SvnService.getInstance().branchTagResources(context, resourceSelected, branchResources, destinationURL, comment, 7, createMissingFolders, preserveFolderStructure);
		assertEquals(true, actionResult);
		// end of Test 1
				
		// Test 2 => check if the new folder exists after the BranchTag
		assertEquals(true, existsFolder("lala3", "lala2"));
		// end of Test 2
				
	}
}


