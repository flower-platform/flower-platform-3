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
package org.flowerplatform.codesync.code.javascript.adapter;

import java.util.List;
import java.util.Map;

import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstFactory;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNodeParameter;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage;

import com.crispico.flower.mp.codesync.code.adapter.AstModelElementAdapter;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * Mapped to {@link RegExAstNode}. Must handle all the features from {@link RegExRegExAstNodeFeatureProvider}.
 * 
 * @author Mariana Gheorghe
 */
public class RegExNodeAstModelAdapter extends AstModelElementAdapter {

	@Override
	public Object getMatchKey(Object element) {
		RegExAstNode node = getRegExAstNode(element);
		return getParameterValue(node, node.getKeyParameter());
	}

	@Override
	public Object removeFromMap(Object element, Map<Object, Object> leftOrRightMap, boolean isRight) {
		return leftOrRightMap.remove(getMatchKey(element));
	}
	
	protected String getParameterValue(RegExAstNode node, String parameterName) {
		for (RegExAstNodeParameter parameter : node.getParameters()) {
			if (parameter.getName().equals(node.getKeyParameter())) {
				return parameter.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Must handle all the features provided by <code>common</code>, except for containment features.
	 */
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name().equals(feature)) {
			return getParameterValue(getRegExAstNode(element), getRegExAstNode(element).getKeyParameter());
		}
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Type().equals(feature)) {
			return getRegExAstNode(element).getType();
		}
		if (RegExAstPackage.eINSTANCE.getRegExAstCacheElement_KeyParameter().equals(feature)) {
			return getRegExAstNode(element).getKeyParameter();
		}
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
	
	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name().equals(feature)) {
			// nothing to do
		}
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Type().equals(feature)) {
			getRegExAstNode(element).setType((String) value);
		}
		if (RegExAstPackage.eINSTANCE.getRegExAstCacheElement_KeyParameter().equals(feature)) {
			getRegExAstNode(element).setKeyParameter((String) value);
		}
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children().equals(feature)) {
			return getRegExAstNode(element).getChildren();
		}
		if (RegExAstPackage.eINSTANCE.getRegExAstCacheElement_Parameters().equals(feature)) {
			return getRegExAstNode(element).getParameters();
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}

	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild) {
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children().equals(feature)) {
			RegExAstNode parent = getRegExAstNode(element);
			RegExAstNode node = RegExAstFactory.eINSTANCE.createRegExAstNode();
			node.setAdded(true);
			parent.getChildren().add(node);
			return node;
		}
		if (RegExAstPackage.eINSTANCE.getRegExAstCacheElement_Parameters().equals(feature)) {
			RegExAstNodeParameter parameter = RegExAstFactory.eINSTANCE.createRegExAstNodeParameter();
			((RegExAstNode) element).getParameters().add(parameter);
			return parameter;
		}
		return null;
	}

	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children().equals(feature)) {
			getRegExAstNode(child).setDeleted(true);
		}

	}

	@Override
	public Object createCorrespondingModelElement(Object element) {
		return RegExAstFactory.eINSTANCE.createRegExAstCacheElement();
	}

	@Override
	public boolean save(Object element) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean discard(Object element) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasChildren(Object modelElement) {
		return true;
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		RegExAstNode node = getRegExAstNode(modelElement);
		return node.getChildren();
	}

	@Override
	public String getLabel(Object modelElement) {
		return (String) getMatchKey(modelElement);
	}

	@Override
	public List<String> getIconUrls(Object modelElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void updateUID(Object element, Object correspondingElement) {
		// TODO Auto-generated method stub

	}
	
	protected RegExAstNode getRegExAstNode(Object element) {
		return (RegExAstNode) element;
	}

}
