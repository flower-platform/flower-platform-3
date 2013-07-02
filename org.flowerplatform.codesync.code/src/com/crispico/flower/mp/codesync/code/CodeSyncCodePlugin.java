package com.crispico.flower.mp.codesync.code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.osgi.framework.BundleContext;

import com.crispico.flower.mp.codesync.base.CodeSyncAlgorithm;
import com.crispico.flower.mp.codesync.base.CodeSyncEditableResource;
import com.crispico.flower.mp.codesync.base.Match;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactory;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactorySet;
import com.crispico.flower.mp.codesync.base.communication.CodeSyncEditorStatefulService;
import com.crispico.flower.mp.codesync.merge.CodeSyncElementFeatureProvider;
import com.crispico.flower.mp.codesync.merge.CodeSyncMergePlugin;
import com.crispico.flower.mp.model.astcache.code.Annotation;
import com.crispico.flower.mp.model.astcache.code.AnnotationValue;
import com.crispico.flower.mp.model.astcache.code.Class;
import com.crispico.flower.mp.model.astcache.code.ExtendedModifier;
import com.crispico.flower.mp.model.astcache.code.ModifiableElement;
import com.crispico.flower.mp.model.astcache.code.Operation;
import com.crispico.flower.mp.model.astcache.code.Parameter;
import com.crispico.flower.mp.model.codesync.AstCacheElement;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;
import com.crispico.flower.mp.model.codesync.FeatureChange;

/**
 * @author Cristi
 */
public class CodeSyncCodePlugin extends AbstractFlowerJavaPlugin {
	
	protected static CodeSyncCodePlugin INSTANCE;
	
	public static CodeSyncCodePlugin getInstance() {
		return INSTANCE;
	}
	
	protected ComposedDragOnDiagramHandler dragOnDiagramHandler;
	
	protected ComposedFullyQualifiedNameProvider fullyQualifiedNameProvider;
	
	protected ModelAdapterFactoryProvider modelAdapterFactoryProvider;
	
	protected List<String> srcDirs = null;
	
	private Utils utils = new Utils();
	
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
	
	public ComposedDragOnDiagramHandler getDragOnDiagramHandler() {
		return dragOnDiagramHandler;
	}

	public ComposedFullyQualifiedNameProvider getFullyQualifiedNameProvider() {
		return fullyQualifiedNameProvider;
	}
	
	/**
	 * @author Mariana
	 */
	public ModelAdapterFactoryProvider getModelAdapterFactoryProvider() {
		return modelAdapterFactoryProvider;
	}

	/**
	 * @author Mariana
	 */
	public Utils getUtils() {
		return utils;
	}
	
	/**
	 * @author Cristi
	 * @author Mariana
	 */
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
		
		dragOnDiagramHandler = new ComposedDragOnDiagramHandler();
		fullyQualifiedNameProvider = new ComposedFullyQualifiedNameProvider();
		
		modelAdapterFactoryProvider = new ModelAdapterFactoryProvider();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// nothing to do yet
	}
	
	/**
	 * @author Cristi
	 * @author Mariana
	 */
