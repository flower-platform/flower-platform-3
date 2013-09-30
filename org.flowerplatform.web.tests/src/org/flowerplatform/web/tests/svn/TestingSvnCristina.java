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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.web.communication.RecordingTestWebCommunicationChannel;
import org.flowerplatform.web.svn.remote.SvnService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tigris.subversion.subclipse.core.SVNException;
import org.tigris.subversion.subclipse.core.SVNProviderPlugin;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.ISVNInfo;

public class TestingSvnCristina {

	private static final CommunicationChannel communicationChannel = new RecordingTestWebCommunicationChannel();;

	private static ServiceInvocationContext context;

	private static String uri;

	private static String username;

	private static String password;
	
	private static ArrayList<PathFragment> workingDirectoryPartialPath;
	
	private static String workspacePath;

	@BeforeClass
	public static void first() throws SVNException {
		
		//info for login
		uri = "<svn://csp1:3690> flower2";
		username = "intern";
		password = "intern_crispico_fpwqoeri";
		
		context = new ServiceInvocationContext(communicationChannel);
		
		//create working directory and checkout project for merge action
		workingDirectoryPartialPath 
		= getArrayOfPathFragmentsFromStringArgs("explorerTreeStatefulService|Explorer1", "r", 
												"hibernate", "organization",
												"workingDirectories", "workingDirectories");
		
		List<List<PathFragment>> selectionForCheckout = new ArrayList<>();
		selectionForCheckout.add(getArrayOfPathFragmentsFromStringArgs("explorerTreeStatefulService|Explorer1", "r", 
				"hibernate", "organization",
				"svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository",
				"test", "svnFile"));
		
		selectionForCheckout.add(getArrayOfPathFragmentsFromStringArgs("explorerTreeStatefulService|Explorer1", "r", 
				"hibernate", "organization",
				"svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository",
				"nuSterge", "svnFile"));
		
		File workspaceRoot = CommonPlugin.getInstance().getWorkspaceRoot();
		workspacePath = workspaceRoot.getPath() + "\\";
		
		//sometime fails due to absence of client 
		CommunicationPlugin.tlCurrentChannel.set(communicationChannel);
		SvnService.getInstance().createFolderAndMarkAsWorkingDirectory(context, "commonWD", "hibernate");
		SvnService.getInstance().checkout(context, selectionForCheckout, workingDirectoryPartialPath, 
				"\\commonWD", "HEAD", 0, true, false, false, "");
		createRepositories();
	}
	
	private static void createRepositories() throws SVNException {
		
		ArrayList<PathFragment> parentPathFragment;
		Boolean result;
		parentPathFragment = getArrayOfPathFragmentsFromStringArgs(
		       "explorerTreeStatefulService|Explorer1", "r", "hibernate",
		       "organization", ".svn-repositories", "svnRepositories", "svn://csp1/flower2", "svnRepository");
		result = SvnService.getInstance().createRemoteFolder(context, parentPathFragment, "merge", "");
		assertEquals(true, result);
		
		parentPathFragment = getArrayOfPathFragmentsFromStringArgs(
			       "explorerTreeStatefulService|Explorer1", "r", "hibernate",
			       "organization", ".svn-repositories", "svnRepositories", "svn://csp1/flower2", "svnRepository");
		result = SvnService.getInstance().createRemoteFolder(context, parentPathFragment, "merge_test", "");
		assertEquals(true, result);
		
		
	}
	
	@AfterClass
	
