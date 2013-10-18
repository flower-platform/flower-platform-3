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
package com.crispico.flower.mp.codesync.base;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.flowerplatform.codesync.operation_extension.AddNewExtension;
import org.flowerplatform.codesync.projects.IProjectsProvider;
import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.osgi.framework.BundleContext;

import com.crispico.flower.mp.model.codesync.AstCacheElement;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncFactory;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;
import com.crispico.flower.mp.model.codesync.FeatureChange;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncPlugin extends AbstractFlowerJavaPlugin {
	
	protected static CodeSyncPlugin INSTANCE;
	
	/**
	 * The location of the CSE mapping file, relative to the project. May be
	 * configurable in the future.
	 * 
	 * @author Mariana
	 */
	public String CSE_MAPPING_FILE_LOCATION = "/CSE.notation";
	
	/**
	 * The location of the ACE file, relative to the project. May be configurable
	 * in the future.
	 * 
	 * @author Mariana
	 */
	public String ACE_FILE_LOCATION = "/ACE.notation";
	
	protected List<String> srcDirs = null;
	
	public static final String FOLDER = "FOLDER";
	
	public static final String FILE = "File";
	
	protected CodeSyncOperationsService codeSyncOperationService;
	
	protected ComposedFullyQualifiedNameProvider fullyQualifiedNameProvider;
	
	protected ComposedCodeSyncAlgorithmRunner codeSyncAlgorithmRunner;
	
	protected List<CodeSyncElementDescriptor> codeSyncElementDescriptors;

	protected List<AddNewExtension> addNewExtensions;
	
	/**
	 * @see #getProjectsProvider()
	 */
	private IProjectsProvider projectsProvider;
	
	protected boolean useUIDs = true;
	
	public static CodeSyncPlugin getInstance() {
		return INSTANCE;
	}
	
	public CodeSyncOperationsService getCodeSyncOperationsService() {
		return codeSyncOperationService;
	}
	
	public ComposedFullyQualifiedNameProvider getFullyQualifiedNameProvider() {
		return fullyQualifiedNameProvider;
	}
	
	public ComposedCodeSyncAlgorithmRunner getCodeSyncAlgorithmRunner() {
		return codeSyncAlgorithmRunner;
	}
	
	/**
	 * Platform-dependent.
	 * 
	 * @author Mariana Gheorghe
	 */
	public IProjectsProvider getProjectsProvider() {
		return projectsProvider;
	}

	public void setProjectsProvider(IProjectsProvider projectsProvider) {
		this.projectsProvider = projectsProvider;
	}
	
	public List<CodeSyncElementDescriptor> getCodeSyncElementDescriptors() {
		return codeSyncElementDescriptors;
	}
	
	public CodeSyncElementDescriptor getCodeSyncElementDescriptor(String codeSyncType) {
		for (CodeSyncElementDescriptor descriptor : getCodeSyncElementDescriptors()) {
			if (descriptor.getCodeSyncType().equals(codeSyncType)) {
				return descriptor;
			}
		}
		return null;
	}
	
	public List<AddNewExtension> getAddNewExtensions() {
		return addNewExtensions;
	}
	
	public boolean useUIDs() {
		return useUIDs;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
		
		codeSyncOperationService = new CodeSyncOperationsService();
		fullyQualifiedNameProvider = new ComposedFullyQualifiedNameProvider();
		initializeExtensionPoint_codeSyncAlgorithmRunner();
		initializeExtensionPoint_codeSyncElementDescriptor();
		initializeExtensionPoint_operationExtension();
	}
	
	private void initializeExtensionPoint_codeSyncAlgorithmRunner() throws CoreException {
		codeSyncAlgorithmRunner = new ComposedCodeSyncAlgorithmRunner();
		IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.flowerplatform.codesync.codeSyncAlgorithmRunner");
		for (IConfigurationElement configurationElement : configurationElements) {
			String id = configurationElement.getAttribute("id");
			String technology = configurationElement.getAttribute("technology");
			Object instance = configurationElement.createExecutableExtension("codeSyncAlgorithmRunnerClass");
			codeSyncAlgorithmRunner.addRunner(technology, (ICodeSyncAlgorithmRunner) instance);
			logger.debug("Added CodeSync algorithm runner with id = {} with class = {}", id, instance.getClass());
		}
	}
	
	private void initializeExtensionPoint_codeSyncElementDescriptor() throws CoreException {
		codeSyncElementDescriptors = new ArrayList<CodeSyncElementDescriptor>();
		IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.flowerplatform.codesync.codeSyncElementDescriptor");
		for (IConfigurationElement configurationElement : configurationElements) {
			String codeSyncType = configurationElement.getAttribute("codeSyncType");
			String iconUrl = configurationElement.getAttribute("iconUrl");
			List<String> codeSyncTypeCategories = getAttributes(configurationElement, "codeSyncTypeCategory");
			List<String> childrenCodeSyncTypeCategories = getAttributes(configurationElement, "childrenCodeSyncTypeCategory");
			List<String> features = getAttributes(configurationElement, "feature");
			CodeSyncElementDescriptor descriptor = new CodeSyncElementDescriptor();
			descriptor.setCodeSyncType(codeSyncType);
			descriptor.setIconUrl(iconUrl);
			descriptor.setCodeSyncTypeCategories(codeSyncTypeCategories);
			descriptor.setChildrenCodeSyncTypeCategories(childrenCodeSyncTypeCategories);
			descriptor.setFeatures(features);
			codeSyncElementDescriptors.add(descriptor);
			logger.debug("Added CodeSyncElementDescriptor with type = {}", codeSyncType);
		}
	}
	
	private List<String> getAttributes(IConfigurationElement parentConfigurationElement, String id) {
		List<String> result = new ArrayList<String>();
		IConfigurationElement[] configurationElements = parentConfigurationElement.getChildren(id);
		for (IConfigurationElement configurationElement : configurationElements) {
			result.add(configurationElement.getAttribute("name"));
		}
		return result;
	}
	
	private void initializeExtensionPoint_operationExtension() throws CoreException {
		addNewExtensions = new ArrayList<AddNewExtension>();
		IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.flowerplatform.codesync.addNewExtension");
		for (IConfigurationElement configurationElement : configurationElements) {
			Object instance = configurationElement.createExecutableExtension("addNewExtension");
			addNewExtensions.add((AddNewExtension) instance);
		}
	}
	
	@Override
	public void registerMessageBundle() throws Exception {
		// no messages yet
	}

	/**
	 * @author Mariana Gheorghe
	 */
	public String getFileExtension(File file) {
		String name = file.getName();
		int index = name.lastIndexOf(".");
		if (index >= 0) {
			return name.substring(index + 1);
		}
		return "";
	}
	
	/**
	 * Important: the code sync mapping and cache resources <b>must</b> be loaded through the same {@link ResourceSet}.
	 */
	public ResourceSet getOrCreateResourceSet(File file, String diagramEditorStatefulServiceId) {
		File project = getProjectsProvider().getContainingProjectForFile(file);
		DiagramEditorStatefulService service = (DiagramEditorStatefulService) CommunicationPlugin.getInstance()
				.getServiceRegistry().getService(diagramEditorStatefulServiceId);
		DiagramEditableResource diagramEditableResource = service.getDiagramEditableResource(project);
		if (diagramEditableResource != null) {
			return diagramEditableResource.getResourceSet();
		}
		
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new ResourceFactoryImpl() {
			
			@Override
			public Resource createResource(URI uri) {
				return new XMIResourceImpl(uri) {
			    	protected boolean useUUIDs() {
			    		return true;
			    	}
				};
			}
		});
		return resourceSet;
	}
	
	/**
	 * @author Mariana
	 */
	public Resource getResource(ResourceSet resourceSet, File file) {
		URI uri = EditorModelPlugin.getInstance().getModelAccessController().getURIFromFile(file);
		boolean fileExists = file.exists();
		return getResource(resourceSet, uri, fileExists);
	}
	
	/**
	 * @author Mariana
	 */
	public Resource getResource(ResourceSet resourceSet, URI uri, boolean fileExists) {
		if (fileExists) {
			return resourceSet.getResource(uri, true);
		} else {
			Resource resource =	resourceSet.getResource(uri, false);
			if (resource == null) {
				resource = resourceSet.createResource(uri);
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
					throw new RuntimeException(e);
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
	 * Returns the value of <code>feature</code> on the <code>codeSyncElement</code>, first from the
	 * list of {@link org.eclipse.emf.ecore.change.FeatureChange}s, if it exists.
	 */
	public Object getFeatureValue(CodeSyncElement codeSyncElement, EStructuralFeature feature) {
		FeatureChange featureChange = codeSyncElement.getFeatureChanges().get(feature);
		if (featureChange != null) {
			return featureChange.getNewValue();
		}
		
		if (feature.getEContainingClass().isSuperTypeOf(codeSyncElement.eClass())) {
			return codeSyncElement.eGet(feature);
		} else {
			AstCacheElement astElement = codeSyncElement.getAstCacheElement();
			if (astElement != null) {
				return astElement.eGet(feature);
			}
		}
	
		return null;
	}
	
	public Object getOldFeatureValue(CodeSyncElement codeSyncElement, EStructuralFeature feature) {
		FeatureChange featureChange = codeSyncElement.getFeatureChanges().get(feature);
		if (featureChange != null) {
			return featureChange.getOldValue();
		} else {
			return getFeatureValue(codeSyncElement, feature);
		}
	}
	
	public void setFeatureValue(CodeSyncElement codeSyncElement, EStructuralFeature feature, Object newValue) {
		Object oldValue = getOldFeatureValue(codeSyncElement, feature);
		if (!codeSyncElement.isAdded()) {
			createAndAddFeatureChange(codeSyncElement, feature, oldValue, newValue);
		} else {
			setFeatureValueDirectly(codeSyncElement, feature, newValue);
		}
	}
	
	/**
	 * Creates and adds a {@link FeatureChange} if the <code>oldValue</code> and <code>newValue</code> are different. 
	 * 
	 * Important: we first remove, and then add a new feature change to trigger a change description on the {@link CodeSyncElement},
	 * so the processors can update the views.
	 * 
	 * @author Sebastian Solomon
	 */
	protected void createAndAddFeatureChange(CodeSyncElement element,
			EStructuralFeature feature, Object oldValue, Object newValue) {
		element.getFeatureChanges().removeKey(feature);
		if (!equal(newValue, oldValue)) {
			FeatureChange featureChange = CodeSyncFactory.eINSTANCE
					.createFeatureChange();
			featureChange.setOldValue(oldValue);
			featureChange.setNewValue(newValue);
			element.getFeatureChanges().put(feature, featureChange);

			if (element.isSynchronized()) {
				element.setSynchronized(false);
				propagateParentSyncFalse(element);
			}

		} else if (element.getFeatureChanges().size() == 0) {
			propagateParentSyncTrue(element);
		}

	}
	
	/**
	 * @author Sebastian Solomon
	 */
	public void propageteOnChildDelete(CodeSyncElement cse) {

		for (CodeSyncElement child : cse.getChildren()) {
			child.setDeleted(true);
			propageteOnChildDelete(child);
		}

	}
	
	/**
	 * @author Sebastian Solomon
	 */
	public void propagateParentSyncFalse(CodeSyncElement element) {
		while (element.eContainer() != null) {

			EObject parent = element.eContainer();
			if (parent instanceof CodeSyncElement) {
				element = (CodeSyncElement) parent;
				if (element.isSynchronized()) {
					element.setChildrenSynchronized(false);

				}

			} else
				return;

		}

	}
	

	/**
	 * @author Sebastian Solomon
	 */
	public void propagateParentSyncTrue(CodeSyncElement element) {
		if (!element.isAdded() && !element.isDeleted()
				&& element.getFeatureChanges().size() == 0) {
			element.setSynchronized(true); // orange
			if (allChildrenGreen(element)) // if all childs green =>become green
				element.setChildrenSynchronized(true);
			// * walk whole parent hierarchy; set childrenSync = true if all
			// children are sync,not newly added not deleted
			while (element.eContainer() != null) {
				if (element.eContainer() instanceof CodeSyncElement) {
					element = (CodeSyncElement) element.eContainer();
					if (allChildrenGreen(element)) {
						element.setChildrenSynchronized(true);
					} else
						return; // if one child is notSync, return
				}
			}
		}
	}
	
//	private boolean childsAreSync(CodeSyncElement element){ //orange or green
//		
//		for (CodeSyncElement childElem : element.getChildren()){
//			if ( childElem.isAdded() || childElem.isDeleted() || !childElem.isSynchronized())
//				return false;
//		}
//		return true;
//	}
	
	
	/**
	 * @author Sebastian Solomon
	 */
	private boolean allChildrenGreen(CodeSyncElement element) {
		for (CodeSyncElement cse : element.getChildren()) {
			if (cse.isAdded() || cse.isDeleted() || !cse.isSynchronized()
					|| !cse.isChildrenSynchronized())
				return false;
		}
		return true;
	}

	protected void setFeatureValueDirectly(CodeSyncElement codeSyncElement, EStructuralFeature feature, Object newValue) {
		if (feature.getEContainingClass().isSuperTypeOf(codeSyncElement.eClass())) {
			codeSyncElement.eSet(feature, newValue);
		} else {
			AstCacheElement astElement = codeSyncElement.getAstCacheElement();
			if (astElement != null) {
				astElement.eSet(feature, newValue);
			}
		}
	}
	
	protected boolean equal(Object a, Object b) {
		if (a == null) {
			return b == null;
		} else {
			return a.equals(b);
		}
	}
	
	/**
	 * @author Mariana
	 */
	public Resource getCodeSyncMapping(File project, ResourceSet resourceSet) {
		File codeSyncElementMappingFile = CodeSyncPlugin.getInstance().getProjectsProvider().getFile(project, CSE_MAPPING_FILE_LOCATION); 
		Resource cseResource = CodeSyncPlugin.getInstance().getResource(resourceSet, codeSyncElementMappingFile);
		if (!codeSyncElementMappingFile.exists()) {
			// first clear the resource in case the mapping file was deleted 
			// after it has been loaded at a previous moment
			cseResource.getContents().clear();
			
			for (String srcDir : getSrcDirs()) {
				CodeSyncRoot cseRoot = (CodeSyncRoot) getRoot(cseResource, srcDir);
				if (cseRoot == null) {
					// create the CSE for the SrcDir
					cseRoot = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createCodeSyncRoot();
					cseRoot.setName(srcDir);
					cseRoot.setType(FOLDER);
				}
				cseResource.getContents().add(cseRoot);
			}
			
			CodeSyncPlugin.getInstance().saveResource(cseResource);
		}
		return cseResource;
	}
	
	/**
	 * @author Mariana
	 */
	public Resource getAstCache(File project, ResourceSet resourceSet) {
		File astCacheElementFile = CodeSyncPlugin.getInstance().getProjectsProvider().getFile(project, ACE_FILE_LOCATION); 
		Resource resource = CodeSyncPlugin.getInstance().getResource(resourceSet, astCacheElementFile);
		if (!astCacheElementFile.exists()) {
			resource.getContents().clear();
			CodeSyncPlugin.getInstance().saveResource(resource);
		}
		return resource;
	}
	
	/**
	 * @author Mariana
	 */
	protected CodeSyncRoot getRoot(Resource resource, String srcDir) {
		for (EObject eObj : resource.getContents()) {
			if (eObj instanceof CodeSyncRoot) {
				CodeSyncRoot root = (CodeSyncRoot) eObj;
				if (root.getName().equals(srcDir))
					return root;
			}
		}
		return null;
	}
	
	/**
	 * @author Mariana
	 */
	public CodeSyncElement getSrcDir(Resource resource, String name) {
		CodeSyncElement srcDir = null;
		for (EObject member : resource.getContents()) {
			if (((CodeSyncElement) member).getName().equals(name)) {
				srcDir = (CodeSyncElement) member;
				break;
			}
		}
		if (srcDir == null) {
			throw new RuntimeException("SrcDir " + name + " is not mapped to a CSE!");
		}
		return srcDir;
	}
	
	/**
	 * @author Mariana
	 */
	public List<String> getSrcDirs() {
		if (srcDirs == null) {
			// TODO Mariana : get user input
			return Collections.singletonList("src");
		} 
		return srcDirs;
	}
	
	public void addSrcDir(String srcDir) {
		if (srcDirs == null) {
			srcDirs = new ArrayList<String>();
		}
		if (!srcDirs.contains(srcDir)) {
			srcDirs.add(srcDir);
		}
	}
	
}
