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
package org.flowerplatform.codesync.regex.ide;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.flowerplatform.codesync.regex.ide.remote.RegexDto;
import org.flowerplatform.codesync.regex.ide.remote.RegexIndexDto;
import org.flowerplatform.codesync.regex.ide.remote.RegexMatchDto;
import org.flowerplatform.codesync.regex.ide.remote.RegexSubMatchDto;
import org.flowerplatform.common.regex.RegexConfiguration;
import org.flowerplatform.common.regex.RegexProcessingSession;
import org.flowerplatform.common.regex.RegexWithAction;
import org.flowerplatform.editor.EditorPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristina Constantinescu
 */
public class RegexRulesConfigService {

	private Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	private Map<String, String> regexes = new HashMap<String, String>();
	private RegexConfiguration config = new RegexConfiguration();
	
	public RegexRulesConfigService() {
		regexes.put("jsChildrenInsertPoint", "// children-insert-point (\\S*)");
		regexes.put("backboneClass", "// template backboneClass");
		regexes.put("multiline commment", "/\\*.*?\\*/");
		regexes.put("single line comment", "//[^\\r^\\n]*\\r?\\n?");
		regexes.put("closing bracket", "[\\}\\]\\)]");
		regexes.put("opening bracket", "[\\{\\[\\(]");
		regexes.put("jsBackboneClassSuperClass", "return\\s*+(.*?).extend\\s*\\(");
		regexes.put("requireEntry", "var\\s*?(\\S*?)\\s*?=\\s*?require\\('(.*?)'\\)\\s*?;");
		regexes.put("javaScriptOperation", "(\\S*?)\\s*?:\\s*?function\\s*?\\((.*?)\\)\\s*?\\{");
		regexes.put("javaScriptAttribute", "(\\w+)\\s*+:\\s*([\\S&&[^,/]]+)");
		
		
		for (Entry<String, String> entry : regexes.entrySet()) {
			config.add(new RegexWithAction.IfFindThisSkip(entry.getKey(), entry.getValue()));
		}
		config.compile(Pattern.DOTALL);
	}
	
	private RegexIndexDto getRegexIndexDtoFromIndex(String str, int index) {
		RegexIndexDto dto = new RegexIndexDto();
		String prefix = str.substring(0, index);
		
		Matcher m = Pattern.compile("(\r\n)|(\n)|(\r)").matcher(prefix);
		int lines = 0;
		int lineEndIndex = -1;
		while (m.find()) {			
		    lines++;
		    lineEndIndex = m.end();
		}
		dto.setNbLine(lines);		
		dto.setNbCharacter(lineEndIndex == -1 ? index : index - lineEndIndex);
		
		return dto;
	}
	
	public List<Object> run(String editorResourcePath) {		
		try {
			File file = (File) EditorPlugin.getInstance().getFileAccessController().getFile(editorResourcePath);
			String cs = FileUtils.readFileToString(file);
			
			List<RegexDto> regexDtos = new ArrayList<RegexDto>();
			for (Entry<String, String> entry : regexes.entrySet()) {
				regexDtos.add(new RegexDto(entry.getKey(), entry.getValue(), null));
			}
			
			List<RegexMatchDto> regexMatchDtos = new ArrayList<RegexMatchDto>();
			RegexProcessingSession session = config.startSession(cs);
			int currentIndex = 1;
			while (session.find()) {
				RegexMatchDto dto = new RegexMatchDto(
						String.valueOf(currentIndex++), 
						getRegexIndexDtoFromIndex(cs, session.getMatcher().start()), 
						getRegexIndexDtoFromIndex(cs, session.getMatcher().end()));
				dto.setRegexName(session.getCurrentRegex().getHumanReadableRegexMeaning());
								
				if (session.getCurrentSubMatchesForCurrentRegex() != null) {
					List<RegexSubMatchDto> regexSubMatchDtos = new ArrayList<RegexSubMatchDto>();
					for (int i = 0; i < session.getCurrentSubMatchesForCurrentRegex().length; i++) {
						int index = session.getCurrentMatchGroupIndex() + i + 1;
						regexSubMatchDtos.add(new RegexSubMatchDto(
								session.getCurrentSubMatchesForCurrentRegex()[i], 
								getRegexIndexDtoFromIndex(cs, session.getMatcher().start(index)), 
								getRegexIndexDtoFromIndex(cs, session.getMatcher().end(index))));
					}
					dto.setSubMatches(regexSubMatchDtos);
				}
				regexMatchDtos.add(dto);
			}
			
			List<Object> result = new ArrayList<Object>();
			result.add(regexDtos);
			result.add(regexMatchDtos);
			
			return result;
		} catch (Exception e) {
			logger.error("error while getting data for regex IDE!", e);
		}		
		return null;
	}
	
}
