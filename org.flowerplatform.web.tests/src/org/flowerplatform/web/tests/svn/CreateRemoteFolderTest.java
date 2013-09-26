package org.flowerplatform.web.tests.svn;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

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

import org.eclipse.team.core.TeamException;


/**
 * 
 * @author Gabriela Murgoci
 */
public class CreateRemoteFolderTest {
	
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
	
	@BeforeClass
	public static void setUp() {
		context = new TestServiceInvocationContext(communicationChannel, new InvokeServiceMethodServerCommand());
	}
	
	@Test
	public void getRepositoriesUrls() {
		ISVNRepositoryLocation repository = null;
		String destinationFolder;
		destinationFolder = "svn://csp1/flower2";
		try {
			repository = SVNProviderPlugin.getPlugin().getRepository(destinationFolder);
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(destinationFolder, repository.getUrl().toString());
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
	
	public Boolean existsNewFolder(String folderName) {
		
		List<RemoteFolder> foldersOfRepo = getRepositoryFolders("svn://csp1/flower2");
		
		for (int i = 0; i < foldersOfRepo.size(); i++) {
			if (foldersOfRepo.get(i).getName().equals(folderName))
				return true;
		}
		return false;
	}
	
	public Boolean existsNewFolder(String folderName, String destinationName) throws TeamException {
		
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
	
	@Test 
	public void testCreateFolder() throws InstantiationException, IllegalAccessException, TeamException {
		//String repositoryUrl = "svn://csp1/flower2";
		ArrayList<PathFragment> parentPathFragment;
		Boolean actionResult;
		// Test 1 => create a new remote folder in the repository
		parentPathFragment = getArrayOfPathFragmentsFromStringArgs(
			    "explorerTreeStatefulService|Explorer1", "r", "hibernate",
			    "organization", ".svn-repositories", "svnRepositories", "svn://csp1/flower2", "svnRepository");
		actionResult = SvnService.getInstance().createRemoteFolder(context, parentPathFragment, "createT1", "lala");
		assertEquals(true, actionResult);
		// end of Test 1
		
		// Test 2 => create a new remote folder inside an existing folder from the repository; it will work if Test 1 works
		parentPathFragment = getArrayOfPathFragmentsFromStringArgs(
			    "explorerTreeStatefulService|Explorer1", "r", "hibernate",
			    "organization", ".svn-repositories", "svnRepositories", "svn://csp1/flower2", "svnRepository", "createT1", "svnFolder");
		actionResult = SvnService.getInstance().createRemoteFolder(context, parentPathFragment, "createT2", "lala");
		assertEquals(true, actionResult);
		// end of Test 2
		
		// Test 3 => create a folder with an existing name; if Tests 1 and 2 worked, this should fail, because the item already exists
		// 			 in the file system
//		parentPathFragment = getArrayOfPathFragmentsFromStringArgs(
//			    "explorerTreeStatefulService|Explorer1", "r", "hibernate",
//			    "organization", ".svn-repositories", "svnRepositories", "svn://csp1/flower2", "svnRepository", "createT1", "svnFolder");
//		actionResult = SvnService.getInstance().createRemoteFolder(context, parentPathFragment, "createT2", "lala");
//		assertEquals(false, actionResult);
		// end of Test 3
		
		// Test 4 => check if the new folder exists in the repository
		assertEquals(true, existsNewFolder("createT2", "createT1"));
		// end of Test 4
	}
}

