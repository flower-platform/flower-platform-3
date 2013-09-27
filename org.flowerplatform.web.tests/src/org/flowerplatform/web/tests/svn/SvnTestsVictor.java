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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.web.communication.RecordingTestWebCommunicationChannel;
import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;
import org.flowerplatform.web.entity.SVNRepositoryURLEntity;
import org.flowerplatform.web.projects.remote.ProjectsService;
import org.flowerplatform.web.svn.remote.SvnService;
import org.flowerplatform.web.svn.remote.dto.FileDto;
import org.flowerplatform.web.svn.remote.dto.GetModifiedFilesDto;
import org.hibernate.Query;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Victor Badila
 */
public class SvnTestsVictor {

	private static String workspacePath;
	
	private static final CommunicationChannel communicationChannel = new RecordingTestWebCommunicationChannel();
	
	public static ServiceInvocationContext context;
	
	public static CommunicationPlugin communicationPlugin = new CommunicationPlugin();
	
	public static List<PathFragment> workingDirectoryPartialPath;
	
	public String workspaceLocation = CommonPlugin.getInstance().getWorkspaceRoot().getAbsolutePath();
	
	/**
	 * @param strings first the name, then the type of PathFragment
	 */
	public static List<PathFragment> getArrayOfPathFragmentsFromStringArgs(String... strings) {
		ArrayList<PathFragment> result = new ArrayList<>();
		for (int i=0; i<strings.length; i++) {
			PathFragment pf = new PathFragment();
			pf.setName(strings[i]);
			i++;
			pf.setType(strings[i]);
			result.add(pf);
		}
		return result;
	}
	
	public File createFile(String path) {
		File f = new File(path);
		try {
			f.createNewFile();
		} catch (IOException e) {
			System.out.println("exceptie");
		}
		return f;
	}
	
	public static void writeFile(String filename, String text) throws IOException {
	    FileWriter fstream = new FileWriter(filename, true); //true tells to append data.
	    BufferedWriter out = new BufferedWriter(fstream);
	    out.write(text);
	    out.close();			    
	}
	
	public List<File> getProjectsForWorkingDirectory(String workingDirectoryFullPath) {
		try {
			ProjectsService pj = new ProjectsService();
			Map<File, List<File>> someMap = pj.getWorkingDirectoryToProjectsMap();
			for (File x : someMap.keySet()) {
				if (x.getAbsolutePath().equals(workingDirectoryFullPath)) {
					return someMap.get(x);
				}				
			}
		} catch (Exception e) {			
		}
		return null;		
	}
	
	public static void checkoutProjectsInPreparationForTests() {
		// if checkouts fail, then all the other tests would have failed nonetheles if checkouts were made in their methods
		List<List<PathFragment>> selectionForCheckout = new ArrayList<>();
		selectionForCheckout.add(getArrayOfPathFragmentsFromStringArgs("explorerTreeStatefulService|Explorer1", "r", 
				"hibernate", "organization",
				"svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository",
				"testing_do_not_delete", "svnFolder",
				"victor", "svnFolder"));		
		SvnService.getInstance().createFolderAndMarkAsWorkingDirectory(context, "commonWD", "hibernate");		
		SvnService.getInstance().createFolderAndMarkAsWorkingDirectory(context, "markResolved", "hibernate");
		SvnService.getInstance().checkout(context, selectionForCheckout, workingDirectoryPartialPath, 
				"\\commonWD", "7000", 0, true, false, false, "commonProject");	
		SvnService.getInstance().checkout(context, selectionForCheckout, workingDirectoryPartialPath, 
				"\\markResolved", "20", 0, true, false, false, "markResolvedProject1");
		SvnService.getInstance().checkout(context, selectionForCheckout, workingDirectoryPartialPath, 
				"\\markResolved", "20", 0, true, false, false, "markResolvedProject2");
		}
	
