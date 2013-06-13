package org.flowerplatform.web.security.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import sun.misc.BASE64Encoder;

import org.flowerplatform.web.entity.Entity;
import org.flowerplatform.web.entity.dto.Dto;

/** 
 * Utility class.
 * 
 * @author Cristi
 * @author Cristina
 *  
 * @flowerModelElementId _HIg8kFcxEeG6S8FiFZ8nVA
 */
public class Util {

	/**
	 * Flag returned by some methods that modify data
	 * from {@link OrganizationService}, {@link GroupService} 
	 * and {@link UserService}. Because these entities share the same screen, this flag indicates
	 * that the corresponding table needs to be updated (or not) after certain operations (modify,
	 * delete).
	 *    
	 * @flowerModelElementId _IT2HQFcxEeG6S8FiFZ8nVA
	 */
//	public static final int REQUEST_ORGANIZATIONS_UPDATE = 1;
	
	/**
	 * Flag returned by some methods that modify data
	 * from {@link OrganizationService}, {@link GroupService} 
	 * and {@link UserService}. Because these entities share the same screen, this flag indicates
	 * that the corresponding table needs to be updated (or not) after certain operations (modify,
	 * delete).
	 *    
	 * @flowerModelElementId _gyPv0FcxEeG6S8FiFZ8nVA
	 */
//	public static final int REQUEST_GROUPS_UPDATE = 2;
	
	/**
	 * Flag returned by some methods that modify data
	 * from {@link OrganizationService}, {@link GroupService} 
	 * and {@link UserService}. Because these entities share the same screen, this flag indicates
	 * that the corresponding table needs to be updated (or not) after certain operations (modify,
	 * delete).
	 *    
	 * @flowerModelElementId _jg8KsFcxEeG6S8FiFZ8nVA
	 */
//	public static final int REQUEST_USERS_UPDATE = 4;

	/**
	 * Given two lists, one containing the original entities and 
	 * the other containing the modified data transfer objects, returns an array of two lists:
	 * <ul>
	 * 	<li> result[0] - a list with ids found in modified list but not in the original list (added entities)
	 * 	<li> result[1] - a list with ids found in original list but not in the modified list (removed entities)
	 * </ul>
	 * 
	 * @flowerModelElementId _09SFwFcxEeG6S8FiFZ8nVA
	 */
	@SuppressWarnings("unchecked")
	public static List<Long>[] getAddedRemovedElements(Collection<? extends Entity> originalEntities, Collection<? extends Dto> modifiedDtos) {
		HashMap<Long, Entity> list1 = new HashMap<Long, Entity>();
		List<Long> list2 = new java.util.ArrayList<Long>();
		
		if (originalEntities != null) {
			for (Entity entity : originalEntities) {
				list1.put(entity.getId(), entity);
			}
		}
		
		if (modifiedDtos != null) {
			for (Dto dto : modifiedDtos) {
				if (list1.get(dto.getId()) != null) {
					list1.remove(dto.getId());
				} else {
					list2.add(dto.getId());
				}
			}
		}
		List<Long>[] result = new ArrayList[2];
		result[0] = list2;
		result[1] = new ArrayList<Long>(list1.keySet());
		
		return result;
	}
	
	/**
	 * Encrypts the given <code>text</code> using the SHA mechanism.
	 * @flowerModelElementId _QUZi8V34EeGwLIVyv_iqEg
	 */
	public static String encrypt(String text) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		try {
			md.update(text.getBytes("UTF-8")); 
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

		byte raw[] = md.digest(); 
		String hash = (new BASE64Encoder()).encode(raw);
		return hash;
	}
}