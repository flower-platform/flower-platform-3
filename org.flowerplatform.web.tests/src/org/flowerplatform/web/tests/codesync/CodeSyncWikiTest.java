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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Iterator;

import javax.security.auth.Subject;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.editor.EditorPlugin;
import org.flowerplatform.model.astcache.wiki.AstCacheWikiFactory;
import org.flowerplatform.model.astcache.wiki.Page;
import org.flowerplatform.web.communication.RecordingTestWebCommunicationChannel;
import org.flowerplatform.web.projects.remote.ProjectsService;
import org.flowerplatform.web.security.sandbox.FlowerWebPrincipal;
import org.flowerplatform.web.tests.TestUtil;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.crispico.flower.mp.codesync.base.CodeSyncEditableResource;
import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.codesync.base.communication.CodeSyncEditorStatefulService;
import com.crispico.flower.mp.codesync.wiki.WikiPlugin;
import com.crispico.flower.mp.codesync.wiki.dokuwiki.DokuWikiClientConfiguration;
import com.crispico.flower.mp.codesync.wiki.dokuwiki.DokuWikiConfigurationProvider;
import com.crispico.flower.mp.codesync.wiki.dokuwiki.DokuWikiPage;
import com.crispico.flower.mp.codesync.wiki.dokuwiki.DokuWikiPlugin;
import com.crispico.flower.mp.codesync.wiki.github.GithubConfigurationProvider;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncFactory;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;

import flex.messaging.FlexContext;
import flex.messaging.HttpFlexSession;

public class CodeSyncWikiTest {

	private static final String PROJECT = "codesync_wiki";
	private static String DIR = TestUtil.getResourcesDir(CodeSyncWikiTest.class) + TestUtil.NORMAL;
	
	public static final String LINK = "/link-to-project";
	
	private File DOKUWIKI_FILE = CodeSyncPlugin.getInstance().getProjectsProvider().getFile(getProject(), ProjectsService.LINK_TO_PROJECT + "/dokuwiki/teste Scenarios in Diagrams.txt");
	private File DOKUWIKI_FILE_2 = CodeSyncPlugin.getInstance().getProjectsProvider().getFile(getProject(), ProjectsService.LINK_TO_PROJECT + "/dokuwiki/teste Scenarios in Diagrams 2.txt");
	private File DOKUWIKI_FILE_3 = CodeSyncPlugin.getInstance().getProjectsProvider().getFile(getProject(), ProjectsService.LINK_TO_PROJECT + "/dokuwiki/teste Scenarios in Diagrams 3.txt");
	
	private File MD_FILE = CodeSyncPlugin.getInstance().getProjectsProvider().getFile(getProject(), ProjectsService.LINK_TO_PROJECT + "/markdown/Test.md");
	private File MD_FILE_2 = CodeSyncPlugin.getInstance().getProjectsProvider().getFile(getProject(), ProjectsService.LINK_TO_PROJECT + "/markdown/left/Test.md");
	private File MD_FILE_3 = CodeSyncPlugin.getInstance().getProjectsProvider().getFile(getProject(), ProjectsService.LINK_TO_PROJECT + "/markdown/right/Test.md");
	
	private Pair[] expected;
	private int index;
	
	private static CommunicationChannel communicationChannel = new RecordingTestWebCommunicationChannel();
	
	@BeforeClass
	public static void setUpBeforeClass() {
		CommunicationPlugin.getInstance().getServiceRegistry().registerService(CodeSyncEditorStatefulService.SERVICE_ID, new CodeSyncEditorStatefulService());
		TestUtil.copyFilesAndCreateProject(new ServiceInvocationContext(communicationChannel), DIR, PROJECT);
	}
	
