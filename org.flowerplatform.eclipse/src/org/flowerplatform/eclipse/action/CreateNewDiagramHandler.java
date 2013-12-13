package org.flowerplatform.eclipse.action;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.ResourceUtil;

/**
 * @author Sebastian Solomon
 */
public class CreateNewDiagramHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) {
		ISelection sel = HandlerUtil.getActiveMenuSelection(event);
		IStructuredSelection selection = (IStructuredSelection) sel;
		Object selectedElement = selection.getFirstElement();
		selectedElement = ResourceUtil.getResource(selectedElement);
		IResource parent = null;

		if (selectedElement instanceof IFile) {
			parent = ((IFile)selectedElement).getParent();
		} else if ((selectedElement instanceof IFolder)
				|| (selectedElement instanceof IProject)) {
			parent = (IResource) selectedElement;
		}
		if (parent != null){
			EclipseNewDiagramAction enda = new EclipseNewDiagramAction();
			enda.parentPath = parent.toString().substring(2);
			enda.name = "NewDiagram.notation";
			enda.executeCommand();
		}
		return null;
	}
	
}
