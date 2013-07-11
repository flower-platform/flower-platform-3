package org.flowerplatform.communication.tree;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.flowerplatform.communication.tree.remote.AbstractTreeStatefulService;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;


/**
 * Represents the context of a tree subscribed in {@link GenericTreeStatefulService}.
 * <p>
 * A tree can have 2 types of context:
 * <ul>
 * 	<li> client context represents the context sent from client to execute a server operation on a specific environment. <br>
 * 	Corresponds to <code>GenericTree.context</code> on client side. <br>
 *  Example: get whole tree data, expand/select specific node
 * 	
 * <li> stateful context represents the context that persists per stateful client life cycle. <br>
 * 	Changed by client only when executing {@link GenericTreeStatefulService#updateTreeStatefulContext()}. <br>
 *  Example: as in SVN/GIT Repositories: shows tree data based on a combo list selection 
 * (it changes only when client selects another item from list).
 * </ul>
 * 
 * <p>
 * This class is used only to store these 2 contexts and to get data from them when necessary.<br>
 * The <code>clientContext</code> has priority when searching for a specific key.
 * 
 * @author Cristina
 */
public class GenericTreeContext implements Map<Object, Object> {

	public static final String SERVICE_KEY = "service";
	
	private Map<Object, Object> clientContext;
	
	private Map<Object, Object> statefulContext;
		
	public GenericTreeContext(AbstractTreeStatefulService service) {
		getStatefulContext().put(SERVICE_KEY, service);
	}

	public Map<Object, Object> getClientContext() {
		if (clientContext == null) {
			clientContext = new HashMap<Object, Object>();
		}
		return clientContext;
	}

	public void setClientContext(Map<Object, Object> clientContext) {	
		this.clientContext = clientContext;
	}

	public Map<Object, Object> getStatefulContext() {
		if (statefulContext == null) {
			statefulContext = new HashMap<Object, Object>();
		}
		return statefulContext;
	}

	public void setStatefulContext(Map<Object, Object> statefulContext) {
		this.statefulContext = statefulContext;
	}

	public boolean containsKey(Object key) {
		if (clientContext != null && clientContext.containsKey(key)) {
			return true;
		}
		if (statefulContext != null && statefulContext.containsKey(key)) {
			return true;
		}
		return false;
	}

	public Object get(Object key) {
		if (clientContext != null && clientContext.containsKey(key)) {
			return clientContext.get(key);
		}
		if (statefulContext != null && statefulContext.containsKey(key)) {
			return statefulContext.get(key);
		}
		return null;
	}
	
	public void clear() {
		throw new UnsupportedOperationException();
	}
	
	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}

	public Set<java.util.Map.Entry<Object, Object>> entrySet() {
		throw new UnsupportedOperationException();
	}

	public boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	public Set<Object> keySet() {
		throw new UnsupportedOperationException();
	}

	public Object put(Object key, Object value) {
		throw new UnsupportedOperationException();
	}

	public void putAll(Map<? extends Object, ? extends Object> m) {
		throw new UnsupportedOperationException();
	}

	public Object remove(Object key) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		throw new UnsupportedOperationException();
	}

	public Collection<Object> values() {
		throw new UnsupportedOperationException();
	}

}
