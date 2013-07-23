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
// This is a test file for the ActionScript partitioner and partition tokenizer.

class interface package function var 

as break .case this_should_not_be_colored catch const continue
default delete do else extends false
finally for if this_should_not_be_colored implements import in
instanceof (internal is, native new null
private. protected this_should_not_be_colored public return
super {switch this_should_not_be_colored [this throw to true
try typeof use void while with each]
get set this_should_not_be_colored namespace include dynamic final
native override static

// metadata tags
[Alternative [ArrayElementType [Bindable( 
[DefaultProperty [this_should_not_be_colored [Deprecated [Effect [Embed 
[Event [Exclude( [this_should_not_be_colored [ExcludeClass(  [HostComponent
[IconFile [Inspectable( [InstanceType 
[NonCommittingChangeEvent [RemoteClass [RichTextContent 
[SkinPart [this_should_not_be_colored [SkinState [Style [SWF [Transient

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