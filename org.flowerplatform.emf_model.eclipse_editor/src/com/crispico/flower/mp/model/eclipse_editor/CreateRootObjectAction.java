/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package com.crispico.flower.mp.model.eclipse_editor;

import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.action.StaticSelectionCommandAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Cristi
 */
public class CreateRootObjectAction extends StaticSelectionCommandAction {

	public static final String MENU_LABEL = "Create Root Object";
	
	private EClass classToCreate;
	
	public CreateRootObjectAction(IEditorPart editorPart, ISelection selection,
			EClass classToCreate) {
		super((IWorkbenchPart) editorPart);
		this.classToCreate = classToCreate;
		setText(classToCreate.getName());
		configureAction(selection);
	}
	
	// dintr-un motiv pe care nu-l inteleg, codul de mai jos da ClassNotFound pentru ChangeRecorder, atunci cand e rulat
	// in configuratie minimala. Cu eclipse Full, pare sa mearga

	@Override
	protected Command createActionCommand(EditingDomain editingDomain,
			Collection<?> collection) {
		if (collection.size() == 0) {
			return UnexecutableCommand.INSTANCE;
		}
		Object first = collection.iterator().next();

		Resource resource = null;
		if (first instanceof Resource) {
			resource = (Resource) first;
		} else if (first instanceof EObject) {
			resource = ((EObject) first).eResource();
		}
		if (resource == null) {
			return UnexecutableCommand.INSTANCE;
		}
		
		final Resource resource1 = resource;

		return new ChangeCommand(resource) {

			@Override
			protected void doExecute() {
				EObject obj = classToCreate.getEPackage().getEFactoryInstance().create(classToCreate);
				resource1.getContents().add(obj);
			}
		};
	}

}