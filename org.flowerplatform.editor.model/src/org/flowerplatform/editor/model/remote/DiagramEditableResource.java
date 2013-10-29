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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.flowerplatform.editor.EditorPlugin;
import org.flowerplatform.editor.remote.FileBasedEditableResource;

public class DiagramEditableResource extends FileBasedEditableResource {

	private ResourceSet resourceSet;
	
	private Resource mainResource;
	
	private ChangeRecorder changeRecorder;
	
	private boolean dirty;
	
	public DiagramEditableResource() {
		resourceSet = new ResourceSetImpl();
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
	}
	
	public ResourceSet getResourceSet() {
		return resourceSet;
	}

	public Resource getMainResource() {
		return mainResource;
	}

	public void setMainResource(Resource mainResource) {
		this.mainResource = mainResource;
	}

	/**
	 * @author Sebastian Solomon
	 */
	@Override
	public String getLabel() {
		return EditorPlugin.getInstance().getFileAccessController().getName(getFile());
	}

	@Override
	public String getIconUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}
	
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
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