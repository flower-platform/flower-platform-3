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
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.flowerplatform.codesync.code.javascript.CodeSyncCodeJavascriptPlugin;
import org.flowerplatform.codesync.code.javascript.parser.Parser;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstFactory;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNodeParameter;
import org.flowerplatform.codesync.code.javascript.remote.JavascriptClassDiagramOperationsService;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.emf_model.notation.Diagram;
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
import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
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
		
		CodeSyncCodePlugin.getInstance().CSE_MAPPING_FILE_LOCATION = "/CSE.notation";
		CodeSyncCodePlugin.getInstance().ACE_FILE_LOCATION = "/ACE.notation";
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		File to = new File(TestUtil.getWorkspaceResourceAbsolutePath("") + "/org/ws_trunk/" + PROJECT);
		FileUtils.copyDirectory(new File(INITIAL_TO_BE_COPIED), to);
		diagramEditorStatefulService.subscribeClientForcefully(communicationChannel, getDiagramEditableResourcePath());
	}
	
	@After
	public void tearDown() throws Exception {
		String codeSyncEditableResourcePath = getProject().getFullPath().toString();
		codeSyncEditorStatefulService.cancelSelectedActions(codeSyncEditableResourcePath, false);
		diagramEditorStatefulService.unsubscribeAllClientsForcefully(getDiagramEditableResourcePath(), false);
	}

	public static final String TABLE = "Companies.html";
	
	@Test
	public void testTable() {
		//////////////////////////////////////////////////
		// Step 1: add to diagram
		//////////////////////////////////////////////////
		
		String fileName = TABLE;
		List<RegExAstNodeParameter> tableParameters = new ArrayList<RegExAstNodeParameter>();
		addParameter(tableParameters, "name", fileName, 0, 0);
		addParameter(tableParameters, "tableId", "companies-list", 79, 14);
		addParameter(tableParameters, "headerRowId", "companies-list-header", 106, 21);
		List<Object> table = getList(
				"htmlFile", 
				"name", 
				false, 
				getParameters(tableParameters), 
				"Table", 
				null, 
				null);
		invokeServiceMethod(table);
		View tableView = findByName(fileName);
		List<RegExAstNodeParameter> tableHeaderEntryParameters = new ArrayList<RegExAstNodeParameter>();
		addParameter(tableHeaderEntryParameters, "title", "Logo", 137, 4);
		List<Object> tableHeaderEntry = getList(
				"htmlTableHeaderEntry",
				"title",
				false,
				getParameters(tableHeaderEntryParameters),
				"TableHeaderEntry",
				tableView.eResource().getURIFragment(tableView),
				null);
		invokeServiceMethod(tableHeaderEntry);
		
		//////////////////////////////////////////////////
		// Step 2: sync -> generate Regex tree + file
		//////////////////////////////////////////////////
		
		sync(fileName);
		
		//////////////////////////////////////////////////
		// Step 3: compare generated code
		//////////////////////////////////////////////////
		
		testGeneratedFileContent(fileName);
		
		Parser parser = new Parser();
		RegExAstNode root = parser.parse(
				ProjectsService.getInstance().getFileFromProjectWrapperResource(getFile(fileName)));
		
		Map<String, Integer> childrenInsertPoints = new HashMap<String, Integer>();
		childrenInsertPoints.put("TableHeaderEntry", 146);
		testParsedAstNode(table, 0, 211, 0, childrenInsertPoints, tableParameters, root);
		assertEquals("Table children count", 1, root.getChildren().size());
		childrenInsertPoints.clear();
		testParsedAstNode(tableHeaderEntry, 133, 13, 146, childrenInsertPoints, tableHeaderEntryParameters, root.getChildren().get(0));
	}
	
	public static final String TABLE_ITEM = "CompaniesTableItem.html";
	
	@Test
	public void testTableItem() {
		//////////////////////////////////////////////////
		// Step 1: add to diagram
		//////////////////////////////////////////////////
		
		String fileName = TABLE_ITEM;
		List<RegExAstNodeParameter> tableItemParameters = new ArrayList<RegExAstNodeParameter>();
		addParameter(tableItemParameters, "name", fileName, 0, 0);
		addParameter(tableItemParameters, "itemUrl", "#company/<%= id %>", 130, 18);
		List<Object> tableItem = getList(
				"htmlFile",
				"name",
				false,
				getParameters(tableItemParameters),
				"TableItem",
				null,
				null);
		invokeServiceMethod(tableItem);
		View tableItemView = findByName(fileName);
		List<RegExAstNodeParameter> tableItemEntryParameters = new ArrayList<RegExAstNodeParameter>();
		addParameter(tableItemEntryParameters, "valueExpression", "name", 37, 4);
		List<Object> tableItemEntry = getList(
				"htmlTableItemEntry",
				"valueExpression",
				false,
				getParameters(tableItemEntryParameters),
				"TableItemEntry",
				tableItemView.eResource().getURIFragment(tableItemView),
				null);
		invokeServiceMethod(tableItemEntry);
		
		//////////////////////////////////////////////////
		// Step 2: sync -> generate Regex tree + file
		//////////////////////////////////////////////////
		
		sync(fileName);
		
		//////////////////////////////////////////////////
		// Step 3: compare generated code
		//////////////////////////////////////////////////
		
		testGeneratedFileContent(fileName);
		
		Parser parser = new Parser();
		RegExAstNode root = parser.parse(
				ProjectsService.getInstance().getFileFromProjectWrapperResource(getFile(fileName)));
				
		Map<String, Integer> childrenInsertPoints = new HashMap<String, Integer>();
		childrenInsertPoints.put("TableItemEntry", 49);
		testParsedAstNode(tableItem, 0, 166, 0, childrenInsertPoints, tableItemParameters, root);
		assertEquals("Table item children count", 1, root.getChildren().size());
		childrenInsertPoints.clear();
		testParsedAstNode(tableItemEntry, 29, 20, 49, childrenInsertPoints, tableItemEntryParameters, root.getChildren().get(0));
	}

	public static final String FORM = "Company.html";
	
	@Test
	public void testForm() {
		//////////////////////////////////////////////////
		// Step 1: add to diagram
		//////////////////////////////////////////////////
		
		String fileName = FORM;
		List<RegExAstNodeParameter> formParameters = new ArrayList<RegExAstNodeParameter>();
		addParameter(formParameters, "name", fileName, 0, 0);
		List<Object> form = getList(
				"htmlFile",
				"name",
				false,
				getParameters(formParameters),
				"Form",
				null,
				null);
		invokeServiceMethod(form);
		View formView = findByName(fileName);
		List<RegExAstNodeParameter> formItemParameters = new ArrayList<RegExAstNodeParameter>();
		addParameter(formItemParameters, "title", "Revenue", 73, 7);
		addParameter(formItemParameters, "valueExpression", "revenue", 148, 7);
		addParameter(formItemParameters, "editId", "revenue", 223, 7);
		List<Object> formItem = getList(
				"htmlFormItem",
				"title",
				false,
				getParameters(formItemParameters),
				"FormItem",
				formView.eResource().getURIFragment(formView),
				null);
		invokeServiceMethod(formItem);
		
		//////////////////////////////////////////////////
		// Step 2: sync -> generate Regex tree + file
		//////////////////////////////////////////////////
		
		sync(fileName);
		
		//////////////////////////////////////////////////
		// Step 3: compare generated code
		//////////////////////////////////////////////////
		
		testGeneratedFileContent(fileName);
		
		Parser parser = new Parser();
		RegExAstNode root = parser.parse(
				ProjectsService.getInstance().getFileFromProjectWrapperResource(getFile(fileName)));
		
		Map<String, Integer> childrenInsertPoints = new HashMap<String, Integer>();
		childrenInsertPoints.put("FormItem", 274);
		testParsedAstNode(form, 0, 323, 0, childrenInsertPoints, formParameters, root);
		assertEquals("Form item children count", 1, root.getChildren().size());
		childrenInsertPoints.clear();
		testParsedAstNode(formItem, 62, 212, 274, childrenInsertPoints, formItemParameters, root.getChildren().get(0));
		
	}
	
	@Test
	public void testBackboneClass() {
		fail("Not yet implemented");
	}
	
	private void sync(String fileName) {
		IFile file = getFile(fileName);
		CodeSyncCodePlugin.getInstance().getCodeSyncElement(getProject(), file, CodeSyncCodeJavascriptPlugin.TECHNOLOGY, communicationChannel, true);
		String editableResourcePath = getProject().getFullPath().toString();
		StatefulServiceInvocationContext context = new StatefulServiceInvocationContext(communicationChannel);
		codeSyncEditorStatefulService.synchronize(context, editableResourcePath);
		codeSyncEditorStatefulService.applySelectedActions(context, editableResourcePath, false);
	}
	
	protected void testGeneratedFileContent(String fileName) {
		String expected = getExpectedFileContent(fileName);
		String actual = getActualFileContent(fileName);
		assertEquals("Generated code is not as expected", expected, actual);
	}
	
	protected void testParsedAstNode(List<Object> expected, int offset, int length, int nextSiblingInsertPoint, Map<String, Integer> childrenInsertPoints, List<RegExAstNodeParameter> expectedParameters, RegExAstNode actual) {
		assertEquals("Node type", expected.get(1), actual.getType());
		assertEquals("Key parameter", expected.get(2), actual.getKeyParameter());
		assertEquals("Is category node", expected.get(3), actual.isCategoryNode());

		assertEquals("Parameters count", expectedParameters.size(), actual.getParameters().size());
		for (RegExAstNodeParameter expectedParameter : expectedParameters) {
			testParsedParameter(expectedParameter, getParameter(expectedParameter.getName(), actual));
		}
		
		assertEquals("Template", expected.get(5), actual.getTemplate());
		
		assertEquals("Offset", offset, actual.getOffset());
		assertEquals("Length", length, actual.getLength());
		assertEquals("Next sibling insert point", nextSiblingInsertPoint, actual.getNextSiblingInsertPoint());
		testParsedChildrenInsertPoints(childrenInsertPoints, actual.getChildrenInsertPoints());
	}
	
	private RegExAstNodeParameter getParameter(String key, RegExAstNode actual) {
		for (RegExAstNodeParameter parameter : actual.getParameters()) {
			if (parameter.getName().equals(key)) {
				return parameter;
			}
		}
		fail("No parameter parsed for key " + key);
		return null;
	}
	
	protected void testParsedParameter(RegExAstNodeParameter expected, RegExAstNodeParameter actual) {
		assertEquals("Parameter name", expected.getName(), actual.getName());
		assertEquals("Parameter value", expected.getValue(), actual.getValue());
		assertEquals("Parameter offset", expected.getOffset(), actual.getOffset());
		assertEquals("Parameter length", expected.getLength(), actual.getLength());
	}
	
	protected void testParsedChildrenInsertPoints(Map<String, Integer> expected, EMap<String, Integer> actual) {
		assertEquals("Children insert points count", expected.size(), actual.size());
		for (String key : expected.keySet()) {
			assertEquals("Children insert point", expected.get(key), actual.get(key));
		}
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
	
	private void addParameter(List<RegExAstNodeParameter> parameters, String name, String value, int offset, int length) {
		RegExAstNodeParameter parameter = RegExAstFactory.eINSTANCE.createRegExAstNodeParameter();
		parameter.setName(name);
		parameter.setValue(value);
		parameter.setOffset(offset);
		parameter.setLength(length);
		parameters.add(parameter);
	}
	
	private Map<String, String> getParameters(List<RegExAstNodeParameter> parameters) {
		Map<String, String> result = new HashMap<String, String>();
		for (RegExAstNodeParameter parameter : parameters) {
			result.put(parameter.getName(), parameter.getValue());
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
			EObject child = it.next();
			if (child instanceof View) {
				View view = (View) child;
				CodeSyncElement cse = (CodeSyncElement) view.getDiagrammableElement();
				if (view instanceof ExpandableNode && cse != null && cse.getName().equals(name)) {
					return view;
				}
			}
		}
		fail("No view for element with name " + name);
		return null;
	}
	
}
