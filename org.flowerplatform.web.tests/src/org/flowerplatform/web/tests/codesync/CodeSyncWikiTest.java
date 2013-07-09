package org.flowerplatform.web.tests.codesync;

import static com.crispico.flower.mp.codesync.wiki.WikiRegexConfiguration.FLOWER_BLOCK_CATEGORY;
import static com.crispico.flower.mp.codesync.wiki.WikiTreeBuilder.FOLDER_CATEGORY;
import static com.crispico.flower.mp.codesync.wiki.WikiTreeBuilder.PAGE_CATEGORY;
import static com.crispico.flower.mp.codesync.wiki.WikiRegexConfiguration.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Iterator;

import javax.security.auth.Subject;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.web.communication.RecordingTestWebCommunicationChannel;
import org.flowerplatform.web.projects.remote.ProjectsService;
import org.flowerplatform.web.security.sandbox.FlowerWebPrincipal;
import org.flowerplatform.web.tests.TestUtil;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import astcache.wiki.Page;
import astcache.wiki.WikiFactory;
import astcache.wiki.WikiPackage;

import com.crispico.flower.mp.codesync.base.CodeSyncEditableResource;
import com.crispico.flower.mp.codesync.base.communication.CodeSyncEditorStatefulService;
import com.crispico.flower.mp.codesync.merge.CodeSyncMergePlugin;
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
	private static String DIR = TestUtil.ECLIPSE_DEPENDENT_FILES_DIR + "/codesync/" +TestUtil.NORMAL;
	
	public static final String LINK = "/link-to-project";
	
	private String DOKUWIKI_FILE = getProject().getFullPath().toString() + LINK + "/dokuwiki/teste Scenarios in Diagrams.txt";
	private String DOKUWIKI_FILE_2 = getProject().getFullPath().toString() + LINK + "/dokuwiki/teste Scenarios in Diagrams 2.txt";
	private String DOKUWIKI_FILE_3 = getProject().getFullPath().toString() + LINK + "/dokuwiki/teste Scenarios in Diagrams 3.txt";
	
	private String MD_FILE = getProject().getFullPath().toString() + LINK + "/markdown/Test.md";
	private String MD_FILE_2 = getProject().getFullPath().toString() + LINK + "/markdown/left/Test.md";
	private String MD_FILE_3 = getProject().getFullPath().toString() + LINK + "/markdown/right/Test.md";
	
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
				IProject project = getProject();
				CodeSyncRoot leftRoot = wikiPlugin.getWikiTree(null, wiki, technology);
				CodeSyncRoot rightRoot = wikiPlugin.getWikiTree(project, null, technology);

				expected = new Pair[] {
					new Pair(FOLDER_CATEGORY, 0),							// Crispico
						new Pair(FOLDER_CATEGORY, 1),							// proiecte
							new Pair(FOLDER_CATEGORY, 2),						// flower
								new Pair(PAGE_CATEGORY, 3),						// teste
								
									new Pair(FOLDER_CATEGORY, 4),				// teste
									new Pair(PAGE_CATEGORY, 5),				// new_test
										new Pair(HEADLINE_LEVEL_2_CATEGORY, 6),
											new Pair(HEADLINE_LEVEL_3_CATEGORY, 7),
												new Pair(PARAGRAPH_CATEGORY, 8),
											
									new Pair(HEADLINE_LEVEL_1_CATEGORY, 4),
									new Pair(HEADLINE_LEVEL_1_CATEGORY, 4),
										new Pair(HEADLINE_LEVEL_2_CATEGORY, 5),
											new Pair(PARAGRAPH_CATEGORY, 6),
											new Pair(FLOWER_BLOCK_CATEGORY, 6),
											new Pair(PARAGRAPH_CATEGORY, 6),
											new Pair(PARAGRAPH_CATEGORY, 6),
											new Pair(PARAGRAPH_CATEGORY, 6),
											new Pair(PARAGRAPH_CATEGORY, 6),
										new Pair(HEADLINE_LEVEL_2_CATEGORY, 5),
									new Pair(HEADLINE_LEVEL_1_CATEGORY, 4)
									
				};
				test(leftRoot, rightRoot, technology, expected);
				
				return null;
			}
		}, null);
	}
	
	@Test
	public void testDokuWiki_dummy() throws CoreException {
		WikiPlugin wikiPlugin = WikiPlugin.getInstance();
		String ancestor = TestUtil.readFile(TestUtil.getWorkspaceResourceAbsolutePath(DOKUWIKI_FILE));
		String left = TestUtil.readFile(TestUtil.getWorkspaceResourceAbsolutePath(DOKUWIKI_FILE_2));
		String right = TestUtil.readFile(TestUtil.getWorkspaceResourceAbsolutePath(DOKUWIKI_FILE_3));
		String pageName = "page";
		DokuWikiPage page = new DokuWikiPage(pageName, ancestor);
		
		String technology = "Dummy";
		DummyDokuWikiConfigurationProvider dummyConfigProvider = new DummyDokuWikiConfigurationProvider();
		WikiPlugin.getInstance().getConfigurationProviders().put(technology, dummyConfigProvider);
		
		IProject project = getProject();
		CodeSyncRoot leftRoot = wikiPlugin.getWikiTree(null, Arrays.asList(page), technology);
		CodeSyncRoot rightRoot = wikiPlugin.getWikiTree(project, null, technology);
		
		expected = 	new Pair[] {
			new Pair(FOLDER_CATEGORY, 0), 
				new Pair(PAGE_CATEGORY, 1),
					new Pair(PARAGRAPH_CATEGORY, 2),
					new Pair(HEADLINE_LEVEL_1_CATEGORY, 2),
						new Pair(PARAGRAPH_CATEGORY, 3),
						new Pair(PARAGRAPH_CATEGORY, 3),
					new Pair(HEADLINE_LEVEL_1_CATEGORY, 2),
						new Pair(PARAGRAPH_CATEGORY, 3),
						new Pair(PARAGRAPH_CATEGORY, 3),
						new Pair(PARAGRAPH_CATEGORY, 3),
						new Pair(PARAGRAPH_CATEGORY, 3),
						new Pair(HEADLINE_LEVEL_2_CATEGORY, 3),
							new Pair(PARAGRAPH_CATEGORY, 4),
							new Pair(PARAGRAPH_CATEGORY, 4),
							new Pair(FLOWER_BLOCK_CATEGORY, 4),
							new Pair(PARAGRAPH_CATEGORY, 4),
							new Pair(PARAGRAPH_CATEGORY, 4),
							new Pair(PARAGRAPH_CATEGORY, 4),
							new Pair(PARAGRAPH_CATEGORY, 4),
							new Pair(PARAGRAPH_CATEGORY, 4),
							new Pair(PARAGRAPH_CATEGORY, 4),
							new Pair(PARAGRAPH_CATEGORY, 4),
						new Pair(HEADLINE_LEVEL_2_CATEGORY, 3),
							new Pair(PARAGRAPH_CATEGORY, 4),
					new Pair(HEADLINE_LEVEL_1_CATEGORY, 2),
						new Pair(PARAGRAPH_CATEGORY, 3),
						new Pair(PARAGRAPH_CATEGORY, 3),
						new Pair(PARAGRAPH_CATEGORY, 3)
		};
		test(leftRoot, rightRoot, technology, expected);
		
		// left modifications
		page = new DokuWikiPage(pageName, left);
		leftRoot = wikiPlugin.getWikiTree(null, Arrays.asList(page), technology);
		// right modifications
		CodeSyncElement newRightPage = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createCodeSyncElement();
		newRightPage.setName("page");
		newRightPage.setType(PAGE_CATEGORY);
		Page wikiPage = WikiPackage.eINSTANCE.getWikiFactory().createPage();
		wikiPage.setInitialContent(ancestor);
		wikiPage.setLineDelimiter("\r\n");
		newRightPage.setAstCacheElement(wikiPage);
		WikiPlugin.getInstance().replaceWikiPage(rightRoot, newRightPage);
		WikiPlugin.getInstance().addToAstCacheResource(rightRoot, WikiPlugin.getInstance().getAstCacheResource(project));
		wikiPlugin.getWikiPageTree(right, newRightPage, technology, null);
		CodeSyncMergePlugin.getInstance().saveResource(rightRoot.eResource());
		
		test(leftRoot, rightRoot, technology, null);
		
		assertEquals(wikiPlugin.getWikiText(newRightPage, technology), dummyConfigProvider.page.getInitialContent());
	}
	
	@Test
	public void testMarkdown() throws IOException {
		WikiPlugin wikiPlugin = WikiPlugin.getInstance();
		String technology = GithubConfigurationProvider.TECHNOLOGY;
		wikiPlugin.getConfigurationProviders().put(technology, new GithubConfigurationProvider());
		
		File ancestor = new File(TestUtil.getWorkspaceResourceAbsolutePath(MD_FILE));
		File left = new File(TestUtil.getWorkspaceResourceAbsolutePath(MD_FILE_2));
		File right = new File(TestUtil.getWorkspaceResourceAbsolutePath(MD_FILE_3));
		
		IProject project = getProject();
		CodeSyncRoot leftRoot = wikiPlugin.getWikiTree(null, ancestor, technology);
		CodeSyncRoot rightRoot = wikiPlugin.getWikiTree(project, null, technology);
		rightRoot.setName(ancestor.getPath());
		
		expected = new Pair[] {
			new Pair(FOLDER_CATEGORY, 0),
				new Pair(PAGE_CATEGORY, 1),
					new Pair(PARAGRAPH_CATEGORY, 2),
					new Pair(HEADLINE_LEVEL_1_CATEGORY, 2),
						new Pair(HEADLINE_LEVEL_4_CATEGORY, 3),
						new Pair(HEADLINE_LEVEL_2_CATEGORY, 3),
							new Pair(PARAGRAPH_CATEGORY, 4),
							new Pair(FLOWER_BLOCK_CATEGORY, 4),
							new Pair(PARAGRAPH_CATEGORY, 4),
							new Pair(PARAGRAPH_CATEGORY, 4),
							new Pair(PARAGRAPH_CATEGORY, 4),
							new Pair(PARAGRAPH_CATEGORY, 4)
		};
		
		test(leftRoot, rightRoot, technology, expected);
		
		// left modifications
		leftRoot = wikiPlugin.getWikiTree(null, left, technology);
		// right modifications
		CodeSyncElement newRightPage = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
		newRightPage.setName(ancestor.getName());
		newRightPage.setType(PAGE_CATEGORY);
		Page wikiPage = WikiFactory.eINSTANCE.createPage();
		wikiPage.setInitialContent(FileUtils.readFileToString(ancestor));
		wikiPage.setLineDelimiter(wikiPlugin.getLineDelimiter(FileUtils.readFileToString(ancestor)));
		newRightPage.setAstCacheElement(wikiPage);
		wikiPlugin.replaceWikiPage(rightRoot, newRightPage);
		wikiPlugin.addToAstCacheResource(rightRoot, wikiPlugin.getAstCacheResource(project));
		wikiPlugin.getWikiPageTree(FileUtils.readFileToString(right), newRightPage, technology, null);
		CodeSyncMergePlugin.getInstance().saveResource(rightRoot.eResource());
		
		test(leftRoot, rightRoot, technology, null);
		
		String newContent = FileUtils.readFileToString(new File(TestUtil.getWorkspaceResourceAbsolutePath(MD_FILE_2)));
		assertEquals(wikiPlugin.getWikiText(newRightPage, technology), newContent);
	}
	
	private IProject getProject() {
		String absolutePath = CommonPlugin.getInstance().getWorkspaceRoot().getAbsolutePath() + "/org/ws_trunk/" + PROJECT;
		IResource resource = ProjectsService.getInstance().getProjectWrapperResourceFromFile(new File(absolutePath));
		return resource.getProject();
	}
	
	private void test(CodeSyncRoot leftRoot, CodeSyncRoot rightRoot, String technology, Pair[] expected) {
		IProject project = getProject();
		WikiPlugin wikiPlugin = WikiPlugin.getInstance();
		wikiPlugin.updateTree(leftRoot, rightRoot, project, technology, communicationChannel);
		
		CodeSyncEditorStatefulService service = (CodeSyncEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(CodeSyncEditorStatefulService.SERVICE_ID);
		CodeSyncEditableResource editableResource = (CodeSyncEditableResource) service.subscribeClientForcefully(communicationChannel, project.getFullPath().toString());
		
		assertNotNull(editableResource);
		
		service.synchronize(new StatefulServiceInvocationContext(communicationChannel), project.getFullPath().toString());
		service.applySelectedActions(new StatefulServiceInvocationContext(communicationChannel), project.getFullPath().toString());
		
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