	public static void deleteCreatedReposForMerge(){
		
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

	@Test
	public void createSvnRepository() {
		Boolean result;
		ArrayList<PathFragment> parentPathFragment = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", ".svn-repositories", "svnRepositories");

		// add 'bad url' repository:
		result = SvnService.getInstance().createSvnRepository(context,
				"nosuchurl", parentPathFragment);

		assertEquals(false, result);

		// add existing repository:
		result = SvnService.getInstance().createSvnRepository(context, "svn://csp1/flower2/tst", parentPathFragment);
		if (result == false) {
			//user login
			CommunicationPlugin.tlCurrentChannel.set(communicationChannel);

			//command from CommunicationChannel is null; had to create new one
			InvokeServiceMethodServerCommand command = new InvokeServiceMethodServerCommand();
			command.setServiceId("svnService");
			command.setMethodName("createSvnRepository");
			command.setCommunicationChannel(communicationChannel);

			List<Object> parameters = new ArrayList<>();
			parameters.add("svn://csp1/flower2/tst");
			parameters.add(parentPathFragment);
			command.setParameters(parameters);

			SvnService.tlCommand.set(command);
			SvnService.getInstance().login(context, uri, username, password, command);
			result = SvnService.getInstance().createSvnRepository(context, "svn://csp1/flower2/tst", parentPathFragment);
		}
		assertEquals(true, result);
	}

	@Test
	public void deleteSvnAction() {
		Boolean result;
		
		//multiple selection test
		//change this paths to test delete for another files
		ArrayList<PathFragment> pathFragment = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", ".svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository", "1", "svnFolder");
		ArrayList<PathFragment> pathFragment1 = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer1", "r", "hibernate",
				"organization", ".svn-repositories", "svnRepositories",
				"svn://csp1/flower2", "svnRepository", "2", "svnFolder");
		List<List<PathFragment>> listPathFragment = new ArrayList<>();
		listPathFragment.add(pathFragment1);
		listPathFragment.add(pathFragment);

		CommunicationPlugin.tlCurrentChannel.set(communicationChannel);
		try {
			result = SvnService.getInstance().deleteSvnAction(context, listPathFragment, "");
		} catch (Exception e) {
			//user login; create new command
			CommunicationPlugin.tlCurrentChannel.set(communicationChannel);
			InvokeServiceMethodServerCommand command = new InvokeServiceMethodServerCommand();
			command.setServiceId("svnService");
			command.setMethodName("deleteSvnAction");
			command.setCommunicationChannel(communicationChannel);

			List<Object> parameters = new ArrayList<>();
			parameters.add(listPathFragment);
			parameters.add("");
			command.setParameters(parameters);

			SvnService.tlCommand.set(command);
			if (command.getParameters().get(0).getClass() == StatefulServiceInvocationContext.class)
				command.getParameters().remove(0);

			SvnService.tlCommand.set(command);
			SvnService.getInstance().login(context, uri, username, password, command);
			result = SvnService.getInstance().deleteSvnAction(context, listPathFragment, "delete comment");
		}
		//files do not exist anymore in Svn Repositories
		//create them and then change false with true to test delete action
		assertEquals(false, result);
	}

	
	//could not use this function; had to introduce merge specifications manually
	public List<String> getMergeSpecs() {

		ArrayList<PathFragment> pathFragment = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer 1", "r", "hibernate",
				"organization", "workingDirectories", "workingDirectories",
				"ws_trunk", "ws_trunk", "ws_trunk", "project");
		List<List<PathFragment>> listPathFragment = new ArrayList<>();
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

	//merge action is tested without login
	
	@Test
	public void mergeIdentical() {
		
		//local file path for merge action
		ArrayList<PathFragment> path = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer 1", "r", "hibernate",
				"organization", "workingDirectories", "workingDirectories",
				"commonWD", "workingDirectory", "nuSterge", "project");
		
		List<List<PathFragment>> listPathFragment = new ArrayList<>();
		listPathFragment.add(path);
		File[] files = SvnService.getInstance().getFilesForSelectionList(listPathFragment);
		
		//merge two identical files
		String url1 = "svn://csp1/flower2/nuSterge";
		String url2 = "svn://csp1/flower2/nuSterge";
		
		long revision1 = 0;
		long revision2 = 0;
		
		boolean force = false;
		boolean ignoreAncestry = false;
		
		boolean result = false;
		
		try {
			result = SvnService.getInstance().merge(context, files[0].getAbsolutePath(),
					listPathFragment, url1, revision1, url2, revision2, force, ignoreAncestry);
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(true, result);

	}
	
	@Test
	public void mergeDifferentProjects(){
		//local file path for merge
		ArrayList<PathFragment> path = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer 1", "r", "hibernate",
				"organization", "workingDirectories", "workingDirectories",
				"commonWD", "workingDirectory", "test", "project", "test", "project");
		
		List<List<PathFragment>> listPathFragment = new ArrayList<>();
		listPathFragment.add(path);
		File[] files = SvnService.getInstance().getFilesForSelectionList(listPathFragment);
		
		//merge different projects
		String url1 = "svn://csp1/flower2/test";
		String url2 = "svn://csp1/flower2/merge";
		
		long revision1 = -1;
		long revision2 = -1;
		
		boolean force = false;
		boolean ignoreAncestry = false;
		
		boolean result = false;
		
		try {
			result = SvnService.getInstance().merge(context, files[0].getAbsolutePath(),
					listPathFragment, url1, revision1, url2, revision2, force, ignoreAncestry);
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(true, result);
		
	}
	
	@Test
	public void mergeModified(){
		//local file path for merge
		List<PathFragment> path = getArrayOfPathFragmentsFromStringArgs(
				"explorerTreeStatefulService|Explorer 1", "r", "hibernate",
				"organization", "workingDirectories", "workingDirectories",
				"commonWD", "workingDirectory", "test", "project");
		
		List<List<PathFragment>> listPathFragment = new ArrayList<>();
		listPathFragment.add(path);
		File[] files = SvnService.getInstance().getFilesForSelectionList(listPathFragment);
		
		//added new file and folder in svnRepo, should have new ones local after merge
				String url1 = "svn://csp1/flower2/test";
				String url2 = "svn://csp1/flower2/merge_test";
				
				long revision1 = -1;
				long revision2 = -1;
				
				boolean force = false;
				boolean ignoreAncestry = false;
				
				boolean result = false;
				
				try {
					result = SvnService.getInstance().merge(context, files[0].getAbsolutePath(),
							listPathFragment, url1, revision1, url2, revision2, force, ignoreAncestry);
				} catch (SVNException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				assertEquals(true, result);
	
	}

}
