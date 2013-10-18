package org.flowerplatform.eclipse.action;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Shows the "Go To Model" menu when the specific combination of keys is pressed.
 * 
 * @author 
 */
public class NewDiagramHandler extends AbstractHandler {
	
		
		private QualifiedName path = new QualifiedName("html", "path");

		  @Override
		  public Object execute(ExecutionEvent event) {
		    Shell shell = HandlerUtil.getActiveShell(event);
		    ISelection sel = HandlerUtil.getActiveMenuSelection(event);
		    IStructuredSelection selection = (IStructuredSelection) sel;
		    Object firstElement = selection.getFirstElement();
		    if (firstElement instanceof File){
		    	//crete file in the parent
		    	//( (File)firstElement ).getParent()...
		    }else{ /*if directory*/
		    	//crete file here
		    }
		    createFile(shell, firstElement);
		    return null;
		  }

		  private void createFile(Shell shell, Object firstElement) {
//		    String directory;
//		    ICompilationUnit cu = (ICompilationUnit) firstElement;
//		    IResource res = cu.getResource();
//
//		    
//		      DirectoryDialog fileDialog = new DirectoryDialog(shell);
//		      directory = fileDialog.open();

		    
		  }

}
		  
