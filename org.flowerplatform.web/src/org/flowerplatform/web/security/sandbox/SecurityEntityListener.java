/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
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
 * 
 */
public class SecurityEntityListener implements 
	PostInsertEventListener, PostDeleteEventListener, PostUpdateEventListener, PreDeleteEventListener {

	private static final long serialVersionUID = 1377271602960613446L;

	/**
	 * 
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
	 * 
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
	 * 
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
	 * 
	 */
	@Override
	public void onPostDelete(PostDeleteEvent evt) {				
		entityUpdatedOrDeleted(evt.getEntity());
	}
	
	/**
	 * 
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