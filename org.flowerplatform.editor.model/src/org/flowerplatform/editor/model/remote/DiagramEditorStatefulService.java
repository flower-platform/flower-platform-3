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
package org.flowerplatform.editor.model.remote;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.command.AbstractServerCommand;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.editor.model.IContentAssist;
import org.flowerplatform.editor.model.change_processor.DiagramUpdaterChangeProcessorContext;
import org.flowerplatform.editor.model.change_processor.IDiagrammableElementFeatureChangesProcessor;
import org.flowerplatform.editor.model.remote.command.AbstractEMFServerCommand;
import org.flowerplatform.editor.model.remote.scenario.ScenarioTreeStatefulService;
import org.flowerplatform.editor.remote.EditableResource;
import org.flowerplatform.editor.remote.EditableResourceClient;
import org.flowerplatform.editor.remote.FileBasedEditorStatefulService;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.notation.Edge;
import org.flowerplatform.emf_model.notation.NotationFactory;
import org.flowerplatform.emf_model.notation.NotationPackage;
import org.flowerplatform.emf_model.notation.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncFactory;
import com.crispico.flower.mp.model.codesync.ScenarioElement;

public class DiagramEditorStatefulService extends FileBasedEditorStatefulService {

	public static final String ADDITIONAL_DATA_EDITABLE_RESOURCE = "editableResource";

	public static final String ADDITIONAL_DATA_EDITABLE_RESOURCE_CLIENT = "editableResourceClient";

	public static final String PROCESSING_CONTEXT_EDITABLE_RESOURCE = ADDITIONAL_DATA_EDITABLE_RESOURCE;
	
	private static final Logger logger = LoggerFactory.getLogger(DiagramEditorStatefulService.class);

	private ScenarioTreeStatefulService scenarioTree;
	
	public DiagramEditorStatefulService() {
		super();
		CommunicationPlugin.getInstance().getCommunicationChannelManager().addWebCommunicationLifecycleListener(this);
		scenarioTree = new ScenarioTreeStatefulService();
		scenarioTree.setDiagramService(this);
	}
	
	@Override
	protected boolean areLocalUpdatesAppliedImmediately() {
		return false;
	}

	@Override
	protected EditableResource createEditableResourceInstance() {
		return new DiagramEditableResource();
	}

	public Diagram getDiagram(EditableResource editableResource) {
		DiagramEditableResource er = (DiagramEditableResource) editableResource;
		Diagram diagram = null;
		for (EObject o : er.getMainResource().getContents()) {
			if (o instanceof Diagram) {
				diagram = (Diagram) o;
			}
		}
		if (diagram == null) {
			throw new RuntimeException("Cannot find a/the diagram in the resource");
		}
		return diagram;
	}
	
	@Override
	protected void loadEditableResource(StatefulServiceInvocationContext context, EditableResource editableResource) throws FileNotFoundException {
		super.loadEditableResource(context, editableResource);		
		DiagramEditableResource er = (DiagramEditableResource) editableResource;
		URI resourceURI = URI.createFileURI(er.getFile().getAbsolutePath());
		er.setChangeRecorder(new ChangeRecorder());
		
		try {
			er.setMainResource(er.getResourceSet().getResource(resourceURI, true));
			er.getResourceSet().eAdapters().add(new ECrossReferenceAdapter());
		} catch (Exception e) {
			throw new RuntimeException("Error while loading file content " + er.getFile(), e);
		}		
	}

	@Override
	protected void disposeEditableResource(EditableResource editableResource) {
//		DiagramCodeSync3EditableResource diagramEditableResource = (DiagramCodeSync3EditableResource) editableResource;
//		diagramEditableResource.transferAdapter.dispose();
		// TODO CS/CS3 implementat codul de dispose
		// cred ca tr. sa fie ceva de genul de mai jos, insa poate pentru toate resursele?
//		mainResource.eAdapters().clear();
//		mainResource.unload();
//		
//		getResourceSet().getResources().remove(mainResource);
//		((ComposedAdapterFactory) adapterFactory).dispose();
	}

