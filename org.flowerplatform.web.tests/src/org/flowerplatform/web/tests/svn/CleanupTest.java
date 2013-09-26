package org.flowerplatform.web.tests.svn;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.web.communication.RecordingTestWebCommunicationChannel;
import org.flowerplatform.web.communication.TestServiceInvocationContext;
import org.flowerplatform.web.svn.remote.SvnService;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Gabriela Murgoci
 */
public class CleanupTest {
	
	private static String workspacePath;

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
		workingDirectoryPartialPath 
		= getArrayOfPathFragmentsFromStringArgs("explorerTreeStatefulService|Explorer1", "r", 
												"hibernate", "organization",
												"workingDirectories", "workingDirectories");
		File workspaceRoot = CommonPlugin.getInstance().getWorkspaceRoot();
		workspacePath = workspaceRoot.getPath() + "\\";
		checkoutProjectsInPreparationForTests();
	}

	@Test
	public void test() {
		Boolean actionResult;
		List<List<PathFragment>> resourcesSelected = new ArrayList<List<PathFragment>>();
		ArrayList<PathFragment> firstResourcePathFragment = getArrayOfPathFragmentsFromStringArgs(
			    "explorerTreeStatefulService|Explorer1", "r", "hibernate",
			    "organization", "workingDirectories", "workingDirectories", "commonWD", "workingDirectory", "commonProject", "project", "organization_default.PROIECT", "projFile");
		resourcesSelected.add(firstResourcePathFragment);
		// Test 1 => check if the action returns true (true = success, false = failure)
		actionResult = SvnService.getInstance().cleanUp(context, resourcesSelected);
		assertEquals(true, actionResult);
	}
	
	@After
	public static void undoChanges() {
		// delete checked out working directory along with content
		File f = new File(workspacePath + "hibernate\\commonWD");
		f = new File(workspacePath + "hibernate\\checkoutWD");
		f.delete();		
		f = new File(workspacePath + "hibernate\\markResolvedWD");
		f.delete();
	}
}
