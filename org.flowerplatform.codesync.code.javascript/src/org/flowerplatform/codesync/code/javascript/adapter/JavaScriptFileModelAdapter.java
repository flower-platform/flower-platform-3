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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
import org.flowerplatform.codesync.code.javascript.CodeSyncCodeJavascriptPlugin;
import org.flowerplatform.codesync.code.javascript.parser.Parser;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstFactory;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNodeParameter;
import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.codesync.code.adapter.AbstractFileModelAdapter;

/**
 * @author Mariana Gheorghe
 */
public class JavaScriptFileModelAdapter extends AbstractFileModelAdapter {
	
	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild) {
		File file = getFile(element);
		RegExAstNode node = (RegExAstNode) getOrCreateFileInfo(file);
		return node;
	}
	
	protected Object createFileInfo(File file) {
		// parse the file
		if (file.exists()) {
			Parser parser = new Parser();
			RegExAstNode node = parser.parse(file);
			return node;
		} else {
			RegExAstNode node = RegExAstFactory.eINSTANCE.createRegExAstNode();
			node.setAdded(true);
			// adding to resource to avoid UNDEFINED values during sync
			// see EObjectModelAdapter.getValueFeatureValue()
			Resource resource = new ResourceImpl();
			resource.getContents().add(node);
			return node;
		}
	}

	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		((RegExAstNode) child).setDeleted(true);
	}

	protected TextEdit rewrite(Document document, Object fileInfo) {
		RegExAstNode node = (RegExAstNode) fileInfo;
		MultiTextEdit edit = new MultiTextEdit();
		rewrite(document, node, edit);
		return edit;
	}

	/**	
	 * @author Mariana Gheorghe
	 * @author Cristina Constantinescu
	 */
	private void rewrite(IDocument document, RegExAstNode node, MultiTextEdit edit) {
		if (node.isAdded()) {			
			String template = loadTemplate(node);
			
			template = getIndentTemplate(document.get(), getInsertPoint(node), template, false);
			
			if (!isFirstChildAdded(node)) {							
				CodeSyncElementDescriptor descriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(node.getType());				
				template = (descriptor.getNextSiblingSeparator() != null ? descriptor.getNextSiblingSeparator() : "") + '\n' + template;
			}			
			edit.addChild(new InsertEdit(getInsertPoint(node), template));
		} else if (node.isDeleted()) {
			edit.addChild(new DeleteEdit(node.getOffset(), node.getLength()));
		} else {
			// TODO we'd also need a modifier flag, that way we woudn't need to replace all the parameters
			for (RegExAstNodeParameter parameter : node.getParameters()) {
				if (parameter.getOffset() > 0 && parameter.getLength() > 0) {
					edit.addChild(new ReplaceEdit(parameter.getOffset(), parameter.getLength(), parameter.getValue()));
				}
			}
			for (RegExAstNode child : node.getChildren()) {
				rewrite(document, child, edit);
			}
		}
	}

	/**
	 * Loads children templates as well, because we can't allow overlapping edits.
	 * @author Mariana Gheorghe
	 * @author Cristina Constantinescu
	 */
	private String loadTemplate(RegExAstNode node) {
		String template = null;
		// first find the template to use
		try {
			URL url = CodeSyncCodeJavascriptPlugin.getInstance().getBundleContext().getBundle().getResource("templates/" + node.getType() + ".tpl");
			File file = new File(FileLocator.resolve(url).toURI());
			template = FileUtils.readFileToString(file);
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException("Template does not exist", e);
		}
		
		// replace the parameters with their values from the node
		for (RegExAstNodeParameter parameter : node.getParameters()) {
			template = template.replaceAll("@" + parameter.getName(), Matcher.quoteReplacement(parameter.getValue()));
		}
		
		// load children templates
		Map<String, Boolean> firstChild = new HashMap<String, Boolean>();
		for (RegExAstNode child : getChildrenWithTemplate(node, new ArrayList<RegExAstNode>())) {
			String childTemplate = loadTemplate(child);
			String childType = getChildType(child);
			if (childType != null) {
				int childInsertPoint = template.indexOf("<!-- children-insert-point " + childType + " -->");
				if (childInsertPoint == -1) {
					childInsertPoint = template.indexOf("// children-insert-point " + childType);
				}
				if (childInsertPoint == -1) {
					throw new RuntimeException("RegExAstNode " + getKeyFeatureValue(node) + " of type " + node.getType() + " does not accept children of type " + childType);
				}
						
				boolean isFirstChildAdded = firstChild.get(childType) == null;
				
				childTemplate = getIndentTemplate(template, childInsertPoint, childTemplate, isFirstChildAdded);
				
				if (!isFirstChildAdded) {				
					String codeSyncType = child.getType();
					CodeSyncElementDescriptor descriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(codeSyncType);
					childTemplate = (descriptor.getNextSiblingSeparator() != null ? descriptor.getNextSiblingSeparator() : "") + '\n' + childTemplate;
				}
				template = template.substring(0, childInsertPoint) + childTemplate + template.substring(childInsertPoint);
				
				firstChild.put(childType, false);
			}
		}
		
		return template;
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
	private String getIndentTemplate(String template, int insertPoint, String childTemplate, boolean isFirstChildAdded) {
		String prefixTemplate = template.substring(0, insertPoint);
		char[] chars = prefixTemplate.toCharArray();
		String indent = "";
		for (int i = chars.length - 1; i >= 0; i--) {
			if (chars[i] == '\n' || chars[i] == '\r') {
				// if newline, stop
				break;
			}
			if (Character.isWhitespace(chars[i])) {
				// compute indentation string
				indent = chars[i] + indent;
			} else {
				// other char found, clear indentation
				indent = "";
			}
		}
		// for each line, except first line if first child, add indentation
		String[] lines = childTemplate.split("\n");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < lines.length; i++) {
			if (i == 0 && isFirstChildAdded) { 
				// don't add indent to first line if it's the first child in parent
			} else {
				sb = sb.append(indent);
			}
			sb = sb.append(lines[i]);
			if (i != lines.length - 1) {
				// add new line at the end, except last line
				sb = sb.append("\n");
			}				
		}			
				
		return sb.toString();		
	}		
	
	private String getKeyFeatureValue(RegExAstNode node) {
		for (RegExAstNodeParameter parameter : node.getParameters()) {
			if (parameter.getName().equals(node.getKeyParameter())) {
				return parameter.getValue();
			}
		}
		return null;
	}
	
	private String getChildType(RegExAstNode child) {
		String codeSyncType = child.getType();
		CodeSyncElementDescriptor descriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(codeSyncType);
		return descriptor.getCodeSyncTypeCategories().get(0);
	}
	
	private List<RegExAstNode> getChildrenWithTemplate(RegExAstNode parent, List<RegExAstNode> children) {
		for (RegExAstNode child : parent.getChildren()) {
//			if (child.isCategoryNode()) {
//				getChildrenWithTemplate(child, children);
//			} else {
				children.add(child);
//			}
		}
		return children;
	}
	
	/**
	 * Returns the {@link RegExAstNode#getChildrenInsertPoint()} of the parent node, or the
	 * {@link RegExAstNode#getNextSiblingInsertPoint()} of the previous sibling, if any.
	 */
	private int getInsertPoint(RegExAstNode node) {
		RegExAstNode parent = (RegExAstNode) node.eContainer();
		if (parent == null) {
			return 0;
		}
		int childIndex = parent.getChildren().indexOf(node);
		for (int i = childIndex - 1; i >= 0 ; i--) {
			RegExAstNode sibling = parent.getChildren().get(i);
			if (!sibling.isAdded() && getChildType(node).equals(sibling)) {
				return sibling.getNextSiblingInsertPoint();
			}
		}
		if (parent.getChildrenInsertPoints().keySet().contains(getChildType(node))) {
			return parent.getChildrenInsertPoints().get(getChildType(node));
		} else {
			throw new RuntimeException("RegExAstNode " + getKeyFeatureValue(parent) + " of type " + parent.getType() + " does not accept children of type " + getChildType(node));
		}
	}

	/**
	 * @author Cristina Constantinescu
	 */
	private boolean isFirstChildAdded(RegExAstNode node) {
		RegExAstNode parent = (RegExAstNode) node.eContainer();
		if (parent == null) {
			return true;
		}
		int childIndex = parent.getChildren().indexOf(node);
		for (int i = childIndex - 1; i >= 0 ; i--) {
			RegExAstNode sibling = parent.getChildren().get(i);
			if (getChildType(node).equals(getChildType(sibling))) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public List<?> getChildren(Object modelElement) {
		return Collections.singletonList(getOrCreateFileInfo(getFile(modelElement)));
	}

}
