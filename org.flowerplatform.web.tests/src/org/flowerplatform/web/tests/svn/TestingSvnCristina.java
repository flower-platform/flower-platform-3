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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.web.communication.RecordingTestWebCommunicationChannel;
import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;
import org.flowerplatform.web.entity.SVNRepositoryURLEntity;
import org.flowerplatform.web.svn.remote.SvnService;
import org.flowerplatform.web.svn.remote.dto.FileDto;
import org.flowerplatform.web.svn.remote.dto.GetModifiedFilesDto;
import org.hibernate.Query;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.SVNProviderPlugin;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.ISVNInfo;

/**
 * 
 * @author Cristina Necula
 * 
 */

public class TestingSvnCristina {

	private static final CommunicationChannel communicationChannel = new RecordingTestWebCommunicationChannel();;

	private static ServiceInvocationContext context;

	private static String uri;

	private static String username;

	private static String password;
	
	private static ArrayList<PathFragment> workingDirectoryPartialPath;
	
	private static String workspacePath;
	
	private static List<List<PathFragment>> selectionForFinalDelte;
	
	public String workspaceLocation = CommonPlugin.getInstance().getWorkspaceRoot().getAbsolutePath();

	@BeforeClass
	public static void first() throws SVNException {
		
		//info for login
		uri = "<svn://csp1:3690> flower2";
		username = "intern";
		password = "intern_crispico_fpwqoeri";
		
		context = new ServiceInvocationContext(communicationChannel);
		CommunicationPlugin.tlCurrentChannel.set(communicationChannel);
		
		selectionForFinalDelte = new ArrayList<>();
		
		//create working directory and checkout project for merge action
		workingDirectoryPartialPath 
		= getArrayOfPathFragmentsFromStringArgs("explorerTreeStatefulService|Explorer1", "r", 
												"hibernate", "organization",
												"workingDirectories", "workingDirectories");
		
		ArrayList<ArrayList<PathFragment>> selectionForCheckout = new ArrayList<>();
		selectionForCheckout.add(getArrayOfPathFragmentsFromStringArgs(
			       "explorerTreeStatefulService|Explorer1", "r", "hibernate",
			       "organization", ".svn-repositories", "svnRepositories", "svn://csp1/flower2", "svnRepository", 
			       "testing_do_not_delete", "svnFolder", "cristina", "svnFolder"));
		
		//create merge remote folder inside "cristina" folder for merge
		ArrayList<PathFragment> folder = createRemoteFolder("merge_test");
		selectionForFinalDelte.add(folder);
		
		File workspaceRoot = CommonPlugin.getInstance().getWorkspaceRoot();
		workspacePath = workspaceRoot.getPath() + "\\";
		
		//sometime fails due to absence of client 
		//if so, run again, only his class
		SvnService.getInstance().createFolderAndMarkAsWorkingDirectory(context, "commonWD", "hibernate");
		
		
		
		Boolean result = SvnService.getInstance().checkout(context, selectionForCheckout, workingDirectoryPartialPath, 
					"\\commonWD", "HEAD", 0, true, false, false, "");
		if (result == false){
			//need login
			InvokeServiceMethodServerCommand command = new InvokeServiceMethodServerCommand();
			command.setServiceId("svnService");
			command.setMethodName("checkout");
			command.setCommunicationChannel(communicationChannel);

			List<Object> parameters = new ArrayList<>();
			parameters.add(selectionForCheckout);
			parameters.add(workingDirectoryPartialPath);
			parameters.add("\\commonWD");
			parameters.add("HEAD");
			parameters.add(0);
			parameters.add(true);
			parameters.add(false);
			parameters.add(false);
			parameters.add("");
			command.setParameters(parameters);

			SvnService.tlCommand.set(command);
			SvnService.getInstance().login(context, uri, username, password, command);
		}
		
	}
	
	@AfterClass
	public static void clean(){
		SvnService.getInstance().deleteSvnAction(context, selectionForFinalDelte, "final");
	}
	
