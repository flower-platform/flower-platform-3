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
package org.flowerplatform.editor.model.remote.scenario;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.communication.tree.GenericTreeContext;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.notation.Edge;
import org.flowerplatform.emf_model.notation.NotationFactory;
import org.flowerplatform.emf_model.notation.NotationPackage;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.ScenarioElement;

/**
 * @author Mariana Gheorghe
 */
public class ScenarioTreeStatefulService extends GenericTreeStatefulService {

	private DiagramEditorStatefulService diagramService;
	
	public void setDiagramService(DiagramEditorStatefulService diagramService) {
		this.diagramService = diagramService;
	}
	
	@Override
	public String getInplaceEditorText(
			StatefulServiceInvocationContext context,
			List<PathFragment> fullPath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setInplaceEditorText(
			StatefulServiceInvocationContext context, List<PathFragment> path,
			String text) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getStatefulClientPrefixId() {
		return "Scenario Tree";
	}

	public Resource getScenariosResource(DiagramEditableResource er) {
		String editableResourcePath = er.getEditableResourcePath();
		int index = editableResourcePath.lastIndexOf("/");
		String scenariosResourcePath = editableResourcePath.substring(0, index);
		StringBuilder name = new StringBuilder(editableResourcePath.substring(index));
		name.insert(name.indexOf("."), "Scenarios");
		scenariosResourcePath += name;
		File file = new File(CommonPlugin.getInstance().getWorkspaceRoot(), scenariosResourcePath);
		URI scenariosResourceUri = URI.createFileURI(file.getAbsolutePath());
		Resource resource = er.getMainResource().getResourceSet().getResource(scenariosResourceUri, false);
		if (resource == null) {
			if (file.exists()) {
				resource = er.getMainResource().getResourceSet().getResource(scenariosResourceUri, true);
			} else {
				resource = er.getMainResource().getResourceSet().createResource(scenariosResourceUri);
			}
		}
		return resource;
	}
	
	@Override
	public Collection<Pair<Object, String>> getChildrenForNode(Object node, TreeNode treeNode, GenericTreeContext context) {
		if (node instanceof Class) {
			String diagramEditableResourcePath = (String) context.getClientContext().get("diagramEditableResourcePath");
			DiagramEditableResource er = (DiagramEditableResource) diagramService.getEditableResource(diagramEditableResourcePath);
			Resource resource = getScenariosResource(er);
			List<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>();
			Diagram diagram = diagramService.getDiagram(er);
			for (EObject scenario : resource.getContents()) {
				Pair<Object, String> pair = new Pair<Object, String>(scenario, ((CodeSyncElement) scenario).getType());
				processScenario(diagram, (ScenarioElement) scenario);
				result.add(pair);
			}
			return result;
		} else {
			CodeSyncElement cse = (CodeSyncElement) node;
			List<Pair<Object, String>> result = new ArrayList<Pair<Object, String>>();
			for (CodeSyncElement child : cse.getChildren()) {
				Pair<Object, String> pair = new Pair<Object, String>(child, cse.getType());
				result.add(pair);
			}
			return result;
		}
	}
	
	@Override
	public Object getNodeByPath(List<PathFragment> fullPath, GenericTreeContext context) { 
		if (fullPath == null) {
			return ScenarioTreeStatefulService.class;
		}
		String diagramEditableResourcePath = (String) context.getClientContext().get("diagramEditableResourcePath");
		DiagramEditableResource er = (DiagramEditableResource) diagramService.getEditableResource(diagramEditableResourcePath);
		Resource resource = getScenariosResource(er);
		Object object = resource;
		for (PathFragment pathFragment : fullPath) {
			Object parent = object;
			object = null;
			for (Object elt : getChildren(parent)) {
				if (((ScenarioElement) elt).getName().equals(pathFragment.getName())) {
					object = elt;
				}
			}
			if (object == null) {
				return null;
			}
		}
		return object;
	}
	
	private List getChildren(Object object) {
		if (object instanceof Resource) {
			return ((Resource) object).getContents();
		} else {
			return ((ScenarioElement) object).getChildren();
		}
	}
	
	protected void processScenario(Diagram diagram, ScenarioElement scenario) {
		CodeSyncElement target = scenario.getInteraction();
		if (target != null) {
			ScenarioElement parent = (ScenarioElement) scenario.eContainer();
			if (parent != null) {
				CodeSyncElement source = parent.getInteraction();
				if (source != null) {
					boolean exists = false;
					for (Edge existingEgde : diagram.getPersistentEdges()) {
						if (existingEgde.getSource().getDiagrammableElement().equals(source) &&
								existingEgde.getTarget().getDiagrammableElement().equals(target)) {
							exists = true;
						}
					}
					if (!exists) {
						// add an egde
						Edge edge = NotationFactory.eINSTANCE.createEdge();
						View sourceView = getViewForElement(diagram, source);
						View targetView = getViewForElement(diagram, target);
						if (sourceView != null && targetView != null) {
							edge.setSource(sourceView);
							edge.setTarget(targetView);
							edge.setViewType("scenarioInterraction");
							edge.setDiagrammableElement(scenario);
							diagram.getPersistentEdges().add(edge);
						}
					}
				}
			}
		}
		for (CodeSyncElement child : scenario.getChildren()) {
			processScenario(diagram, (ScenarioElement) child);
		}
	}
	
	protected View getViewForElement(Diagram diagram, EObject object) {
		ECrossReferenceAdapter adapter = ECrossReferenceAdapter.getCrossReferenceAdapter(object);
		for (Setting setting : adapter.getNonNavigableInverseReferences(object)) {
			if (NotationPackage.eINSTANCE.getView_DiagrammableElement().equals(setting.getEStructuralFeature())) {
				View view = (View) setting.getEObject();
				return view;
			}
		}
		return null;
	}

	@Override
	public Boolean nodeHasChildren(Object node, TreeNode treeNode, GenericTreeContext context) {
		CodeSyncElement cse = (CodeSyncElement) node;
		if (cse.getChildren() != null && cse.getChildren().size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean populateTreeNode(Object source, TreeNode destination, GenericTreeContext context) {
		ScenarioElement cse = (ScenarioElement) source;
		String label = cse.getName();
		if (cse.getNumber() != null) {
			label = cse.getNumber() + (cse.getNumber().length() > 0 ? ". " : "") + label;
		}
		destination.setLabel(label);
		return true;
	}
	
	@Override
	public PathFragment getPathFragmentForNode(Object node, String nodeType, GenericTreeContext context) {
		CodeSyncElement cse = (CodeSyncElement) node;
		PathFragment fragment = new PathFragment(cse.getName(), cse.getType());
		return fragment;
		
	}

	@Override
	public String getLabelForLog(Object node, String nodeType) {
		return node.toString();
	}

}