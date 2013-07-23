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
package org.flowerplatform.ant;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.flowerplatform.ant.utils.FileUtil;

public class Main {

	
	public static void main(String[] args) throws InterruptedException {

//		//String p = "/\\*[\\s\\S]*?license-begin.*?license-end[\\s\\S]*?\\*/";
//		String p = "/\\*.*?license-start[\\s\\S]*license-end[\\s\\S]*?\\*/";
//		Pattern pattern = Pattern.compile(p); 
//		
//		String text = FileUtil.readFile(new File("D:/data/java_work/eclipse_workspace_brabo_proteus_FB3/ant-scripts/test_ws/ZMain/src/p/Main.java"));
//		Matcher m = pattern.matcher(text);
//		System.out.println(m.find());
	
		HeaderUpdaterTask t = new HeaderUpdaterTask();
		t.setWorkspaceFolder(new File("D:/data/java_work/eclipse_workspace_brabo_proteus_FB3/ant-scripts/test_ws"));
		t.setProjectFilterRegex("ZFl.*");
		t.setFileExtension("mxml");
		t.setHeaderFile(new File("D:/data/java_work/eclipse_workspace_brabo_proteus_FB3/ant-scripts/mxmlStandardHeader.txt"));
		t.setStartToken("license-start");
		t.setEndToken("license-end");
		t.execute();
	}
	

}