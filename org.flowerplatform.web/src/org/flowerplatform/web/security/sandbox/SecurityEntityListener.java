package org.flowerplatform.web.security.sandbox;

import java.security.Policy;
import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.common.util.RunnableWithParam;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.web.entity.Entity;
import org.flowerplatform.web.entity.ISecurityEntity;
import org.flowerplatform.web.entity.PermissionEntity;
import org.flowerplatform.web.entity.User;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.event.spi.PreDeleteEvent;
import org.hibernate.event.spi.PreDeleteEventListener;

/**
 * Responsibility of this class is to listen for modifications 
 * of entities like permissions, users, groups and organizations
 * and to clear the caches of {@link FlowerWebPolicy} accordingly. 
 * 
 * @author Cristi
 * @author Florin
 * @author Mariana
 * 
 * @flowerModelElementId _o9bbIGRvEeGyd4yTk74SKw
 */
public class SecurityEntityListener implements 
	PostInsertEventListener, PostDeleteEventListener, PostUpdateEventListener, PreDeleteEventListener {

	private static final long serialVersionUID = 1377271602960613446L;

	/**
	 * @flowerModelElementId _Z-8r0HgGEeGtTo1wOb4S9A
	 */
	@Override
	public void onPostInsert(PostInsertEvent evt) {
		Object entity = evt.getEntity();
		if (!(Policy.getPolicy() instanceof FlowerWebPolicy)) {
			return;
		}
		if (!(entity instanceof PermissionEntity)) {
			return;
		}
		FlowerWebPolicy policy = (FlowerWebPolicy) Policy.getPolicy();
		policy.updateCachesFor((PermissionEntity) entity);
	}
	
	/**
	 * When an ISecurityEntity or a PermissionEntity is updated or deleted, the
	 * policy needs to properly clear its caches.
	 * 
	 * @author Florin
	 * @author Cristi
	 * @flowerModelElementId _reYU8GRvEeGyd4yTk74SKw
	 */
	@Override
	public void onPostUpdate(PostUpdateEvent evt) {		
		if (evt.getEntity() instanceof User) {
			final Entity entity = (Entity) evt.getEntity();
			// when user is deleted we do not clear it from the principal, because the can still be logged on
			
			CommunicationPlugin.getInstance().getCommunicationChannelManager().iterateCommunicationChannels(new RunnableWithParam<Boolean, CommunicationChannel>() {

				@Override
				public Boolean run(CommunicationChannel param) {
					if (param.getPrincipal() != null && param.getPrincipal().getUserId() == entity.getId()) {
						param.getPrincipal().clearCachedUser();
					}
					return null;
				}
				
			});
			
		}
		
		entityUpdatedOrDeleted(evt.getEntity());
	}
	
	/**
	 * @author Florin
	 * @author Cristi
	 * @return 
	 * @flowerModelElementId _pufnAIMAEeG2LdcUX32kBA
	 */
	@Override
	public boolean onPreDelete(PreDeleteEvent evt) {	
		if (evt.getEntity() instanceof User) {
			final Entity entity = (Entity) evt.getEntity();
			if (CommunicationPlugin.getInstance().getCommunicationChannelManager() != null) { // condition for junit tests
				final List<CommunicationChannel> clientsToDisconnect = new ArrayList<CommunicationChannel>();
				
				CommunicationPlugin.getInstance().getCommunicationChannelManager().iterateCommunicationChannels(new RunnableWithParam<Boolean, CommunicationChannel>() {

					@Override
					public Boolean run(CommunicationChannel param) {
						if (param.getPrincipal().getUserId() == entity.getId()) {
							clientsToDisconnect.add((CommunicationChannel) param);
						}
						return null;
					}
		
				});
				
				// we do this here, after the loop, to avoid ConcurrentModifException
				for (CommunicationChannel channel : clientsToDisconnect) {
					channel.disconnect();
				}
			}
		}
		return false;
	}
	
	/**
	 * @flowerModelElementId _YN6VQHl1EeG7_fzMWZxiHA
	 */
	@Override
	public void onPostDelete(PostDeleteEvent evt) {				
		entityUpdatedOrDeleted(evt.getEntity());
	}
	
	/**
	 * @flowerModelElementId _YN68U3l1EeG7_fzMWZxiHA
	 */
	private void entityUpdatedOrDeleted(Object entity) {
		if (!(Policy.getPolicy() instanceof FlowerWebPolicy)) {
			return;
		}
		FlowerWebPolicy policy = (FlowerWebPolicy) Policy.getPolicy();		
		if (entity instanceof ISecurityEntity) {	
			policy.updateCachesFor((ISecurityEntity) entity);	
		} else if (entity instanceof PermissionEntity) {
			policy.updateCachesFor((PermissionEntity) entity);
		}
	}

}