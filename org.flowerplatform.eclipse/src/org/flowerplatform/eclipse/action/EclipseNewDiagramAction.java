package org.flowerplatform.eclipse.action;

import org.flowerplatform.editor.model.java.remote.NewJavaClassDiagramAction;

public class EclipseNewDiagramAction extends NewJavaClassDiagramAction {

	@Override
	public void executeCommand() {
		createDiagram();
	}

}
