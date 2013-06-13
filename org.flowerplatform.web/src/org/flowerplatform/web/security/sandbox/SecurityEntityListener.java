package org.flowerplatform.web.security.sandbox;

import java.security.Policy;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PreRemove;

import org.flowerplatform.common.util.RunnableWithParam;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.web.entity.Entity;
import org.flowerplatform.web.entity.ISecurityEntity;
import org.flowerplatform.web.entity.PermissionEntity;
import org.flowerplatform.web.entity.User;

/**
 * Responsibility of this class is to listen for modifications 
 * of entities like permissions, users, groups and organizations
 * and to clear the caches of {@link FlowerWebPolicy} accordingly. 
 * 
 * @author Cristi
 * @author Florin
 * 
 * @flowerModelElementId _o9bbIGRvEeGyd4yTk74SKw
 */
public class SecurityEntityListener {

	/**
	 * @flowerModelElementId _Z-8r0HgGEeGtTo1wOb4S9A
	 */
	@PostPersist
	public void permissionEntityInserted(Entity entity) {
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
	@PostUpdate
	public void entityUpdated(final Entity entity) {		
		if (entity instanceof User) {
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
		
		entityUpdatedOrDeleted(entity);
	}
	
	/**
	 * @author Florin
	 * @author Cristi
	 * @flowerModelElementId _pufnAIMAEeG2LdcUX32kBA
	 */
	@PreRemove
	public void preRemoveEntity(final Entity entity) {	
		if (entity instanceof User) {
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
	}
	
	/**
	 * @flowerModelElementId _YN6VQHl1EeG7_fzMWZxiHA
	 */
	@PostRemove
	public void entityDeleted(Entity entity) {				
		entityUpdatedOrDeleted(entity);
	}
	
	/**
	 * @flowerModelElementId _YN68U3l1EeG7_fzMWZxiHA
	 */
	private void entityUpdatedOrDeleted(Entity entity) {
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