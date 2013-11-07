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
package com.crispico.flower.mp.codesync.code;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.editor.EditorPlugin;
import org.flowerplatform.editor.file.IFileAccessController;
import org.flowerplatform.editor.remote.EditorStatefulClientLocalState;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crispico.flower.mp.codesync.base.CodeSyncAlgorithm;
import com.crispico.flower.mp.codesync.base.CodeSyncEditableResource;
import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.codesync.base.Match;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactorySet;
import com.crispico.flower.mp.codesync.base.communication.CodeSyncEditorStatefulService;
import com.crispico.flower.mp.model.astcache.code.Annotation;
import com.crispico.flower.mp.model.astcache.code.AnnotationValue;
import com.crispico.flower.mp.model.astcache.code.Class;
import com.crispico.flower.mp.model.astcache.code.ExtendedModifier;
import com.crispico.flower.mp.model.astcache.code.ModifiableElement;
import com.crispico.flower.mp.model.astcache.code.Operation;
import com.crispico.flower.mp.model.astcache.code.Parameter;
import com.crispico.flower.mp.model.codesync.AstCacheElement;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.FeatureChange;

/**
 * @author Cristi
 */
public class CodeSyncCodePlugin extends AbstractFlowerJavaPlugin {
	
	public static String MARKER_ANNOTATION = "_MARKER";
	
	public static String SINGLE_MEMBER_ANNOTATION = "_SINGLE_MEMBER";
	 
	public static String NORMAL_ANNOTATION = "_NORMAL";
	
	public final static String SINGLE_MEMBER_ANNOTATION_VALUE_NAME = "_";
	
	protected static CodeSyncCodePlugin INSTANCE;
	
	private final static Logger logger = LoggerFactory.getLogger(CodeSyncCodePlugin.class);

	public static CodeSyncCodePlugin getInstance() {
		return INSTANCE;
	}
	
	protected ModelAdapterFactorySetProvider modelAdapterFactorySetProvider;
	
	/**
	 * @see #getOrCreateEditingDomain()
	 * 
	 * @author Mariana
	 */
	protected Map<File, AdapterFactoryEditingDomain> editingDomains = new HashMap<File, AdapterFactoryEditingDomain>();
	
	private Utils utils = new Utils();
	
	/**
	 * @author Mariana
	 */
	public ModelAdapterFactorySetProvider getModelAdapterFactorySetProvider() {
		return modelAdapterFactorySetProvider;
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
		
		initializeExtensionPoint_modelAdapterFactorySet();
	}
	
