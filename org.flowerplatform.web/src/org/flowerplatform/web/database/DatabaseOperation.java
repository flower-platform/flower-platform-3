package org.flowerplatform.web.database;

/**
 * @author Mariana
 */
public abstract class DatabaseOperation implements Runnable {
	
	/**
	 * Used to call find, merge or delete methods from this runnable.
	 */
	protected DatabaseOperationWrapper wrapper;
	
}