//	public CodeSyncElement getCodeSyncElement(String fullyQualifiedName, String technology, CommunicationChannel communicationChannel) {
	public CodeSyncElement getCodeSyncElement(IProject project, IFile file, String technology, CommunicationChannel communicationChannel) {
//		if (fullyQualifiedName.startsWith("/")) {
//			fullyQualifiedName = fullyQualifiedName.substring(1);
//		}
//		String[] pathFragments = fullyQualifiedName.split("/");
//		
//		// STEP 1 : identify the project corresponding to the 1st path fragment and find the CSE mapping file
//		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(pathFragments[0]);
//		if (!project.exists()) {
//			throw new RuntimeException("Project " + pathFragments[0] + " does not exist!");
//		}
		
		// TEST : if there is already an open ER for this project, don't perform sync
//		CodeSyncEditorStatefulService service = (CodeSyncEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(CodeSyncEditorStatefulService.SERVICE_ID);
//		CodeSyncEditableResource editableResource = (CodeSyncEditableResource) service.getEditableResource(project.getFullPath().toString());
//		if (editableResource != null) {
//			service.subscribeClientForcefully(communicationChannel, project.getFullPath().toString());
//			return null;
//		}
		
		Resource cseResource = getCodeSyncMapping(project);
		
		// STEP 2 : find the SrcDir corresponding to the 2nd path fragment
//		CodeSyncElement srcDir = getSrcDir(cseResource, pathFragments[1]);
		CodeSyncElement srcDir = getSrcDir(cseResource, "src");
		
//		// STEP 3 : find the CSE corresponding to the name
//		String[] path = Arrays.copyOfRange(pathFragments, 2, pathFragments.length);
//		CodeSyncElement codeSyncElement = getCodeSyncElement(srcDir, path);
//		if (codeSyncElement == null) {
			// SUBSTEP : run the CodeSync algorithm if the CSE does not exist
//			runCodeSyncAlgorithm(srcDir, project, pathFragments[1], technology, communicationChannel);
			runCodeSyncAlgorithm(srcDir, project, file, technology, communicationChannel);
//		}
//		return getCodeSyncElement(srcDir, path);
		return null;
	}
	
	/**
	 * Finds the {@link CodeSyncElement} corresponding to the <code>path</code> by traversing the CSE tree
	 * rooted at the <code>srcDir</code>.
	 * 
	 * @author Mariana
	 */
	public CodeSyncElement getCodeSyncElement(CodeSyncElement srcDir, String[] path) {
		CodeSyncElement codeSyncElement = srcDir;
		boolean foundForPath = true;
		for (int i = 0; i < path.length; i++) {
			boolean foundChild = false;
			for (CodeSyncElement cse : codeSyncElement.getChildren()) {
				if (cse.getName().equals(path[i])) {
					codeSyncElement = cse;
					foundChild = true;
					break;
				}
			}
			if (!foundChild) {
				foundForPath = false;
				break;
			}
		}
		if (foundForPath) {
			return codeSyncElement;
		}
		return null;
	}
	
	/**
	 * @author Mariana
	 */
