package com.crispico.flower.mp.codesync.wiki;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.editor.remote.EditorStatefulClientLocalState;
import org.flowerplatform.emf_model.notation.util.NotationAdapterFactory;
import org.flowerplatform.web.projects.remote.ProjectsService;
import org.osgi.framework.BundleContext;

import astcache.wiki.FlowerBlock;
import astcache.wiki.Page;
import astcache.wiki.util.WikiAdapterFactory;

import com.crispico.flower.mp.codesync.base.CodeSyncEditableResource;
import com.crispico.flower.mp.codesync.base.CodeSyncElementFeatureProvider;
import com.crispico.flower.mp.codesync.base.Match;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactory;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactorySet;
import com.crispico.flower.mp.codesync.base.communication.CodeSyncEditorStatefulService;
import com.crispico.flower.mp.codesync.merge.CodeSyncMergePlugin;
import com.crispico.flower.mp.codesync.wiki.adapter.WikiNodeModelAdapter;
import com.crispico.flower.mp.codesync.wiki.adapter.WikiNodeModelAdapterRight;
import com.crispico.flower.mp.codesync.wiki.featureprovider.WikiPageFeatureProvider;
import com.crispico.flower.mp.codesync.wiki.github.GithubConfigurationProvider;
import com.crispico.flower.mp.model.codesync.AstCacheElement;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;
import com.crispico.flower.mp.model.codesync.util.CodeSyncAdapterFactory;

/**
 * @author Mariana
 */
public class WikiPlugin extends AbstractFlowerJavaPlugin {

	protected static WikiPlugin INSTANCE;
	
	public static final String CSE_FILE_LOCATION = "/Wiki.notation";
	
	public static final String ACE_FILE_LOCATION = "/WikiCache.notation";
	
	public static final String FOLDER_CATEGORY = "folder";
	public static final String PAGE_CATEGORY = "page";
		
	public static final String HEADLINE_LEVEL_1_CATEGORY = "headline 1";
	public static final String HEADLINE_LEVEL_2_CATEGORY = "headline 2";
	public static final String HEADLINE_LEVEL_3_CATEGORY = "headline 3";
	public static final String HEADLINE_LEVEL_4_CATEGORY = "headline 4";
	public static final String HEADLINE_LEVEL_5_CATEGORY = "headline 5";
	public static final String HEADLINE_LEVEL_6_CATEGORY = "headline 6";
	
	public static final String PARAGRAPH_CATEGORY = "paragraph";
	public static final String FLOWER_BLOCK_CATEGORY = "flowerBlock";
	
	protected Map<IProject, AdapterFactoryEditingDomain> editingDomains = new HashMap<IProject, AdapterFactoryEditingDomain>();
	
	protected Map<String, IConfigurationProvider> configurationProviders = new HashMap<String, IConfigurationProvider>();
	
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
		
