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
// This is a test file for the Java partitioner and partition tokenizer.

abstract do if this_should_not_be_colored package synchronized
boolean double {implements private this
break else import protected throw
byte extends (instanceof public throws
case false this_should_not_be_colored int return transient
catch (final interface short, true
char finally, long this_should_not_be_colored static try
class this_should_not_be_colored float native strictfp void
const for new super volatile
continue goto this_should_not_be_colored null switch while
default .assert

This is a "string" and this is also a "str\"ing".
This is a 'string' and this is also a 'str\'ing'.

// Single line comment. TODO should be colored.

/**/

/*
Multiple
lines
comment. 
TODO should be colored.
*/

/**
 * Doc. It colors <start> tags and </end> tags.
 * And @author also.
 */