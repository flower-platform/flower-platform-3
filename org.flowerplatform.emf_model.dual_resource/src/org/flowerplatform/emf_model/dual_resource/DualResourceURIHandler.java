package org.flowerplatform.emf_model.dual_resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.emf.ecore.resource.impl.URIHandlerImpl;

/**
 * Handles dual resource URIs by delegating to a platform-dependent implementation.
 * 
 * @author Mariana Gheorghe
 */
public class DualResourceURIHandler extends URIHandlerImpl {

	public static final String SCHEME_DUAL_RESOURCE = "dual_resource";
	
	public static final String BASE_PATH = "basePath";
	
	protected URIHandler platformDependentHandler;
	
	public DualResourceURIHandler(URIHandler handler) {
		this.platformDependentHandler = handler;
	}
	
	/**
	 * @return dual_resource:mapping.notation
	 */
	public static URI createDualResourceURI() {
		return URI.createGenericURI(SCHEME_DUAL_RESOURCE, MAPPING_FILE_NAME, null);
	}
	
	/**
	 * @return dual_resource:opaquePart#fragment
	 */
	public static URI createDualResourceURI(String opaquePart, String fragment) {
		if (opaquePart.startsWith("/")) {
			opaquePart = opaquePart.substring(1);
		}
		return URI.createGenericURI(SCHEME_DUAL_RESOURCE, opaquePart, fragment);
	}
	
	/**
	 * @return dual_resource:basePath:uri#fragment
	 */
	public static URI createDualResourceURI(URI baseUri, URI uri) {
		String basePath = getOpaquePart(baseUri);
		String opaquePart = getOpaquePart(uri);
		String fragment = uri.fragment();
		return createDualResourceURI(basePath + ":" + opaquePart, fragment);
	}
	
	protected static String getOpaquePart(URI uri) {
		if (isDualResourceURI(uri)) {
			return uri.opaquePart();
		}
		if (uri.isPlatformResource()) {
			return uri.toPlatformString(true);
		}
		if (uri.isFile()) {
			return uri.toFileString();
		}
		throw new RuntimeException("Cannot handle URI " + uri);
	}
	
	public static String getBasePathFromURI(URI uri) {
		String opaquePart = getOpaquePart(uri);
		if (opaquePart.contains(":")) {
			return opaquePart.split(":")[0];
		}
		throw new RuntimeException("Dual resource with URI " + uri + " must have a base path");
	}
	
	public static URI removeBasePathFromURI(URI uri) {
		String opaquePart = getOpaquePart(uri);
		if (!opaquePart.contains(":")) {
			return uri;
		} else {
			String[] split = opaquePart.split(":");
			opaquePart = split[1];
			return createDualResourceURI(opaquePart, uri.fragment());
		}
	}
	
	public static IResource getPlatformDependentResource(URI uri) {
		String basePath = getBasePathFromURI(uri);
		return ResourcesPlugin.getWorkspace().getRoot().findMember(basePath);
	}
	
	public static boolean isDualResourceURI(URI uri) {
		return SCHEME_DUAL_RESOURCE.equals(uri.scheme());
	}
	
	@Override
	public boolean canHandle(URI uri) {
		return isDualResourceURI(uri);
	}

	@Override
	public OutputStream createOutputStream(URI uri, Map<?, ?> options) throws IOException {
		uri = convertToPlatformDependentURI(uri);
		return platformDependentHandler.createOutputStream(uri, options);
	}

	@Override
	public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException {
		uri = convertToPlatformDependentURI(uri);
		return platformDependentHandler.createInputStream(uri, options);
	}

	@Override
	public void delete(URI uri, Map<?, ?> options) throws IOException {
		uri = convertToPlatformDependentURI(uri);
		platformDependentHandler.delete(uri, options);
	}

	@Override
	public boolean exists(URI uri, Map<?, ?> options) {
		uri = convertToPlatformDependentURI(uri);
		return platformDependentHandler.exists(uri, options);
	}

	@Override
	public Map<String, ?> getAttributes(URI uri, Map<?, ?> options) {
		uri = convertToPlatformDependentURI(uri);
		return platformDependentHandler.getAttributes(uri, options);
	}

	@Override
	public void setAttributes(URI uri, Map<String, ?> attributes, Map<?, ?> options) throws IOException {
		uri = convertToPlatformDependentURI(uri);
		platformDependentHandler.setAttributes(uri, attributes, options);
	}

	@Override
	public Map<String, ?> contentDescription(URI uri, Map<?, ?> options) throws IOException {
		uri = convertToPlatformDependentURI(uri);
		return platformDependentHandler.contentDescription(uri, options);
	}
	
	public static final String MAPPING_FILE_NAME = "mapping.notation";
	
	public static final String GLOBAL_MAPPING_FILE_PATH = ".flower-platform/mapping.notation";
	
	protected URI convertToPlatformDependentURI(URI uri) {
		IResource resource = getPlatformDependentResource(uri);
		
		// go up until we find a mapping file or the global mapping file
		String mappingResourcePath = null;
		IContainer parent = resource.getParent();
		while (parent != null) {
			IResource candidate = parent.findMember(MAPPING_FILE_NAME);
			if (candidate != null) {
				mappingResourcePath = candidate.getFullPath().toString();
				break;
			} else {
				candidate = parent.findMember(GLOBAL_MAPPING_FILE_PATH);
				if (candidate != null) {
					mappingResourcePath = candidate.getFullPath().toString();
					break;
				}
			}
			parent = parent.getParent();
		}
		
		return URI.createPlatformResourceURI(mappingResourcePath, true);
	}
	
}
