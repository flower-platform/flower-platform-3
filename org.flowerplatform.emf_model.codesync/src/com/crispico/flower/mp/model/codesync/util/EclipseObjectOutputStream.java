package com.crispico.flower.mp.model.codesync.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * Used along with {@link EclipseObjectInputStream}, to retrieve classes from different bundles.
 * 
 * @author Mariana
 */
public class EclipseObjectOutputStream extends ObjectOutputStream {

	public EclipseObjectOutputStream(ByteArrayOutputStream output) throws IOException {
		super(output);
	}

	/**
	 * Writes the bundle location for the class, to avoid <code>ClassNotFoundException</code>s
	 * at deserialization.
	 * 
	 * @see EclipseObjectInputStream#resolveClass()
	 */
	@Override
	protected void annotateClass(Class<?> cl) throws IOException {
		super.annotateClass(cl);
		
		Bundle declaringBundle = FrameworkUtil.getBundle(cl);
		if (declaringBundle == null) {
			declaringBundle = FrameworkUtil.getBundle(this.getClass());
		}
		this.writeUTF(declaringBundle.getLocation()); 
	}
	
}
