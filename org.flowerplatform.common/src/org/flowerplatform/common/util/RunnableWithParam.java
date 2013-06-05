package org.flowerplatform.common.util;

import java.util.concurrent.Callable;

/**
 * Callback similar to {@link Runnable} or {@link Callable}, 
 * that takes a param and returns a value.
 * 
 * @author Cristi
 * @param <R> The type of the result. Can be {@link Void} if not used.
 * @param <P> The type of the parameter. Can be {@link Void} if not used.
 */
public interface RunnableWithParam<R, P> {
	
	public R run(P param);

}
