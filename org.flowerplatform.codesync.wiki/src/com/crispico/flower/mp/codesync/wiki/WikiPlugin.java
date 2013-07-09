package com.crispico.flower.mp.codesync.wiki;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
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
import org.flowerplatform.emf_model.notation.util.NotationAdapterFactory;
import org.osgi.framework.BundleContext;

import astcache.wiki.FlowerBlock;
import astcache.wiki.Page;
import astcache.wiki.util.WikiAdapterFactory;

import com.crispico.flower.mp.codesync.base.CodeSyncEditableResource;
import com.crispico.flower.mp.codesync.base.Match;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactory;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactorySet;
import com.crispico.flower.mp.codesync.base.communication.CodeSyncEditorStatefulService;
import com.crispico.flower.mp.codesync.merge.CodeSyncMergePlugin;
import com.crispico.flower.mp.codesync.wiki.dokuwiki.DokuWikiConfigurationProvider;
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
	
	protected Map<IProject, AdapterFactoryEditingDomain> editingDomains = new HashMap<IProject, AdapterFactoryEditingDomain>();
	
	protected Map<String, IConfigurationProvider> configurationProviders = new HashMap<String, IConfigurationProvider>();
	
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
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

	public void updateTree(CodeSyncElement left, CodeSyncElement right, IProject project, String technology, CommunicationChannel communicationChannel) {
		CodeSyncEditorStatefulService service = (CodeSyncEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(CodeSyncEditorStatefulService.SERVICE_ID);
		CodeSyncEditableResource editableResource = (CodeSyncEditableResource) service.subscribeClientForcefully(communicationChannel, project.getFullPath().toString());
		
		Match match = new Match();
		match.setAncestor(right);
		match.setLeft(left);
		match.setRight(right);
		
		ModelAdapterFactory rightFactory = new ModelAdapterFactory();
		WikiNodeModelAdapterRight rightAdapter = new WikiNodeModelAdapterRight();
		rightAdapter.setEObjectConverter(rightFactory);
		rightAdapter.setResource(getAstCacheResource(project));
		rightAdapter.setTechnology(technology);
		rightFactory.addModelAdapter(CodeSyncElement.class, rightAdapter);
		
		ModelAdapterFactory ancestorFactory = new ModelAdapterFactory();
		WikiNodeModelAdapter ancestorAdapter = new WikiNodeModelAdapter();
		ancestorAdapter.setEObjectConverter(ancestorFactory);
		ancestorAdapter.setTechnology(technology);
		ancestorFactory.addModelAdapter(CodeSyncElement.class, ancestorAdapter);
		
		ModelAdapterFactory leftFactory = new ModelAdapterFactory();
		WikiNodeModelAdapter leftAdapter = new WikiNodeModelAdapter();
		leftAdapter.setEObjectConverter(leftFactory);
		leftAdapter.setResource(getAstCacheResource(left));
		leftAdapter.setTechnology(technology);
		leftFactory.addModelAdapter(CodeSyncElement.class, leftAdapter);
		
		match.setEditableResource(editableResource);
		editableResource.setMatch(match);
		editableResource.setModelAdapterFactorySet(new ModelAdapterFactorySet(ancestorFactory, leftFactory, rightFactory));
		
		new WikiSyncAlgorithm(editableResource.getModelAdapterFactorySet(), technology).generateDiff(match);
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
		IFile astCacheElementFile = project == null ? null : project.getFile(new Path(ACE_FILE_LOCATION));
		return CodeSyncMergePlugin.getInstance().getResource(getOrCreateEditingDomain(project), astCacheElementFile);
	}
	
	public Resource getCodeSyncMappingResource(IProject project) {
		IFile codeSyncElementMappingFile = project.getFile(new Path(CSE_FILE_LOCATION));
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
	public CodeSyncRoot getWikiTree(IProject project, Object wiki, String technology) {
		CodeSyncRoot root = configurationProviders.get(technology).getWikiTree(wiki);
		Resource codeSyncResource = project == null ? getOrCreateEditingDomain(project).createResource("tempCSE") : getCodeSyncMappingResource(project);
		codeSyncResource.getContents().add(root);
		Resource astCacheResource = project == null ? getOrCreateEditingDomain(project).createResource("tempACE") : getAstCacheResource(project);
		addToAstCacheResource(root, astCacheResource);
		return root;
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
		if (category.startsWith("Headline")) {
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
