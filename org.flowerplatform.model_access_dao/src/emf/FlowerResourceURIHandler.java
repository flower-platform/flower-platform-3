package emf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.emf.ecore.resource.impl.URIHandlerImpl;
import org.flowerplatform.model_access_dao.DAOFactory;
import org.flowerplatform.model_access_dao.RegistryDAO;


/**
 * Delegates to a platform-dependent URI handler.
 * 
 * @author Mariana Gheorghe
 */
public class FlowerResourceURIHandler extends URIHandlerImpl {

	public static final String SCHEME_FP_REPO = "fp-repo";
	
	public static final String OPTION_REPO = "repoId";
	public static final String OPTION_DISCUSSABLE_DESIGN = "discussableDesignId";

	protected URIHandler platformDependentHandler;

	public FlowerResourceURIHandler(URIHandler handler) {
		this.platformDependentHandler = handler;
	}

	/**
	 * @return fp-repo:resourceId
	 */
	public static URI createFlowerResourceURI(String resourceId) {
		return URI.createGenericURI(SCHEME_FP_REPO, resourceId, null);
	}

	/**
	 * @return fp-repo:resourceId#fragment
	 */
	public static URI createFlowerResourceURI(String resourceId, String fragment) {
		if (resourceId.startsWith("/")) {
			resourceId = resourceId.substring(1);
		}
		return URI.createGenericURI(SCHEME_FP_REPO, resourceId, fragment);
	}

	@Override
	public boolean canHandle(URI uri) {
		return SCHEME_FP_REPO.equals(uri.scheme());
	}

	@Override
	public OutputStream createOutputStream(URI uri, Map<?, ?> options) throws IOException {
		uri = convertToPlatformDependentURI(uri, options);
		return platformDependentHandler.createOutputStream(uri, options);
	}

	@Override
	public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException {
		uri = convertToPlatformDependentURI(uri, options);
		return platformDependentHandler.createInputStream(uri, options);
	}

	@Override
	public void delete(URI uri, Map<?, ?> options) throws IOException {
		uri = convertToPlatformDependentURI(uri, options);
		platformDependentHandler.delete(uri, options);
	}

	@Override
	public boolean exists(URI uri, Map<?, ?> options) {
		uri = convertToPlatformDependentURI(uri, options);
		return platformDependentHandler.exists(uri, options);
	}

	@Override
	public Map<String, ?> getAttributes(URI uri, Map<?, ?> options) {
		uri = convertToPlatformDependentURI(uri, options);
		return platformDependentHandler.getAttributes(uri, options);
	}

	@Override
	public void setAttributes(URI uri, Map<String, ?> attributes, Map<?, ?> options) throws IOException {
		uri = convertToPlatformDependentURI(uri, options);
		platformDependentHandler.setAttributes(uri, attributes, options);
	}

	@Override
	public Map<String, ?> contentDescription(URI uri, Map<?, ?> options) throws IOException {
		uri = convertToPlatformDependentURI(uri, options);
		return platformDependentHandler.contentDescription(uri, options);
	}

	/**
	 * Uses the registered {@link RegistryDAO} to find the platform-dependent URI.
	 * 
	 * @param uri FP URI: {@code fpRepo:resourceUUID}
	 * @param options
	 * @return platform-dependent URI that can be handled by the {@link #platformDependentHandler}
	 */
	protected URI convertToPlatformDependentURI(URI uri, Map<?, ?> options) {
		String resourceId = uri.opaquePart();
		uri = DAOFactory.registryDAO.getResource(
				(String) options.get(OPTION_REPO), 
				(String) options.get(OPTION_DISCUSSABLE_DESIGN), 
				resourceId);
		return uri;
	}

}