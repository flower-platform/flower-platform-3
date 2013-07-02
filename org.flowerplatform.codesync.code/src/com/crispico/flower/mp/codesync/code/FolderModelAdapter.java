package com.crispico.flower.mp.codesync.code;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import com.crispico.flower.mp.codesync.merge.CodeSyncElementFeatureProvider;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * Mapped to {@link IFolder}. Children are {@link IResource}s that match the {@link #limitedPath}, if set.
 * 
 * @author Mariana
 */
public class FolderModelAdapter extends AstModelElementAdapter {

	protected String limitedPath;
	
	private final String DELETED = "deleted";
	
	private final String RENAMED = "renamed";
	
	public FolderModelAdapter() {
		super();
		common = new CodeSyncElementFeatureProvider();
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
			IFolder folder = getFolder(element);
			try {
				IMarker marker = folder.createMarker(IMarker.MARKER);
				marker.setAttribute(RENAMED, value);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild) {
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children().equals(feature)) {
			CodeSyncElement cse = (CodeSyncElement) correspondingChild;
			IPath path = getFolder(element).getFullPath().append(cse.getName());
			if (FOLDER.equals(cse.getType())) {
				return ResourcesPlugin.getWorkspace().getRoot().getFolder(path);
			}
			if (FILE.equals(cse.getType())) {
				return ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			}
		}
		return null;
	}

	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children().equals(feature)) {
			IResource resource = (IResource) child;
			try {
				IMarker marker = resource.createMarker(IMarker.MARKER);
				marker.setAttribute(DELETED, true);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean hasChildren(Object modelElement) {
		return getChildren(modelElement).size() > 0;
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		try {
			return Arrays.asList(((IFolder) modelElement).members());
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
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
					String candidatePath = ((IResource) candidate).getFullPath().toString();
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
		IFolder folder = getFolder(element);
		try {
			if (!folder.exists()) {
				// create the folder if it doesn't exist
				folder.create(true, true, null);
			}
			
			// remove children that were mark deleted
			for (Object child : getChildren(element)) {
				IResource resource = (IResource) child;
				for (IMarker childMarker : resource.findMarkers(IMarker.MARKER, false, IResource.DEPTH_ZERO)) {
					Object deleted = childMarker.getAttribute(DELETED);
					if (deleted != null && (boolean) childMarker.getAttribute(DELETED)) {
						resource.delete(true, null);
					}
				}
			}
			
			// move the folder if it was marked renamed
			for (IMarker marker : folder.findMarkers(IMarker.MARKER, false, IResource.DEPTH_ZERO)) {
				String value = (String) marker.getAttribute(RENAMED);
				if (value != null) {
					folder.move(folder.getFullPath().removeLastSegments(1).append((String) value), true, null);
					break;
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	@Override
	public boolean discard(Object element) {
		return true;
	}
	
	private IFolder getFolder(Object element) {
		return (IFolder) element;
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
