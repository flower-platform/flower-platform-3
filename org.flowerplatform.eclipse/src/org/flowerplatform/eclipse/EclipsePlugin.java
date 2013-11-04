package org.flowerplatform.eclipse;

import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.swt.widgets.Display;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.public_resources.PublicResourcesServlet;
import org.flowerplatform.editor.EditorPlugin;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class EclipsePlugin extends AbstractFlowerJavaPlugin {

	protected static EclipsePlugin INSTANCE;
	
	public static EclipsePlugin getInstance() {
		return INSTANCE;
	}

	public EclipsePlugin() {
		super();
		INSTANCE = this;
		
		CommonPlugin.getInstance().initializeFlowerProperties(
				this.getClass().getClassLoader().getResourceAsStream("META-INF/flower.properties"));		
	}
	
	private FlowerJettyServer server;
			
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
		
		PublicResourcesServlet s;

		// CommunicationChannelManager.INSTANCE = new
		// EclipseCommunicationChannelManager();

		// AbstractSecurityUtils.INSTANCE = new AbstractSecurityUtils() {
		//
		// @Override
		// public boolean hasWritePermission(File file) {
		// // TODO Auto-generated method stub
		// return true;
		// }
		// };
		// SingletonRefsInEditorPluginFromWebPlugin.INSTANCE_ACTIVITY_LISTENER =
		// new DummyActivityListener();
		//
		// new EclipseCustomSerializationDescriptors().register();

		// // (WP-ME) Initializations
		// FlowerEditingDomain.idRegistryProvider =
		// WebIdRegistryProvider.INSTANCE;
		// TransferAdapter.transferAdapterLogicProvider = new
		// WebTransferAdapterLogicProvider();
		//
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				// Perform this initialization now, this way we won't need to
				// run
				// <code>ExtendedImageRegistry.getImage()</code> on the UI
				// thread,
				// since the only reason this was needed was to lazy load the
				// registry.
				ExtendedImageRegistry.getInstance();
			}
		});

		// ServiceRegistry.INSTANCE.registerService(PackageTreeStatefulService.SERVICE_ID,
		// new PackageTreeStatefulService());

		// initializeEditorSupport();

		server = new FlowerJettyServer();
		server.start();
		
		EditorPlugin.getInstance().setFileAccessController(new EclipseFileAccessController());
		EditorModelPlugin.getInstance().setModelAccessController(new EclipseModelAccessController());
		CommunicationPlugin.getInstance().getAllServicesStartedListeners().add(new Runnable() {
			@Override
			public void run() {
				EditorPlugin.getInstance().setFileAccessController(new EclipseFileAccessController());
				EditorModelPlugin.getInstance().setModelAccessController(new EclipseModelAccessController());
			}
		});		
		
	}

	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;

		server.stop();
		server = null;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// no MB yet
	}

	 public FlowerJettyServer getFlowerJettyServer() {
		 return server;
	 }

	// private void initializeEditorSupport() {
	// ServiceRegistry.INSTANCE.registerService(EditorSupport.SERVICE_ID,
	// EditorSupport.INSTANCE);
	//
	// SingletonRefsInEditorPluginFromWebPlugin.INSTANCE_MODEL_TREE_SUBSERVICE =
	// new DummyModelTreeSubService();
	// SingletonRefsInEditorPluginFromWebPlugin.INSTANCE_PROJECT_EXPLORER_TREE_STATEFUL_SERVICE
	// = new DummyProjectExplorerTreeStatefulService();
	//
	// // (WP-ME) initializations
	// // see this method for details
	// try {
	// WebIdRegistryProvider.INSTANCE.useLegacyGlobalIdRegistry = true;
	// new
	// EclipseServerSnapshotClientCommand(EclipseCommunicationChannel.createDiagramRelatedInitializationsCommand());
	// } finally {
	// WebIdRegistryProvider.INSTANCE.useLegacyGlobalIdRegistry = false;
	// }

	// model
	// EditorSupport.INSTANCE.addContentType(ModelEditorStatefulService.MODEL_CONTENT,
	// true);
	// ModelEditorStatefulService modelEditorStatefulService = new
	// ModelEditorStatefulService(ModelEditorStatefulService.MODEL_EDITOR);
	// ServiceRegistry.INSTANCE.registerService(ModelEditorStatefulService.SERVICE_ID,
	// modelEditorStatefulService);
	// // EditorSupport.INSTANCE.addContentTypeContributor(new
	// ModelMatchingContentTypeProvider(ModelEditorStatefulService.MODEL_CONTENT));
	// EditorSupport.INSTANCE.addEditorStatefulService(modelEditorStatefulService);
	// EditorSupport.INSTANCE.addCompatibleEditorToContentType(ModelEditorStatefulService.MODEL_CONTENT,
	// modelEditorStatefulService);
	//
	// // diagram
	// EditorSupport.INSTANCE.addContentType(DiagramEditorStatefulService.DIAGRAM_CONTENT,
	// false);
	// DiagramEditorStatefulService diagramEditorStatefulService = new
	// DiagramEditorStatefulService(DiagramEditorStatefulService.DIAGRAM_EDITOR);
	// ServiceRegistry.INSTANCE.registerService(DiagramEditorStatefulService.SERVICE_ID,
	// diagramEditorStatefulService);
	// EditorSupport.INSTANCE.addContentTypeContributor(new
	// EditorSupport.ClassMatchingContentTypeProvider(Diagram.class,
	// DiagramEditorStatefulService.DIAGRAM_CONTENT));
	// EditorSupport.INSTANCE.addEditorStatefulService(diagramEditorStatefulService);
	// EditorSupport.INSTANCE.addCompatibleEditorToContentType(DiagramEditorStatefulService.DIAGRAM_CONTENT,
	// diagramEditorStatefulService);
	//
	// EditorSupport.INSTANCE.setDefaultEditorStatefulService(modelEditorStatefulService);
	// }

}
