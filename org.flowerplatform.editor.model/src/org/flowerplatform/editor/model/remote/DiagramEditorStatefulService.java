package org.flowerplatform.editor.model.remote;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.editor.remote.EditableResource;
import org.flowerplatform.editor.remote.EditableResourceClient;
import org.flowerplatform.editor.remote.FileBasedEditorStatefulService;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.notation.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiagramEditorStatefulService extends FileBasedEditorStatefulService {

	private static final Logger logger = LoggerFactory.getLogger(DiagramEditorStatefulService.class);

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
		
		try {
			er.setMainResource(er.getResourceSet().getResource(resourceURI, true));
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

	@Override
	protected void sendFullContentToClient(EditableResource editableResource, EditableResourceClient client) {
		Diagram diagram = getDiagram(editableResource);
		List<EObject> list = new ArrayList<EObject>();
		// walking the tree this way breaks the convention that ensures sending the children before the parents
		// BUT it is not a problem because the OpenDiagramCommand's logic executes after all the children
		// have been received
		TreeIterator<EObject> iter = diagram.eAllContents();
		while (iter.hasNext()) {
			EObject next = iter.next();
			if (next instanceof View) {
				View view = (View) next;
//				IDiagrammableElementFeatureChangesProcessor processor = DiagramPlugin.getInstance().diagramUpdaterChangeDescriptionProcessor.diagrammableElementFeatureChangeProcessors.get(view.getViewType());
//				if (processor != null) {
//					processor.processFeatureChanges(view.getDiagrammableElement(), null, view, null);
//				}
			}
			list.add(next);
		}
		list.add(diagram);

		client_updateTransferableObjects(client.getCommunicationChannel(), client.getStatefulClientId(), list, null);
		String diagramId = diagram.eResource().getURIFragment(diagram);
		invokeClientMethod(client.getCommunicationChannel(), client.getStatefulClientId(), "openDiagram", new Object[] { diagramId });
	}

	// TODO CS/CS3 de facut semnatura sa accepte ERC; e absurd, ca eu il calculez prin iteratie din nou aici; si semnatura nu e uniforma cu celelalte
	@Override
	protected void updateEditableResourceContentAndDispatchUpdates(CommunicationChannel originatingCommunicationChannel, String originatingStatefulClientId,
			EditableResource editableResource, Object updatesToApply) {
		
//		if (!(updatesToApply instanceof AbstractEMFServerCommand)) {
//			logger.error("Content update cannot be casted to a command: {}", updatesToApply);
//		}
//
////		CDOTransaction transaction = session.openTransaction();
//
//		DiagramCodeSync3EditableResource diagramEditableResource = (DiagramCodeSync3EditableResource) editableResource;
////		diagramEditableResource.mainResource = transaction.getResourceSet().getResource(CDOURIUtil.createResourceURI(session, "My.flower_diagram"), true);
////		diagramEditableResource.diagram = (Diagram) transaction.getObject(diagramEditableResource.diagram.cdoID());
////		diagramEditableResource.diagram.setOpen(true);
//		
//		URI resourceURI = URI.createURI("hibernate://?dsname=" + TeneoConfig.DS_NAME);
//		diagramEditableResource.mainResource = diagramEditableResource.getEditingDomain().getResourceSet().getResource(resourceURI, true);
//		try {
//			diagramEditableResource.mainResource.load(Collections.emptyMap());
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//		
//		diagramEditableResource.changeRecorder = new MyChangeRecorder();
//		MyChangeRecorder.threadLocalChangeRecorder.set((MyChangeRecorder) diagramEditableResource.changeRecorder);
////		diagramEditableResource.changeRecorder.beginRecording(Collections.singleton(diagramEditableResource.editingDomain.getResourceSet()));
//
//		diagramEditableResource.changeRecorder.beginRecording(Collections.singleton(diagramEditableResource.mainResource));
//		
//		try {
//			AbstractEMFServerCommand command = (AbstractEMFServerCommand) updatesToApply;
//			command.setEditorStatefulService(this);
//			EditableResourceClient client = editableResource.getEditableResourceClientByCommunicationChannel(originatingCommunicationChannel);
//			command.setEditableResourceClient(client);
//			command.setEditableResource(diagramEditableResource);
////			command.transaction = transaction;
//			
//			command.executeCommand();
//			
//	//		diagramEditableResource.transferAdapter.prepareForTrasfer();
//			// TODO CS/CS3 HIGH nu trimitem lista de obiecte pt dispose inca; trebuie si un try/finally: clear
//	//		client_updateTransferableObjects(originatingCommunicationChannel, originatingStatefulClientId, diagramEditableResource.transferAdapter.getObjectsToUpdateQueue(), null);
//	//		diagramEditableResource.transferAdapter.clearQueue();
//		} finally {
//			ChangeDescription changeDescription = diagramEditableResource.changeRecorder.endRecording();
//			Map<String, Object> context = new HashMap<String, Object>();
//			
////			diagramEditableResource.changeRecorder.beginRecording(Collections.singleton(diagramEditableResource.diagram));
//			diagramEditableResource.changeRecorder.beginRecording(Collections.singleton(diagramEditableResource.mainResource));
//			// TODO CS/CS3 dubla inregistrare: sa facem prin constructie mai intai procesare elemente si apoi view-uri?...
//			// oricum, cred ca ar trebui inca un try/finally; sa facem un mecanism multiplu, ca la MDA? dar cum ne dam seama ca nu e infinit?
//			// sa nu mai facem viewdetails ca camp, si sa facem comanda speciala?
//			// de asemenea, tr. si un map, pentru a evita adaugarea de 2 ori in timpul celor 2 inregistrari
//			DiagramPlugin.getInstance().getComposedChangeDescriptionProcessor().processChangeDescription(changeDescription, context);
//			changeDescription = diagramEditableResource.changeRecorder.endRecording();
//			
//			diagramEditableResource.changeRecorder.dispose();
//			MyChangeRecorder.threadLocalChangeRecorder.set(null);
//			diagramEditableResource.changeRecorder = null;
//			
//			DiagramPlugin.getInstance().getComposedChangeDescriptionProcessor().processChangeDescription(changeDescription, context);
//			
//			DiagramUpdaterChangeDescriptionProcessingContext diagramUpdaterChangeDescriptionProcessingContext = DiagramUpdaterChangeDescriptionProcessingContext.getDiagramUpdaterChangeDescriptionProcessingContext(context, false);
//			if (diagramUpdaterChangeDescriptionProcessingContext != null) {
//				if (!diagramUpdaterChangeDescriptionProcessingContext.getObjectsToUpdate().isEmpty()) {
//					client_updateTransferableObjects(originatingCommunicationChannel, originatingStatefulClientId, 
//							diagramUpdaterChangeDescriptionProcessingContext.getObjectsToUpdate(), 
//							diagramUpdaterChangeDescriptionProcessingContext.getObjectIdsToDispose());
//				}
//			}
//		}
//		
//		try {
//			diagramEditableResource.mainResource.save(Collections.emptyMap());
//			diagramEditableResource.mainResource.unload();
//			diagramEditableResource.mainResource = null;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
////		System.out.println("Closing transaction; active objects: " + ((AbstractCDOView) transaction).getObjects());
////		try {
////			transaction.commit();
////		} catch (CommitException e) {
////			System.out.println("Commit failed");
////			e.printStackTrace();
////			transaction.rollback();
////		}
	}

	///////////////////////////////////////////////////////////////
	// Proxies to client methods
	///////////////////////////////////////////////////////////////
	
	public void client_updateTransferableObjects(CommunicationChannel communicationChannel, String statefulClientId, Collection<?> objectsToUpdate, Collection<?> objectsIdsToDispose) {
		invokeClientMethod(communicationChannel, statefulClientId, "updateTransferableObjects", new Object[] { objectsToUpdate, objectsIdsToDispose });
	}
	
	///////////////////////////////////////////////////////////////
	// @RemoteInvocation methods
	///////////////////////////////////////////////////////////////

}
