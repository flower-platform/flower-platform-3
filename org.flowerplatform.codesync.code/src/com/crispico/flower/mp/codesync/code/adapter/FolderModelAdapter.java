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
package com.crispico.flower.mp.codesync.code.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.codesync.base.FilteredIterable;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * Mapped to {@link File}. Children are {@link File}s that match the {@link #limitedPath}, if set.
 * 
 * @author Mariana
 */
public class FolderModelAdapter extends AstModelElementAdapter {

	protected String limitedPath;
	
	private List<File> filesToDelete = new ArrayList<File>();

	private Map<File, String> filesToRename = new HashMap<File, String>();
	
	public FolderModelAdapter() {
		super();
	}
	
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name().equals(feature)) {
			return getLabel(element);
		}
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Type().equals(feature)) {
			return FOLDER;
		}
		return null;
	}
	
	@Override
	public Object getMatchKey(Object element) {
		return getLabel(element);
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name().equals(feature)) {
			File folder = getFolder(element);
			filesToRename.put(folder, (String) value);
		}
	}

	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild) {
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children().equals(feature)) {
			CodeSyncElement cse = (CodeSyncElement) correspondingChild;
			return new File(getFolder(element), cse.getName());
		}
		return null;
	}

	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children().equals(feature)) {
			File resource = (File) child;
			filesToDelete.add(resource);
		}
	}

	@Override
	public boolean hasChildren(Object modelElement) {
		return getChildren(modelElement).size() > 0;
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		File[] files = getFolder(modelElement).listFiles();
		if (files == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(files);
	}

	@Override
	public String getLabel(Object modelElement) {
		return getFolder(modelElement).getName();
	}

	@Override
	public List<String> getIconUrls(Object modelElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children().equals(feature)) {
			return new FilteredIterable(getChildren(element).iterator()) {
	
				@Override
				protected boolean isAccepted(Object candidate) {
					String candidatePath = CodeSyncPlugin.getInstance().getProjectsProvider().getPathRelativeToProject((File) candidate);
					if (limitedPath == null || limitedPath.startsWith(candidatePath)) {
						return true;
					}
					return false;
				}
				
			};
		}
		return Collections.emptyList();
	}

	public String getLimitedPath() {
		return limitedPath;
	}

	public void setLimitedPath(String limitedPath) {
		this.limitedPath = limitedPath;
	}

	@Override
	public Object createCorrespondingModelElement(Object element) {
		return null;
	}

	@Override
	public boolean save(Object element) {
		File folder = getFolder(element);
		if (!folder.exists()) {
			// create the folder if it doesn't exist
			folder.mkdirs();
		}
		
		// remove children that were mark deleted	
		File[] children = folder.listFiles();
		if (children != null) {
			for (File child : folder.listFiles()) {
				if (filesToDelete.contains(child)) {
					child.delete();
					filesToDelete.remove(child);
				}
			}
		}
		
		// move the folder if it was marked renamed
		String newName = filesToRename.get(folder);
		if (newName != null) {
			File dest = new File(folder.getParent(), newName);
			folder.renameTo(dest);
			filesToRename.remove(folder);
		}
		return true;
	}
	
	@Override
	public boolean discard(Object element) {
		return true;
	}
	
	private File getFolder(Object element) {
		return (File) element;
	}

	@Override
	public void beforeFeaturesProcessed(Object element, Object correspondingElement) {
		// nothing to do
	}
	
	@Override
	public void featuresProcessed(Object element) {
		// nothing to do
	}
	
}