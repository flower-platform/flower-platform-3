package org.flowerplatform.editor.mindmap.processor;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.flowerplatform.codesync.remote.CodeSyncElementFeatureChangesProcessor;
import org.flowerplatform.emf_model.notation.MindMapNode;
import org.flowerplatform.emf_model.notation.Node;
import org.flowerplatform.emf_model.notation.NotationFactory;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Cristina Constantinescu
 */
public class MindMapChildrenChangeProcessor extends CodeSyncElementFeatureChangesProcessor {

	@Override
	protected int getNewViewsIndex(EObject object, List<EObject> childModelElements, View associatedViewOnOpenDiagram) {		
		return 0;
	}

	@Override
	protected Node createChildView(View associatedViewOnOpenDiagram, EObject child) {		
		MindMapNode node = NotationFactory.eINSTANCE.createMindMapNode();
		node.setViewType(getCodeSyncElement(child).getType());		
		node.setSide(((MindMapNode) associatedViewOnOpenDiagram).getSide() == 0 ? 1 : ((MindMapNode) associatedViewOnOpenDiagram).getSide());		
		node.setExpanded(false);		
		node.setHasChildren(getChildrenForCodeSyncElement(child).size() > 0);
		return node;
	}
	
	@Override
	protected boolean canAddChildView(View view, EObject candidate) {
		boolean canAdd = super.canAddChildView(view, candidate);
		if (canAdd) {
			return ((MindMapNode) view).isExpanded();
		}
		return canAdd;
	}
			
	@Override
	protected CodeSyncElement createModelElementChild(EObject object, View child) {
		// TODO Auto-generated method stub
		return null;
	}

}
