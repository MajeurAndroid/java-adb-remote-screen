package com.majeur.ars;

import java.awt.Button;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class InputFrame extends JFrame implements ActionListener {
	
	private AdbHelper mAdbHelper;
	
	public InputFrame(AdbHelper adbHelper) {
		mAdbHelper = adbHelper;
		
		setVisible(true);
		setTitle(Constants.Strings.WINDOW_TILE_INPUT);
		setSize(800, 300);
		setResizable(false);
		
		setLayout(new GridLayout(15, 4));
		
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
