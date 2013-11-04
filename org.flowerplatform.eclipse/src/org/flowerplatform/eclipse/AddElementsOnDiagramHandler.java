package org.flowerplatform.eclipse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.browser.Browser;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.flowerplatform.eclipse.part.FlowerDiagramEditor;
import org.flowerplatform.editor.EditorPlugin;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.editor.remote.EditableResource;

/**
 * @author Sebastian Solomon
 * 
 */
public class AddElementsOnDiagramHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		ISelection sel = HandlerUtil.getActiveMenuSelection(event);
		IStructuredSelection selection = (IStructuredSelection) sel;
		Iterator iterator = selection.iterator();
		List<String> paths = new ArrayList<>();
		
		// TODO to delete
//		if (EditorModelPlugin.getInstance().getModelAccessController() == null) {
//			EditorModelPlugin.getInstance().setModelAccessController(new EclipseModelAccessController());
//		}
//		if (EditorPlugin.getInstance().getFileAccessController() == null) {
//			EditorPlugin.getInstance().setFileAccessController(new EclipseFileAccessController());
//		}
		//
		
		while (iterator.hasNext()) {
			Object element = iterator.next();
			if (element instanceof IFile) {
				// EditorModelPlugin.getInstance().getModelAccessController().getURIFromFile(element);
				String path = EditorPlugin.getInstance().getFileAccessController().getPath(element);
				paths.add(path);
			}
		}
		// TODO gasesti 'editorul' bun
		FlowerDiagramEditor flowerEditor = null;
		IEditorPart editor = PlatformUI.getWorkbench()
				                 .getActiveWorkbenchWindow().getActivePage()
				                 .getActiveEditor();
		if (editor instanceof FlowerDiagramEditor) {
			flowerEditor = (FlowerDiagramEditor)editor;
		}else {
			IEditorReference[] editorReference = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.getEditorReferences();
			for (int i = editorReference.length - 1; i >= 0 ; i--) {
				IEditorPart edit = editorReference[i].getEditor(false);
				if (edit instanceof FlowerDiagramEditor) {
					flowerEditor = (FlowerDiagramEditor)edit;
					break;
				}
			}
		}
		
		if (flowerEditor != null) {
			final Browser browser = flowerEditor.getBrowser();
			browser.execute("dragOnDiagram('" + paths + "')");
		}else{
			return null;
			//nu exista editor deschis
		}
		return null;
	}

}