	@Before
	public void setUp() {
		expected = null;
		index = 0;
	}
	
//	@Test
	public void testDokuWiki() {
		Subject subject = new Subject();
		final FlowerWebPrincipal principal = new FlowerWebPrincipal(0);
		final String technology = "Doku";
		String url = "http://csp1/dokuwiki/lib/exe/xmlrpc.php";
		String user = "";
		String password = "";
		principal.getWikiClientConfigurations().put(technology, new DokuWikiClientConfiguration(url, user, password));
		subject.getPrincipals().add(principal);
		Subject.doAsPrivileged(subject, new PrivilegedAction<Void>() {

			@Override
			public Void run() {
				FlexContext.setThreadLocalSession(new HttpFlexSession());
				FlexContext.setUserPrincipal(principal);
				RecordingTestWebCommunicationChannel cc = new RecordingTestWebCommunicationChannel();
				cc.setPrincipal((FlowerWebPrincipal) principal);
				ServiceInvocationContext context = new ServiceInvocationContext(cc);
				
				Object wiki = DokuWikiPlugin.getInstance().getWikiPages("proiecte:flower:teste");
			
				WikiPlugin.getInstance().getConfigurationProviders().put(technology, new DokuWikiConfigurationProvider());
				
				WikiPlugin wikiPlugin = WikiPlugin.getInstance();
				File project = getProject();
				ResourceSet resourceSet = CodeSyncPlugin.getInstance().getOrCreateResourceSet(project, "mindmapEditorStatefulService");
				CodeSyncRoot leftRoot = wikiPlugin.getWikiTree(null, resourceSet, wiki, "proiecte:flower:teste", technology);
				CodeSyncRoot rightRoot = wikiPlugin.getWikiTree(project, resourceSet, null, "proiecte:flower:teste", technology);

				expected = new Pair[] {
					new Pair(WikiPlugin.FOLDER_CATEGORY, 0),							// Crispico
						new Pair(WikiPlugin.FOLDER_CATEGORY, 1),							// proiecte
							new Pair(WikiPlugin.FOLDER_CATEGORY, 2),						// flower
								new Pair(WikiPlugin.PAGE_CATEGORY, 3),						// teste
								
									new Pair(WikiPlugin.FOLDER_CATEGORY, 4),				// teste
									new Pair(WikiPlugin.PAGE_CATEGORY, 5),				// new_test
										new Pair(WikiPlugin.HEADING_LEVEL_2_CATEGORY, 6),
											new Pair(WikiPlugin.HEADING_LEVEL_3_CATEGORY, 7),
												new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 8),
											
									new Pair(WikiPlugin.HEADING_LEVEL_1_CATEGORY, 4),
									new Pair(WikiPlugin.HEADING_LEVEL_1_CATEGORY, 4),
										new Pair(WikiPlugin.HEADING_LEVEL_2_CATEGORY, 5),
											new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 6),
											new Pair(WikiPlugin.FLOWER_BLOCK_CATEGORY, 6),
											new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 6),
											new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 6),
											new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 6),
											new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 6),
										new Pair(WikiPlugin.HEADING_LEVEL_2_CATEGORY, 5),
									new Pair(WikiPlugin.HEADING_LEVEL_1_CATEGORY, 4)
									
				};
				test(leftRoot, rightRoot, resourceSet, technology, expected);
				
				return null;
			}
		}, null);
	}
	
	@Test
	public void testDokuWiki_dummy() throws CoreException, IOException {
		WikiPlugin wikiPlugin = WikiPlugin.getInstance();
		String ancestor = FileUtils.readFileToString(DOKUWIKI_FILE);
		String left = FileUtils.readFileToString(DOKUWIKI_FILE_2);
		String right = FileUtils.readFileToString(DOKUWIKI_FILE_3);
		String pageName = "page";
		DokuWikiPage page = new DokuWikiPage(pageName, ancestor);
		
		String technology = "Dummy";
		DummyDokuWikiConfigurationProvider dummyConfigProvider = new DummyDokuWikiConfigurationProvider();
		WikiPlugin.getInstance().getConfigurationProviders().put(technology, dummyConfigProvider);
		
		File project = getProject();
		ResourceSet resourceSet = CodeSyncPlugin.getInstance().getOrCreateResourceSet(project, "mindmapEditorStatefulService");
		CodeSyncRoot leftRoot = wikiPlugin.getWikiTree(null, resourceSet, Arrays.asList(page), "", technology);
		CodeSyncRoot rightRoot = wikiPlugin.getWikiTree(project, resourceSet, null, "", technology);
		
		expected = 	new Pair[] {
			new Pair(WikiPlugin.FOLDER_CATEGORY, 0), 
				new Pair(WikiPlugin.PAGE_CATEGORY, 1),
					new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 2),
					new Pair(WikiPlugin.HEADING_LEVEL_1_CATEGORY, 2),
						new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 3),
						new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 3),
					new Pair(WikiPlugin.HEADING_LEVEL_1_CATEGORY, 2),
						new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 3),
						new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 3),
						new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 3),
						new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 3),
						new Pair(WikiPlugin.HEADING_LEVEL_2_CATEGORY, 3),
							new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 4),
							new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 4),
							new Pair(WikiPlugin.FLOWER_BLOCK_CATEGORY, 4),
							new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 4),
							new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 4),
							new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 4),
							new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 4),
							new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 4),
							new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 4),
							new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 4),
						new Pair(WikiPlugin.HEADING_LEVEL_2_CATEGORY, 3),
							new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 4),
					new Pair(WikiPlugin.HEADING_LEVEL_1_CATEGORY, 2),
						new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 3),
						new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 3),
						new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 3)
		};
		test(leftRoot, rightRoot, resourceSet, technology, expected);
		
		// left modifications
		page = new DokuWikiPage(pageName, left);
		leftRoot = wikiPlugin.getWikiTree(null, resourceSet, Arrays.asList(page), "", technology);
		// right modifications
		CodeSyncElement newRightPage = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createCodeSyncElement();
		newRightPage.setName("page");
		newRightPage.setType(WikiPlugin.PAGE_CATEGORY);
		Page wikiPage = AstCacheWikiFactory.eINSTANCE.createPage();
		wikiPage.setInitialContent(ancestor);
		wikiPage.setLineDelimiter("\r\n");
		newRightPage.setAstCacheElement(wikiPage);
		WikiPlugin.getInstance().replaceWikiPage(rightRoot, newRightPage);
		WikiPlugin.getInstance().addToAstCacheResource(rightRoot, WikiPlugin.getInstance().getAstCacheResource(project, resourceSet));
		wikiPlugin.getWikiPageTree(right, newRightPage, technology, null);
		CodeSyncPlugin.getInstance().saveResource(rightRoot.eResource());
		
		test(leftRoot, rightRoot, resourceSet, technology, null);
		
		assertEquals(wikiPlugin.getWikiText(newRightPage, technology), dummyConfigProvider.page.getInitialContent());
	}
	
	@Test
	public void testMarkdown() throws IOException {
		WikiPlugin wikiPlugin = WikiPlugin.getInstance();
		String technology = GithubConfigurationProvider.TECHNOLOGY;
		wikiPlugin.getConfigurationProviders().put(technology, new GithubConfigurationProvider());
		
		File ancestor = MD_FILE;
		File left = MD_FILE_2;
		File right = MD_FILE_3;
		
		File project = getProject();
		ResourceSet resourceSet = CodeSyncPlugin.getInstance().getOrCreateResourceSet(project, "mindmapEditorStatefulService");
		CodeSyncRoot leftRoot = wikiPlugin.getWikiTree(null, resourceSet, ancestor, ancestor.getPath(), technology);
		CodeSyncRoot rightRoot = wikiPlugin.getWikiTree(project, resourceSet, null, ancestor.getPath(), technology);
		rightRoot.setName(ancestor.getPath());
		
		expected = new Pair[] {
			new Pair(WikiPlugin.FOLDER_CATEGORY, 0),
				new Pair(WikiPlugin.PAGE_CATEGORY, 1),
					new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 2),
					new Pair(WikiPlugin.HEADING_LEVEL_1_CATEGORY, 2),
						new Pair(WikiPlugin.HEADING_LEVEL_4_CATEGORY, 3),
						new Pair(WikiPlugin.HEADING_LEVEL_2_CATEGORY, 3),
							new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 4),
							new Pair(WikiPlugin.FLOWER_BLOCK_CATEGORY, 4),
							new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 4),
							new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 4),
							new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 4),
							new Pair(WikiPlugin.PARAGRAPH_CATEGORY, 4)
		};
		
		test(leftRoot, rightRoot, resourceSet, technology, expected);
		
		// left modifications
		leftRoot = wikiPlugin.getWikiTree(null, resourceSet, left, ancestor.getPath(), technology);
		// right modifications
		CodeSyncElement newRightPage = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
		newRightPage.setName(ancestor.getName());
		newRightPage.setType(WikiPlugin.PAGE_CATEGORY);
		Page wikiPage = AstCacheWikiFactory.eINSTANCE.createPage();
		wikiPage.setInitialContent(FileUtils.readFileToString(ancestor));
		wikiPage.setLineDelimiter(wikiPlugin.getLineDelimiter(FileUtils.readFileToString(ancestor)));
		newRightPage.setAstCacheElement(wikiPage);
		wikiPlugin.replaceWikiPage(rightRoot, newRightPage);
		wikiPlugin.addToAstCacheResource(rightRoot, wikiPlugin.getAstCacheResource(project, resourceSet));
		wikiPlugin.getWikiPageTree(FileUtils.readFileToString(right), newRightPage, technology, null);
		CodeSyncPlugin.getInstance().saveResource(rightRoot.eResource());
		
		test(leftRoot, rightRoot, resourceSet, technology, null);
		
		String newContent = FileUtils.readFileToString(MD_FILE_2);
		assertEquals(wikiPlugin.getWikiText(newRightPage, technology), newContent);
	}
	
	private File getProject() {
		return CodeSyncTestSuite.getProject(PROJECT);
	}
	
	private void test(CodeSyncRoot leftRoot, CodeSyncRoot rightRoot, ResourceSet resourceSet, String technology, Pair[] expected) {
		File project = getProject();
		WikiPlugin wikiPlugin = WikiPlugin.getInstance();
		wikiPlugin.updateTree(leftRoot, rightRoot, project, resourceSet, technology, communicationChannel, true);
		
		CodeSyncEditorStatefulService service = (CodeSyncEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(CodeSyncEditorStatefulService.SERVICE_ID);
		CodeSyncEditableResource editableResource = (CodeSyncEditableResource) service.subscribeClientForcefully(communicationChannel, EditorPlugin.getInstance().getFileAccessController().getPath(project));
		
		assertNotNull(editableResource);
		
		service.synchronize(new StatefulServiceInvocationContext(communicationChannel), EditorPlugin.getInstance().getFileAccessController().getPath(project));
		service.applySelectedActions(new StatefulServiceInvocationContext(communicationChannel), EditorPlugin.getInstance().getFileAccessController().getPath(project), true);
		
		if (expected != null) {
			testWikiTree(rightRoot, 0);
			assertEquals(expected.length, index);
		}
	}
	
	private void testWikiTree(CodeSyncElement node, int level) {
		assertEquals("At index " + index, expected[index].type, node.getType());
		assertEquals("At index " + index, expected[index].level, level);
		index++;
		for (Iterator it = WikiPlugin.getInstance().getChildrenIterator(node); it.hasNext();) {
			CodeSyncElement child = (CodeSyncElement) it.next();
			testWikiTree(child, level + 1);
		}
	}
	
	class Pair {
		public String type;
		public int level;
		
		public Pair(String type, int level) {
			this.type = type;
			this.level = level;
		}
	}
	
}