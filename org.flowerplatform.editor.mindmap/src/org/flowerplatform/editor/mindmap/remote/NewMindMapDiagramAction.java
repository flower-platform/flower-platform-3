package org.flowerplatform.editor.mindmap.remote;

import java.io.File;

import org.flowerplatform.editor.model.remote.NewDiagramAction;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.notation.MindMapNode;
import org.flowerplatform.emf_model.notation.NotationFactory;

import com.crispico.flower.mp.codesync.wiki.WikiPlugin;

/**
 * @author Mariana Gheorghe
 */
public class NewMindMapDiagramAction extends NewDiagramAction {

	@Override
	protected Diagram createDiagram(File file) {
		Diagram diagram = NotationFactory.eINSTANCE.createDiagram();
		MindMapNode root = NotationFactory.eINSTANCE.createMindMapNode();
		root.setViewType("folder");
		root.setExpanded(true);
		root.setHasChildren(true);
		root.setDiagrammableElement(WikiPlugin.getInstance().getWikiRoot(file, "github", communicationChannel));
		diagram.getPersistentChildren().add(root);
		return diagram;
	}

	@Override
	protected String getServiceId() {
		return "mindmapEditorStatefulService";
	}

}
