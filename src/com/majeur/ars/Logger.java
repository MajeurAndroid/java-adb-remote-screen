package com.majeur.ars;

import java.lang.ref.WeakReference;
import java.util.Calendar;

import javax.print.attribute.standard.Media;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DateFormatter;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class Logger {

	private static WeakReference<JTextArea> sTextAreaRef = new WeakReference<JTextArea>(
			null);
	private static StringBuilder sStringBuilder = new StringBuilder();

	static void e(String msg, Object... args) {
		e(String.format(msg, args));
	}

	static void e(String msg) {
		log("ERROR", msg);		
		updateUI();
	}

	static void i(String msg, Object... args) {
		i(String.format(msg, args));
	}

	static void i(String msg) {
		log("INFO", msg);
		updateUI();
	}
	
	private static void log(String tag, String msg) {
		sStringBuilder.append(buildDate());
		sStringBuilder.append("\t");
		sStringBuilder.append(tag);
		sStringBuilder.append(": ");
		sStringBuilder.append(msg);
		sStringBuilder.append("\n");
	}
	
	private static String buildDate() {
		Calendar calendar = Calendar.getInstance();
		return String.format("%02d:%02d:%02d.%03d",
				calendar.get(Calendar.HOUR),
				calendar.get(Calendar.MINUTE),
				calendar.get(Calendar.SECOND),
				calendar.get(Calendar.MILLISECOND));
	}

	private static void updateUI() {
		JTextArea textArea;
		if ((textArea = sTextAreaRef.get()) != null) {
			textArea.setText(sStringBuilder.toString());
			
			JScrollPane scrollPane;			
			JScrollBar scrollBar;
			if (textArea.getParent() instanceof JScrollPane && (scrollPane = (JScrollPane) textArea.getParent()) != null 
					&& (scrollBar = scrollPane.getVerticalScrollBar()) != null)
				scrollBar.setValue(scrollBar.getMaximum());
		}
	}

	static void bindUI(JTextArea textArea) {
		sTextAreaRef = new WeakReference<JTextArea>(textArea);
		updateUI();
	}

	static void unbindUI() {
		sTextAreaRef.clear();
	}

}
