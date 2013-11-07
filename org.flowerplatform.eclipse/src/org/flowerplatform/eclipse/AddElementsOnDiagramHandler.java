package org.flowerplatform.eclipse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.browser.Browser;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.flowerplatform.eclipse.part.FlowerDiagramEditor;
import org.flowerplatform.editor.EditorPlugin;

/**
 * @author Sebastian Solomon
 * 
 */
public class AddElementsOnDiagramHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		FlowerDiagramEditor flowerEditor = null;
		ISelection sel = HandlerUtil.getActiveMenuSelection(event);
		IStructuredSelection selection = (IStructuredSelection) sel;
		Iterator iterator = selection.iterator();
		List<String> paths = new ArrayList<>();
		
		while (iterator.hasNext()) {
			Object element = iterator.next();
			if (element instanceof IFile) {
				String path = EditorPlugin.getInstance().getFileAccessController().getPath(element);
				paths.add(path);
			}
		}
		
		flowerEditor = findLastOpendDiagramEditor();
		
		if (flowerEditor != null) {
			final Browser browser = flowerEditor.getBrowser();
			browser.execute("dragOnDiagram('" + paths + "')");
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().activate(flowerEditor);
		}
		return null;
	}

	protected FlowerDiagramEditor findLastOpendDiagramEditor() {
		FlowerDiagramEditor flowerEditor = null;
		IEditorPart activeEditor = PlatformUI.getWorkbench()
				                 .getActiveWorkbenchWindow().getActivePage()
				                 .getActiveEditor();
		if (activeEditor instanceof FlowerDiagramEditor) {
			return (FlowerDiagramEditor)activeEditor;
		}else {
			IEditorReference[] editorReference = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.getEditorReferences();
			for (int i = editorReference.length - 1; i >= 0 ; i--) {
				IEditorPart edit = editorReference[i].getEditor(false);
				if (edit instanceof FlowerDiagramEditor) {
					return (FlowerDiagramEditor)edit;
				}
			}
		}
		return flowerEditor;
	}
	
}