	private void initializeExtensionPoint_modelAdapterFactorySet() throws CoreException {
		modelAdapterFactorySetProvider = new ModelAdapterFactorySetProvider();
		IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.flowerplatform.codesync.code.modelAdapterFactorySet");
		for (IConfigurationElement configurationElement : configurationElements) {
			String id = configurationElement.getAttribute("id");
			String technology = configurationElement.getAttribute("technology");
			Object instance = configurationElement.createExecutableExtension("modelAdapterFactorySetClass");
			modelAdapterFactorySetProvider.getFactorieSets().put(technology, (ModelAdapterFactorySet) instance);
			logger.debug("Added CodeSync ModelAdapterFactorySet with id = {} with class = {}", id, instance.getClass());
		}
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
	 * @author Sebastian Solomon
	 */
	public CodeSyncElement getCodeSyncElement(Object project, Object file, String technology, CommunicationChannel communicationChannel, boolean showDialog) {
		IFileAccessController fileAccessController = EditorPlugin.getInstance().getFileAccessController();
		// find model files
		ResourceSet resourceSet = CodeSyncPlugin.getInstance().getOrCreateResourceSet(project, "diagramEditorStatefulService");
		Resource cseResource = CodeSyncPlugin.getInstance().getCodeSyncMapping(project, resourceSet);
		
		// find containing SrcDir
		CodeSyncElement srcDir = null;
		Object srcDirFile = null;
		Object parent = file;
		do {
			srcDir = CodeSyncPlugin.getInstance().getSrcDir(cseResource, fileAccessController.getName(parent));
			srcDirFile = parent;
			parent = fileAccessController.getParentFile(parent);
		} while (srcDir == null && !parent.equals(project));
		if (srcDir == null) {
			throw new RuntimeException("File " + file + " is not contained in a SrcDir!");
		}
		
		// find the CodeSyncElement in the SrcDir
		String relativeToSrcDir = EditorPlugin.getInstance().getFileAccessController().getPathRelativeToFile(file, srcDirFile);
		// there are cases when path format is a\b\c and the split method will not return correctly.
		// so replace \ with /
		relativeToSrcDir = relativeToSrcDir.replaceAll("\\\\", "/");
		if (relativeToSrcDir.startsWith("/")) {
			relativeToSrcDir = relativeToSrcDir.substring(1);
		}
		String[] fragments = relativeToSrcDir.length() > 0 ? relativeToSrcDir.split("/") : new String[0];
		CodeSyncElement codeSyncElement = getCodeSyncElement(srcDir, fragments);
		
		String srcDirPath = EditorPlugin.getInstance().getFileAccessController().getPathRelativeToFile(srcDirFile, project);
		String relativeToProject =EditorPlugin.getInstance().getFileAccessController().getPathRelativeToFile(file, project);
		if (codeSyncElement == null || showDialog) {
			runCodeSyncAlgorithm(srcDir, project, resourceSet, srcDirPath, relativeToProject, technology, communicationChannel, showDialog);
		} else {
			if (showDialog) {
				runCodeSyncAlgorithm(srcDir, project, resourceSet, srcDirPath, relativeToProject, technology, communicationChannel, showDialog);
			}
			return codeSyncElement;
		}
		return getCodeSyncElement(srcDir, fragments);
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
				String name = (String) CodeSyncOperationsService.getInstance().getKeyFeatureValue(cse);
				if (name.equals(path[i])) {
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
	 * @author Sebastian Solomon
	 */
	public CodeSyncEditableResource runCodeSyncAlgorithm(CodeSyncElement model, Object project, ResourceSet resourceSet, String path, String limitedPath, String technology, CommunicationChannel communicationChannel, boolean showDialog) {
//	public CodeSyncEditableResource runCodeSyncAlgorithm(CodeSyncElement model, IProject project, IResource file, String technology, CommunicationChannel communicationChannel) {
		CodeSyncEditorStatefulService service = (CodeSyncEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(CodeSyncEditorStatefulService.SERVICE_ID);
		String editableResourcePath = EditorPlugin.getInstance().getFileAccessController().getPath(project);
		CodeSyncEditableResource editableResource; // = (CodeSyncEditableResource) service.getEditableResource(project.getFullPath().toString());
		if (showDialog) {
			editableResource = (CodeSyncEditableResource) service.subscribeClientForcefully(communicationChannel, editableResourcePath);
		} else {
			service.subscribe(new StatefulServiceInvocationContext(communicationChannel), new EditorStatefulClientLocalState(editableResourcePath));
			editableResource = (CodeSyncEditableResource) service.getEditableResource(editableResourcePath);
		}
//		CodeSyncEditableResource editableResource = new CodeSyncEditableResource();
	
		Match match = new Match();
		match.setAncestor(model);
		match.setLeft(model);
		Object ast;
		if (model.getType().equals(CodeSyncPlugin.FOLDER)) {
			ast = CodeSyncPlugin.getInstance().getProjectsProvider().getFolder(project, path);
		}else {
			ast = CodeSyncPlugin.getInstance().getProjectsProvider().getFile(project, path);
		}
		
//		ast = file.getParent();
		match.setRight(ast);
		
		match.setEditableResource(editableResource);
		editableResource.setMatch(match);
		ModelAdapterFactorySet modelAdapterFactorySet = getModelAdapterFactorySetProvider().getFactorieSets().get(technology);
		if (!limitedPath.startsWith("/")) {
			limitedPath = "/" + limitedPath;
		}
		modelAdapterFactorySet.initialize(CodeSyncPlugin.getInstance().getAstCache(project, resourceSet), null, CodeSyncPlugin.getInstance().useUIDs());
		editableResource.setModelAdapterFactorySet(modelAdapterFactorySet);
		
		new CodeSyncAlgorithm(editableResource.getModelAdapterFactorySet()).generateDiff(match);
		
//		StatefulServiceInvocationContext context = new StatefulServiceInvocationContext(communicationChannel);
//		service.attemptUpdateEditableResourceContent(context, project.getFullPath().toString(), null);
		
		if (!showDialog) {
			// we're not showing the dialog => perform sync
			StatefulServiceInvocationContext context = new StatefulServiceInvocationContext(communicationChannel);
			service.synchronize(context, editableResourcePath);
			// and unsubscribe
			service.unsubscribeAllClientsForcefully(editableResourcePath, false);
		}
		
		return editableResource;
//		return null;
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
		
		public void addToResource(File project, ResourceSet resourceSet, AstCacheElement ace) {
			CodeSyncPlugin.getInstance().getAstCache(project, resourceSet).getContents().add(ace);
		}
		
		public boolean testEquality(Resource expected, Resource actual, String name) {
			return EcoreUtil.equals(CodeSyncPlugin.getInstance().getSrcDir(expected, name), CodeSyncPlugin.getInstance().getSrcDir(actual, name));
		}
	}
	
}
