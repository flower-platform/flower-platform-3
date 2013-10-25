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
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.flowerplatform.blazeds.custom_serialization.CustomSerializationDescriptor;
import org.flowerplatform.codesync.changes_processor.CodeSyncTypeCriterionDispatcherProcessor;
import org.flowerplatform.codesync.operation_extension.AddNewExtension;
import org.flowerplatform.codesync.operation_extension.AddNewTopLevelElementExtension;
import org.flowerplatform.codesync.operation_extension.FeatureAccessExtension;
import org.flowerplatform.codesync.projects.IProjectsProvider;
import org.flowerplatform.codesync.remote.CodeSyncAction;
import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.editor.remote.EditableResource;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;

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
	
	public static final String FOLDER = "Folder";
	
	public static final String FILE = "File";
	
	private final static Logger logger = LoggerFactory.getLogger(CodeSyncPlugin.class);

	protected ComposedFullyQualifiedNameProvider fullyQualifiedNameProvider;
	
	protected ComposedCodeSyncAlgorithmRunner codeSyncAlgorithmRunner;
	
	protected List<CodeSyncElementDescriptor> codeSyncElementDescriptors;

	protected List<FeatureAccessExtension> featureAccessExtensions;
	
	protected List<AddNewExtension> addNewExtensions;
	
	protected CodeSyncTypeCriterionDispatcherProcessor codeSyncTypeCriterionDispatcherProcessor;
	
	/**
	 * @see #getProjectsProvider()
	 */
	private IProjectsProvider projectsProvider;
	
	protected boolean useUIDs = true;
	
	public static CodeSyncPlugin getInstance() {
		return INSTANCE;
	}
	
	public CodeSyncTypeCriterionDispatcherProcessor getCodeSyncTypeCriterionDispatcherProcessor() {
		return codeSyncTypeCriterionDispatcherProcessor;
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
	
	public List<FeatureAccessExtension> getFeatureAccessExtensions() {
		return featureAccessExtensions;
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
		
		codeSyncTypeCriterionDispatcherProcessor = new CodeSyncTypeCriterionDispatcherProcessor();
		EditorModelPlugin.getInstance().getMainChangesDispatcher().addProcessor(codeSyncTypeCriterionDispatcherProcessor);
		
		codeSyncElementDescriptors = new ArrayList<CodeSyncElementDescriptor>();
		
		addNewExtensions = new ArrayList<AddNewExtension>();
		addNewExtensions.add(new AddNewTopLevelElementExtension());
		featureAccessExtensions = new ArrayList<FeatureAccessExtension>();
		
		fullyQualifiedNameProvider = new ComposedFullyQualifiedNameProvider();
		initializeExtensionPoint_codeSyncAlgorithmRunner();
		
		getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType(FOLDER)
				.setLabel(FOLDER)
				.addChildrenCodeSyncTypeCategory(FILE));
		getCodeSyncElementDescriptors().add(
				new CodeSyncElementDescriptor()
				.setCodeSyncType(FILE)
				.setLabel(FILE)
				.addCodeSyncTypeCategory(FILE));
		
		// needs custom descriptor because it uses the builder template (i.e. setters return the instance)
		new CustomSerializationDescriptor(CodeSyncElementDescriptor.class)
			.addDeclaredProperty("codeSyncType")
			.addDeclaredProperty("label")
			.addDeclaredProperty("iconUrl")
			.addDeclaredProperty("defaultName")
			.addDeclaredProperty("extension")
			.addDeclaredProperty("codeSyncTypeCategories")
			.addDeclaredProperty("childrenCodeSyncTypeCategories")
			.addDeclaredProperty("features")
			.addDeclaredProperty("keyFeature")
			.register();
	}
	
	private void initializeExtensionPoint_codeSyncAlgorithmRunner() throws CoreException {
		codeSyncAlgorithmRunner = new ComposedCodeSyncAlgorithmRunner();
		IConfigurationElement[] configurationElements = 
				Platform.getExtensionRegistry().getConfigurationElementsFor("org.flowerplatform.codesync.codeSyncAlgorithmRunner");
		for (IConfigurationElement configurationElement : configurationElements) {
			String id = configurationElement.getAttribute("id");
			String technology = configurationElement.getAttribute("technology");
			Object instance = configurationElement.createExecutableExtension("codeSyncAlgorithmRunnerClass");
			codeSyncAlgorithmRunner.addRunner(technology, (ICodeSyncAlgorithmRunner) instance);
			logger.debug("Added CodeSync algorithm runner with id = {} with class = {}", id, instance.getClass());
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

		DiagramEditableResource diagramEditableResource = null;		
		if (project != null) {
			String path = project.getAbsolutePath();
			for (EditableResource er : service.getEditableResources().values()) {
				DiagramEditableResource der = (DiagramEditableResource) er;				
				if (((File)der.getFile()).getAbsolutePath().startsWith(path)) {
					diagramEditableResource = der;
					break;
				}
			}
		}
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
