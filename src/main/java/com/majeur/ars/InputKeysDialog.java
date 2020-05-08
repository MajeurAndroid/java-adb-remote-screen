package com.majeur.ars;

import com.majeur.ars.adb.AdbHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class InputKeysDialog extends JDialog implements ActionListener {

	private AdbHelper mAdbHelper;

	public InputKeysDialog(Frame owner, AdbHelper adbHelper) {
		super(owner, Constants.Strings.WINDOW_TILE_INPUT);
		setVisible(true);
		setSize(800, 400);
		setResizable(false);
		setLocationRelativeTo(owner);
		URL iconURL = getClass().getResource("/app_icon.png");
		setIconImage(new ImageIcon(iconURL).getImage());
		setLayout(new GridLayout(15, 4));

		mAdbHelper = adbHelper;

		for (int i = 0; i < Constants.Adb.INPUT_KEY_MAP.length; i++) {
			JButton jButton = new JButton((String) Constants.Adb.INPUT_KEY_MAP[i][1]);
			jButton.setName(String.valueOf(i));
			jButton.addActionListener(this);
			add(jButton);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int index = Integer.parseInt(((JButton) e.getSource()).getName());
		mAdbHelper.performInputKey((Integer) Constants.Adb.INPUT_KEY_MAP[index][0]);
	}

}
