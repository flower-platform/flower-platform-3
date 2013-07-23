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
package com.crispico.flower.mp.codesync.base;

import java.util.List;

import com.crispico.flower.mp.codesync.base.action.DiffActionEntry;
import com.crispico.flower.mp.codesync.base.action.DiffActionRegistry;
import com.crispico.flower.mp.codesync.base.action.DiffContextMenuEntry;
import com.crispico.flower.mp.codesync.base.communication.DiffTreeNode;

public class MatchModelAdapterUI implements IModelAdapterUI {

	public static final int ADDED_COLOR = 0x8797FF;
	
	public static final int REMOVED_COLOR = 0xFF9C9C;
	
	public static final int MODIFIED_COLOR = 0x62DB5E;
	
	public static final int CHILDREN_MODIFIED_COLOR = 0xCFCFCF;
	
	public static final int CONFLICT_COLOR = 0xFF0000;
	
	public static final int CHILDREN_CONFLICT_COLOR = 0x000000;
	
	@Override
	public List<?> getChildren(Object modelElement) {
		return ((Match) modelElement).getSubMatches();
	}

	@Override
	public List<String> getIconUrls(Object modelElement) {
		if (modelElement instanceof Match) {
			Match match = (Match) modelElement;
			Object[] delegateAndAdapter = match.getDelegateAndModelAdapter(match.getEditableResource().getModelAdapterFactorySet());
			if (delegateAndAdapter != null)
				return ((IModelAdapter) delegateAndAdapter[1]).getIconUrls(delegateAndAdapter[0]);
		}
		return null;
	}

