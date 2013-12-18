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
package org.flowerplatform.idea;

import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.common.plugin.AbstractFlowerJavaPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.editor.EditorPlugin;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.idea.file.IdeaFileAccessController;
import org.flowerplatform.idea.file.IdeaModelAccessController;
import org.flowerplatform.idea.file.IdeaProjectAccessController;
import org.osgi.framework.BundleContext;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;

/**
 * @author Sebastian Solomon
 */
public class IdeaPlugin extends AbstractFlowerJavaPlugin {

	private static BundleContext context;
	protected static IdeaPlugin INSTANCE;
	
	public static IdeaPlugin getInstance() {
		return INSTANCE;
	}

	public IdeaPlugin() {
		super();
		INSTANCE = this;
		
		CommonPlugin.getInstance().initializeFlowerProperties(
				this.getClass().getClassLoader().getResourceAsStream("META-INF/flower.properties"));		
	}

	static BundleContext getContext() {
		return context;
	}
	
	private FlowerJettyServer server;

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;

		//		
//		PublicResourcesServlet s;
//
//		// CommunicationChannelManager.INSTANCE = new
//		// EclipseCommunicationChannelManager();
//
//		// AbstractSecurityUtils.INSTANCE = new AbstractSecurityUtils() {
//		//
//		// @Override
//		// public boolean hasWritePermission(File file) {
//		// // TODO Auto-generated method stub
//		// return true;
//		// }
//		// };
//		// SingletonRefsInEditorPluginFromWebPlugin.INSTANCE_ACTIVITY_LISTENER =
//		// new DummyActivityListener();
//		//
//		// new EclipseCustomSerializationDescriptors().register();
//
//		// // (WP-ME) Initializations
//		// FlowerEditingDomain.idRegistryProvider =
//		// WebIdRegistryProvider.INSTANCE;
//		// TransferAdapter.transferAdapterLogicProvider = new
//		// WebTransferAdapterLogicProvider();
//		//
//		Display.getDefault().syncExec(new Runnable() {
//
//			@Override
//			public void run() {
//				// Perform this initialization now, this way we won't need to
//				// run
//				// <code>ExtendedImageRegistry.getImage()</code> on the UI
//				// thread,
//				// since the only reason this was needed was to lazy load the
//				// registry.
//				ExtendedImageRegistry.getInstance();
//			}
//		});
//
//		// ServiceRegistry.INSTANCE.registerService(PackageTreeStatefulService.SERVICE_ID,
//		// new PackageTreeStatefulService());
//
//		// initializeEditorSupport();

		server = new FlowerJettyServer();
		server.start();
		
		CommunicationPlugin.getInstance().getAllServicesStartedListeners().add(new Runnable() {
			@Override
			public void run() {
				CodeSyncPlugin.getInstance().setProjectsProvider(new IdeaProjectAccessController());
				EditorPlugin.getInstance().setFileAccessController(new IdeaFileAccessController());
				EditorModelPlugin.getInstance().setModelAccessController(new IdeaModelAccessController());
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		IdeaPlugin.context = null;
	}
	
	public FlowerJettyServer getServer() {
		return server;
	}

}
