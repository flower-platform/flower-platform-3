package org.flowerplatform.eclipse.part;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.internal.EditorPane;
import org.eclipse.ui.internal.EditorSite;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.eclipse.EclipsePlugin;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class FlowerDiagramEditor extends EditorPart {

	public static final String EDITOR_ID = "org.flowerplatform.eclipse.editor.FlowerDiagramEditor";

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

	// private EclipseCommunicationChannel communicationChannel;
	
	private Browser browser;

	private Composite parent;

	public Composite getParent() {
		return parent;
	}

	public Browser getBrowser() {
		return browser;
	}

	// public EclipseCommunicationChannel getCommunicationChannel() {
	// return communicationChannel;
	// }
	//

	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
	}
	
	public FlowerDiagramEditor() {		
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
				// only if this FlowerDiagramEditor has been brought to top
				if (part == FlowerDiagramEditor.this) {
					browser.setParent(((FlowerDiagramEditor) part).getParent());
					// DiagramEditorStatefulService.getInstance().revealEditor(
					// communicationChannel,
					// getFriendlyEditableResourcePath()
					// + "/|_1eeo4L7FEeKY4uWt8tp0Gw");
				}
			}

			public void partClosed(IWorkbenchPart part) {
				if (part == FlowerDiagramEditor.this) {
					IEditorReference[] editorReferences = FlowerDiagramEditor.this
							.getSite().getPage().getEditorReferences();
					try {
						// if (editorReferences.length == 0) {
						// DiagramEditorStatefulService.getInstance()
						// .unsubscribeClientForcefully(
						// communicationChannel.getClientId());
						// return;
						// }
						IEditorPart editorPart = part.getSite()
								.getWorkbenchWindow().getActivePage()
								.getActiveEditor();
						if (FlowerDiagramEditor.EDITOR_ID.equals(editorPart
								.getEditorSite().getId())) {
							browser.setParent(((FlowerDiagramEditor) editorPart)
									.getParent());
							// DiagramEditorStatefulService
							// .getInstance()
							// .unsubscribeClientForcefully(
							// communicationChannel.getClientId(),
							// getFriendlyEditableResourcePath()
							// + "/|_1eeo4L7FEeKY4uWt8tp0Gw");

						} else {
							for (IEditorReference ref : editorReferences) {
								IWorkbenchPart refPart = ref.getPart(false);
								if (FlowerDiagramEditor.EDITOR_ID.equals(ref
										.getId()) && !part.equals(refPart)) {
									FlowerDiagramEditor firstDiagram = (FlowerDiagramEditor) refPart;

									browser.setParent(firstDiagram.getParent());
									// DiagramEditorStatefulService
									// .getInstance()
									// .unsubscribeClientForcefully(
									// communicationChannel
									// .getClientId(),
									// getFriendlyEditableResourcePath()
									// + "/|_1eeo4L7FEeKY4uWt8tp0Gw");

									break;
								}
							}
						}
					} catch (Exception ex) {
						logger.error("Failed to change browser parent!", ex);
					}
				}
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

	public boolean isSaveAsAllowed() {
		return false;
	}

	@SuppressWarnings("restriction")
	public void createPartControl(Composite parent) {
		this.parent = parent;

		parent.setLayout(new FillLayout());

		IEditorReference[] editorReferences = FlowerDiagramEditor.this
				.getSite().getPage().getEditorReferences();
		if (editorReferences.length > 0) {
			for (IEditorReference ref : editorReferences) {
				FlowerDiagramEditor editor = (FlowerDiagramEditor) ref
						.getEditor(false);
				if (editor != null) {
					EditorPane editorPartPane = (EditorPane) ((EditorSite) editor
							.getSite()).getPane();
					if (FlowerDiagramEditor.EDITOR_ID.equals(ref.getId())
							&& editorPartPane.getContainer().equals(
									((EditorPane) ((EditorSite) this.getSite())
											.getPane()).getContainer())) {
						// communicationChannel =
						// editor.getCommunicationChannel();
						browser = editor.getBrowser();
					}
					break;
				}
			}
		}
		
		int type = SWT.NONE;
		if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
			type = SWT.MOZILLA;
		}
		browser = new Browser(parent, type);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// TODO CC: here we must append openResources command
		browser.setUrl(EclipsePlugin.getInstance().getFlowerJettyServer().getUrl());		
	}

	@Override
	public void setFocus() {
		browser.setFocus();
	}

	private String getFriendlyEditableResourcePath() {
		 return ((FileEditorInput)
		 getEditorInput()).getFile().getFullPath().toString();
	}

}
