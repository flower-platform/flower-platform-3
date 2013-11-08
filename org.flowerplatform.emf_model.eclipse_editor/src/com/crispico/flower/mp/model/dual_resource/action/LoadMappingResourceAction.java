package com.crispico.flower.mp.model.dual_resource.action;

import java.util.Collection;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.action.StaticSelectionCommandAction;
import org.flowerplatform.emf_model.dual_resource.DualResourceURIHandler;


public class LoadMappingResourceAction extends StaticSelectionCommandAction {

	private EditingDomain editingDomain;
	
	public LoadMappingResourceAction(EditingDomain editingDomain) {
		super();
		setText("Load Mapping Resource");
		
		this.editingDomain = editingDomain;
	}

	@Override
	protected Command createActionCommand(EditingDomain editingDomain, Collection<?> collection) {
		return null;
	}

	@Override
	public void run() {
		ResourceSet resourceSet = editingDomain.getResourceSet();
//		Resource diagramResource = resourceSet.getResources().get(0);
//		
//		if (!diagramResource.getURI().isPlatformResource()) {
//			throw new RuntimeException("Diagram resource should be a platform resource!");
//		}
//		
//		String diagramPath = diagramResource.getURI().toPlatformString(true);
//		IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(diagramPath);
//		URI uri = DualResourceURIHandler.createDualResourceURI(resource.getFullPath().toString(), null);
//		resourceSet.getResource(uri, true);
		resourceSet.getResource(DualResourceURIHandler.createDualResourceURI(), true);
	}

}
