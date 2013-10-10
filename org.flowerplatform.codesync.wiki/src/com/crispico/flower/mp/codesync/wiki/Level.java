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
package com.crispico.flower.mp.codesync.wiki;

/**
 * @author Mariana Gheorghe
 */
public enum Level {

	FOLDER,
	FILE,
	FLOWER_BLOCK(100),
	PARAGRAPH	(100),
	HEADING_1	(1),
	HEADING_2	(2),
	HEADING_3	(3),
	HEADING_4	(4),
	HEADING_5	(5),
	HEADING_6	(6),
	LIST		(10),
	BLOCKQUOTE	(10),
	CODE		(10),
	LIST_ITEM,
	BLOCKQUOTE_CHILD,
	CODE_LINE;
	
	private int level = 0;

	private Level() {
		// nothing to do
	}
	
	private Level(int level) {
		this.level = level;
	}
	
	public boolean acceptChild(Level candidate) {
		switch (this) {
		case FOLDER:
			return candidate == FILE || candidate == FOLDER;

		case FILE:
			return candidate != FOLDER && candidate != LIST_ITEM && candidate != BLOCKQUOTE_CHILD;

		case HEADING_1:
		case HEADING_2:
		case HEADING_3:
		case HEADING_4:
		case HEADING_5:
		case HEADING_6:
			return candidate.level > this.level;
			
		case LIST:
			return candidate == LIST_ITEM;
			
		case BLOCKQUOTE:
			return candidate == BLOCKQUOTE_CHILD;
			
		case CODE:
			return candidate == CODE_LINE;
		default:
			return false;
		}
	}
	
	public static Level getHeading(int level) {
		switch (level) {
		case 1:
			return HEADING_1;
		case 2:
			return HEADING_2;
		case 3:
			return HEADING_3;
		case 4:
			return HEADING_4;
		case 5:
			return HEADING_5;
		case 6:
			return HEADING_6;
		default:
			throw new RuntimeException("Cannot have a heading of level " + level);
		}
	}
}