	public static void writeFile(String filename, String text) throws IOException {
	    FileWriter fstream = new FileWriter(filename, true); //true tells to append data.
	    BufferedWriter out = new BufferedWriter(fstream);
	    out.write(text);
	    out.close();			    
	}
	
	//create necessary folders and repos for testing
	private static void create() throws SVNException {
			
		//create "Cristina" folder in "testins_do_not_delete" svn remote folder
		ArrayList<PathFragment> parentPathFragment;
		Boolean result;
	    parentPathFragment = getArrayOfPathFragmentsFromStringArgs(
		       "explorerTreeStatefulService|Explorer1", "r", "hibernate",
		       "organization", ".svn-repositories", "svnRepositories", "svn://csp1/flower2", "svnRepository", 
		       "testing_do_not_delete", "svnFolder");
		result = SvnService.getInstance().createRemoteFolder(context, parentPathFragment, "cristina", "cristina create folder");
		if (result == false) {
			//user login

			//command from CommunicationChannel is null; had to create new one
			InvokeServiceMethodServerCommand command = new InvokeServiceMethodServerCommand();
			command.setServiceId("svnService");
			command.setMethodName("createRemoteFolder");
			command.setCommunicationChannel(communicationChannel);

			List<Object> parameters = new ArrayList<>();
			parameters.add(parentPathFragment);
			parameters.add("cristina");
			parameters.add("cristina create folder");
			command.setParameters(parameters);

			SvnService.tlCommand.set(command);
			SvnService.getInstance().login(context, uri, username, password, command);
			result = SvnService.getInstance().createRemoteFolder(context, parentPathFragment, "cristina", "cristina create folder");
		}
		assertEquals("Could not create folder for testing", true, result);
		
	}
	

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
	
	//function that creates a new remote folder inside "cristina" folder, testing login action
	//assume createRemoteFolder is correct
	public static ArrayList<PathFragment> createRemoteFolder(String folderName) throws SVNException{
		ArrayList<PathFragment> parentPathFragment;
		Boolean result;
	    parentPathFragment = getArrayOfPathFragmentsFromStringArgs(
		       "explorerTreeStatefulService|Explorer1", "r", "hibernate",
		       "organization", ".svn-repositories", "svnRepositories", "svn://csp1/flower2", "svnRepository", 
		       "testing_do_not_delete", "svnFolder", "cristina", "svnFolder");
	    
	    //change credentials to test login
	    
	    SvnService.getInstance().changeCredentials(context, "<svn://csp1:3690> flower2", "fgh", "bgb");
	    try {
	    	result = SvnService.getInstance().createRemoteFolder(context, parentPathFragment, folderName, folderName + " create folder");
	    } catch (Exception e) {
			//user login

			//command from CommunicationChannel is null; had to create new one
			InvokeServiceMethodServerCommand command = new InvokeServiceMethodServerCommand();
			command.setServiceId("svnService");
			command.setMethodName("createRemoteFolder");
			command.setCommunicationChannel(communicationChannel);

			List<Object> parameters = new ArrayList<>();
			parameters.add(parentPathFragment);
			parameters.add(folderName);
			parameters.add(folderName + " create folder");
			command.setParameters(parameters);

			SvnService.tlCommand.set(command);
			SvnService.getInstance().login(context, uri, username, password, command);
			result = true;
		}
		assertEquals("Could not create remote folder", true, result);
		if (result == true)
			return getArrayOfPathFragmentsFromStringArgs(
					"explorerTreeStatefulService|Explorer1", "r", "hibernate",
					"organization", ".svn-repositories", "svnRepositories", "svn://csp1/flower2", "svnRepository", 
					"testing_do_not_delete", "svnFolder", "cristina", "svnFolder", 
					folderName, "svnFolder");
		else
			return null;
	}

