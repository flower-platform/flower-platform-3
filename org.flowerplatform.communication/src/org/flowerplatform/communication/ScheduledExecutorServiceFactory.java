package org.flowerplatform.communication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Keeps a reference to all created <code>ScheduledExecutorService</code>s.
 * 
 * When the server stops it will ensure that these <code>ScheduledExecutorService</code>s
 * are also shutdown.
 * 
 * @author Sorin
 */
public class ScheduledExecutorServiceFactory {
	
	private Collection<ScheduledExecutorService> executorServices = Collections.synchronizedList(new ArrayList<ScheduledExecutorService>());

	public ScheduledExecutorService createScheduledExecutorService() {
		ScheduledExecutorService executorService =  Executors.newScheduledThreadPool(1);
		executorServices.add(executorService);
		return executorService;
	}

	public void dispose() {
		for (ScheduledExecutorService executorService : executorServices) { 
			executorService.shutdownNow();
		}
	}
}
