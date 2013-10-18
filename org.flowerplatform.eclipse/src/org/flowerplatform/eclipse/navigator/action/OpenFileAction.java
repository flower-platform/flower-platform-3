package org.flowerplatform.eclipse.navigator.action;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;

//**


/**
 * Action shown on the Root element and File.
 * 
 * Closes the file.
 * 
 * It also serves as an entry for the org.eclipse.popupMenus 
 * extension, so that the action can be shown on the IFile's menu.
 * 
 * @author mircea
 * @flowerModelElementId _TCTRENkXEd6EC53rmwPZGg
 */
public class OpenFileAction extends NavigatorAction {

	/**
	 * The <code>ISelectionService</code> that will provide
	 * the current selection when the action is shown on an
	 * IFile
	 */
//	protected ISelectionService selectionService = null;
//	
//	/**
//	 * Constructor without parameters.
//	 * 
//	 * Need it because we have to buil a CloseFileAction in
//	 * <code>CloseFileCompoundContribution</code>.
//	 */
//	public OpenFileAction() {
//		this(null);
//	}
//	
//	public OpenFileAction(NavigatorActionProvider actionProvider) {
//		super(actionProvider);
//		setText(EclipseFlowerMPAssets.INSTANCE.getMessage("_UI_CloseFileAction_Title"));
//	}
//
//	/**
//	 * When the action is added to the menu on the IFile,
//	 * we need a selection service so that we can get the selection.
//	 * 
//	 * @param selectionService
//	 */
//	public void setSelectionService(ISelectionService selectionService) {
//		this.selectionService = selectionService;
//	}
//	
//	/**
//	 * Checks to see if it was called on a Root element or on a IFile element.
//	 * 
//	 * If it is on a Root element return true.
//	 * If it is on a File/Files and all the 
//	 * 	selected files are opened return true.
//	 * Otherwise return false.
//	 */
//	public boolean isEnabled() {
//		IStructuredSelection selection;
//		if (actionProvider != null) {
//			selection = (IStructuredSelection) actionProvider
//					.getContext().getSelection();
//			// we work only for one selected item
//			if (selection.size() == 1 
//					&& selection.getFirstElement() instanceof Package
//					&& (((Package) selection.getFirstElement()).getOwner() == null) ) {
//				// we are on the root package
//				return true;
//			}
//		} else if (selectionService != null) {
//			// this is the case when the menu is on a IFile
//			// The selection is Ok otherwise the action would not be 
//			// visible
//			// Now only check if the files are opened or closed 
//			// so that we enable/disable the command
//			// if any file is closed => disable the command
//			ISelection sel = selectionService.getSelection();
//			if (sel instanceof IStructuredSelection) {
//				selection = (IStructuredSelection) sel;
//				if (selection.size() > 0) {
//					for (Object obj: selection.toList()) {
//						// use contains because get is redefined
//						if (!EditingDomainRegistry.INSTANCE.containsKey(obj)) {
//							return false;
//						}
//					}
//					return true;
//				}
//			}
//		}
//		return false;
//	}

	/**
	 * Delegates closing to the EditingDomainRegistry,
	 * collapses the ProjectExplorer (only the leaves 
	 * corresponding to the closed files), and then 
	 * refresh the ProjectExplorer (only for the closed
	 * files).
	 * 
	 * We need to collapses the leaves in the ProjectExplorer
	 * because, on refresh, the files will be opened again.
	 * 
	 * @flowerModelElementId _u76cQNkXEd6EC53rmwPZGg
	 */
	@SuppressWarnings("unchecked")
	public void run() {

//		// The list of files to be closed
//		final ArrayList<IFile> filesToBeClosed = new ArrayList<IFile>();
//		
//		// current selection
//		IStructuredSelection selection;
//		
//		if (actionProvider != null) {
//			// called on a Root package
//			// close the IFile attached to the editingDomain
//			selection = (IStructuredSelection) actionProvider
//					.getContext().getSelection();
//			EditingDomain editingDomain = FlowerEditingDomain
//					.getEditingDomainFor((EObject) selection.getFirstElement());
//			
//			filesToBeClosed.add(((EclipseFlowerEditingDomain) editingDomain).getMainFile());
//		} else if (selectionService != null) {
//			// called on a selection of opened IFile's
//			selection = (IStructuredSelection) selectionService.getSelection();
//			filesToBeClosed.addAll(selection.toList());
//		} else {
//			throw new RuntimeException("Run called without selection.");
//		}
//		
//		// closing the files using EditingDomainRegistry
//		EditingDomainRegistry.INSTANCE.unloadFiles(filesToBeClosed, true);
//		
//		// close the tree containing the file, and
//		// then refresh the tree so that it knows that the 
//		// File has been closed.
//		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
//			public void run() {
//				CommonNavigator common = (CommonNavigator) EclipseFlowerMPPlugin
//						.getViewPartByName("org.eclipse.ui.navigator.ProjectExplorer");
//				if (common != null) {
//					if (common.getCommonViewer() != null) {
//						for (IFile ifile: filesToBeClosed) {
//							common.getCommonViewer().collapseToLevel(ifile, AbstractTreeViewer.ALL_LEVELS);
//						}
//						common.getCommonViewer().refresh(filesToBeClosed);
//					}
//				}
//			}
//		});		
	}

}