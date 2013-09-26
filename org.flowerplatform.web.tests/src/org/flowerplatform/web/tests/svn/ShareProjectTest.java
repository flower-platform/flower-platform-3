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
public class ShareProjectTest {

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
	
	@BeforeClass
	public static void beforeClassMethods() {
		context = new TestServiceInvocationContext(communicationChannel, new InvokeServiceMethodServerCommand());
//		workingDirectoryPartialPath = getArrayOfPathFragmentsFromStringArgs("explorerTreeStatefulService|Explorer1", "r", 
//												"hibernate", "organization",
//												"workingDirectories", "workingDirectories");
	}
	
	@Test
	public void test() {
		ArrayList<PathFragment> projectPath;
		String repositoryUrl;
		String directoryName;
		boolean create;
		String comment;
		boolean actionResult;
		//fail("Not yet implemented");
		
		//Test 1 => Share Project (the destination is the root folder
		projectPath = getArrayOfPathFragmentsFromStringArgs(
			    "explorerTreeStatefulService|Explorer1", "r", "hibernate",
			    "organization", "workingDirectories", "workingDirectories", "my_workingdir", "workingDirectory", "river", "project");
		repositoryUrl = "svn://csp1/flower2";
		directoryName = "river";
		create = false;
		comment = "lala";
		actionResult = SvnService.getInstance().shareProject(context, projectPath, repositoryUrl, directoryName, create, comment);
		assertEquals(true, actionResult);
		// end of Test 1
				
		
		
		
	}

}
