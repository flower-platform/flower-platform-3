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
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.emf_model.regex.MacroRegex;
import org.flowerplatform.emf_model.regex.ParserRegex;
import org.flowerplatform.properties.providers.IPropertiesProvider;
import org.flowerplatform.properties.remote.Property;
import org.flowerplatform.properties.remote.SelectedItem;

/**
 * @author Cristina Constantinescu
 */
public class RegexPropertiesProvider implements IPropertiesProvider {

	@Override
	public List<Property> getProperties(SelectedItem selectedItem) {
		List<Property> properties = new ArrayList<Property>();	
		
		if (selectedItem.getItemType().equals("regex_match")) {
			RegexMatchDto match = ((RegexSelectedItem) selectedItem).getMatch();
			
			properties.add(new Property("RegEx Macros", match.getParserRegex().getRegex()));
			properties.add(new Property("RegEx Full", match.getParserRegex().getFullRegex()));
			properties.add(new Property("Start", match.getStart().toString()));
			properties.add(new Property("End", match.getEnd().toString()));
			
			if (match.getSubMatches() != null) {
				for (RegexSubMatchDto subMatch : match.getSubMatches()) {
					properties.add(new Property(String.format("$%d", match.getSubMatches().indexOf(subMatch) + 1), subMatch.getValue()));
				}
			}
		} else if (selectedItem.getItemType().equals("regex_config")) {
			properties.add(new Property("Name", ((RegexSelectedItem) selectedItem).getConfig(), false));
		} else if (selectedItem.getItemType().equals("regex_macro")) {
			MacroRegex regex = ((RegexSelectedItem) selectedItem).getRegex();
			
			properties.add(new Property("Name", regex.getName(), false));
			properties.add(new Property("Regex", regex.getRegex(), false));		
		} else if (selectedItem.getItemType().equals("regex_parser")) {
			ParserRegex regex = (ParserRegex) ((RegexSelectedItem) selectedItem).getRegex();
			
			properties.add(new Property("Name", regex.getName(), false));
			properties.add(new Property("RegEx Macros", regex.getRegex(), false));
			properties.add(new Property("RegEx Full", regex.getFullRegex()));
			properties.add(new Property("Action", regex.getAction()));
		}
		
		return properties;
	}

	@Override
	public List<String> getPropertyNames() {
		// doesn't support this
		return null;
	}

	@Override
	public Property getProperty(SelectedItem selectedItem, String propertyName) {	
		// doesn't support this
		return null;
	}
	
	@Override
	public boolean setProperty(ServiceInvocationContext context, SelectedItem selectedItem, String propertyName, Object propertyValue) {
		RegexSelectedItem item = (RegexSelectedItem) selectedItem;
		if (selectedItem.getItemType().equals("regex_config")) {
			RegexService.getInstance().renameConfig(
					context.getCommunicationChannel(), 
					item.getConfig(), 
					(String) propertyValue);			
			return true;
		} else if (selectedItem.getItemType().equals("regex_macro")) {
			if ("Name".equals(propertyName)) {
				RegexService.getInstance().changeMacroName(
						context.getCommunicationChannel(), 
						item.getConfig(), 
						item.getRegex(), 
						(String) propertyValue);	
			} else if ("Regex".equals(propertyName)) {
				RegexService.getInstance().changeMacroRegex(
						context.getCommunicationChannel(), 
						item.getConfig(), 
						item.getRegex(), 
						(String) propertyValue);	
			}
			return true;
		} else if (selectedItem.getItemType().equals("regex_parser")) {
			if ("Name".equals(propertyName)) {
				RegexService.getInstance().changeParserName(
						context.getCommunicationChannel(), 
						item.getConfig(), 
						(ParserRegex) item.getRegex(), 
						(String) propertyValue);	
			} else if ("RegEx Macros".equals(propertyName)) {
				RegexService.getInstance().changeParserRegex(
						context.getCommunicationChannel(), 
						item.getConfig(), 
						(ParserRegex) item.getRegex(), 
						(String) propertyValue);	
			} else if ("Action".equals(propertyName)) {
				RegexService.getInstance().changeParserAction(
						context.getCommunicationChannel(), 
						item.getConfig(), 
						(ParserRegex) item.getRegex(), 
						(String) propertyValue);	
			}
			return true;
		}
		return false;
	}

}
