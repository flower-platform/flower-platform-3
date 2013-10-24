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
package com.crispico.flower.mp.codesync.merge;

import java.io.File;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.editor.EditorPlugin;
import org.osgi.framework.BundleContext;

import com.crispico.flower.mp.codesync.base.CodeSyncAlgorithm;
import com.crispico.flower.mp.codesync.base.CodeSyncEditableResource;
import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.codesync.base.Match;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactory;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactorySet;
import com.crispico.flower.mp.codesync.base.communication.CodeSyncEditorStatefulService;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * 
 */
public class CodeSyncMergePlugin extends AbstractFlowerJavaPlugin {

	protected static CodeSyncMergePlugin INSTANCE;
	
	public static CodeSyncMergePlugin getInstance() {
		return INSTANCE;
	}
	
	/**
	 * 
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
		File ancestorFile = (File) selection.get(0);
		File leftFile = (File) selection.get(1);
		File rightFile = (File) selection.get(2);
		
		Match match = new Match();
		match.setAncestor(CodeSyncPlugin.getInstance().getResource(null, ancestorFile).getContents().get(0));
		match.setLeft(CodeSyncPlugin.getInstance().getResource(null, leftFile).getContents().get(0));
		match.setRight(CodeSyncPlugin.getInstance().getResource(null, rightFile).getContents().get(0));
		
		File project = CodeSyncPlugin.getInstance().getProjectsProvider().getContainingProjectForFile(ancestorFile);
		String projectPath = EditorPlugin.getInstance().getFileAccessController().getPath(project);
//		CodeSyncEditorStatefulService service = (CodeSyncEditorStatefulService) ServiceRegistry.INSTANCE.getService(CodeSyncEditorStatefulService.SERVICE_ID);
		CodeSyncEditorStatefulService service = (CodeSyncEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(CodeSyncEditorStatefulService.SERVICE_ID);

		CodeSyncEditableResource editableResource = (CodeSyncEditableResource) service.subscribeClientForcefully(communicationChannel, projectPath);
		
		ModelAdapterFactory codeSyncModelAdapterFactory = new ModelAdapterFactory();
		// TODO fix this; add correct types
		codeSyncModelAdapterFactory.addModelAdapter(CodeSyncElement.class, new SyncElementModelAdapter(), "");
		codeSyncModelAdapterFactory.addModelAdapter(EObject.class, new EObjectModelAdapter(), "");
		
		match.setEditableResource(editableResource);
		editableResource.setMatch(match);
		editableResource.setModelAdapterFactorySet(new ModelAdapterFactorySet(codeSyncModelAdapterFactory, codeSyncModelAdapterFactory, codeSyncModelAdapterFactory));

		new CodeSyncAlgorithm(editableResource.getModelAdapterFactorySet()).generateDiff(match);
		
		StatefulServiceInvocationContext context = new StatefulServiceInvocationContext(communicationChannel);
		service.attemptUpdateEditableResourceContent(context, projectPath, null);
	}
}