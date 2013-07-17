package org.flowerplatform.editor.model.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.flowerplatform.codesync.remote.CodeSyncElementFeatureChangesProcessor;
import org.flowerplatform.editor.model.java.remote.JavaClassDiagramOperationsService;
import org.flowerplatform.emf_model.notation.Node;
import org.flowerplatform.emf_model.notation.NotationFactory;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.codesync.code.java.adapter.JavaAttributeModelAdapter;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana
 */
public class JavaClassProcessor extends CodeSyncElementFeatureChangesProcessor {

	protected void processChildren(EObject object, List<EObject> childModelElements, View associatedViewOnOpenDiagram, List<Node> childViews, Map<String, Object> context) {
		// attributes
		super.processChildren(object, filterChildModelElements(childModelElements, JavaAttributeModelAdapter.ATTRIBUTE), associatedViewOnOpenDiagram, filterChildViews(childViews, "classAttribute"), context);
	}
	
	protected List<EObject> filterChildModelElements(List<EObject> list, String filter) {
		List<EObject> result = new ArrayList<EObject>();
		for (EObject child : list) {
			if (filter.equals(((CodeSyncElement) child).getType())) {
				result.add(child);
			}
		}
		return result;
	}
	
	protected List<Node> filterChildViews(List<Node> list, String filter) {
		List<Node> result = new ArrayList<Node>();
		for (Node child : list) {
			if (filter.equals(child.getViewType())) {
				result.add(child);
			}
		}
		return result;
	}
	
	@Override
	protected int getNewViewsIndex(EObject object, View associatedViewOnOpenDiagram) {
		int index = getAttributeSeparatorIndex(associatedViewOnOpenDiagram);
		if (index == -1) {
			return index;
		}
		return ++index;
	}

	@Override
	protected Node createChildView(View associatedViewOnOpenDiagram, EObject child) {
		Node node = NotationFactory.eINSTANCE.createNode();
//		node.setViewType(getCodeSyncElement(child).getType());
		node.setViewType("classAttribute");
		return node;
	}

	@Override
	protected CodeSyncElement createModelElementChild(EObject object, View child) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected int getAttributeSeparatorIndex(View view) {
		for (Node node : view.getPersistentChildren()) {
			if (JavaClassDiagramOperationsService.ATTRIBUTE_SEPARATOR.equals(node.getViewType())) {
				return view.getPersistentChildren().indexOf(node);
			}
		}
		return -1;
	}
	
}
