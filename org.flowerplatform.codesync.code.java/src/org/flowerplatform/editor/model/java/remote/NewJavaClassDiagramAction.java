package org.flowerplatform.editor.model.java.remote;

import java.io.File;

import org.flowerplatform.editor.model.remote.NewDiagramAction;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.notation.NotationFactory;

/**
 * @author Mariana Gheorghe
 */
public class NewJavaClassDiagramAction extends NewDiagramAction {

	@Override
	protected Diagram createDiagram(File file) {
		Diagram diagram = NotationFactory.eINSTANCE.createDiagram();
		diagram.setViewType("classDiagram");
		return diagram;
	}

	@Override
	protected String getServiceId() {
		return "diagramEditorStatefulService";
	}
	
}
