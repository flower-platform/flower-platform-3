package com.crispico.flower.mp.model.codesync.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * Used along with {@link EclipseObjectOutputStream}, to retrieve classes from different bundles.
 * 
 * @author Mariana
 */
public class EclipseObjectInputStream extends ObjectInputStream {

	public EclipseObjectInputStream(InputStream in) throws IOException {
		super(in);
	}

	/**
	 * Loads the class described by <code>desc</code>, using the bundle at the location written in the stream.
	 * 
	 * @see EclipseObjectOutputStream#annotateClass()
	 */
	@Override
	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        try {
        	String location = readUTF();
    		Bundle bundle = FrameworkUtil.getBundle(this.getClass()).getBundleContext().getBundle(location);
    		if (bundle != null) {
	    		String name = desc.getName();
	    		return bundle.loadClass(name);
    		} else {
    			return super.resolveClass(desc);
    		}
        } catch (ClassNotFoundException ex) {
        	return super.resolveClass(desc);
        }
	}

}
