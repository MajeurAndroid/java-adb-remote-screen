package com.majeur.ars;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Scanner;

public class Utils {

	static String streamToString(java.io.InputStream is) {
		java.util.Scanner s = new Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}
	
	static String getRunningJarPath() {
		try {
			String s = Utils.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			return s.substring(0, s.lastIndexOf(System.getProperty("file.separator")));
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	static void sleep(long t) {
		try {
			Thread.sleep(t);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	static String executeCommand(String... command) {
		StringBuilder builder = new StringBuilder();
		Scanner scanner = new Scanner(executeCommandGetInputStream(command));
	    while (scanner.hasNext()) {
	        builder.append(scanner.nextLine());
	        builder.append("\n");
	    }
	    scanner.close();
		
		return builder.toString();
	}
	
	static InputStream executeCommandGetInputStream(String... command) {
		try {
			Runtime runtime = Runtime.getRuntime();
		    Process process = command.length == 1 ? runtime.exec(command[0]) : runtime.exec(command);
		    
		    new StreamGobbler(process.getErrorStream()).start();
		    
		    return process.getInputStream();		    
		} catch(IOException e1) {
		    e1.printStackTrace();
		    return null;
		}
	}

	static class StreamGobbler extends Thread {

		InputStream mInputStream;

		StreamGobbler(InputStream is) {
			this.mInputStream = is;
		}

		public void run() {
			try {
				InputStreamReader isr = new InputStreamReader(mInputStream);
				BufferedReader br = new BufferedReader(isr);
				while (br.readLine() != null);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
}
