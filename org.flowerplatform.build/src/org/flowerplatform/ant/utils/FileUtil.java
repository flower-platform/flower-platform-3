package org.flowerplatform.ant.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * @author Florin
 * 
 */
public class FileUtil {

	public static String readFile(File file) {
		StringBuilder sb = new StringBuilder();
		FileReader fr = null;
		BufferedReader br = null;

		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);

			String line;
			while ((line = br.readLine()) != null) {
				if (sb.length() > 0) {
					sb.append(System.getProperty("line.separator"));
				}
				sb.append(line);				
			} 
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
			}
		}

		return sb.toString();
	}

	public static void writeFile(File file, String text) {
		FileWriter fw = null;
		BufferedWriter bw = null;

		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(text);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
			} catch (IOException e) {
			}
		}
	}
}
