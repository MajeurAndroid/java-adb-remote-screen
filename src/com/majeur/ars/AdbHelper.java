package com.majeur.ars;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.Scanner;

import javax.swing.JTextArea;

public class AdbHelper {
	
	private String mAdbPath = "/home/pdroid/sdk/platform-tools/adb"; //TODO
	
	private JTextArea mTextArea;
	
	public void performScreenShot(File file) {
		String commandLine = mAdbPath + " shell screencap -p | perl -pe \'BEGIN { $/=\"\\cM\\cJ\"; $\\=\"\\cJ\"; } chomp;\' > " 
				+ file.getAbsolutePath();
		executeCommand(new String[] {"/bin/sh", "-c", commandLine}, false);		
	}
	
	public void performClick(double x, double y) {
		updateInfo("Touch at " + x + "x" + y);
		executeAdbShellCommand("input tap " + x + " " + y);
	}
	
	public void performSwipe(double x1, double y1, double x2, double y2, long duration) {
		updateInfo("Swipe from " + x1 + "x" + y1 + " " + x2 + "x" + y2 + " while " + duration + "ms");
		executeAdbShellCommand("input swipe " + x1 + " " + y1 + " " + x2 + " " + y2 + " " + duration);
	}
	
	private String executeAdbShellCommand(String command) {
		return executeAdbCommand("shell " + command);
	}
	
	public String executeAdbCommand(String command) {
		return executeCommand(mAdbPath + " " + command);
	}

	private String executeCommand(String command) {
		System.out.println(command);
		StringBuilder builder = new StringBuilder();
		
		Runtime rut = Runtime.getRuntime();
		try {
		    Process process = rut.exec(command);
		    // prints out any message that are usually displayed in the console
		    Scanner scanner = new Scanner(process.getInputStream());
		    while (scanner.hasNext()) {
		        builder.append(scanner.nextLine());
		    }
		}catch(IOException e1) {
		    e1.printStackTrace();
		}
		
		return builder.toString();
	}
	
	private String executeCommand(String[] command, boolean requestResponse) {
		String print = "";
		for (String s : command) print += s +", ";
		System.out.println(print);
		StringBuilder builder = new StringBuilder();
		
		Runtime rut = Runtime.getRuntime();
		try {
		    Process process = rut.exec(command);
		    // prints out any message that are usually displayed in the console
		    Scanner scanner = new Scanner(process.getInputStream());
		    while (scanner.hasNext()) {
		        builder.append(scanner.nextLine());
		    }
		    scanner.close();
		}catch(IOException e1) {
		    e1.printStackTrace();
		}
		return builder.toString();
	}
	
	public void setTextArea(JTextArea t) {
		mTextArea = t;
	}
	
	private void updateInfo(String string) {
		if (mTextArea == null) return;
		
		mTextArea.setText(string);
	}
}
