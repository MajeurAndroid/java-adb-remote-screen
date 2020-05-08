package com.majeur.ars;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

import javax.management.RuntimeErrorException;

public class LocalProperties {
	
	private static final String PROP_FILE_NAME = "/local.properties";
	private static final String COMMENT_NAME = "#";
	private static final String ATTR_ADB_PATH = "adbPath";
	private static final String ATTR_LIMIT_FRAMERATE = "limitFrameRate";
	
	public static String adbPath = null;
	public static boolean limitFrameRate = true;
	
	static {
		File file = new File(Utils.getRunningJarPath() + PROP_FILE_NAME);
		System.out.println(file.getAbsolutePath());
		
		if (file.exists()) {
			
			try {
				Scanner scanner = new Scanner(file);
				
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();

					if (line.startsWith(COMMENT_NAME))
						continue;
					
					String[] attrValue = line.split("=");
					
					switch (attrValue[0]) {
						case ATTR_ADB_PATH:
							adbPath = attrValue[1];
							break;
							
						case ATTR_LIMIT_FRAMERATE:
							limitFrameRate = Boolean.parseBoolean(attrValue[1]);
							break;
					}				
				}
				scanner.close();
			} catch(FileNotFoundException e) {
				e.printStackTrace();
			}
		
		}
	}
	
	private LocalProperties() {};

}