//	public CodeSyncEditableResource runCodeSyncAlgorithm(CodeSyncElement model, IProject project, String path, String technology, CommunicationChannel communicationChannel) {
	public CodeSyncEditableResource runCodeSyncAlgorithm(CodeSyncElement model, IProject project, IFile file, String technology, CommunicationChannel communicationChannel) {
//		CodeSyncEditorStatefulService service = (CodeSyncEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(CodeSyncEditorStatefulService.SERVICE_ID);
//		CodeSyncEditableResource editableResource = (CodeSyncEditableResource) service.subscribeClientForcefully(communicationChannel, project.getFullPath().toString());
		CodeSyncEditableResource editableResource = new CodeSyncEditableResource();
	
		Match match = new Match();
		match.setAncestor(model);
		match.setLeft(model);
		IResource ast = null;
//		if (model.getType().equals(AstModelElementAdapter.FOLDER)) {
//			ast = project.getFolder(path);
//		} else {
//			if (model.getType().equals(AstModelElementAdapter.FILE)) {
//				ast = project.getFile(path);
//			}
//		}
		ast = file;
		match.setRight(ast);
		
		// right - AST
		ModelAdapterFactory astModelAdapterFactory = modelAdapterFactoryProvider.getFactories().get(technology);
		Resource aceResource = getAstCache(project);
		CodeSyncElementFeatureProvider featureProvider = modelAdapterFactoryProvider.getFeatureProviders().get(technology);
		
		// ancestor - CodeSyncElements
		ModelAdapterFactory codeSyncElementModelAdapterFactoryAncestor = new ModelAdapterFactory();
		CodeSyncElementModelAdapterAncestor ancestor = new CodeSyncElementModelAdapterAncestor();
		ancestor.setFeatureProvider(featureProvider);
		ancestor.setEObjectConverter(astModelAdapterFactory);
		codeSyncElementModelAdapterFactoryAncestor.addModelAdapter(EObject.class, ancestor);
		codeSyncElementModelAdapterFactoryAncestor.addModelAdapter(String.class, new StringModelAdapter(codeSyncElementModelAdapterFactoryAncestor));
		
		// left - CodeSyncElements
		ModelAdapterFactory codeSyncElementModelAdapterFactoryLeft = new ModelAdapterFactory();
		CodeSyncElementModelAdapterLeft left = new CodeSyncElementModelAdapterLeft();
		left.setFeatureProvider(featureProvider);
		left.setEObjectConverter(astModelAdapterFactory);
		left.setResource(aceResource);
		codeSyncElementModelAdapterFactoryLeft.addModelAdapter(EObject.class, left);
		codeSyncElementModelAdapterFactoryLeft.addModelAdapter(String.class, new StringModelAdapter(codeSyncElementModelAdapterFactoryLeft));
		
		match.setEditableResource(editableResource);
		editableResource.setMatch(match);
		editableResource.setModelAdapterFactorySet(new ModelAdapterFactorySet(codeSyncElementModelAdapterFactoryAncestor, codeSyncElementModelAdapterFactoryLeft, astModelAdapterFactory));

		new CodeSyncAlgorithm(editableResource.getModelAdapterFactorySet()).generateDiff(match);
		
//		StatefulServiceInvocationContext context = new StatefulServiceInvocationContext(communicationChannel);
//		service.attemptUpdateEditableResourceContent(context, project.getFullPath().toString(), null);
//		
//		return editableResource;
		return null;
	}
	
	/**
	 * @author Mariana
	 */
	public Resource getCodeSyncMapping(IProject project) {
		IFile codeSyncElementMappingFile = project.getFile(new Path(CSE_MAPPING_FILE_LOCATION));
		Resource cseResource = CodeSyncMergePlugin.getInstance().getResource(codeSyncElementMappingFile);
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
					cseRoot.setType(FolderModelAdapter.FOLDER);
				}
				cseResource.getContents().add(cseRoot);
			}
			
			CodeSyncMergePlugin.getInstance().saveResource(cseResource);
		}
		return cseResource;
	}
	
	/**
	 * @author Mariana
	 */
	public Resource getAstCache(IProject project) {
		IFile astCacheElementFile = project.getFile(new Path(ACE_FILE_LOCATION));
		return CodeSyncMergePlugin.getInstance().getResource(astCacheElementFile);
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

	/**
	 * @author Mariana
	 */
	public class Utils {

		public void addFeatureChange(CodeSyncElement cse, EStructuralFeature feature, FeatureChange featureChange) {
			cse.getFeatureChanges().put(feature, featureChange);
		}
		
		public void removeFeatureChange(CodeSyncElement cse, Object feature) {
			cse.getFeatureChanges().removeKey(feature);
		}
		
		public void addChild(CodeSyncElement parent, CodeSyncElement child) {
			parent.getChildren().add(child);
		}
		
		public void addAnnotationValue(Annotation annotation, AnnotationValue value) {
			annotation.getValues().add(value);
		}
		
		public void setSuperClasses(Class cls, List<String> superClasses) {
			cls.getSuperClasses().clear();
			cls.getSuperClasses().addAll(superClasses);
		}
		
		public void setSuperInterfaces(Class cls, List<String> superInterfaces) {
			cls.getSuperInterfaces().clear();
			cls.getSuperInterfaces().addAll(superInterfaces);
		}
		
		public void setModifiers(ModifiableElement op, List<ExtendedModifier> modifiers) {
			op.getModifiers().clear();
			op.getModifiers().addAll(modifiers);
		}
		
		public void setParams(Operation op, List<Parameter> params) {
			op.getParameters().clear();
			op.getParameters().addAll(params);
		}
		
		public void addToResource(IProject project, AstCacheElement ace) {
			getAstCache(project).getContents().add(ace);
		}
		
		public boolean testEquality(Resource expected, Resource actual, String name) {
			return EcoreUtil.equals(getSrcDir(expected, name), getSrcDir(actual, name));
		}
	}
	
}