	//function creates a new svn repository, testing login action
	//assume CreateSvnRepository from SvnService is correct 
	public void createSvnRepository(String url) {
		Boolean result;
		ArrayList<PathFragment> parentPathFragment = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", ".svn-repositories", "svnRepositories");

		//change credentials to test login
	    SvnService.getInstance().changeCredentials(context, "<svn://csp1:3690> flower2", "fgh", "bgb");
		result = SvnService.getInstance().createSvnRepository(context, url, parentPathFragment);

		if(result == false){
			InvokeServiceMethodServerCommand command = new InvokeServiceMethodServerCommand();
			command.setServiceId("svnService");
			command.setMethodName("createSvnRepository");
			command.setCommunicationChannel(communicationChannel);

			List<Object> parameters = new ArrayList<>();
			parameters.add(url);
			parameters.add(parentPathFragment);
			command.setParameters(parameters);

			SvnService.tlCommand.set(command);
			SvnService.getInstance().login(context, uri, username, password, command);
			result = true;
			
		}
		assertEquals("Could not create repository", true, result);
	}

	//test delete remote folder with login action
	@Test
	public void deleteRemoteFolder() throws SVNException {
		Boolean result;
		//multiple selection test - 2 remote folders
		
		//first, create two remote folders
		ArrayList<PathFragment> one = createRemoteFolder("one");
		ArrayList<PathFragment> two = createRemoteFolder("two");

		//change credentials to test login
	    SvnService.getInstance().changeCredentials(context, "<svn://csp1:3690> flower2", "fgh", "bgb");
	    
		if(one != null && two != null){
		
			List<List<PathFragment>> listPathFragment = new ArrayList<>();
			listPathFragment.add(one);
			listPathFragment.add(two);
	
			//delete them
			try {
				result = SvnService.getInstance().deleteSvnAction(context, listPathFragment, "cc c");
			}catch (Exception e) {
				//user login; create new command
				InvokeServiceMethodServerCommand command = new InvokeServiceMethodServerCommand();
				command.setServiceId("svnService");
				command.setMethodName("deleteSvnAction");
				command.setCommunicationChannel(communicationChannel);
	
				List<Object> parameters = new ArrayList<>();
				parameters.add(listPathFragment);
				parameters.add("cc c");
				command.setParameters(parameters);
	
				SvnService.tlCommand.set(command);
				SvnService.getInstance().login(context, uri, username, password, command);
				result = true;
			}
			assertEquals("Could not delete selected folders", true, result);
			
			//check to see if folders were deleted
			//if folders were deleted, if you try to delete them again, result will be false
			//login should not be necessary
			result = SvnService.getInstance().deleteSvnAction(context, listPathFragment, "cc c");
			assertEquals("Files were not deleted!", false, result);
		}
		else 
			assertEquals("Delete method failed because folders were not created", true, false);
		
	}
	
	//test delete svn repository with login action
	@Test
	public void deleteSvnRepository() {
		Boolean result;
		// first create svn repository
		String url = "svn://csp1/flower2/testing_do_not_delete";
		createSvnRepository(url);
	    
		// delete repo
		List<List<PathFragment>> listPathFragment = new ArrayList<>();
		listPathFragment.add(getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", ".svn-repositories", "svnRepositories", url,
				"svnRepository"));
		
		CommunicationPlugin.tlCurrentChannel.set(communicationChannel);
		try {
			result = SvnService.getInstance().deleteSvnAction(context, listPathFragment, "delete svn repo");
		} catch (Exception e){
			//user login; create new command
			CommunicationPlugin.tlCurrentChannel.set(communicationChannel);
			InvokeServiceMethodServerCommand command = new InvokeServiceMethodServerCommand();
			command.setServiceId("svnService");
			command.setMethodName("deleteSvnAction");
			command.setCommunicationChannel(communicationChannel);

			List<Object> parameters = new ArrayList<>();
			parameters.add(listPathFragment);
			parameters.add("delete svn repo");
			command.setParameters(parameters);

			SvnService.tlCommand.set(command);
			SvnService.getInstance().login(context, uri, username, password, command);
			result = SvnService.getInstance().deleteSvnAction(context, listPathFragment, "delete svn repo");
		}
		assertEquals("Could not delete selected folders", true, result);
		