	@Override
	protected void doSave(EditableResource editableResource) {
		Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(XMLResource.OPTION_ENCODING, "UTF-8");
		options.put(XMLResource.OPTION_XML_VERSION, "1.1");
		for (Resource resource : ((DiagramEditableResource) editableResource).getResourceSet().getResources()) {
			try {
				resource.save(options);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	protected Map<String, Object> createProcessingContext(EditableResource editableResource) {
		Map<String, Object> processingContext = new HashMap<String, Object>();
		processingContext.put(PROCESSING_CONTEXT_EDITABLE_RESOURCE, editableResource);
		return processingContext;
	}

	@Override
	protected void sendFullContentToClient(EditableResource editableResource, EditableResourceClient client) {
//		Resource hbResource = new FlowerTeneoResource(URI.createURI("hb:/?dsname=flowerDataStore&query=from Diagram"));
//		try {
//			hbResource.load(Collections.EMPTY_MAP);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		((DiagramEditableResource) editableResource).setMainResource(hbResource);
		
		Diagram diagram = getDiagram(editableResource);
		List<EObject> list = new ArrayList<EObject>();
		Map<String, Object> processingContext = createProcessingContext(editableResource);
		
		list.add(diagram);
		iterateContents(diagram, list, processingContext);
		
		DiagramUpdaterChangeProcessorContext diagramUpdaterChangeDescriptionProcessingContext = DiagramUpdaterChangeProcessorContext.getDiagramUpdaterChangeDescriptionProcessingContext(processingContext, false);

		client_updateTransferableObjects(client.getCommunicationChannel(), client.getStatefulClientId(), list, Collections.emptyList(), null, diagramUpdaterChangeDescriptionProcessingContext != null ? diagramUpdaterChangeDescriptionProcessingContext.getViewDetailsUpdates() : null);
		String diagramId = diagram.eResource().getURIFragment(diagram);
		invokeClientMethod(client.getCommunicationChannel(), client.getStatefulClientId(), "openDiagram", new Object[] { diagramId });
		
//		hbResource.unload();
	}
	
	private void iterateContents(View view, List<EObject> list, Map<String, Object> processingContext) {
		Iterator<EObject> iter = view.eContents().iterator();
		
		while (iter.hasNext()) {
			EObject next = iter.next();
			if (next instanceof View) {
				View child = (View) next;
				List<IDiagrammableElementFeatureChangesProcessor> processors = EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().getDiagrammableElementFeatureChangesProcessors(child.getViewType());
				if (processors != null) {
					for (IDiagrammableElementFeatureChangesProcessor processor : processors) {
						processor.processFeatureChanges(child.getDiagrammableElement(), null, child, processingContext);
					}
				}
				iterateContents(child, list, processingContext);
			}
			list.add(next);
		}
	}

	// TODO CS/CS3 de facut semnatura sa accepte ERC; e absurd, ca eu il calculez prin iteratie din nou aici; si semnatura nu e uniforma cu celelalte
	@Override
	protected void updateEditableResourceContentAndDispatchUpdates(StatefulServiceInvocationContext statefulServiceInvocationContext, EditableResource editableResource, Object updatesToApply) {
		
//		if (!(updatesToApply instanceof AbstractEMFServerCommand)) {
//			logger.error("Content update cannot be casted to a command: {}", updatesToApply);
//		}

//		FlowerTeneoResource hbResource = new FlowerTeneoResource(URI.createURI("hb:/?dsname=flowerDataStore&query=from Diagram"));
//		hbResource.load(Collections.EMPTY_MAP);
//		((DiagramEditableResource) editableResource).setMainResource(hbResource);
//		((DiagramEditableResource) editableResource).setChangeRecorder(hbResource.getChangeRecorder());
		
		DiagramEditableResource diagramEditableResource = (DiagramEditableResource) editableResource;
		diagramEditableResource.getChangeRecorder().beginRecording(Collections.singleton(diagramEditableResource.getMainResource().getResourceSet()));
		
		try {
			EditableResourceClient client = editableResource.getEditableResourceClientByCommunicationChannel(statefulServiceInvocationContext.getCommunicationChannel());
			if (updatesToApply instanceof AbstractEMFServerCommand) {
				AbstractEMFServerCommand command = (AbstractEMFServerCommand) updatesToApply;
				command.setEditorStatefulService(this);
				command.setEditableResourceClient(client);
				command.setEditableResource(diagramEditableResource);
				
				command.executeCommand();
			} else if (updatesToApply instanceof AbstractServerCommand) {
				AbstractServerCommand command = (AbstractServerCommand) updatesToApply;
				if (command instanceof InvokeServiceMethodServerCommand) {
					Map<String, Object> additionalData = new HashMap<String, Object>();
					additionalData.put(ADDITIONAL_DATA_EDITABLE_RESOURCE_CLIENT, client);
					additionalData.put(ADDITIONAL_DATA_EDITABLE_RESOURCE, editableResource);
					((InvokeServiceMethodServerCommand) command).setAdditionalDataForServiceInvocationContext(additionalData);
				}
				command.setCommunicationChannel(statefulServiceInvocationContext.getCommunicationChannel());
				command.executeCommand();
			}
			
	//		diagramEditableResource.transferAdapter.prepareForTrasfer();
			// TODO CS/CS3 HIGH nu trimitem lista de obiecte pt dispose inca; trebuie si un try/finally: clear
	//		client_updateTransferableObjects(originatingCommunicationChannel, originatingStatefulClientId, diagramEditableResource.transferAdapter.getObjectsToUpdateQueue(), null);
	//		diagramEditableResource.transferAdapter.clearQueue();
		} finally {
			ChangeDescription changeDescription = diagramEditableResource.getChangeRecorder().endRecording();
			Map<String, Object> processingContext = createProcessingContext(diagramEditableResource);
			
			diagramEditableResource.getChangeRecorder().beginRecording(Collections.singleton(diagramEditableResource.getMainResource().getResourceSet()));
			// TODO CS/CS3 dubla inregistrare: sa facem prin constructie mai intai procesare elemente si apoi view-uri?...
			// oricum, cred ca ar trebui inca un try/finally; sa facem un mecanism multiplu, ca la MDA? dar cum ne dam seama ca nu e infinit?
			// sa nu mai facem viewdetails ca camp, si sa facem comanda speciala?
			// de asemenea, tr. si un map, pentru a evita adaugarea de 2 ori in timpul celor 2 inregistrari
			EditorModelPlugin.getInstance().getComposedChangeProcessor().processChangeDescription(changeDescription, processingContext);
			changeDescription = diagramEditableResource.getChangeRecorder().endRecording();
			
			diagramEditableResource.getChangeRecorder().dispose();
//			MyChangeRecorder.threadLocalChangeRecorder.set(null);
//			diagramEditableResource.changeRecorder = null;
			
			EditorModelPlugin.getInstance().getComposedChangeProcessor().processChangeDescription(changeDescription, processingContext);
			
			DiagramUpdaterChangeProcessorContext diagramUpdaterChangeDescriptionProcessingContext = DiagramUpdaterChangeProcessorContext.getDiagramUpdaterChangeDescriptionProcessingContext(processingContext, false);
			if (diagramUpdaterChangeDescriptionProcessingContext != null && !diagramUpdaterChangeDescriptionProcessingContext.isEmpty()) {
				for (EditableResourceClient client : diagramEditableResource.getClients()) {
					client_updateTransferableObjects(client.getCommunicationChannel(), client.getStatefulClientId(), 
							diagramUpdaterChangeDescriptionProcessingContext.getObjectsToUpdate(), 
							diagramUpdaterChangeDescriptionProcessingContext.getObjectsToDispose(),
							diagramUpdaterChangeDescriptionProcessingContext.getObjectIdsToDispose(),
							diagramUpdaterChangeDescriptionProcessingContext.getViewDetailsUpdates());
				}
			}
		}
		
//		hbResource.save(Collections.EMPTY_MAP);
//		hbResource.unload();
	}

	public ScenarioTreeStatefulService getScenarioTreeStatefulService() {
		return scenarioTree;
	}
	
	public DiagramEditableResource getDiagramEditableResource(File project) {
		if (project != null) {
			String path = project.getAbsolutePath();
			for (EditableResource er : editableResources.values()) {
				DiagramEditableResource der = (DiagramEditableResource) er;
				File diagram = der.getFile();
				if (diagram.getAbsolutePath().startsWith(path)) {
					return der;
				}
			}
		}
		return null;
	}
	
	private DiagramEditableResource getDiagramEditableResource(ServiceInvocationContext context) {
		return (DiagramEditableResource) context.getAdditionalData().get(ADDITIONAL_DATA_EDITABLE_RESOURCE);
	}
	
	///////////////////////////////////////////////////////////////
	// Proxies to client methods
	///////////////////////////////////////////////////////////////
	
	/**
	 * Before sending the objects to the client, first iterate the <code>objectsToUpdate</code> list and
	 * clean references towards model elements. This should be done when the objects are removed from the 
	 * resource; however, we do not want to treat this as a change and process it.
	 * 
	 * @author Mariana Gheorghe
	 */
	public void client_updateTransferableObjects(CommunicationChannel communicationChannel, String statefulClientId, Collection<?> objectsToUpdate, Collection<?> objectsToDispose, Collection<?> objectsIdsToDispose, Collection<ViewDetailsUpdate> viewDetailsUpdates) {
		for (Object object : objectsToDispose) {
			if (object instanceof View) {
				((View) object).setDiagrammableElement(null);
			}
		}
		
		invokeClientMethod(communicationChannel, statefulClientId, "updateTransferableObjects", new Object[] { objectsToUpdate, objectsIdsToDispose, viewDetailsUpdates });
	}
	
	///////////////////////////////////////////////////////////////
	// @RemoteInvocation methods
	///////////////////////////////////////////////////////////////
	
	@RemoteInvocation
	public void handleDragOnDiagram(StatefulServiceInvocationContext context, List<List<PathFragment>> pathsWithRoot, String diagramId) {
		DiagramEditableResource editableResource = getDiagramEditableResource(context);
		Diagram diagram = (Diagram) editableResource.getEObjectById(diagramId);
		
//		List<Object> objects = new ArrayList<Object>(pathsWithRoot.size());
//		for (List<PathFragment> pathWithRoot : pathsWithRoot) {
//			objects.add(GenericTreeStatefulService.getNodeByPathFor(pathWithRoot, null));
//		}
		EditorModelPlugin.getInstance().getComposedDragOnDiagramHandler().handleDragOnDiagram(pathsWithRoot, diagram, null, null, context.getCommunicationChannel());
	}
	
	/**
	 * @author Mariana Gheorghe
	 */
	@RemoteInvocation
	public void contentAssist(StatefulServiceInvocationContext context, String viewId, String pattern) {
		logger.debug("Search types for pattern [{}]", pattern);
		DiagramEditableResource editableResource = getDiagramEditableResource(context);
		View view = (View) editableResource.getEObjectById(viewId);
		CodeSyncElement diagrammableElement = (CodeSyncElement) view.getDiagrammableElement();
		if (diagrammableElement == null) {
			logger.error("No diagrammable element for view with id [{}]", viewId);
			return;
		}
		
		// populate the search context, needed to set the search scope
		Map<String, Object> searchContext = new HashMap<String, Object>();
		searchContext.put(IContentAssist.TYPE, diagrammableElement.getType());
		searchContext.put(IContentAssist.RESOURCE, editableResource.getFile());
		List<String> types = EditorModelPlugin.getInstance().getComposedContentAssist().findMatches(searchContext, pattern);
		if (types == null) {
			logger.debug("No types found for pattern [{}]", pattern);
		} else {
			for (String type : types) {
				logger.debug("Found type [{}] for pattern [{}]", type, pattern);
			}
			logger.debug("Found [{}] types for pattern [{}]", types.size(), pattern);
		}
	}
	
	/**
	 * @author Mariana Gheorghe
	 */
	@RemoteInvocation
	public void addNewScenario(StatefulServiceInvocationContext context, String editableResourcePath, String name) {
		DiagramEditableResource er = (DiagramEditableResource) getEditableResource(editableResourcePath);
		Resource resource = scenarioTree.getScenariosResource(er);
		ScenarioElement scenario = CodeSyncFactory.eINSTANCE.createScenarioElement();
		scenario.setName(name);
		scenario.setType("scenarioRoot");
		resource.getContents().add(scenario);
		Map<Object, Object> clientContext = new HashMap<Object, Object>();
		clientContext.put("diagramEditableResourcePath", editableResourcePath);
		clientContext.put("selectNode", true);
		openNode(context, null, clientContext);
	}
	
	@RemoteInvocation
	public void addNewConnection(StatefulServiceInvocationContext context, String editableResourcePath, 
			String diagramId, String sourceViewId, String targetViewId) {
		DiagramEditableResource er = (DiagramEditableResource) getEditableResource(editableResourcePath);
		View sourceView = (View) er.getEObjectById(sourceViewId);
		View targetView = (View) er.getEObjectById(targetViewId);
		Diagram diagram = (Diagram) er.getEObjectById(diagramId);
		Edge edge = NotationFactory.eINSTANCE.createEdge();
		edge.setViewType("scenarioInterraction");
		edge.setSource(sourceView);
		edge.setTarget(targetView);
		diagram.getPersistentEdges().add(edge);
		
		DiagramEditorStatefulService service = (DiagramEditorStatefulService) CommunicationPlugin.getInstance()
				.getServiceRegistry().getService("diagramEditorStatefulService");
		Resource resource = service.getScenarioTreeStatefulService().getScenariosResource(er);
		if (resource.getContents().size() > 0) {
			ScenarioElement scenario = (ScenarioElement) resource.getContents().get(0);
			CodeSyncElement source = (CodeSyncElement) sourceView.getDiagrammableElement();
			CodeSyncElement target = (CodeSyncElement) targetView.getDiagrammableElement();
			ScenarioElement interaction = addScenarioInteraction(scenario, source, target);
			if (interaction == null) {
				ScenarioElement elt = createScenarioElement(source, scenario);
				interaction = createScenarioElement(target, elt);
			} 
			edge.setDiagrammableElement(interaction);
			Map<Object, Object> clientContext = new HashMap<Object, Object>();
			clientContext.put("diagramEditableResourcePath", editableResourcePath);
			service.getScenarioTreeStatefulService().openNode(context, null, clientContext);
		}
	}
	
	protected ScenarioElement createScenarioElement(CodeSyncElement cse, ScenarioElement parent) {
		ScenarioElement interaction = CodeSyncFactory.eINSTANCE.createScenarioElement();
		interaction.setInteraction(cse);
		interaction.setName(cse.getName());
		interaction.setType("scenarioElement");
		parent.getChildren().add(interaction);
		String number = getNumberLabel(interaction);
		if (number.startsWith(".")) {
			number = number.substring(1);
		}
		interaction.setNumber(number);
		return interaction;
	}
	
	protected String getNumberLabel(ScenarioElement scenario) {
		ScenarioElement parent = ((ScenarioElement) scenario.eContainer());
		if (parent.getType().equals("scenarioRoot")) {
			return "";
		}
		int i = 0;
		for (CodeSyncElement child : parent.getChildren()) {
			if (child.getType() != null && child.getType().equals("scenarioElement")) {
				i++;
			}
			if (child.equals(scenario)) {
				break;
			}
		}
		return getNumberLabel(parent) + "." + i;
	}
	
	protected ScenarioElement addScenarioInteraction(ScenarioElement scenario, CodeSyncElement source, CodeSyncElement target) {
		if (source.equals(scenario.getInteraction())) {
			ScenarioElement interaction = createScenarioElement(target, scenario);
			return interaction;
		}
		for (CodeSyncElement child : scenario.getChildren()) {
			ScenarioElement interaction = addScenarioInteraction((ScenarioElement) child, source, target);
			if (interaction != null) {
				return interaction;
			}
		}
		return null;
	}
	
	protected void updateLabelsAfterIndex(ScenarioElement scenario, int index) {
		for (int i = index; i < scenario.getChildren().size(); i++) {
			ScenarioElement child = (ScenarioElement) scenario.getChildren().get(i);
			if (child.getType() != null && child.getType().equals("scenarioElement")) {
				StringBuilder label = new StringBuilder(child.getNumber());
				int lastIndex = label.lastIndexOf(".");
				if (lastIndex < 0) {
					lastIndex = 0;
				} else {
					lastIndex++;
				}
				int newIndex = Integer.parseInt(label.substring(lastIndex)) - 1;
				label.replace(lastIndex, label.length(), String.valueOf(newIndex));
				child.setNumber(label.toString());
			}
		}
	}

	/**
	 * @author Mariana Gheorghe
	 */
	@RemoteInvocation
	public void openNode(StatefulServiceInvocationContext context, List<PathFragment> path, Map<Object, Object> clientContext) {
		scenarioTree.openNode(context, path, clientContext);
	}
	
	/**
	 * @author Mariana Gheorghe
	 */
	@RemoteInvocation
	public void addNewComment(StatefulServiceInvocationContext context, List<PathFragment> path, String editableResourcePath, String comment, Map<Object, Object> clientContext) {
		clientContext.put("diagramEditableResourcePath", editableResourcePath);
		GenericTreeContext treeContext = new GenericTreeContext(scenarioTree);
		treeContext.setClientContext(clientContext);
		ScenarioElement parent = (ScenarioElement) scenarioTree.getNodeByPath(path, treeContext);
		ScenarioElement commentNode = CodeSyncFactory.eINSTANCE.createScenarioElement();
		commentNode.setComment(comment);
		commentNode.setName(comment);
		commentNode.setType("commentNode");
		parent.getChildren().add(commentNode);
		scenarioTree.openNode(context, path, clientContext);
	}

	/**
	 * @author Mariana Gheorghe
	 */
	@RemoteInvocation
	public void deleteScenarioElement(StatefulServiceInvocationContext context, List<PathFragment> path, String editableResourcePath, Map<Object, Object> clientContext) {
		clientContext.put("diagramEditableResourcePath", editableResourcePath);
		GenericTreeContext treeContext = new GenericTreeContext(scenarioTree);
		treeContext.setClientContext(clientContext);
		ScenarioElement scenarioElement = (ScenarioElement) scenarioTree.getNodeByPath(path, treeContext);
		ECrossReferenceAdapter adapter = ECrossReferenceAdapter.getCrossReferenceAdapter(scenarioElement);
		Diagram diagram = getDiagram(getEditableResource(editableResourcePath));
		for (Setting setting : adapter.getNonNavigableInverseReferences(scenarioElement)) {
			if (NotationPackage.eINSTANCE.getView_DiagrammableElement().equals(setting.getEStructuralFeature())) {
				View view = (View) setting.getEObject();
				diagram.getPersistentEdges().remove(view);
			}
		}
		ScenarioElement parent = (ScenarioElement) scenarioElement.eContainer();
		int index = parent.getChildren().indexOf(scenarioElement);
		parent.getChildren().remove(scenarioElement);
		updateLabelsAfterIndex(parent, index);
	}
	
}