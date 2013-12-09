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
package org.flowerplatform.codesync.regex;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.flowerplatform.codesync.regex.remote.MacroRegexDto;
import org.flowerplatform.codesync.regex.remote.ParserRegexDto;
import org.flowerplatform.codesync.regex.remote.RegexActionDto;
import org.flowerplatform.codesync.regex.remote.RegexMatchDto;
import org.flowerplatform.codesync.regex.remote.RegexSelectedItem;
import org.flowerplatform.codesync.regex.remote.RegexSubMatchDto;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.emf_model.regex.Root;
import org.flowerplatform.properties.providers.IPropertiesProvider;
import org.flowerplatform.properties.remote.Property;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;

/**
 * @author Cristina Constantinescu
 */
public class RegexPropertiesProvider implements IPropertiesProvider<RegexSelectedItem, Object> {

	public static final String NAME = "Name";
	public static final String REGEX = "Regex";
	public static final String REGEX_FULL = "Regex Full";
	public static final String REGEX_MACROS = "Regex Macros";
	public static final String START = "Start";
	public static final String END = "End";
	public static final String ACTION = "Action";
	public static final String EXTENSIONS = "Extensions";
	
	public static final String REGEX_CONFIG_TYPE = "regex_config";	
	public static final String REGEX_PARSER_TYPE = "regex_parser";	
	public static final String REGEX_MACRO_TYPE = "regex_macro";	
	public static final String REGEX_MATCH_TYPE = "regex_match";	
	
	@Override
	public boolean setProperty(ServiceInvocationContext context, RegexSelectedItem selectedItem, String propertyName, Object propertyValue) {	
		CommunicationChannel channel = context.getCommunicationChannel();
		String config = selectedItem.getConfig();
		switch (selectedItem.getItemType()) {
			case REGEX_CONFIG_TYPE:
				switch (propertyName) {
					case NAME:
						RegexService.getInstance().changeRegexConfigName(channel, config, (String) propertyValue);
						break;
					case EXTENSIONS:
						RegexService.getInstance().changeRegexConfigExtensions(channel, config, (String) propertyValue);	
						break;
				}				
				return true;
			case REGEX_MACRO_TYPE:
				switch (propertyName) {
					case NAME:
						RegexService.getInstance().changeMacroRegexName(channel, config, selectedItem.getRegex(), (String) propertyValue);
						break;
					case REGEX:
						RegexService.getInstance().changeMacroRegexRegex(channel, config, selectedItem.getRegex(), (String) propertyValue);	
						break;
				}		
				return true;
			case REGEX_PARSER_TYPE:
				switch (propertyName) {
					case NAME:
						RegexService.getInstance().changeParserRegexName(channel, config, (ParserRegexDto) selectedItem.getRegex(), (String) propertyValue);
						break;
					case REGEX_MACROS:
						RegexService.getInstance().changeParserRegexRegex(channel, config, (ParserRegexDto) selectedItem.getRegex(), (String) propertyValue);	
						break;
					case ACTION:
						RegexService.getInstance().changeParserRegexAction(channel, config,	(ParserRegexDto) selectedItem.getRegex(), ((RegexActionDto) propertyValue).getName());
						break;
				}			
				return true;
		}		
		return false;
	}

	@Override
	public List<String> getPropertyNames(RegexSelectedItem selectedItem, Object resolvedSelectedItem) {
		List<String> properties = new ArrayList<String>();	
		
		switch (selectedItem.getItemType()) {
			case REGEX_CONFIG_TYPE:
				properties.add(NAME);
				properties.add(EXTENSIONS);
				break;
			case REGEX_MACRO_TYPE:
				properties.add(NAME);
				properties.add(REGEX);	
				break;
			case REGEX_PARSER_TYPE:
				properties.add(NAME);
				properties.add(REGEX_MACROS);
				properties.add(REGEX_FULL);
				properties.add(ACTION);
				break;
			case REGEX_MATCH_TYPE :
				RegexMatchDto match = selectedItem.getMatch();
				
				properties.add(REGEX_MACROS);
				properties.add(REGEX_FULL);
				properties.add(START);
				properties.add(END);
				
				if (match.getSubMatches() != null) {
					for (RegexSubMatchDto subMatch : match.getSubMatches()) {
						properties.add(String.format("$%d", match.getSubMatches().indexOf(subMatch) + 1));
					}
				}
				break;
		}		
		return properties;
	}

