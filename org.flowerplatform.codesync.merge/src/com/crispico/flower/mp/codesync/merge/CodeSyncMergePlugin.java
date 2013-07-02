package com.crispico.flower.mp.codesync.merge;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.service.ServiceRegistry;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.emf_model.notation.util.NotationAdapterFactory;
import org.osgi.framework.BundleContext;

import com.crispico.flower.mp.codesync.base.CodeSyncAlgorithm;
import com.crispico.flower.mp.codesync.base.CodeSyncEditableResource;
import com.crispico.flower.mp.codesync.base.Match;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactory;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactorySet;
import com.crispico.flower.mp.codesync.base.communication.CodeSyncEditorStatefulService;
import com.crispico.flower.mp.model.astcache.code.AstCacheCodeFactory;
import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;
import com.crispico.flower.mp.model.astcache.code.ExtendedModifier;
import com.crispico.flower.mp.model.astcache.code.Modifier;
import com.crispico.flower.mp.model.astcache.code.util.AstCacheCodeAdapterFactory;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.FeatureChange;
import com.crispico.flower.mp.model.codesync.util.CodeSyncAdapterFactory;

/**
 * @flowerModelElementId _FzGvEK3bEeC2ycztxbwS7Q
 */
public class CodeSyncMergePlugin extends AbstractFlowerJavaPlugin {

	public static String MARKER_ANNOTATION = "_MARKER";
	
	public static String SINGLE_MEMBER_ANNOTATION = "_SINGLE_MEMBER";
	 
	public static String NORMAL_ANNOTATION = "_NORMAL";
	
	public final static String SINGLE_MEMBER_ANNOTATION_VALUE_NAME = "_";
	
	protected static CodeSyncMergePlugin INSTANCE;
	
	/**
	 * @see #getOrCreateEditingDomain()
	 * 
	 * @author Mariana
	 */
	protected Map<IProject, AdapterFactoryEditingDomain> editingDomains = new HashMap<IProject, AdapterFactoryEditingDomain>();
	
	public static CodeSyncMergePlugin getInstance() {
		return INSTANCE;
	}
	
