package org.flowerplatform.eclipse.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorLauncher;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.model.AdaptableList;
import org.eclipse.ui.part.FileEditorInput;
import org.flowerplatform.editor.EditorPlugin;
import org.flowerplatform.editor.remote.EditableResource;

public class FlowerDiagramEditorLauncher implements IEditorLauncher {
	

	/**
	 * Called when the user duble-click on a notation file.
	 * Loads the file if not loaded, else set focus on it
	 * 
	 * @author Sebastian Solomon
	 */
	
	//it will be set when the answer comes from javascript
	boolean isOpenedFlag = false;
	
	//needed to know in what editor is the file oppend when the answer comes from javascrips
	FlowerDiagramEditor currentEditor;
	
	
	@Override
	public void open(IPath file) {
		// transformation from IPath to IFile

		IFile[] iFiles = ResourcesPlugin.getWorkspace().getRoot()
				.findFilesForLocation(file);
		final FileEditorInput fileEditorInput = new FileEditorInput(iFiles[0]);

		if (!isFileOpened(fileEditorInput)) {

			PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

				public void run() {
					try {
						IEditorPart editor = PlatformUI.getWorkbench()
								.getActiveWorkbenchWindow().getActivePage()
								.getActiveEditor();
						if (editor != null) {
							if (editor instanceof FlowerDiagramEditor) {
								((FlowerDiagramEditor) editor).getBrowser()
										.execute(
												"handleLink('openResources="
														+ fileEditorInput
																.getFile()
																.getFullPath()
														+ "')");
								return;
							} else {// find the last opened FlowerDiagramEditor,
									// &set focus on it
								IEditorReference[] editorReference = PlatformUI
										.getWorkbench()
										.getActiveWorkbenchWindow()
										.getActivePage().getEditorReferences();
								for (int i = editorReference.length - 1; i >= 0; i--) {
									IEditorPart edit = editorReference[i]
											.getEditor(false);
									if (edit instanceof FlowerDiagramEditor) {
										((FlowerDiagramEditor) edit)
												.getBrowser()
												.execute(
														"handleLink('openResources="
																+ fileEditorInput
																		.getFile()
																		.getFullPath()
																+ "')");
										PlatformUI.getWorkbench()
												.getActiveWorkbenchWindow()
												.getActivePage().activate(edit);
										return;

									}
								}
							}
						}
						// if no FlowerDiagram editors, open a new one
						IDE.openEditor(PlatformUI.getWorkbench()
								.getActiveWorkbenchWindow().getActivePage(),
								fileEditorInput, FlowerDiagramEditor.EDITOR_ID);
					} catch (Exception e) {
						throw new RuntimeException("Could not open editor.", e);
					}
				}

			});
		}

	}
	
	
	private boolean isFileOpened(FileEditorInput file) {

		IEditorReference[] editorReference = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.getEditorReferences();

		for (int i = 0; i < editorReference.length; i++) {
			IEditorPart edit = editorReference[i].getEditor(false);
			if (edit instanceof FlowerDiagramEditor) {
				final Browser browser = ((FlowerDiagramEditor) edit)
						.getBrowser();
				final BrowserFunction function = new GetIsDiagramOpened(browser,
						"sendIsDiagramOpenedToJava");

				browser.addProgressListener(new ProgressAdapter() {
					public void completed(ProgressEvent event) {
						browser.addLocationListener(new LocationAdapter() {
							public void changed(LocationEvent event) {
								browser.removeLocationListener(this);
								// System.out.println
								// ("left java function-aware page, so disposed CustomFunction");
								function.dispose();
							}
						});
					}
				});
				currentEditor = (FlowerDiagramEditor) edit;
				((FlowerDiagramEditor) edit).getBrowser().execute(
						"isFileOpened('" + file.getFile().getFullPath() + "')");
			}
		}

		return isOpenedFlag;

	}
	
	
	 // Called by JavaScript
	class GetIsDiagramOpened extends BrowserFunction {

		GetIsDiagramOpened(Browser browser, String name) {
			super(browser, name);
		}

		public Object function(Object[] arguments) {

			boolean isOpened = ((Boolean) arguments[0]).booleanValue();

			FlowerDiagramEditorLauncher.this.isOpenedFlag = isOpened;
			if (isOpened) {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().activate(currentEditor);
			}
			return null;
		}

	}
}
						