	@BeforeClass
	public static void beforeClassMethods() {
		communicationPlugin.tlCurrentChannel.set(communicationChannel);
		context = new ServiceInvocationContext(communicationChannel);
		workingDirectoryPartialPath 
		= getArrayOfPathFragmentsFromStringArgs("explorerTreeStatefulService|Explorer1", "r", 
												"hibernate", "organization",
												"workingDirectories", "workingDirectories");
		File workspaceRoot = CommonPlugin.getInstance().getWorkspaceRoot();
		workspacePath = workspaceRoot.getPath() + "\\";
		checkoutProjectsInPreparationForTests();
	}

	@Test
	public void updateTest() {	
		// updateToHead is just a case of updateToVersion. only updateToVersion will be tested
		List<List<PathFragment>> selectionForUpdate = new ArrayList<>();
		selectionForUpdate.add(getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", 
				"hibernate", "organization",
				"workingDirectories", "workingDirectories",
				"commonWD", "workingDirectory",
				"commonProject", "project"));
		// update to version 8283 (it exists)
		SvnService.getInstance().updateToVersion(context, selectionForUpdate, "8283", 0, false, false, true);		
		//check to see if files correspond to revision 8283
		String r = "8283";
		List<String> revs = SvnService.getInstance().getRevisionsForFilesInSelection(selectionForUpdate);
		for (String s : revs) {
			if (!s.equals(r)) {
				fail("updateTest: not all files belong to revision 8243");
			}
		}		
		// update repository back to head
		Boolean result = SvnService.getInstance().updateToHEAD(context, selectionForUpdate);
		assertEquals("updateTest: updateToHEAD not successful", result, true);
		// check to see if files correspond to head revision
		GetModifiedFilesDto modDtos = SvnService.getInstance().getDifferences(context, selectionForUpdate);
		assertEquals("updateTest: differences with head revision found. updateToHEAD failed", 0, modDtos.getFiles().size());
	}

	@Test
	public void checkoutTest() {
		List<List<PathFragment>> selectionList = new ArrayList<>();
		selectionList.add(getArrayOfPathFragmentsFromStringArgs("explorerTreeStatefulService|Explorer1", "r", 
																"hibernate", "organization",
																"svn-repositories", "svnRepositories",
																"svn://csp1/flower2", "svnRepository",
																"testing_do_not_delete", "svnFolder",
																"victor", "svnFolder"));			
		Boolean finalResult;
		// see if normal checkout works
		finalResult = SvnService.getInstance().checkout(context, selectionList, workingDirectoryPartialPath, "\\commonWD", "7000", 0, true, false, false, "commonProject2");
		assertEquals("checkoutTest: Checkout failed", true, finalResult); 
		// check for working directory that was added during first checkout in the database
		ArrayList<String> workingDirectoryList = SvnService.getInstance().getWorkingDirectoriesForOrganization(context, "hibernate");
		Boolean wdFound = false;
		for (String s : workingDirectoryList) {
			if (s.equals("commonWD")) {
				wdFound = true;
			}
		}
		assertEquals("checkoutTest: Working directory not found", true, wdFound);
		//check if project was added to working directory
		List<File> projects = getProjectsForWorkingDirectory(workspacePath + "hibernate\\commonWD");
		Boolean projectFound = false;
		for (File f : projects) {
			if (f.getAbsolutePath().equals(workspacePath + "hibernate\\commonWD\\commonProject2")) {
				projectFound = true;
			}
		}
		assertEquals("checkoutTest: Project not found", true, projectFound);
	}

	@Test
	public void createSvnRepositoryTest() {
		Boolean result;
		List<PathFragment> parentPathFragment 
 		= getArrayOfPathFragmentsFromStringArgs("explorerTreeStatefulService|Explorer1", "r", 
 												"hibernate", "organization",
 												".svn-repositories", "svnRepositories");		
		// add 'bad url' repository:
		result = SvnService.getInstance().createSvnRepository(context, "nosuchurl", parentPathFragment);
		assertEquals("createSvnRepositoryTest: 'nosuchurl' repository was added", false, result);		
		// add existing repository:
		result = SvnService.getInstance().createSvnRepository(context, "svn://csp1/flower2/testing_do_not_delete/victor", parentPathFragment);
		assertEquals("createSvnRepositoryTest: existing repository url was not added", true, result);
		new DatabaseOperationWrapper(new DatabaseOperation() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				try {
					Query q = wrapper.getSession()
							.createQuery(String.format("SELECT e from %s e where e.name = '%s' and e.organization.name = '%s'", 
										 SVNRepositoryURLEntity.class.getSimpleName(),
										 "svn://csp1/flower2/testing_do_not_delete/victor",
										 "hibernate"));
					assertEquals("createSvnRepositoryTest: new repository does not belong to organization in the database", 1, q.list().size());
					System.out.println(q.list().toString());
				} catch (Exception e) {					
				}
			}
		});
	}
	
	@Test
	public void commitTest() {
		List<PathFragment> selectionForDiff = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", 
				"hibernate", "organization",
				"workingDirectories", "workingDirectories",
				"commonWD", "workingDirectory",
				"commonProject", "project");
		List<List<PathFragment>> m = new ArrayList<>();
		m.add(selectionForDiff);		
		// get revision of project. we will revert to this revision after commitTest succeeds
		String revision = SvnService.getInstance().getRevisionsForFilesInSelection(m).get(0);
		//after each successful run newFile and oldFile must be incremented by 1.
		// add a new file:
		String newFile = "19";
		String oldFile = "18";
		File f = createFile(workspacePath + "hibernate\\commonWD\\commonProject\\" + newFile);
		// delete a file (to avoid creation of file on repository before each test run, we delete the file created at the earlier run. don't forget to increment!!)
		File f1 = new File(workspacePath + "hibernate\\commonWD\\commonProject\\" + oldFile);
		f1.delete();
		// commit:
		ArrayList<FileDto> selectionForCommit = new ArrayList<>();		
		FileDto dto = new FileDto();
		dto.setLabel(newFile);
		dto.setPathFromRoot(f.getAbsolutePath().substring(workspaceLocation.length()));
		dto.setStatus("unversioned");
		selectionForCommit.add(dto);
		FileDto dto2 = new FileDto();
		dto2.setLabel(oldFile);
		dto2.setPathFromRoot(f1.getAbsolutePath().substring(workspaceLocation.length()));
		dto2.setStatus("missing");		
		selectionForCommit.add(dto2);
		Boolean result = SvnService.getInstance().commit(context, selectionForCommit, "new message", false);
		assertEquals("commitTest: commit operation did not end succesfully", true, result);
		// check differences with head (if there are no differences it means that the deleted file is not on the repository and that the new file is)
			GetModifiedFilesDto modDto = SvnService.getInstance().getDifferences(context, m);
		assertEquals("commitTest: differences between local revision and head revision found", 0, modDto.getFiles().size());
		
		SvnService.getInstance().updateToVersion(context, m, revision, 0, false, false, true);
		modDto = SvnService.getInstance().getDifferences(context, m);
		SvnService.getInstance().commit(context, modDto.getFiles(), "", false);	
	}
	
	@Test
	public void addToSvnIgnoreTest() {	
		String fileName = "qqxyz";
		// add a new file
		File f = createFile(workspacePath + "hibernate\\commonWD\\commonProject\\" + fileName);
		// add to svn ignore with filename as pattern
		List<List<PathFragment>> selectionForIgnore = new ArrayList<>();
		selectionForIgnore.add(getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", 
				"hibernate", "organization",
				"workingDirectories", "workingDirectories",
				"commonWD", "workingDirectory",
				"commonProject", "project"));
		SvnService.getInstance().addToSvnIgnore(context, selectionForIgnore, fileName);		
		// check if file and only that file present in getDifferences();
		GetModifiedFilesDto modDtos = SvnService.getInstance().getDifferences(context, selectionForIgnore);
		for (FileDto fd : modDtos.getFiles()) {
			if (!fd.getStatus().equals("ignored")) {
				fail("addToSvnIgnoreTest: method did not behave as it should have");
			}
		}
		assertEquals("addToSvnIgnoreTest: more or less than one files were ignored",1 , modDtos.getFiles().size());
		f.delete();		
		//check if ignore works for other patterns.
		//create 3 new files:
		f = createFile(workspacePath + "hibernate\\commonWD\\commonProject\\mustBeIgnored.txt");
		File f2 = createFile(workspacePath + "hibernate\\commonWD\\commonProject\\mustNotBeIgnored1.png");
		File f3 = createFile(workspacePath + "hibernate\\commonWD\\commonProject\\mustNotBeIgnored2");
		// add to svn ignore with *.txt as pattern
		SvnService.getInstance().addToSvnIgnore(context, selectionForIgnore, "*.txt");
		modDtos = SvnService.getInstance().getDifferences(context, selectionForIgnore);
		ArrayList<FileDto> files  = modDtos.getFiles();
		int originalFilesFound = 0;
		for (FileDto fd : files) {
			if (fd.getLabel().equals("\\mustBeIgnored.txt")) {
				if (!fd.getStatus().equals("ignored")) {
					fail("addToSvnIgnoreTest: file that had to be ignored was not ignored");
				}
				originalFilesFound++;
			}
			if (fd.getLabel().equals("\\mustNotBeIgnored1.png")) {
				if (!fd.getStatus().equals("unversioned")) {
					fail("addToSvnIgnoreTest: mustNotBeIgnored1.png status is not 'unverioned'");
				}
				originalFilesFound++;
			}
			if (fd.getLabel().equals("\\mustNotBeIgnored2")) {
				if (!fd.getStatus().equals("unversioned")) {
					fail("addToSvnIgnoreTest: mustNotBeIgnored2 status is not 'unverioned'");
				}
				originalFilesFound++;
			}			
		}
		if (originalFilesFound!=3 || files.size()!=3) {
			fail ("addToSvnIgnoreTest: improper number of files/original files found");
		}		
		f.delete();
		f2.delete();
		f3.delete();
	}
	
	@Test
	public void revertTest() {
		String filename = "ergh";
		// add a new file
		File f = createFile(workspacePath + "hibernate\\commonWD\\commonProject\\" + filename);
		// revert
		FileDto fd = new FileDto();
		fd.setPathFromRoot(f.getAbsolutePath().substring(workspaceLocation.length()));
		fd.setStatus("unversioned");
		ArrayList<FileDto> array = new ArrayList<>();
		array.add(fd);
		SvnService.getInstance().revert(context, array);
		// check for differences
		List<PathFragment> arrayOfPaths = getArrayOfPathFragmentsFromStringArgs(
						"explorerTreeStatefulService|Explorer1", "r", 
						"hibernate", "organization",
						"workingDirectories", "workingDirectories",
						"commonWD", "workingDirectory",
						"commonProject", "project");
		List<List<PathFragment>> x = new ArrayList<>();
		x.add(arrayOfPaths);
		GetModifiedFilesDto modDtos = SvnService.getInstance().getDifferences(context, x);
		assertEquals("revertTest: revert did not succeed. differences were found", true, modDtos.getFiles().size() == 0);
	}
	
	@Test
	public void addToVersionControlTest() {
		String filename = "willBeAddedToVersionControl";
		// add a new file
		File f = createFile(workspacePath + "hibernate\\commonWD\\commonProject\\" + filename);
		// add file to version control
		List<List<PathFragment>> selectionForAddToVersion = new ArrayList<>();
		selectionForAddToVersion.add(getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", 
				"hibernate", "organization",
				"workingDirectories", "workingDirectories",
				"commonWD", "workingDirectory",
				"commonProject", "project",
				filename, "projFile"));
		SvnService.getInstance().addToVersion(context, selectionForAddToVersion);
		selectionForAddToVersion.get(0).remove(selectionForAddToVersion.get(0).size()-1);
		// check differences and see if it is seen as added
		GetModifiedFilesDto modDtos = SvnService.getInstance().getDifferences(context, selectionForAddToVersion);
		assertEquals("addToVersionControlTest: more than one difference found after file was added", 1, modDtos.getFiles().size());
		assertEquals("addToVersionControlTest: added file's status is not 'added'", "added", modDtos.getFiles().get(0).getStatus());
		
		SvnService.getInstance().commit(context, modDtos.getFiles(), "", false);
		SvnService.getInstance().deleteSvnAction(context, selectionForAddToVersion, "");
		selectionForAddToVersion = new ArrayList<>();
		selectionForAddToVersion.add(getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", 
				"hibernate", "organization",
				"workingDirectories", "workingDirectories",
				"commonWD", "workingDirectory",
				"commonProject", "project"));
		SvnService.getInstance().updateToHEAD(context, selectionForAddToVersion);
	}
	
	public List<List<PathFragment>> markResolvedCommonBlock(String option) {
		String testName = option + "Test";
		// get two files from two identical versions of a file from repository
		File f1 = new File(workspacePath + "hibernate\\markResolved\\markResolvedProject1\\66zWzz33XzYz");
		File f2 = new File(workspacePath + "hibernate\\markResolved\\markResolvedProject2\\66zWzz33XzYz");
		File f3 = null; // is only used while resolving using 'my' or 'base' version		
		if (option.equals("markResolvedUsingBase")) {
			f3 = new File(workspacePath + "hibernate\\markResolved\\66zWzz33XzYz" + "beforeResolve");
			try {
				FileUtils.copyFile(f2, f3);
			} catch (IOException e) {
			}
		}			
		// write different stuff into both files:
		try {
			writeFile(f1.getAbsolutePath(), "A");
			writeFile(f2.getAbsolutePath(), "B");
		} catch (IOException e) {
			e.printStackTrace(); // won't ever get here
		}		
		if (option.equals("markResolvedUsingMy")) {
			f3 = new File(workspacePath + "hibernate\\markResolved\\66zWzz33XzYz" + "beforeResolve");
			try {
				FileUtils.copyFile(f2, f3);
			} catch (IOException e) {
			}
		}		
		// get the correct dto file for committing the first project
		List<List<PathFragment>> selectionForUpdate = new ArrayList<>();
		selectionForUpdate.add(getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", 
				"hibernate", "organization",
				"workingDirectories", "workingDirectories",				
				"markResolved", "workingDirectory",
				"markResolvedProject1", "project"));
		FileDto fd = SvnService.getInstance().getDifferences(context, selectionForUpdate).getFiles().get(0);		
		// commit the first project
		ArrayList<FileDto> selectionForCommit = new ArrayList<>();
		selectionForCommit.add(fd);
		Boolean res = SvnService.getInstance().commit(context, selectionForCommit, "new message", false);
		assertEquals(testName + ": commit was not successful", true, res);
		// update the second project
		selectionForUpdate = new ArrayList<>();
		selectionForUpdate.add(getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", 
				"hibernate", "organization",
				"workingDirectories", "workingDirectories",
				"markResolved", "workingDirectory",
				"markResolvedProject2", "project"));			
		SvnService.getInstance().updateToHEAD(context, selectionForUpdate);
		// get differences
		GetModifiedFilesDto modDtos = SvnService.getInstance().getDifferences(context, selectionForUpdate);
		assertEquals(testName + ": Files on second projects do not differ from head revision of project", true, modDtos.getFiles().size()>0);
		for (FileDto fdto : modDtos.getFiles()) {
			if (!fdto.getStatus().equals("conflicted")) {
				fail(testName + ": Other files beside conflicted files show up as different from head revision");
			}
		}
		// mark as resolved
		fd = modDtos.getFiles().get(0);
		ArrayList<String> resolveArgument = new ArrayList<String>();
		resolveArgument.add(fd.getPathFromRoot());
		int choice;
		if (option.equals("markResolvedUsingMy")) {
			choice = 1;
		} else if (option.equals("markResolvedUsingIncoming")) {
			choice = 2;
		} else {
			choice = 3;
		}
		SvnService.getInstance().resolve(context, resolveArgument, choice);	
		
		if (option.equals("markResolvedUsingMy") || option.equals("markResolvedUsingBase")) {
			try {
				assertEquals(option + ": resolve did not keep the file to base/my (see test name) revision", true, FileUtils.contentEquals(f2, f3));
				f3.delete();
			} catch (IOException e) {
			}
			return null;
		}		
		return selectionForUpdate;
	}	
	
	public void markResolvedUsingMyTest() {
		markResolvedCommonBlock("markResolvedUsingMy");				
	}
	
	public void markResolvedUsingIncomingTest() {
		List<List<PathFragment>> selectionForUpdate = markResolvedCommonBlock("markResolvedUsingIncoming");		
		// get differences and see if files conflict
		GetModifiedFilesDto modDtos = SvnService.getInstance().getDifferences(context, selectionForUpdate);
		assertEquals("markResolvedUsingIncoming: after 'resolve' action there are still differences with the head revision", 0, modDtos.getFiles().size());
		// reset the files to their normal state (in case another markResolved test method follows)
		
		
	}
	
	public void markResolvedUsingBaseTest() {
		markResolvedCommonBlock("markResolvedUsingBase");		
	}
	
	@Test
	public void markResolvedMethods() {
		// in order not to have many different testing directories checked out for markResolve tests, after each execution, project is brought down to a previous revision
		markResolvedUsingIncomingTest();
		markResolvedUsingMyTest();
		// update to head the second project
		List<List<PathFragment>> selectionForUpdate = new ArrayList<>();
		selectionForUpdate.add(getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", 
				"hibernate", "organization",
				"workingDirectories", "workingDirectories",
				"markResolved", "workingDirectory",
				"markResolvedProject2", "project"));
		(new File(workspacePath + "hibernate\\markResolved\\markResolvedProject2\\66zWzz33XzYz")).delete();
		SvnService.getInstance().updateToHEAD(context, selectionForUpdate);
		markResolvedUsingBaseTest();		
	}
	
	public static void resolveCommitAndAddToVersionChanges() {
		// first we delete the file added during the addToVersion test
		List<List<PathFragment>> selectionForDelete = new ArrayList<>();
			
		selectionForDelete.add(getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", 
				"hibernate", "organization",
				"svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository",
				"testing_do_not_delete", "svnFolder",
				"victor", "svnFolder",
				"willBeAddedToVersionControl", "svnFile"));
		SvnService.getInstance().deleteSvnAction(context, selectionForDelete, "");
		// rename the file committed during CommitTest
		SvnService.getInstance().renameMove(context, 
				getArrayOfPathFragmentsFromStringArgs("explorerTreeStatefulService|Explorer1", "r", 
						"hibernate", "organization",
						"svn-repositories", "svnRepositories",
						"svn://csp1/flower2", "svnRepository",
						"testing_do_not_delete", "svnFolder",
						"victor", "svnFolder",
						"19", "svnFile"),
				getArrayOfPathFragmentsFromStringArgs("explorerTreeStatefulService|Explorer1", "r", 
						"hibernate", "organization",
						"svn-repositories", "svnRepositories",
						"svn://csp1/flower2", "svnRepository",
						"testing_do_not_delete", "svnFolder",
						"victor", "svnFolder"), "18", "");
	}
	
	@AfterClass
	public static void undoChanges() {
		resolveCommitAndAddToVersionChanges();
		// delete checked out working directory along with content
		try {
			FileUtils.deleteDirectory(new File(workspacePath + "hibernate\\commonWD"));
			FileUtils.deleteDirectory(new File(workspacePath + "hibernate\\markResolved"));			
			FileUtils.deleteDirectory(new File(workspacePath + "hibernate\\project-wrappers"));
		} catch (IOException e) {
		}		
	}

}

