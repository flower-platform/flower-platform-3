package org.flowerplatform.editor.model.remote;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.flowerplatform.editor.remote.FileBasedEditableResource;

public class DiagramEditableResource extends FileBasedEditableResource {

	private ResourceSet resourceSet = new ResourceSetImpl();
	
	private Resource mainResource;
	
	private ChangeRecorder changeRecorder;
	
	public ResourceSet getResourceSet() {
		return resourceSet;
	}

	public Resource getMainResource() {
		return mainResource;
	}

	public void setMainResource(Resource mainResource) {
		this.mainResource = mainResource;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return "hey, I'm a diagram!";
	}

	@Override
	public String getIconUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	public ChangeRecorder getChangeRecorder() {
		return changeRecorder;
	}

	public void setChangeRecorder(ChangeRecorder changeRecorder) {
		this.changeRecorder = changeRecorder;
	}

	public EObject getEObjectById(String id) {
		return mainResource.getEObject(id);
	}
}