		//check to see if repo was deleted from database
		new DatabaseOperationWrapper(new DatabaseOperation() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				try {
					Query q = wrapper.getSession()
							.createQuery(String.format("SELECT e from %s e where e.name = '%s' and e.organization.name = '%s'", 
										 SVNRepositoryURLEntity.class.getSimpleName(),
										 "svn://csp1/distr",
										 "hibernate"));
					assertEquals("SvnRepository still in database", 0, q.list().size());
				} catch (Exception e) {					
				}
			}
		});
		
	}

	
	//could not use this function because status of project - unversioned 
	//had to introduce merge specifications manually
	public List<String> getMergeSpecs() {

		ArrayList<PathFragment> pathFragment = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer 1", "r", "hibernate",
				"organization", "workingDirectories", "workingDirectories",
				"ws_trunk", "ws_trunk", "ws_trunk", "project");
		ArrayList<ArrayList<PathFragment>> listPathFragment = new ArrayList<>();
		listPathFragment.add(pathFragment);
		CommunicationPlugin.tlCurrentChannel.set(communicationChannel);
		List<String> specs = new ArrayList<String>();
		File[] files = SvnService.getInstance().getFilesForSelectionList(listPathFragment);
		String workingDirectoryPath = SvnService.getInstance().getDirectoryFullPathFromPathFragments(pathFragment);
		try {
			ISVNClientAdapter myClientAdapter;
			myClientAdapter = SVNProviderPlugin.getPlugin().getSVNClient();
			ISVNInfo info = myClientAdapter.getInfo(new File(workingDirectoryPath));
			specs.add(info.getUrlString());
			specs.add(files[0].getAbsolutePath());
		} catch (Exception e) {
			if (SvnService.getInstance().isAuthentificationException(e)){
				specs.add("loginmessage");
			}
		}
		return specs;
		
	}


	@Test
	public void mergeWithNewFilesAdded() throws SVNException, IOException{
		
		ArrayList<PathFragment> folder = new ArrayList<>();
		
		//need new remote folder on svn to merge
		try {
			folder = createRemoteFolder("to_merge");
		} catch (Exception e){
			//already exists
			folder = getArrayOfPathFragmentsFromStringArgs(
					"explorerTreeStatefulService|Explorer1", "r", "hibernate",
					"organization", ".svn-repositories", "svnRepositories", "svn://csp1/flower2", "svnRepository", 
					"testing_do_not_delete", "svnFolder", "cristina", "svnFolder", 
					"to_merge", "svnFolder");
		}
		selectionForFinalDelte.add(folder);
		
		//checkout folder
		ArrayList<ArrayList<PathFragment>> selectionForCheckout = new ArrayList<>();
		selectionForCheckout.add(folder);
		try { 
			SvnService.getInstance().checkout(context, selectionForCheckout, workingDirectoryPartialPath, 
				"\\commonWD", "HEAD", 0, true, false, false, "");
		} catch (Exception e){
			e.printStackTrace();
		}
			
		//create new files
		File f1 = new File(workspacePath + "hibernate\\commonWD\\to_merge\\" + "1");
		f1.createNewFile();
		File f2 = new File(workspacePath + "hibernate\\commonWD\\to_merge\\" + "2");
		f2.createNewFile();
		
		//commit
		ArrayList<FileDto> selectionForCommit = new ArrayList<>();		
		FileDto dto = new FileDto();
		dto.setLabel("1");
		dto.setPathFromRoot(f1.getAbsolutePath().substring(workspaceLocation.length()));
		dto.setStatus("unversioned");
		selectionForCommit.add(dto);
		FileDto dto2 = new FileDto();
		dto2.setLabel("2");
		dto2.setPathFromRoot(f2.getAbsolutePath().substring(workspaceLocation.length()));
		dto2.setStatus("unversioned");		
		selectionForCommit.add(dto2);
		SvnService.getInstance().commit(context, selectionForCommit, "new message", false);
		
		//merge the files
		
		//local folder path for merge
		ArrayList<PathFragment> path = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer 1", "r", "hibernate",
				"organization", "workingDirectories", "workingDirectories",
				"commonWD", "workingDirectory", "cristina", "project", "merge_test", "project");
		ArrayList<ArrayList<PathFragment>> listPathFragment = new ArrayList<>();
		listPathFragment.add(path);
		File[] files = SvnService.getInstance().getFilesForSelectionList(listPathFragment);
		
		String url1 = "svn://csp1/flower2/testing_do_not_delete/cristina/merge_test";
		String url2 = "svn://csp1/flower2/testing_do_not_delete/cristina/to_merge";
		
		long revision1 = -1;
		long revision2 = -1;
		
		boolean force = false;
		boolean ignoreAncestry = false;
		
		boolean result = false;
		
		SvnService.getInstance().changeCredentials(context, "", "", "");
		
		try {
			result = SvnService.getInstance().merge(context, files[0].getAbsolutePath(),
					listPathFragment, url1, revision1, url2, revision2, force, ignoreAncestry);
		} catch (SVNException e) {
			// user login
			InvokeServiceMethodServerCommand command = new InvokeServiceMethodServerCommand();
			command.setServiceId("svnService");
			command.setMethodName("merge");
			command.setCommunicationChannel(communicationChannel);

			List<Object> parameters = new ArrayList<>();
			parameters.add(files[0].getAbsolutePath());
			parameters.add(listPathFragment);
			parameters.add(url1);
			parameters.add(revision1);
			parameters.add(url2);
			parameters.add(revision2);
			parameters.add(force);
			parameters.add(ignoreAncestry);
			command.setParameters(parameters);

			SvnService.tlCommand.set(command);
			SvnService.getInstance().login(context, uri, username, password, command);
			result = SvnService.getInstance().merge(context, files[0].getAbsolutePath(),
					listPathFragment, url1, revision1, url2, revision2, force, ignoreAncestry);
		}
		assertEquals("Merge operation did not succeed", true, result);
		File check1 = new File(workspacePath + "hibernate\\commonWD\\cristina\\merge_test\\1");
		result = check1.exists();
		assertEquals("Files does not exist on the disk", true, result);
		File check2 = new File(workspacePath + "hibernate\\commonWD\\cristina\\merge_test\\2");
		result = check2.exists();
		assertEquals("Files does not exist on the disk", true, result);
		
	}
	
	@Test
	public void mergeModifiedFiles() throws IOException, SVNException{
		
		ArrayList<PathFragment> folder = new ArrayList<>();
		
		//change something to file, then commit it before merge
		boolean modified = false;
		File f1 = new File(workspacePath + "hibernate\\commonWD\\cristina\\merge_test\\" + "1");
		if (f1.exists())
			modified = true;
		else 
			f1.createNewFile();
		
		FileWriter fstream1 = new FileWriter(workspacePath + "hibernate\\commonWD\\cristina\\merge_test\\" + "1", true);
	    BufferedWriter out1 = new BufferedWriter(fstream1);
	    out1.write("first line\n");
	    out1.close();
	    
	    //commit
	    ArrayList<FileDto> selectionForCommit = new ArrayList<>();		
	    FileDto dto = new FileDto();
	    dto.setLabel("1");
	    dto.setPathFromRoot(f1.getAbsolutePath().substring(workspaceLocation.length()));
	    if (modified)
	    	dto.setStatus("modified");
	    else
	    	dto.setStatus("unversioned");
	    selectionForCommit.add(dto);
	  	SvnService.getInstance().commit(context, selectionForCommit, "new message", false);
		
		//need new remote folder on svn to merge
		try {
			folder = createRemoteFolder("to_merge_modified");
		} catch (Exception e){
			//already exists
			folder = getArrayOfPathFragmentsFromStringArgs(
					"explorerTreeStatefulService|Explorer1", "r", "hibernate",
					"organization", ".svn-repositories", "svnRepositories", "svn://csp1/flower2", "svnRepository", 
					"testing_do_not_delete", "svnFolder", "cristina", "svnFolder", 
					"to_merge_modified", "svnFolder");
		}
		selectionForFinalDelte.add(folder);
		
		//checkout folder
		ArrayList<ArrayList<PathFragment>> selectionForCheckout = new ArrayList<>();
		selectionForCheckout.add(folder);
		try { 
			SvnService.getInstance().checkout(context, selectionForCheckout, workingDirectoryPartialPath, 
				"\\commonWD", "HEAD", 0, true, false, false, "");
		} catch (Exception e){
			e.printStackTrace();
		}
			
		//create new files
		f1 = new File(workspacePath + "hibernate\\commonWD\\to_merge_modified\\" + "1");
		f1.createNewFile();
		
		FileWriter fstream2 = new FileWriter(workspacePath + "hibernate\\commonWD\\to_merge_modified\\" + "1", true);
	    BufferedWriter out2 = new BufferedWriter(fstream2);
	    out2.write("tests\n" + "added some content");
	    out2.close();
	    
		//commit
		selectionForCommit = new ArrayList<>();		
		dto = new FileDto();
		dto.setLabel("1");
		dto.setPathFromRoot(f1.getAbsolutePath().substring(workspaceLocation.length()));
		dto.setStatus("unversioned");
		selectionForCommit.add(dto);
		SvnService.getInstance().commit(context, selectionForCommit, "new message", false);
		
		//merge the files
		
		//local folder path for merge
		ArrayList<PathFragment> path = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer 1", "r", "hibernate",
				"organization", "workingDirectories", "workingDirectories",
				"commonWD", "workingDirectory", "cristina", "project", "merge_test", "project");
		ArrayList<ArrayList<PathFragment>> listPathFragment = new ArrayList<>();
		listPathFragment.add(path);
		File[] files = SvnService.getInstance().getFilesForSelectionList(listPathFragment);
		
		String url1 = "svn://csp1/flower2/testing_do_not_delete/cristina/merge_test";
		String url2 = "svn://csp1/flower2/testing_do_not_delete/cristina/to_merge_modified";
		
		long revision1 = -1;
		long revision2 = -1;
		
		boolean force = false;
		boolean ignoreAncestry = false;
		
		boolean result = false;
		
		SvnService.getInstance().changeCredentials(context, "", "", "");
		
		try {
			result = SvnService.getInstance().merge(context, files[0].getAbsolutePath(),
					listPathFragment, url1, revision1, url2, revision2, force, ignoreAncestry);
		} catch (SVNException e) {
			// user login
			InvokeServiceMethodServerCommand command = new InvokeServiceMethodServerCommand();
			command.setServiceId("svnService");
			command.setMethodName("merge");
			command.setCommunicationChannel(communicationChannel);

			List<Object> parameters = new ArrayList<>();
			parameters.add(files[0].getAbsolutePath());
			parameters.add(listPathFragment);
			parameters.add(url1);
			parameters.add(revision1);
			parameters.add(url2);
			parameters.add(revision2);
			parameters.add(force);
			parameters.add(ignoreAncestry);
			command.setParameters(parameters);

			SvnService.tlCommand.set(command);
			SvnService.getInstance().login(context, uri, username, password,
					command);
			result = SvnService.getInstance().merge(context,
					files[0].getAbsolutePath(), listPathFragment, url1,
					revision1, url2, revision2, force, ignoreAncestry);
		}
		assertEquals("Merge operation did not succeed", true, result);
		
		//test to see if the new file was modified
		ArrayList<ArrayList<PathFragment>> selection = new ArrayList<>();
		selection.add(getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", 
				"hibernate", "organization",
				"workingDirectories", "workingDirectories",
				"commonWD", "workingDirectory",
				"cristina", "project",
				"merge_test", "project"));
		GetModifiedFilesDto modDtos = SvnService.getInstance().getDifferences(context, selection);
		assertEquals("File was not modified", true, modDtos.getFiles().size()>0);
	}
}
