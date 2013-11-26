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

import org.flowerplatform.codesync.regex.ide.RegexService;
import org.flowerplatform.codesync.regex.ide.remote.RegexMatchDto;
import org.flowerplatform.codesync.regex.ide.remote.RegexSelectedItem;
import org.flowerplatform.codesync.regex.ide.remote.RegexSubMatchDto;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.emf_model.regex.MacroRegex;
import org.flowerplatform.emf_model.regex.ParserRegex;
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
		
	@Override
	public boolean setProperty(ServiceInvocationContext context, RegexSelectedItem selectedItem, String propertyName, Object propertyValue) {		
		if (selectedItem.getItemType().equals("regex_config")) {
			RegexService.getInstance().renameConfig(
					context.getCommunicationChannel(), 
					selectedItem.getConfig(), 
					(String) propertyValue);			
			return true;
		} else if (selectedItem.getItemType().equals("regex_macro")) {
			if ("Name".equals(propertyName)) {
				RegexService.getInstance().changeMacroName(
						context.getCommunicationChannel(), 
						selectedItem.getConfig(), 
						selectedItem.getRegex(), 
						(String) propertyValue);	
			} else if ("Regex".equals(propertyName)) {
				RegexService.getInstance().changeMacroRegex(
						context.getCommunicationChannel(), 
						selectedItem.getConfig(), 
						selectedItem.getRegex(), 
						(String) propertyValue);	
			}
			return true;
		} else if (selectedItem.getItemType().equals("regex_parser")) {
			if ("Name".equals(propertyName)) {
				RegexService.getInstance().changeParserName(
						context.getCommunicationChannel(), 
						selectedItem.getConfig(), 
						(ParserRegex) selectedItem.getRegex(), 
						(String) propertyValue);	
			} else if ("RegEx Macros".equals(propertyName)) {
				RegexService.getInstance().changeParserRegex(
						context.getCommunicationChannel(), 
						selectedItem.getConfig(), 
						(ParserRegex) selectedItem.getRegex(), 
						(String) propertyValue);	
			} else if ("Action".equals(propertyName)) {
				RegexService.getInstance().changeParserAction(
						context.getCommunicationChannel(), 
						selectedItem.getConfig(), 
						(ParserRegex) selectedItem.getRegex(), 
						(String) propertyValue);	
			}
			return true;
		}
		return false;
	}

	@Override
	public List<String> getPropertyNames(RegexSelectedItem selectedItem, Object resolvedSelectedItem) {
		List<String> properties = new ArrayList<String>();	
		
		if (selectedItem.getItemType().equals("regex_match")) {
			RegexMatchDto match = ((RegexSelectedItem) selectedItem).getMatch();
			
			properties.add(REGEX_MACROS);
			properties.add(REGEX_FULL);
			properties.add(START);
			properties.add(END);
			
			if (match.getSubMatches() != null) {
				for (RegexSubMatchDto subMatch : match.getSubMatches()) {
					properties.add(String.format("$%d", match.getSubMatches().indexOf(subMatch) + 1));
				}
			}
		} else if (selectedItem.getItemType().equals("regex_config")) {
			properties.add(NAME);
		} else if (selectedItem.getItemType().equals("regex_macro")) {			
			properties.add(NAME);
			properties.add(REGEX);		
		} else if (selectedItem.getItemType().equals("regex_parser")) {			
			properties.add(NAME);
			properties.add(REGEX_MACROS);
			properties.add(REGEX_FULL);
			properties.add(ACTION);
		}
		
		return properties;
	}

	@Override
	public Property getProperty(RegexSelectedItem selectedItem,	Object resolvedSelectedItem, String propertyName) {
		if (selectedItem.getItemType().equals("regex_match")) {
			RegexMatchDto match = ((RegexSelectedItem) selectedItem).getMatch();
			
			switch (propertyName) {
				case REGEX_MACROS: {
					return new Property()
							.setName(REGEX_MACROS)
							.setValue(match.getParserRegex().getRegex())
							.setReadOnly(false);
				}
				case REGEX_FULL: {
					return new Property()
						.setName(REGEX_FULL)
						.setValue(match.getParserRegex().getFullRegex());
				}
				case START: {
					return new Property()
						.setName(START)
						.setValue(match.getStart());
				}
				case END: {
					return new Property()
						.setName(END)
						.setValue(match.getEnd());
				}			
			}
						
			if (match.getSubMatches() != null) {
				int index = Integer.parseInt(propertyName.substring(1));
				return new Property()
						.setName(propertyName)
						.setValue(match.getSubMatches().get(index).getValue());				
			}
		} else if (selectedItem.getItemType().equals("regex_config")) {
			return new Property()
					.setName(NAME)
					.setValue(selectedItem.getConfig())
					.setReadOnly(false);
		} else if (selectedItem.getItemType().equals("regex_macro")) {	
			MacroRegex regex = selectedItem.getRegex();
			
			switch (propertyName) {
				case NAME: {
					return new Property()
							.setName(NAME)
							.setValue(regex.getName())
							.setReadOnly(false);
				}
				case REGEX: {
					return new Property()
						.setName(REGEX)
						.setValue(regex.getRegex());
				}			
			}				
		} else if (selectedItem.getItemType().equals("regex_parser")) {			
			ParserRegex regex = (ParserRegex) selectedItem.getRegex();
			
			switch (propertyName) {
				case NAME: {
					return new Property()
							.setName(NAME)
							.setValue(regex.getName())
							.setReadOnly(false);
				}
				case REGEX_MACROS: {
					return new Property()
						.setName(REGEX_MACROS)
						.setValue(regex.getRegex())
						.setReadOnly(false);
				}					
				case REGEX_FULL: {
					return new Property()
						.setName(REGEX_FULL)
						.setValue(regex.getFullRegex());
				}
				case ACTION: {
					return new Property()
						.setName(ACTION)
						.setValue(regex.getAction())
						.setReadOnly(false);
				}
			}			
		}	
		
		return null;
	}

	@Override
	public Object resolveSelectedItem(RegexSelectedItem selectedItem) {
		return null;
	}

	@Override
	public Pair<String, String> getIconAndLabel(RegexSelectedItem selectedItem, Object resolvedSelectedItem) {
		if (selectedItem.getItemType().equals("regex_config")) {
			return new Pair<String, String>(CodeSyncPlugin.getInstance().getResourceUrl("images/regex/wrench.png"), selectedItem.getConfig());
		} else if (selectedItem.getItemType().equals("regex_macro")) {
			return new Pair<String, String>(CodeSyncPlugin.getInstance().getResourceUrl("images/regex/star.png"), selectedItem.getRegex().getName());
		} else if (selectedItem.getItemType().equals("regex_parser")) {
			return new Pair<String, String>(CodeSyncPlugin.getInstance().getResourceUrl("images/regex/bricks.png"), selectedItem.getRegex().getName());
		}
		return null;
	}

	
}
