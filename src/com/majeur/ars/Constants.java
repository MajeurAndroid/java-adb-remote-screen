package com.majeur.ars;

public final class Constants {
	
	public final class Strings {
		
		public static final String MESSAGE_NO_ADB_PATH = "Make shure you specified adb path in local.properties file. This file must be in the same folder as .jar file.\n"
				+ "Content must be:\nadbPath=/path/to/adb binary";
		public static final String TITLE_NO_ADB_PATH = "Error: No adb path specified";
		
		public static final String MESSAGE_NO_ADB_FILE = "Specified adb path isn't valid, cannot find adb binary.";
		public static final String TITLE_NO_ADB_FILE = "Error: Adb binary not found";
	}

}
