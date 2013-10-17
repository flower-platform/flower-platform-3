package org.flowerplatform.eclipse.part;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorLauncher;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;

public class FlowerDiagramEditorLauncherOpenInNewEditor implements
		IEditorLauncher {

	public static final String EDITOR_LAUNCHER_ID = "org.flowerplatform.eclipse.part.FlowerDiagramEditorLauncher";

	/**
	 * Called when selecting open in new editor Loads the file in a new editor
	 * 
	 */

	@Override
	public void open(IPath file) {
		// transformation from IPath to IFile
		IFile[] iFiles = ResourcesPlugin.getWorkspace().getRoot()
				.findFilesForLocation(file);
		final FileEditorInput fileEditorInput = new FileEditorInput(iFiles[0]);

		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

			public void run() {
				try {
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