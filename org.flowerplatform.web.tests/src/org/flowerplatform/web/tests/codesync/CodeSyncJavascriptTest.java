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
package org.flowerplatform.web.tests.codesync;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.flowerplatform.codesync.code.javascript.CodeSyncCodeJavascriptPlugin;
import org.flowerplatform.codesync.code.javascript.parser.Parser;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode;
import org.flowerplatform.codesync.code.javascript.remote.JavascriptClassDiagramOperationsService;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.emf_model.notation.ExpandableNode;
import org.flowerplatform.emf_model.notation.View;
import org.flowerplatform.web.communication.RecordingTestWebCommunicationChannel;
import org.flowerplatform.web.projects.remote.ProjectsService;
import org.flowerplatform.web.tests.TestUtil;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.crispico.flower.mp.codesync.base.CodeSyncEditableResource;
import com.crispico.flower.mp.codesync.base.Match;
import com.crispico.flower.mp.codesync.base.communication.CodeSyncEditorStatefulService;
import com.crispico.flower.mp.codesync.code.CodeSyncCodePlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncJavascriptTest {
	
	public static final String PROJECT = "codesync_javascript";
	public static final String DIR = TestUtil.getResourcesDir(CodeSyncJavascriptTest.class);
	public static final String INITIAL_TO_BE_COPIED = DIR + TestUtil.INITIAL_TO_BE_COPIED + "/javascript";
	public static final String EXPECTED = DIR + TestUtil.EXPECTED + "/javascript/";
	
	private static CommunicationChannel communicationChannel = new RecordingTestWebCommunicationChannel();

	private static DiagramEditorStatefulService diagramEditorStatefulService;
	
	private static CodeSyncEditorStatefulService codeSyncEditorStatefulService;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TestUtil.copyFilesAndCreateProject(new ServiceInvocationContext(communicationChannel), INITIAL_TO_BE_COPIED, PROJECT);
		diagramEditorStatefulService = (DiagramEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry()
				.getService("diagramEditorStatefulService");
		assertNotNull("DiagramEditorStatefulService was not registered", diagramEditorStatefulService);
		codeSyncEditorStatefulService = (CodeSyncEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry()
				.getService("codeSyncEditorStatefulService");
		assertNotNull("CodeSyncEditorStatefulService was not registered", codeSyncEditorStatefulService);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		IProject project = getProject();
		codeSyncEditorStatefulService.cancelSelectedActions(project.getFullPath().toString(), false);
		
		diagramEditorStatefulService.subscribeClientForcefully(communicationChannel, getDiagramEditableResourcePath());
	}
	
	@After
	public void tearDown() throws Exception {
		diagramEditorStatefulService.unsubscribeAllClientsForcefully(getDiagramEditableResourcePath(), false);
	}

	public static final String TABLE = "Companies.html";
	
	@Test
	public void testTable() {
		//////////////////////////////////////////////////
		// Step 1: add to diagram
		//////////////////////////////////////////////////
		
		String fileName = TABLE;
		List<Object> tableParameters = getList(
				"htmlFile", 
				"name", 
				false, 
				getParameters(new String[] { "name", fileName, "tableId", "companies-list", "headerRowId", "companies-list-header" }), 
				"Table", 
				null, 
				null);
		invokeServiceMethod(tableParameters);
		View table = findByName(fileName);
		List<Object> tableHeaderEntryParameters = getList(
				"htmlTableHeaderEntry",
				"title",
				false,
				getParameters(new String[] { "title", "Logo" }),
				"TableHeaderEntry",
				table.eResource().getURIFragment(table),
				null);
		invokeServiceMethod(tableHeaderEntryParameters);
		
		//////////////////////////////////////////////////
		// Step 2: sync -> generate Regex tree + file
		//////////////////////////////////////////////////
		
		IFile file = getFile(fileName);
		CodeSyncCodePlugin.getInstance().getCodeSyncElement(getProject(), file, CodeSyncCodeJavascriptPlugin.TECHNOLOGY, communicationChannel, true);
		String editableResourcePath = getProject().getFullPath().toString();
		StatefulServiceInvocationContext context = new StatefulServiceInvocationContext(communicationChannel);
		codeSyncEditorStatefulService.synchronize(context, editableResourcePath);
		codeSyncEditorStatefulService.applySelectedActions(context, editableResourcePath, false);
		
		//////////////////////////////////////////////////
		// Step 3: compare generated code
		//////////////////////////////////////////////////
		
		testGeneratedFileContent(fileName);
		
		Parser parser = new Parser();
		RegExAstNode root = parser.parse(
				ProjectsService.getInstance().getFileFromProjectWrapperResource(getFile(fileName)));
		testParsedAst(null, root);
	}
	
	@Test
	public void testTableItem() {
		fail("Not yet implemented");
	}

	@Test
	public void testForm() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testBackboneClass() {
		fail("Not yet implemented");
	}
	
	protected void testGeneratedFileContent(String fileName) {
		String expected = getExpectedFileContent(fileName);
		String actual = getActualFileContent(fileName);
		assertEquals("Generated code is not as expected", expected, actual);
	}
	protected void testParsedAst(List<Object> expected, RegExAstNode actual) {
	}
	
	///////////////////////////
	// Utils
	///////////////////////////
	
	private IProject getProject() {
		return CodeSyncTestSuite.getProject(PROJECT);
	}
	
	private IFile getFile(String fileName) {
		return CodeSyncTestSuite.getFile("/" + PROJECT + "/js/" + fileName);
	}
	
	private String getExpectedFileContent(String fileName) {
		return TestUtil.readFile(EXPECTED + fileName);
	}
	
	private String getActualFileContent(String fileName) {
		return TestUtil.readFile(TestUtil.getWorkspaceResourceAbsolutePath(
				getFile(fileName).getFullPath().toString()));
	}
	
	private String getDiagramEditableResourcePath() {
		return "org/ws_trunk/" + PROJECT + "/NewDiagram1.notation";
	}
	
	private void invokeServiceMethod(List<Object> parameters) {
		InvokeServiceMethodServerCommand cmd = new InvokeServiceMethodServerCommand();
		cmd.setServiceId(JavascriptClassDiagramOperationsService.SERVICE_ID);
		cmd.setMethodName("addElement");
		cmd.setParameters(parameters);
		cmd.setCommunicationChannel(communicationChannel);
		
		diagramEditorStatefulService.attemptUpdateEditableResourceContent(new StatefulServiceInvocationContext(communicationChannel), getDiagramEditableResourcePath(), cmd);
	}
	
	private Map<String, String> getParameters(String[] parameters) {
		Map<String, String> result = new HashMap<String, String>();
		for (int i = 0; i < parameters.length; i += 2) {
			result.put(parameters[i], parameters[i + 1]);
		}
		return result;
	}
	
	/**
	 * We can't use {@link Arrays#asList(Object...)} because the context will later be added
	 * to the list and we'd get an {@link UnsupportedOperationException}.
	 */
	private List<Object> getList(Object... params) {
		List<Object> result = new ArrayList<Object>();
		for (Object param : params) {
			result.add(param);
		}
		return result;
	}
	
	private View findByName(String name) {
		DiagramEditableResource der = (DiagramEditableResource) diagramEditorStatefulService.getEditableResource(getDiagramEditableResourcePath());
		assertNotNull("Diagram not found", der);
		for (Iterator<EObject> it = der.getMainResource().getAllContents(); it.hasNext(); ) {
			View child = (View) it.next();
			CodeSyncElement cse = (CodeSyncElement) child.getDiagrammableElement();
			if (child instanceof ExpandableNode && cse != null && cse.getName().equals(name)) {
				return child;
			}
		}
		fail("No view for element with name " + name);
		return null;
	}
	
}