	@Override
	public Property getProperty(RegexSelectedItem selectedItem,	Object resolvedSelectedItem, String propertyName) {
		switch (selectedItem.getItemType()) {
			case REGEX_CONFIG_TYPE:
				switch (propertyName) {
					case NAME:
						return new Property().setName(NAME).setValue(selectedItem.getConfig()).setReadOnly(false);				
					case EXTENSIONS:
						Root root = RegexService.getInstance().getRoot(selectedItem.getConfig());					
						return new Property().setName(EXTENSIONS).setValue(StringUtils.join(root.getExtensions().toArray(), ",")).setReadOnly(false);
				}
				break;
			case REGEX_MACRO_TYPE:
				MacroRegexDto regex = selectedItem.getRegex();
				
				switch (propertyName) {
					case NAME:
						return new Property().setName(NAME).setValue(regex.getName()).setReadOnly(false);				
					case REGEX:
						return new Property().setName(REGEX).setValue(regex.getRegex()).setReadOnly(false);						
				}			
				break;
			case REGEX_PARSER_TYPE:
				ParserRegexDto parserRegex = (ParserRegexDto) selectedItem.getRegex();
				
				switch (propertyName) {
					case NAME:
						return new Property().setName(NAME).setValue(parserRegex.getName()).setReadOnly(false);				
					case REGEX_MACROS:
						return new Property().setName(REGEX_MACROS).setValue(parserRegex.getRegex()).setReadOnly(false);								
					case REGEX_FULL:
						return new Property().setName(REGEX_FULL).setValue(RegexService.getInstance().getFullRegex(selectedItem.getConfig(), parserRegex.getRegex()));
					case ACTION:
						return new Property().setName(ACTION).setValue(parserRegex.getAction()).setReadOnly(false).setType("DropDownListWithRegexActions");
				}	
				break;
			case REGEX_MATCH_TYPE:
				RegexMatchDto match = selectedItem.getMatch();
				
				switch (propertyName) {
					case REGEX_MACROS:
						return new Property().setName(REGEX_MACROS).setValue(match.getParserRegex().getRegex());
					case REGEX_FULL:
						return new Property().setName(REGEX_FULL).setValue(RegexService.getInstance().getFullRegex(selectedItem.getConfig(), match.getParserRegex().getRegex()));					
					case START:
						return new Property().setName(START).setValue(match.getStart().toString());					
					case END:
						return new Property().setName(END).setValue(match.getEnd().toString());								
				}
				if (match.getSubMatches() != null) {
					int index = Integer.parseInt(propertyName.substring(1));
					return new Property().setName(propertyName).setValue(match.getSubMatches().get(index - 1).getValue());				
				}
				break;
		}		
		return null;
	}

	@Override
	public Object resolveSelectedItem(RegexSelectedItem selectedItem) {
		return null;
	}

	@Override
	public Pair<String, String> getIconAndLabel(RegexSelectedItem selectedItem, Object resolvedSelectedItem) {
		switch (selectedItem.getItemType()) {
			case REGEX_CONFIG_TYPE:
				return new Pair<String, String>(CodeSyncPlugin.getInstance().getResourceUrl("images/regex/wrench.png"), selectedItem.getConfig());
			case REGEX_MACRO_TYPE:
				return new Pair<String, String>(CodeSyncPlugin.getInstance().getResourceUrl("images/regex/percent.png"), selectedItem.getRegex().getName());
			case REGEX_PARSER_TYPE:
				return new Pair<String, String>(CodeSyncPlugin.getInstance().getResourceUrl("images/regex/bricks.png"), selectedItem.getRegex().getName());
			case REGEX_MATCH_TYPE :
				return new Pair<String, String>(CodeSyncPlugin.getInstance().getResourceUrl("images/regex/brick.png"), selectedItem.getMatch().getParserRegex().getName());
		}		
		return null;
	}

	
}