		// TODO this should be done from a new plugin probably
		getConfigurationProviders().put(GithubConfigurationProvider.TECHNOLOGY, new GithubConfigurationProvider());
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// nothing to do yet
	}
	
	public static WikiPlugin getInstance() {
		return INSTANCE;
	}
	
	public Map<String, IConfigurationProvider> getConfigurationProviders() {
		return configurationProviders;
	}

	public CodeSyncRoot getWikiRoot(File file, String technology, CommunicationChannel communicationChannel) {
		IResource resource = ProjectsService.getInstance().getProjectWrapperResourceFromFile(file);
		IProject project = resource.getProject();
		File projectFile = ProjectsService.getInstance().getFileFromProjectWrapperResource(project);
		String name = projectFile.getPath();
		// this will be a temporary tree, do not send the project
		CodeSyncRoot leftRoot = getWikiTree(null, projectFile, name, technology);
		CodeSyncRoot rightRoot = getWikiTree(project, null, name, technology);
		
		updateTree(leftRoot, rightRoot, project, technology, communicationChannel, false);
		return rightRoot;
	}
	
	public void updateTree(CodeSyncElement left, CodeSyncElement right, IProject project, String technology, CommunicationChannel communicationChannel, boolean showDialog) {
		CodeSyncEditorStatefulService service = (CodeSyncEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(CodeSyncEditorStatefulService.SERVICE_ID);
		String editableResourcePath = project.getFullPath().toString();
		CodeSyncEditableResource editableResource;
		if (showDialog) {
			editableResource = (CodeSyncEditableResource) service.subscribeClientForcefully(communicationChannel, project.getFullPath().toString());
		} else {
			service.subscribe(new StatefulServiceInvocationContext(communicationChannel), new EditorStatefulClientLocalState(editableResourcePath));
			editableResource = (CodeSyncEditableResource) service.getEditableResource(editableResourcePath);
		}
		
		Match match = new Match();
		match.setAncestor(right);
		match.setLeft(left);
		match.setRight(right);
		
		ModelAdapterFactory rightFactory = new ModelAdapterFactory();
		WikiNodeModelAdapterRight rightAdapter = new WikiNodeModelAdapterRight();
		rightAdapter.setModelAdapterFactory(rightFactory);
		rightAdapter.setEObjectConverter(rightFactory);
		rightAdapter.setResource(getAstCacheResource(project));
		rightAdapter.setTechnology(technology);
		rightFactory.addModelAdapter(CodeSyncElement.class, rightAdapter);
		
		ModelAdapterFactory ancestorFactory = new ModelAdapterFactory();
		WikiNodeModelAdapter ancestorAdapter = new WikiNodeModelAdapter();
		ancestorAdapter.setModelAdapterFactory(ancestorFactory);
		ancestorAdapter.setEObjectConverter(ancestorFactory);
		ancestorAdapter.setTechnology(technology);
		ancestorFactory.addModelAdapter(CodeSyncElement.class, ancestorAdapter);
		
		ModelAdapterFactory leftFactory = new ModelAdapterFactory();
		WikiNodeModelAdapter leftAdapter = new WikiNodeModelAdapter();
		leftAdapter.setModelAdapterFactory(leftFactory);
		leftAdapter.setEObjectConverter(leftFactory);
		leftAdapter.setResource(getAstCacheResource(left));
		leftAdapter.setTechnology(technology);
		leftFactory.addModelAdapter(CodeSyncElement.class, leftAdapter);
		
		ModelAdapterFactorySet factorySet = new ModelAdapterFactorySet(ancestorFactory, leftFactory, rightFactory);
		factorySet.addFeatureProvider(CodeSyncElement.class, new CodeSyncElementFeatureProvider());
		factorySet.addFeatureProvider(WikiPlugin.PAGE_CATEGORY, new WikiPageFeatureProvider());
		
		match.setEditableResource(editableResource);
		editableResource.setMatch(match);
		editableResource.setModelAdapterFactorySet(factorySet);
		
		new WikiSyncAlgorithm(editableResource.getModelAdapterFactorySet(), technology).generateDiff(match);
		
		if (!showDialog) {
			// we're not showing the dialog => perform sync
			StatefulServiceInvocationContext context = new StatefulServiceInvocationContext(communicationChannel);
			service.synchronize(context, editableResourcePath);
			service.applySelectedActions(context, editableResourcePath, false);
		}
	}
	
	public Resource getAstCacheResource(CodeSyncElement root) {
		if (root.getAstCacheElement() != null) {
			return root.getAstCacheElement().eResource();
		}
		for (CodeSyncElement child : root.getChildren()) {
			return getAstCacheResource(child);
		}
		return null;
	}
	
	public Resource getAstCacheResource(IProject project) {
		File astCacheElementFile = ProjectsService.getInstance().getFileFromProjectWrapperResource(project.getFile(new Path(ProjectsService.LINK_TO_PROJECT + ACE_FILE_LOCATION)));
		return CodeSyncMergePlugin.getInstance().getResource(getOrCreateEditingDomain(project), astCacheElementFile);
	}
	
	public Resource getCodeSyncMappingResource(IProject project) {
		File codeSyncElementMappingFile = ProjectsService.getInstance().getFileFromProjectWrapperResource(project.getFile(new Path(ProjectsService.LINK_TO_PROJECT + CSE_FILE_LOCATION)));
		return CodeSyncMergePlugin.getInstance().getResource(getOrCreateEditingDomain(project), codeSyncElementMappingFile);
	}

	/**
	 * Creates a wiki tree corresponding to the content from <code>wikiText</code>, rooted at <code>page</code>.
	 * The <code>flowerBlocks</code> will be used to populate the content of Flower blocks found in the content.
	 */
	public void getWikiPageTree(String wikiText, CodeSyncElement page, String technology, List<FlowerBlock> flowerBlocks) {
		WikiRegexConfiguration config = new WikiRegexConfiguration();
		IConfigurationProvider configurationProvider = configurationProviders.get(technology);
		configurationProvider.buildConfiguration(config, page);
		config.setSessionClass(configurationProvider.getWikiTreeBuilderClass());
		config.compile();
		WikiTreeBuilder session = (WikiTreeBuilder) config.startSession(wikiText);
		session.setRoot(page);
		session.setFlowerBlocks(flowerBlocks);
		session.setResource(getAstCacheResource(page));
		
		while (session.find()) {
		}
	}
	
	/**
	 * Generates the content from a wiki page tree.
	 */
	public String getWikiText(CodeSyncElement tree, String technology) {
		if (tree == null) {
			return null;
		}
		return configurationProviders.get(technology).getWikiTextBuilder(tree).getWikiText(tree);
	}
	
	/**
	 * Delegates to {@link IConfigurationProvider#getWikiTree(String, Object)}.
	 */
	public CodeSyncRoot getWikiTree(IProject project, Object wiki, String name, String technology) {
		Resource codeSyncResource = project == null ? getOrCreateEditingDomain(project).createResource("tempCSE") : getCodeSyncMappingResource(project);
		CodeSyncRoot root = getRoot(codeSyncResource, name);
		if (root == null) {
			root = configurationProviders.get(technology).getWikiTree(wiki);
			codeSyncResource.getContents().add(root);
			if (wiki == null) {
				root.setName(name);
			}
		}
		Resource astCacheResource = project == null ? getOrCreateEditingDomain(project).createResource("tempACE") : getAstCacheResource(project);
		addToAstCacheResource(root, astCacheResource);
		return root;
	}
	
	private CodeSyncRoot getRoot(Resource resource, String name) {
		for (EObject object : resource.getContents()) {
			if (name.equals(((CodeSyncRoot) object).getName())) {
				return (CodeSyncRoot) object;
			}
		}
		return null;
	}
	
	public void addToAstCacheResource(CodeSyncElement root, Resource resource) {
		AstCacheElement ace = root.getAstCacheElement();
		if (ace != null) {
			resource.getContents().add(ace);
		}
		for (CodeSyncElement cse : root.getChildren()) {
			addToAstCacheResource(cse, resource);
		}
	}

	public AdapterFactoryEditingDomain getOrCreateEditingDomain(IProject project) {
		AdapterFactoryEditingDomain domain = editingDomains.get(project);
		if (domain == null) {
			ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		
			adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
			adapterFactory.addAdapterFactory(new CodeSyncAdapterFactory());
			adapterFactory.addAdapterFactory(new WikiAdapterFactory());
			adapterFactory.addAdapterFactory(new NotationAdapterFactory());
			adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
			
			domain = new AdapterFactoryEditingDomain(adapterFactory, null, new HashMap<Resource, Boolean>());
			editingDomains.put(project, domain);
		}
		return domain;
	}
	
	/**
	 * Delegates to {@link IConfigurationProvider#savePage(Page)}.
	 */
	public void savePage(Page page, String technology) {
		configurationProviders.get(technology).savePage(page);
	}
	
	public String getLineDelimiter(String content) {
		if (content.contains("\r\n")) {
			return "\r\n";
		}
		if (content.contains("\r")) {
			return "\r";
		}
		return "\n";
	}
	
	public int getHeadlineLevel(String category) {
		if (category == null) {
			return -1;
		}
		if (category.startsWith("headline")) {
			return Integer.parseInt(category.substring(category.length() - 1)); 
		}
		return -1;
	}
	
	public String getPagePath(Page page, String delimiter, boolean fromRoot) {
		StringBuilder builder = new StringBuilder();
		CodeSyncElement crt = page.getCodeSyncElement();
		while (getParent(crt) != null) {
			builder.insert(0, delimiter + crt.getName());
			crt = getParent(crt);
		}
		if (fromRoot) {
			builder.insert(0, crt.getName());
		} else {
			builder.deleteCharAt(0);
		}
		return builder.toString();
	}
	
	private CodeSyncElement getParent(CodeSyncElement cse) {
		EObject eObj = cse.eContainer();
		if (eObj instanceof CodeSyncElement) {
			return (CodeSyncElement) eObj;
		}
		return null;
	}

	
	/**
	 * TODO move to test
	 */
	public void replaceWikiPage(CodeSyncElement rightRoot, CodeSyncElement newRightPage) {
		for (CodeSyncElement child : rightRoot.getChildren()) {
			if (child.getAstCacheElement() instanceof Page) {
				rightRoot.getChildren().remove(child);
				rightRoot.getChildren().add(newRightPage);
				return;
			}
			replaceWikiPage(child, newRightPage);
		}
	}

	/**
	 * TODO move to test 
	 */
	public Iterator<CodeSyncElement> getChildrenIterator(CodeSyncElement root) {
		return root.getChildren().iterator();
	}
	
}
