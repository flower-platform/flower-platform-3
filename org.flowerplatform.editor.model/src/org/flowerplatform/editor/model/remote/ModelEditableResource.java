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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.editor.remote.EditableResource;

/**
 * @author Mariana Gheorghe
 */
public class ModelEditableResource extends EditableResource {
	
	private ResourceSet resourceSet;
	
	private ChangeRecorder changeRecorder;
	
	// TODO CS/STFL de trecut pe lazy cand vom face lock si la Master cand operam pe slave
	private List<EditableResource> slaveEditableResources = Collections.synchronizedList(new ArrayList<EditableResource>());
	
	public ModelEditableResource() {
		resourceSet = new ResourceSetImpl();
		Map<Object, Object> options = EditorModelPlugin.getInstance().getLoadSaveOptions();
		resourceSet.getLoadOptions().putAll(options);
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
		resourceSet.eAdapters().add(new ECrossReferenceAdapter());
		
		changeRecorder = new ChangeRecorder();
	}
	
	public ResourceSet getResourceSet() {
		return resourceSet;
	}
	
	public ChangeRecorder getChangeRecorder() {
		return changeRecorder;
	}

	public void setChangeRecorder(ChangeRecorder changeRecorder) {
		this.changeRecorder = changeRecorder;
	}
	
	@Override
	public List<EditableResource> getSlaveEditableResources() {
		return slaveEditableResources;
	}

	@Override
	public void setSlaveEditableResources(List<EditableResource> value) {
		slaveEditableResources = value;
	}
	
	@Override
	public String getLabel() {
		return getEditorInput().toString();
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

}
