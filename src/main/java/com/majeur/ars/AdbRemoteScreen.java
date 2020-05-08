package com.majeur.ars;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class AdbRemoteScreen {
	
	public static void main(String... args) {		
		final String adbPath = args.length == 0 ? LocalProperties.adbPath : args[0];
		
		if (adbPath == null) {
			JOptionPane.showMessageDialog(null, Constants.Strings.MESSAGE_NO_ADB_PATH, Constants.Strings.TITLE_NO_ADB_PATH, JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (!new File(adbPath).exists()) {
			JOptionPane.showMessageDialog(null, Constants.Strings.MESSAGE_NO_ADB_FILE, Constants.Strings.TITLE_NO_ADB_FILE, JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		Logger.i("Adb path used: " + adbPath);
		
		SwingUtilities.invokeLater(new Runnable() {			
			@Override
			public void run() {
				new MainFrame(new AdbHelper(adbPath));				
			}
		});
	}
}

