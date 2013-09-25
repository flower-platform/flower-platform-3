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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.web.communication.RecordingTestWebCommunicationChannel;
import org.flowerplatform.web.svn.remote.SvnService;
import org.flowerplatform.web.svn.remote.dto.FileDto;
import org.flowerplatform.web.svn.remote.dto.GetModifiedFilesDto;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Victor Badila
 */
public class SvnTestsVictor {
	
	// TODO In order for the tests to work, several lines in SvnService (few of them progress monitor related, others log-in related
	// must be commented

	private static String workspacePath;
	
	private static final CommunicationChannel communicationChannel = new RecordingTestWebCommunicationChannel();

	public static ServiceInvocationContext context;
	
	public static ArrayList<PathFragment> workingDirectoryPartialPath;	
	
	/**
	 * @param strings first the name, then the type of PathFragment
	 */
	public static ArrayList<PathFragment> getArrayOfPathFragmentsFromStringArgs(String... strings) {
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
	    FileOutputStream fos = null;
	    fos = new FileOutputStream(filename);
	    fos.write(text.getBytes("UTF-8"));
	    fos.close();	    
	}
	
	public static void checkoutProjectsInPreparationForTests() {
		// if checkouts fail, then all the other tests would have failed nonetheles if checkouts were made in their methods
		ArrayList<ArrayList<PathFragment>> selectionForCheckout = new ArrayList<>();
		selectionForCheckout.add(getArrayOfPathFragmentsFromStringArgs("explorerTreeStatefulService|Explorer1", "r", 
				"hibernate", "organization",
				"svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository",
				"nuSterge", "svnFile"));
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
	public void updateToHead() {	
		// works just like updateToVersion(). In fact, it calls updateToVersion() with specific arguments. No need to
		// do separate test for updateToVersion().				
		ArrayList<ArrayList<PathFragment>> selectionForUpdate = new ArrayList<>();
		selectionForUpdate.add(getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", 
				"hibernate", "organization",
				"workingDirectories", "workingDirectories",
				"commonWD", "workingDirectory",
				"commonProject", "project"));			
		// delete a file from repository:		
		File f = new File(workspacePath + "hibernate\\commonWD\\commonProject\\nuSterge2");
		assertEquals(true, f.delete());		
		// update repository
		Boolean result = SvnService.getInstance().updateToHEAD(context, selectionForUpdate);
		assertEquals(result, true);		
		// check if file was added during update:
		assertEquals(true, f.exists());	
	}

	@Test
	public void checkout() {		
		ArrayList<ArrayList<PathFragment>> selectionList = new ArrayList<ArrayList<PathFragment>>();
		selectionList.add(getArrayOfPathFragmentsFromStringArgs("explorerTreeStatefulService|Explorer1", "r", 
																"hibernate", "organization",
																"svn-repositories", "svnRepositories",
																"svn://csp1/flower2", "svnRepository",
																"nuSterge", "svnFile"));			
		Boolean finalResult = true;
		finalResult &= SvnService.getInstance().checkout(context, selectionList, workingDirectoryPartialPath, "/checkoutWD", "7000", 0, true, false, false, "checkedOutProject");
		assertEquals(true, finalResult); // see if normal checkout works
	}

	@Test
	public void createSvnRepository() {
		Boolean result;
		ArrayList<PathFragment> parentPathFragment 
 		= getArrayOfPathFragmentsFromStringArgs("explorerTreeStatefulService|Explorer1", "r", 
 												"hibernate", "organization",
 												".svn-repositories", "svnRepositories");		
		// add 'bad url' repository:
		result = SvnService.getInstance().createSvnRepository(context, "nosuchurl", parentPathFragment);
		assertEquals(false, result);		
		// add existing repository:
		result = SvnService.getInstance().createSvnRepository(context, "svn://csp1/flower2/nuSterge", parentPathFragment);
		assertEquals(true, result);		 		
	}
	
	@Test
	public void commit() {		
		String filename = "9"; // after test is successful, increment by 1. from time to time clear remote folder of files
		// add a new file:
		File f = createFile(workspacePath + "hibernate\\commonWD\\commonProject\\" + filename);
		// commit:
		ArrayList<FileDto> selectionForCommit = new ArrayList<>();
		FileDto dto = new FileDto();
		dto.setLabel(filename);
		dto.setPath(f.getAbsolutePath());
		dto.setStatus("unversioned");
		selectionForCommit.add(dto);
		Boolean result = SvnService.getInstance().commit(context, selectionForCommit, "new message", false);
		assertEquals(true, result); //after each successful run filename must be changed or corresponding remote file deleted, in order for future tests to succeed
		
	}
	
