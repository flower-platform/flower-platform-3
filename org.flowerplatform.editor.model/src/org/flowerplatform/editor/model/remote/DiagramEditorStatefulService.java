package org.flowerplatform.editor.model.remote;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.command.AbstractServerCommand;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.editor.model.change_processor.DiagramUpdaterChangeProcessorContext;
import org.flowerplatform.editor.model.change_processor.IDiagrammableElementFeatureChangesProcessor;
import org.flowerplatform.editor.model.remote.command.AbstractEMFServerCommand;
import org.flowerplatform.editor.remote.EditableResource;
import org.flowerplatform.editor.remote.EditableResourceClient;
import org.flowerplatform.editor.remote.FileBasedEditorStatefulService;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.notation.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiagramEditorStatefulService extends FileBasedEditorStatefulService {

	public static final String ADDITIONAL_DATA_EDITABLE_RESOURCE = "editableResource";

	public static final String ADDITIONAL_DATA_EDITABLE_RESOURCE_CLIENT = "editableResourceClient";

	public static final String PROCESSING_CONTEXT_EDITABLE_RESOURCE = ADDITIONAL_DATA_EDITABLE_RESOURCE;
	
	private static final Logger logger = LoggerFactory.getLogger(DiagramEditorStatefulService.class);

	public DiagramEditorStatefulService() {
		super();
		CommunicationPlugin.getInstance().getCommunicationChannelManager().addWebCommunicationLifecycleListener(this);
	}
	
	@Override
	protected boolean areLocalUpdatesAppliedImmediately() {
		return false;
	}

	@Override
	protected EditableResource createEditableResourceInstance() {
		return new DiagramEditableResource();
	}

	protected Diagram getDiagram(EditableResource editableResource) {
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
		// TODO Auto-generated method stub
		
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

		client_updateTransferableObjects(client.getCommunicationChannel(), client.getStatefulClientId(), list, null, diagramUpdaterChangeDescriptionProcessingContext != null ? diagramUpdaterChangeDescriptionProcessingContext.getViewDetailsUpdates() : null);
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
		diagramEditableResource.getChangeRecorder().beginRecording(Collections.singleton(diagramEditableResource.getMainResource()));
		
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
			
			diagramEditableResource.getChangeRecorder().beginRecording(Collections.singleton(diagramEditableResource.getMainResource()));
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
							diagramUpdaterChangeDescriptionProcessingContext.getObjectIdsToDispose(),
							diagramUpdaterChangeDescriptionProcessingContext.getViewDetailsUpdates());
				}
			}
		}
		
//		hbResource.save(Collections.EMPTY_MAP);
//		hbResource.unload();
	}

	///////////////////////////////////////////////////////////////
	// Proxies to client methods
	///////////////////////////////////////////////////////////////
	
	public void client_updateTransferableObjects(CommunicationChannel communicationChannel, String statefulClientId, Collection<?> objectsToUpdate, Collection<?> objectsIdsToDispose, Collection<ViewDetailsUpdate> viewDetailsUpdates) {
		invokeClientMethod(communicationChannel, statefulClientId, "updateTransferableObjects", new Object[] { objectsToUpdate, objectsIdsToDispose, viewDetailsUpdates });
	}
	
	///////////////////////////////////////////////////////////////
	// @RemoteInvocation methods
	///////////////////////////////////////////////////////////////
	public void handleDragOnDiagram(StatefulServiceInvocationContext context, List<List<PathFragment>> pathsWithRoot, String diagramId) {
		DiagramEditableResource editableResource = (DiagramEditableResource) context.getAdditionalData().get(ADDITIONAL_DATA_EDITABLE_RESOURCE);
		Diagram diagram = (Diagram) editableResource.getEObjectById(diagramId);
		
//		List<Object> objects = new ArrayList<Object>(pathsWithRoot.size());
//		for (List<PathFragment> pathWithRoot : pathsWithRoot) {
//			objects.add(GenericTreeStatefulService.getNodeByPathFor(pathWithRoot, null));
//		}
		EditorModelPlugin.getInstance().getComposedDragOnDiagramHandler().handleDragOnDiagram(pathsWithRoot, diagram, null, null, context.getCommunicationChannel());
	}

}
