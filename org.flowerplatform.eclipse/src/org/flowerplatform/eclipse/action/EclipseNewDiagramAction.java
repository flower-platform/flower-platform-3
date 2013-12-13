package org.flowerplatform.eclipse.action;

import org.flowerplatform.editor.model.java.remote.NewJavaClassDiagramAction;

/**
 * @author Sebastian Solomon
 */
public class EclipseNewDiagramAction extends NewJavaClassDiagramAction {

	@Override
	public void executeCommand() {
		createDiagram();
	}

}
