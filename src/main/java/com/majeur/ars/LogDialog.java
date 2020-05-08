package com.majeur.ars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;

public class LogDialog extends JDialog implements WindowListener {

	public LogDialog(Frame owner) {
		super(owner, Constants.Strings.WINDOW_TILE_LOG);
		setVisible(true);
		setSize(650, 450);
		setLocationRelativeTo(owner);
		URL iconURL = getClass().getResource("/app_icon.png");
		setIconImage(new ImageIcon(iconURL).getImage());

		addWindowListener(this);

		JTextPane textArea = new JTextPane();
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
