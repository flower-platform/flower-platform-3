package org.flowerplatform.eclipse;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.actions.OpenFileAction;

/**
 * <code>CompoundContributionItem</code> responsible to
 * show the menu entry for <code>CloseFileAction</code> on IFile
 * only for the files that have a metamodelSet registered extension.
 * 
 * @author mircea
 * @flowerModelElementId _dOCTkODeEd6mHJ_uPEM2_g
 */
public class OpenFileCompoundContribution extends CompoundContributionItem {
	
	/**
	 * The close file action.
	 */
//	OpenFileAction action = new OpenFileAction();
//	
//	public OpenFileCompoundContribution() {
//	}
//
//	public OpenFileCompoundContribution(String id) {
//		super(id);
//	}

	/**
	 * {@link CompoundContributionItem.getContributionItems()}
	 * 
	 * Returns the <code>CloseFileAction</code> action and a 
	 * menu separator after it. 
	 * 
	 * @flowerModelElementId _dOCTouDeEd6mHJ_uPEM2_g
	 */
	@Override
	protected IContributionItem[] getContributionItems() {
		// get the selection from the active workbench window
//		ISelectionService selectionService = PlatformUI.getWorkbench()
//				.getActiveWorkbenchWindow().getSelectionService();
//		ISelection selection = selectionService.getSelection();
//		if (selection != null && selection instanceof IStructuredSelection) {
//			Object file = ((IStructuredSelection) selection).getFirstElement();
//		
//				action.setSelectionService(selectionService);
//				return new IContributionItem[] {
//						new ActionContributionItem(action), new Separator() };
//			}
		
//		return new IContributionItem[] {};
		return null;
	}
		
}