	@Override
	public String getLabel(Object modelElement) {
		if (modelElement instanceof Match) {
			Match match = (Match) modelElement;
			Object[] delegateAndAdapter = match.getDelegateAndModelAdapter(match.getEditableResource().getModelAdapterFactorySet());
			if (delegateAndAdapter != null)
				return ((IModelAdapter) delegateAndAdapter[1]).getLabel(delegateAndAdapter[0]);
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object modelElement) {
		return !((Match) modelElement).getSubMatches().isEmpty();
	}
	
	private void appendToolTipLine(StringBuffer existingToolTip, String newToolTipLine) {
		if (existingToolTip.length() != 0)
			existingToolTip.append('\n');
		existingToolTip.append(newToolTipLine);
	}
	
	public void processDiffTreeNode(Object modelElement, DiffTreeNode treeNode) {
		if (modelElement instanceof Match) {
			StringBuffer leftToolTip = new StringBuffer();
			StringBuffer rightToolTip = new StringBuffer();
			
			Match match = (Match) modelElement;
			boolean leftColored = false;
			boolean rightColored = false;
			DiffContextMenuEntry cmEntry;
		
			if (match.isLeftAdd()) {
				leftColored = true;
				treeNode.setLeftColor(ADDED_COLOR);
				appendToolTipLine(leftToolTip, "Newly added");
				{
					cmEntry = new DiffContextMenuEntry();
					cmEntry.setLabel("Newly added");
					cmEntry.setRight(false);
					cmEntry.setColor(ADDED_COLOR);
					cmEntry.setActionEntries(DiffActionRegistry.INSTANCE.getActionEntriesForUI(match, null, false).entries);
					treeNode.getContextMenuEntries().add(cmEntry);
				}
			} else if (match.isLeftRemove()) {
				leftColored = true;
				treeNode.setLeftColor(REMOVED_COLOR);
				appendToolTipLine(leftToolTip, "Removed");
				{
					cmEntry = new DiffContextMenuEntry();
					cmEntry.setLabel("Removed");
					cmEntry.setRight(false);
					cmEntry.setColor(REMOVED_COLOR);
					cmEntry.setActionEntries(DiffActionRegistry.INSTANCE.getActionEntriesForUI(match, null, false).entries);
					treeNode.getContextMenuEntries().add(cmEntry);
				}
			} 

			if (match.isRightAdd()) {
				rightColored = true;
				treeNode.setRightColor(ADDED_COLOR);
				appendToolTipLine(rightToolTip, "Newly added");
				{
					cmEntry = new DiffContextMenuEntry();
					cmEntry.setLabel("Newly added");
					cmEntry.setRight(true);
					cmEntry.setColor(ADDED_COLOR);
					cmEntry.setActionEntries(DiffActionRegistry.INSTANCE.getActionEntriesForUI(match, null, false).entries);
					treeNode.getContextMenuEntries().add(cmEntry);
				}
			} else if (match.isRightRemove()) {
				rightColored = true;
				treeNode.setRightColor(REMOVED_COLOR);
				appendToolTipLine(rightToolTip, "Removed");
				{
					cmEntry = new DiffContextMenuEntry();
					cmEntry.setLabel("Removed");
					cmEntry.setRight(true);
					cmEntry.setColor(REMOVED_COLOR);
					cmEntry.setActionEntries(DiffActionRegistry.INSTANCE.getActionEntriesForUI(match, null, false).entries);
					treeNode.getContextMenuEntries().add(cmEntry);
				}
			}
				
			// equalize 1st line (in order to have left&right aligned)	
			if (leftColored && !rightColored) {
				appendToolTipLine(rightToolTip, " ");
				{
					cmEntry = new DiffContextMenuEntry();
					cmEntry.setLabel(" ");
					cmEntry.setRight(true);
					treeNode.getContextMenuEntries().add(cmEntry);
				}
			} else if (rightColored && !leftColored) {
				appendToolTipLine(leftToolTip, " ");
				{
					cmEntry = new DiffContextMenuEntry();
					cmEntry.setLabel(" ");
					cmEntry.setRight(false);
					treeNode.getContextMenuEntries().add(cmEntry);
				}
			}
			
			for (Diff diff : match.getDiffs()) {
				Object feature = diff.getFeature();
				String string = null;
				if (diff.isLeftModified()) {
					leftColored = true;
					treeNode.setLeftColor(MODIFIED_COLOR);
					IModelAdapter leftModelAdapter = match.getEditableResource().getModelAdapterFactorySet().getLeftFactory().getModelAdapter(match.getLeft());
					if (match.getAncestor() == null) {
						string = String.format("%s = %s", 
								match.getEditableResource().getModelAdapterFactorySet().getFeatureProvider(match.getLeft()).getFeatureName(feature), 
								leftModelAdapter.getValueFeatureValue(match.getLeft(), feature, null));
						appendToolTipLine(leftToolTip, string);
					} else {
						IModelAdapter ancestorModelAdapter = match.getEditableResource().getModelAdapterFactorySet().getAncestorFactory().getModelAdapter(match.getAncestor());
						string = String.format("%s = %s (was %s)", 
								match.getEditableResource().getModelAdapterFactorySet().getFeatureProvider(match.getAncestor()).getFeatureName(feature), 
								leftModelAdapter.getValueFeatureValue(match.getLeft(), feature, null), 
								ancestorModelAdapter.getValueFeatureValue(match.getAncestor(), feature, null));
						appendToolTipLine(leftToolTip, string);
					}
				} 
				if (string == null) {
					string = " ";
					appendToolTipLine(leftToolTip, " ");
				}
				{
					cmEntry = new DiffContextMenuEntry();
					cmEntry.setLabel(string);
					cmEntry.setRight(false);
					cmEntry.setColor(MODIFIED_COLOR);
					if (string != " ")
						cmEntry.setActionEntries(DiffActionRegistry.INSTANCE.getActionEntriesForUI(match, diff, false).entries);
					treeNode.getContextMenuEntries().add(cmEntry);
				}
				string = null;

				if (diff.isRightModified()) {
					rightColored = true;
					treeNode.setRightColor(MODIFIED_COLOR);
					IModelAdapter rightModelAdapter = match.getEditableResource().getModelAdapterFactorySet().getRightFactory().getModelAdapter(match.getRight());
					if (match.getAncestor() == null) {
						string = String.format("%s = %s", 
								match.getEditableResource().getModelAdapterFactorySet().getFeatureProvider(match.getRight()).getFeatureName(feature), 
								rightModelAdapter.getValueFeatureValue(match.getRight(), feature, null));
						appendToolTipLine(rightToolTip, string);
					} else {
						IModelAdapter ancestorModelAdapter = match.getEditableResource().getModelAdapterFactorySet().getAncestorFactory().getModelAdapter(match.getAncestor());
						string = String.format("%s = %s (was %s)", 
								match.getEditableResource().getModelAdapterFactorySet().getFeatureProvider(match.getAncestor()).getFeatureName(feature), 
								rightModelAdapter.getValueFeatureValue(match.getRight(), feature, null), 
								ancestorModelAdapter.getValueFeatureValue(match.getAncestor(), feature, null));
						appendToolTipLine(rightToolTip, string);
					}
				} 
				if (string == null) {
					string = " ";
					appendToolTipLine(rightToolTip, " ");
				}
				{
					cmEntry = new DiffContextMenuEntry();
					cmEntry.setLabel(string);
					cmEntry.setRight(true);
					cmEntry.setColor(MODIFIED_COLOR);
					if (string != " ")
						cmEntry.setActionEntries(DiffActionRegistry.INSTANCE.getActionEntriesForUI(match, diff, false).entries);
					treeNode.getContextMenuEntries().add(cmEntry);
				}
			}
			
			if (!leftColored && match.isChildrenModifiedLeft()) {
				treeNode.setLeftColor(CHILDREN_MODIFIED_COLOR);			
			}
			
			if (!rightColored && match.isChildrenModifiedRight()) {
				treeNode.setRightColor(CHILDREN_MODIFIED_COLOR);

			}

			
//			String leftChildrenOperations = "";
//			if (match.isChildrenHaveLeftAdd()) {
//				leftChildrenOperations += "add, ";
//				if (!leftColored) {
//					treeNode.setTopLeftColor(0x222222);
//					treeNode.setBottomLeftColor(0x222222);
//				}
//			}
//			if (match.isChildrenHaveLeftRemove()) {
//				leftChildrenOperations += "remove, ";
//				if (!leftColored) {
//					treeNode.setTopLeftColor(0x222222);
//					treeNode.setBottomLeftColor(0x222222);
//				}
//			}
//			if (match.isChildrenHaveDiff()) {
//				leftChildrenOperations += "modification, ";
//				if (!leftColored) {
//					treeNode.setTopLeftColor(0x222222);
//					treeNode.setBottomLeftColor(0x222222);
//				}
//			}
//			if (leftChildrenOperations.length() > 0)
//				appendToolTipLine(leftToolTip, "Children: " + leftChildrenOperations);
//
//			String rightChildrenOperations = "";
//			if (match.isChildrenHaveRightAdd()) {
//				rightChildrenOperations += "add, ";
//				if (!rightColored) {
//					treeNode.setTopRightColor(0x222222);
//					treeNode.setBottomRightColor(0x222222);
//				}
//			}		
//			if (match.isChildrenHaveRightRemove()) {
//				rightChildrenOperations += "remove, ";
//				if (!rightColored) {
//					treeNode.setTopRightColor(0x222222);
//					treeNode.setBottomRightColor(0x222222);
//				}
//			}
//			if (match.isChildrenHaveDiff()) {
//				rightChildrenOperations += "modification, ";
//				if (!rightColored) {
//					treeNode.setTopLeftColor(0x222222);
//					treeNode.setBottomLeftColor(0x222222);
//				}
//			}
//			if (rightChildrenOperations.length() > 0)
//				appendToolTipLine(rightToolTip, "Children: " + rightChildrenOperations);
			
			if (match.isConflict())
				treeNode.setCrossColor(CONFLICT_COLOR);
			else if (match.isChildrenConflict())
				treeNode.setCrossColor(CHILDREN_CONFLICT_COLOR);
			
			treeNode.setToolTip(leftToolTip.toString() + "|||" + rightToolTip.toString());

			DiffActionEntry actionEntry;

//			cmEntry = new DiffContextMenuEntry();
//			cmEntry.setLabel("Label left");
//			cmEntry.setRight(false);
//			treeNode.getContextMenuEntries().add(cmEntry);
//
//			cmEntry = new DiffContextMenuEntry();
//			cmEntry.setLabel("Label left");
//			cmEntry.setRight(false);
//			treeNode.getContextMenuEntries().add(cmEntry);
//			
//			actionEntry= new DiffActionEntry();
//			actionEntry.setLabel("Action Label");
//			cmEntry.getActionEntries().add(actionEntry);
//
//			actionEntry= new DiffActionEntry();
//			actionEntry.setLabel("Action Label");
//			cmEntry.getActionEntries().add(actionEntry);
//
//			cmEntry = new DiffContextMenuEntry();
//			cmEntry.setLabel("Label right");
//			cmEntry.setRight(true);
//			treeNode.getContextMenuEntries().add(cmEntry);
//
//			cmEntry = new DiffContextMenuEntry();
//			cmEntry.setLabel("Label right");
//			cmEntry.setRight(true);
//			treeNode.getContextMenuEntries().add(cmEntry);
//
//			actionEntry= new DiffActionEntry();
//			actionEntry.setLabel("Action Label");
//			cmEntry.getActionEntries().add(actionEntry);
//
//			actionEntry= new DiffActionEntry();
//			actionEntry.setLabel("Action Label");
//			cmEntry.getActionEntries().add(actionEntry);

		}
	}
	
}