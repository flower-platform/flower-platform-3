package org.flowerplatform.web;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

/**
 * Dummy application to convince the IDE to generate and run the associated
 * run configuration. Not used during runtime.
 * 
 * @author Cristian Spiescu
 */
public class DummyApplication implements IApplication {

	@Override
	public Object start(IApplicationContext context) throws Exception {
		System.out.println("Application.start()");
		Thread.sleep(5000);
		return null;
	}

	@Override
	public void stop() {
	}

}
