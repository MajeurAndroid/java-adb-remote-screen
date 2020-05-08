package com.majeur.ars;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Utils {

	public static void openUrlInBrowser(String url) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(new URI(url));
			} catch (IOException | URISyntaxException exception) {
				exception.printStackTrace();
			}
		}
	}

	static String streamToString(java.io.InputStream is) {
		java.util.Scanner s = new Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	static String getRunningJarPath() {
		try {
			String path = Utils.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			File classFile = new File(path);
			return classFile.getParent();

		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void executeOnUiThread(Runnable r) {
		SwingUtilities.invokeLater(r);
	}

	public static boolean isEmpty(String str) {
		for (Character c : str.toCharArray())
			if (Character.isLetterOrDigit(c))
				return false;
		return true;
	}

	public static String toString(double d) {
		return String.format(Locale.US, "%.3f", d);
	}

	public static <T> boolean equalsOrder(List<T> c1, List<T> c2) {
		if (c1 == null || c2 == null || (c1.size() != c2.size()))
			return false;
		for (int i = 0; i < c1.size(); i++)
			if (!c1.get(i).equals(c2.get(i)))
				return false;
		return true;
	}

	public static <T> String toString(Collection<T> c) {
		StringBuilder builder = new StringBuilder("(");
		for (T t : c) {
			builder.append(t.toString());
			builder.append(", ");
		}
		builder.delete(builder.length() - 3, builder.length() - 1);
		builder.append(")");
		return builder.toString();
	}

	public static void sleep(long t) {
		try {
			Thread.sleep(t);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static class OS {

		public enum OSName {
			WINDOWS, LINUX, MAC
		}

		public static OSName name;

		static {
			String os = System.getProperty("os.name").toLowerCase();
			if (os.contains("win"))
				name = OSName.WINDOWS;
			else if (os.contains("osx"))
				name = OSName.MAC;
			else if (os.contains("nix") || os.contains("aix") || os.contains("nux"))
				name = OSName.LINUX;
		}

	}
}
