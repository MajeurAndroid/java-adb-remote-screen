package com.majeur.ars;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LogFrame extends JFrame implements WindowListener {
	
	private static final int WIDTH = 450, HEIGHT = 250;
	
	public LogFrame() {
		setVisible(true);
		setSize(WIDTH, HEIGHT);
		setTitle(Constants.Strings.WINDOW_TILE_LOG);
		
		addWindowListener(this);
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		Logger.bindUI(textArea);
		
		getContentPane().add(new JScrollPane(textArea));
	}

	@Override
	public void windowOpened(WindowEvent e) {
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		Logger.unbindUI();		
	}

	@Override
	public void windowClosed(WindowEvent e) {	
		
	}

	@Override
	public void windowIconified(WindowEvent e) {	
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		
	}
}