	@Test
	public void addToSvnIgnore() {	
		String fileName = "qqxyz";
		// add a new file
		File f = createFile(workspacePath + "hibernate\\commonWD\\commonProject\\" + fileName);
		// add to svn ignore
		ArrayList<ArrayList<PathFragment>> selectionForIgnore = new ArrayList<>();
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
				fail("");
			}
		}
		assertEquals(1, modDtos.getFiles().size());
		f.delete();
	}
	
	@Test
	public void revert() {
		String filename = "ergh";
		// add a new file
		File f = createFile(workspacePath + "hibernate\\commonWD\\commonProject\\" + filename);
		// revert
		FileDto fd = new FileDto();
		fd.setPath(f.getAbsolutePath());
		fd.setStatus("unversioned");
		ArrayList<FileDto> array = new ArrayList<>();
		array.add(fd);
		SvnService.getInstance().revert(context, array);
		// check for differences
		ArrayList<PathFragment> arrayOfPaths = getArrayOfPathFragmentsFromStringArgs(
						"explorerTreeStatefulService|Explorer1", "r", 
						"hibernate", "organization",
						"workingDirectories", "workingDirectories",
						"commonWD", "workingDirectory",
						"commonProject", "project");
		ArrayList<ArrayList<PathFragment>> x = new ArrayList<>();
		x.add(arrayOfPaths);
		GetModifiedFilesDto modDtos = SvnService.getInstance().getDifferences(context, x);
		assertEquals(true, modDtos.getFiles().size()==0);
	}
	
	@Test
	public void addToVersionControl() {
		String filename = "blablaasd";
		// add a new file
		File f = createFile(workspacePath + "hibernate\\commonWD\\commonProject\\" + filename);
		// add file to version control
		ArrayList<ArrayList<PathFragment>> selectionForAddToVersion = new ArrayList<>();
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
		assertEquals(1, modDtos.getFiles().size());
		assertEquals("added", modDtos.getFiles().get(0).getStatus());
	}
	
	@Test
	public void markResolved() {
		// get two files from two identical versions of a file from repository
		String filename = "giej";
		File f1 = new File(workspacePath + "hibernate\\markResolvedWD\\markResolvedProject1\\66zWzz33XzYz");
		File f2 = new File(workspacePath + "hibernate\\markResolvedWD\\markResolvedProject2\\66zWzz33XzYz");
		// write different stuff into both files:
		try {
			writeFile(f1.getAbsolutePath(), "a");
			writeFile(f2.getAbsolutePath(), "b");
		} catch (IOException e) {
			e.printStackTrace(); // won't ever get here
		}
		// commit the first project
		ArrayList<FileDto> selectionForCommit = new ArrayList<>();
		FileDto dto = new FileDto();
		dto.setLabel(filename);
		dto.setPath(workspacePath + "hibernate\\markResolvedWD\\markResolvedProject1\\66zWzz33XzYz");
		dto.setStatus("modified");
		selectionForCommit.add(dto);
		Boolean res = SvnService.getInstance().commit(context, selectionForCommit, "new message", false);
		assertEquals(true, res);
		// update the second project
		ArrayList<ArrayList<PathFragment>> selectionForUpdate = new ArrayList<>();
		selectionForUpdate.add(getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", 
				"hibernate", "organization",
				"workingDirectories", "workingDirectories",
				"markResolvedWD", "workingDirectory",
				"markResolvedProject2", "project"));			
		SvnService.getInstance().updateToHEAD(context, selectionForUpdate);
		// get differences
		GetModifiedFilesDto modDtos = SvnService.getInstance().getDifferences(context, selectionForUpdate);
		assertEquals(true, modDtos.getFiles().size()>0);
		for (FileDto fdto : modDtos.getFiles()) {
			if (!fdto.getStatus().equals("modified")) {
				fail();
			}
		}						
	}
	
	@AfterClass
	public static void undoChanges() {
		// delete checked out working directory along with content
		File f = new File(workspacePath + "hibernate\\commonWD");
		f = new File(workspacePath + "hibernate\\checkoutWD");
		f.delete();		
		f = new File(workspacePath + "hibernate\\markResolvedWD");
		f.delete();
	}

}

