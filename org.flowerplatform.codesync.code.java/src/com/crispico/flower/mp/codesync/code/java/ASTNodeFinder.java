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
package com.crispico.flower.mp.codesync.code.java;


/**
 * @author Mariana
 */
public class ASTNodeFinder { 
//extends GenericVisitor {
//
//	private JavaComposedAstNodeModelAdapter adapter = new JavaComposedAstNodeModelAdapter();
//	
//	private ASTNode foundNode;
//	
//	private Object matchKey;
//	
//	private ASTNodeFinder(ASTNode node) {
//		matchKey = getMatchKey(node);
//	}
//	
//	public ASTNode getFoundNode() {
//		return foundNode;
//	}
//	
//	@Override
//	protected boolean visitNode(ASTNode node) {
//		if (matchKey.equals(getMatchKey(node))) {
//			foundNode = node;
//		}
//		if (foundNode == null)
//			return true;
//		return false; // no need to keep looking	
//	}
//	
//	private Object getMatchKey(ASTNode node) {
//		JavaAbstractAstNodeModelAdapter specificAdapter = adapter.getAstNodeModelAdapter(node);
//		if (specificAdapter == null)
//			return null;
//		return specificAdapter.getMatchKey(node);
//	}
//
//	/**
//	 * Visits the tree rooted at <code>root</code> and returns a node
//	 * corresponding to <code>node</code>. Note: the ASTs of <code>root</code>
//	 * and <code>node</code> may be different.
//	 */
//	public static ASTNode findNode(ASTNode root, ASTNode node) {
//		ASTNodeFinder finder = new ASTNodeFinder(node);
//		root.accept(finder);
//		return finder.getFoundNode();
//	}
	
}