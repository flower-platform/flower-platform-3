package com.crispico.flower.mp.codesync.merge;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactory;
import com.crispico.flower.mp.model.astcache.code.Annotation;
import com.crispico.flower.mp.model.astcache.code.AnnotationValue;
import com.crispico.flower.mp.model.astcache.code.Modifier;
import com.crispico.flower.mp.model.astcache.code.Parameter;
import com.crispico.flower.mp.model.codesync.AstCacheElement;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.FeatureChange;

/**
 * Mapped to {@link CodeSyncElement}.
 * 
 * @author Mariana
 */
public class CodeSyncElementModelAdapter extends EObjectModelAdapter {

	protected CodeSyncElementFeatureProvider common;
	
	protected ModelAdapterFactory codeSyncElementConverter;
	
	protected Resource resource;
	
	public CodeSyncElementModelAdapter() {
		super();
		
		common = new CodeSyncElementFeatureProvider();
	}
	
	public void setFeatureProvider(CodeSyncElementFeatureProvider common) {
		this.common = common;
	}

	/**
	 * The {@link IModelAdapter}s of the {@link #codeSyncElementConverter} are responsible
	 * with providing a model element that corresponds to the 
	 */
	public void setEObjectConverter(ModelAdapterFactory codeSyncElementConverter) {
		this.codeSyncElementConverter = codeSyncElementConverter;
	}
	
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	public void addToResource(AstCacheElement element) {
		resource.getContents().add(element);
	}

	@Override
	public String getFeatureName(Object feature) {
		return common.getFeatureName(feature);
	}

	@Override
	public List<?> getFeatures(Object element) {
		return common.getFeatures(element);
	}

	@Override
	public int getFeatureType(Object feature) {
		return common.getFeatureType(feature);
	}
	
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		EObject eObject = getContainingEObjectForFeature(element, feature);
		if (eObject != null) {
			return super.getContainmentFeatureIterable(eObject, feature, correspondingIterable);
		}
		return Collections.emptyList();
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		EObject eObject = getContainingEObjectForFeature(element, feature);
		if (eObject != null) {
			return super.getValueFeatureValue(eObject, feature, correspondingValue);
		}
		return null;
	}
	
	@Override
	public Object getMatchKey(Object element) {
		if (element instanceof CodeSyncElement) {
			return ((CodeSyncElement) element).getName();
		}
		if (element instanceof Parameter) {
			return ((Parameter) element).getName();
		}
		if (element instanceof Modifier) {
			return String.valueOf(((Modifier) element).getType());
		}
		// for Annotations, the match key contains the name and the type of the annotation
		if (element instanceof Annotation) {
			Annotation annotation = (Annotation) element;
			String matchKey = annotation.getName();
			if (annotation.getValues().size() == 0) {
				matchKey += CodeSyncMergePlugin.MARKER_ANNOTATION;
			} else {
				if (annotation.getValues().size() == 1 && CodeSyncMergePlugin.SINGLE_MEMBER_ANNOTATION_VALUE_NAME.equals(annotation.getValues().get(0).getName())) {
					matchKey += CodeSyncMergePlugin.SINGLE_MEMBER_ANNOTATION;
				} else {
					matchKey += CodeSyncMergePlugin.NORMAL_ANNOTATION;
				}
			}
			return matchKey;
		}
		if (element instanceof AnnotationValue) {
			return ((AnnotationValue) element).getName();
		}
		if (element instanceof String) {
			return element;
		}
		return null;
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object newValue) {
		EObject eObject = getContainingEObjectForFeature(element, feature);
		if (eObject != null) {
			super.setValueFeatureValue(eObject, feature, newValue);
		}
	}
	
	/**
	 * For the <code>children</code> feature of {@link CodeSyncElement}, also add the new child to the {@link AstCacheElement}s
	 * resource.
	 */
	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild) {
		EObject eObject = getContainingEObjectForFeature(element, feature);
		
		// first check if the child already exists
		Iterable<?> children = super.getContainmentFeatureIterable(eObject, feature, null);
		IModelAdapter adapter = codeSyncElementConverter.getModelAdapter(correspondingChild);
		Object matchKey = null;
		if (adapter != null) {
			matchKey = adapter.getMatchKey(correspondingChild);
		} else {
			matchKey = getMatchKey(correspondingChild);
		}
		if (matchKey != null) {
			for (Object child : children) {
				if (matchKey.equals(getMatchKey(child))) {
					return child;
				}
			}
		}
		
		if (eObject != null) {
			if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children().equals(feature)) {
				CodeSyncElement parent = (CodeSyncElement) element;
				CodeSyncElement cse = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createCodeSyncElement();
				AstCacheElement ace = (AstCacheElement) createCorrespondingModelElement(correspondingChild);
				if (ace != null) {
					addToResource(ace);
				}
				cse.setAstCacheElement(ace);
				parent.getChildren().add(cse);
				return cse;
			}
			else {
				return super.createChildOnContainmentFeature(eObject, feature, correspondingChild);
			}
		}
		
		return null;
	}
	
	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		EObject eObject = getContainingEObjectForFeature(parent, feature);
		if (eObject != null) {
			super.removeChildrenOnContainmentFeature(eObject, feature, child);
		}
	}
	
	@Override
	public String getLabel(Object modelElement) {
		return (String) getMatchKey(modelElement);
	}

	@Override
	public List<String> getIconUrls(Object modelElement) {
		return super.getIconUrls(modelElement);
	}
	
	/**
	 * Checks if the <code>feature</code> belongs to the {@link EClass} of the <code>element</code>.
	 * If not, delegates to the {@link AstCacheElement} of a {@link CodeSyncElement}.
	 */
	protected EObject getContainingEObjectForFeature(Object element, Object feature) {
		EClass containingClass = ((EStructuralFeature) feature).getEContainingClass();
		EObject eObject = (EObject) element;
		
		if (eObject != null && containingClass.isSuperTypeOf(eObject.eClass())) {
			// check if the feature's containing class is the same as the EObject
			return eObject;
		}
		
		if (eObject instanceof CodeSyncElement) {
			eObject = ((CodeSyncElement) eObject).getAstCacheElement();
			return getContainingEObjectForFeature(eObject, feature);
		}
		
		return null;
	}
	
	@Override
	public Object createCorrespondingModelElement(Object element) {
		if (element == null) {
			return null;
		}
		IModelAdapter adapter = codeSyncElementConverter.getModelAdapter(element);
		if (adapter != null) {
			return adapter.createCorrespondingModelElement(element);
		} else {
			return super.createCorrespondingModelElement(element);
		}
	}
	
	protected FeatureChange getFeatureChange(Object element, Object feature) {
		CodeSyncElement cse = null;
		if (element instanceof CodeSyncElement) {
			cse = (CodeSyncElement) element;
		} else {
			if (element instanceof AstCacheElement) {
				cse = ((AstCacheElement) element).getCodeSyncElement();
			}
		}
		if (cse != null) {
			return cse.getFeatureChanges().get(feature);
		}
		return null;
	}

}
