package com.crispico.flower.mp.codesync.code.java;

import org.eclipse.jdt.internal.core.CompilationUnit;

import com.crispico.flower.mp.codesync.code.IResourceFullyQualifiedNameProvider;

/**
 * @author Mariana
 */
public class JavaFullyQualifiedNameProvider extends IResourceFullyQualifiedNameProvider {

	@Override
	public String getFullyQualifiedName(Object object) {
		String fqName = super.getFullyQualifiedName(object);
		if (fqName != null) {
			return fqName;
		}
		// TODO Mariana : add support for JE
		if (object instanceof CompilationUnit) {
			return String.copyValueOf((((CompilationUnit) object).getFileName()));
		}
		return null;
	}

}
