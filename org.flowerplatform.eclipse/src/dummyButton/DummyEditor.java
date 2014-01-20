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
package dummyButton;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.eclipse.part.FlowerDiagramEditor;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DummyEditor extends EditorPart {

	public static final String EDITOR_ID = "DummyEditor";

	public CommunicationChannel communicationChannel;
	
	private static final Logger logger = LoggerFactory
			.getLogger(FlowerDiagramEditor.class);

	static {
		Bundle bundle = Platform.getBundle("org.flowerplatform.eclipse");
		if (bundle != null) {
			URL resourceUrl = bundle
					.getResource("libs/mozilla/xulrunner_3_6_28_win32");
			if (resourceUrl != null) {
				try {
					URL fileUrl = FileLocator.toFileURL(resourceUrl);
					File file = new File(fileUrl.toURI());
					System.setProperty("org.eclipse.swt.browser.XULRunnerPath",
							file.getAbsolutePath());
				} catch (Exception e) {
					logger.error(
							"Failed to configure xulrunner path for mozilla browser!",
							e);
				}
			}
		}
	}

	
	private Browser browser;

	private Composite parent;
	
	public Composite getParent() {
		return parent;
	}

	public Browser getBrowser() {
		return browser;
	}


	@Override
	public void doSaveAs() {
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		
		setSite(site);
		setInput(input);
		
		setPartName(getEditorInput().getName());
		setTitleToolTip(getEditorInput().getToolTipText());

		site.getPage().addPartListener(new IPartListener() {

			public void partActivated(IWorkbenchPart part) {
			}

			public void partBroughtToTop(IWorkbenchPart part) {
			}

			public void partClosed(IWorkbenchPart part) {
			}

			public void partDeactivated(IWorkbenchPart part) {
			}

			public void partOpened(IWorkbenchPart part) {
			}

		});

	}

	public boolean isDirty() {
		return false;
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
	}
	
	public boolean isSaveAsAllowed() {
		return false;
	}

	@SuppressWarnings("restriction")
	public void createPartControl(Composite parent) {
		if (getEditorInput() instanceof StringInput) {
			browser = new Browser(parent, SWT.NONE);
			browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			browser.setUrl("https://www.google.ro/");
			return;
		}
	}

	@Override
	public void setFocus() {
		browser.setFocus();
	}

}
