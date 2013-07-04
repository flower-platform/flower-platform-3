package com.crispico.flower.mp.codesync.merge;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.edit.domain.EditingDomain;
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
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @flowerModelElementId _FzGvEK3bEeC2ycztxbwS7Q
 */
public class CodeSyncMergePlugin extends AbstractFlowerJavaPlugin {

	protected static CodeSyncMergePlugin INSTANCE;
	
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
		match.setAncestor(getResource(null, ancestorFile).getContents().get(0));
		match.setLeft(getResource(null, leftFile).getContents().get(0));
		match.setRight(getResource(null, rightFile).getContents().get(0));
		
		String projectPath = ancestorFile.getProject().getFullPath().toString();
//		CodeSyncEditorStatefulService service = (CodeSyncEditorStatefulService) ServiceRegistry.INSTANCE.getService(CodeSyncEditorStatefulService.SERVICE_ID);
		CodeSyncEditorStatefulService service = (CodeSyncEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(CodeSyncEditorStatefulService.SERVICE_ID);

		CodeSyncEditableResource editableResource = (CodeSyncEditableResource) service.subscribeClientForcefully(communicationChannel, projectPath);
		
		ModelAdapterFactory codeSyncModelAdapterFactory = new ModelAdapterFactory();
		codeSyncModelAdapterFactory.addModelAdapter(CodeSyncElement.class, new SyncElementModelAdapter());
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
	public Resource getResource(EditingDomain editingDomain, IFile file) {
		URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
		boolean fileExists = file == null ? true : file.exists();
		return getResource(editingDomain, uri, fileExists);
	}
	
	/**
	 * @author Mariana
	 */
	public Resource getResource(EditingDomain editingDomain, File file) {
		URI uri = URI.createFileURI(file.getAbsolutePath());
		boolean fileExists = file.exists();
		return getResource(editingDomain, uri, fileExists);
	}
	
	/**
	 * @author Mariana
	 */
	public Resource getResource(EditingDomain editingDomain, URI uri, boolean fileExists) {
		if (fileExists) {
			return editingDomain.getResourceSet().getResource(uri, true);
		} else {
			Resource resource =	editingDomain.getResourceSet().getResource(uri, false);
			if (resource == null) {
				resource = editingDomain.getResourceSet().createResource(uri);
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
	
}