	/**
	 * @flowerModelElementId _FzH9Mq3bEeC2ycztxbwS7Q
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		INSTANCE = this;
	}
	
	@Override
	public void registerMessageBundle() throws Exception {
		// nothing to do yet
	}
	
	/**
	 * @author Mariana
	 */
	public void mergeModels(List<?> selection, CommunicationChannel communicationChannel) {
		IFile ancestorFile = (IFile) selection.get(0);
		IFile leftFile = (IFile) selection.get(1);
		IFile rightFile = (IFile) selection.get(2);
		
		Match match = new Match();
		match.setAncestor(getResource(ancestorFile).getContents().get(0));
		match.setLeft(getResource(leftFile).getContents().get(0));
		match.setRight(getResource(rightFile).getContents().get(0));
		
		String projectPath = ancestorFile.getProject().getFullPath().toString();
		CodeSyncEditorStatefulService service = (CodeSyncEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(CodeSyncEditorStatefulService.SERVICE_ID);
		CodeSyncEditableResource editableResource = (CodeSyncEditableResource) service.subscribeClientForcefully(communicationChannel, projectPath);
		
		ModelAdapterFactory codeSyncModelAdapterFactory = new ModelAdapterFactory();
		codeSyncModelAdapterFactory.addModelAdapter(CodeSyncElement.class, new CodeSyncElementModelAdapter());
		codeSyncModelAdapterFactory.addModelAdapter(EObject.class, new EObjectModelAdapter());
		
		match.setEditableResource(editableResource);
		editableResource.setMatch(match);
		editableResource.setModelAdapterFactorySet(new ModelAdapterFactorySet(codeSyncModelAdapterFactory, codeSyncModelAdapterFactory, codeSyncModelAdapterFactory));

		new CodeSyncAlgorithm(editableResource.getModelAdapterFactorySet()).generateDiff(match);
		
		StatefulServiceInvocationContext context = new StatefulServiceInvocationContext(communicationChannel);
		service.attemptUpdateEditableResourceContent(context, projectPath, null);
	}
	
	/**
	 * @author Mariana
	 */
	public Resource getResource(IFile file) {
		URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
		boolean fileExists = file.exists();
		return getResource(uri, file.getProject(), fileExists);
	}
	
	/**
	 * @author Mariana
	 */
	public Resource getResource(File file) {
		URI uri = URI.createFileURI(file.getAbsolutePath());
		boolean fileExists = file.exists();
		return getResource(uri, null, fileExists);
	}
	
	/**
	 * @author Mariana
	 */
	public Resource getResource(URI uri, IProject project, boolean fileExists) {
		if (fileExists) {
			return getOrCreateEditingDomain(project).getResourceSet().getResource(uri, true);
		} else {
			Resource resource =	getOrCreateEditingDomain(project).getResourceSet().getResource(uri, false);
			if (resource == null) {
				resource = getOrCreateEditingDomain(project).getResourceSet().createResource(uri);
			}
			resource.unload();
			return resource;
		}
	}
	
	/**
	 * Saves all the resources from the {@link ResourceSet} where <code>resourceToSave</code>
	 * is contained.
	 * 
	 * @author Mariana
	 */
	public void saveResource(Resource resourceToSave) {
		if (resourceToSave != null) {
			List<Resource> resources = Collections.singletonList(resourceToSave);
			if (resourceToSave.getResourceSet() != null) {
				resources = resourceToSave.getResourceSet().getResources();
			}
			for (Resource resource : resources) {
				try { 
					Map<Object, Object> options = new HashMap<Object, Object>();
					options.put(XMLResource.OPTION_ENCODING, "UTF-8");
					options.put(XMLResource.OPTION_XML_VERSION, "1.1");
					resource.save(options);
				} catch (IOException e) {
				}
			}
		}
	}
	
	/**
	 * Discards the modifications from all the resources from the {@link ResourceSet} 
	 * where <code>resourceToDiscard</code> is contained.
	 * 
	 * @author Mariana
	 */
	public void discardResource(Resource resourceToDiscard) {
		if (resourceToDiscard != null) {
			List<Resource> resources = Collections.singletonList(resourceToDiscard);
			if (resourceToDiscard.getResourceSet() != null) {
				resources = resourceToDiscard.getResourceSet().getResources();
			}
			for (Resource resource : resources) {
				resource.unload();
			}
		}
	}
	
	/**
	 * @author Mariana
	 */
	public AdapterFactoryEditingDomain getOrCreateEditingDomain(IProject project) {
		AdapterFactoryEditingDomain domain = editingDomains.get(project);
		if (domain == null) {
			ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

			adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
			adapterFactory.addAdapterFactory(new CodeSyncAdapterFactory());
			adapterFactory.addAdapterFactory(new AstCacheCodeAdapterFactory());
			adapterFactory.addAdapterFactory(new NotationAdapterFactory());
			adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
			
			domain = new AdapterFactoryEditingDomain(adapterFactory, null, new HashMap<Resource, Boolean>());
			editingDomains.put(project, domain);
		}
		return domain;
	}
	
	/**
	 * TODO Mariana : move to test
	 */
	public boolean testValueAsString() {
		// STEP 1 : create FeatureChange
		CodeSyncElement expectedCSE = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createCodeSyncElement();
		FeatureChange expectedChange = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createFeatureChange();
		expectedCSE.getFeatureChanges().put(AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers(), expectedChange); 
		EList<ExtendedModifier> modifiers = new BasicEList<ExtendedModifier>();
		Modifier modifier = AstCacheCodeFactory.eINSTANCE.createModifier();
		modifier.setType(3);
		modifiers.add(modifier);
		expectedChange.setOldValue(modifiers);
		
		// STEP 2 : put FC in resource and save
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("test");
		try {
			if (!project.exists()) {
				project.create(null);
			}
			project.open(null);
			IFile codeSyncElementsFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path("test/CSE.notation"));
			Resource resource = null;
			if (!codeSyncElementsFile.exists()) {
				resource = CodeSyncMergePlugin.getInstance().getResource(codeSyncElementsFile);
				resource.getContents().add(expectedCSE);
				CodeSyncMergePlugin.getInstance().saveResource(resource);
			}
			
			discardResource(resource);
			
			// STEP 3 : get value from resource
			resource = CodeSyncMergePlugin.getInstance().getResource(codeSyncElementsFile);
//					assertEquals(1, resource.getContents().size());
			CodeSyncElement actualCSE = (CodeSyncElement) resource.getContents().get(0);
			FeatureChange actualChange = actualCSE.getFeatureChanges().get(AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers());
//					assertNotNull(actualChange);
			EList<ExtendedModifier> actual = (EList<ExtendedModifier>) actualChange.getOldValue();
//					assertEquals(1, actual.size());
//					assertEquals(3, ((Modifier) actual.get(0)).getType());
			return ((Modifier) actual.get(0)).getType() == 3;
		} catch (CoreException e) {
			return false;
		}
	}
}
