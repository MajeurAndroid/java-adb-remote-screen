package com.majeur.ars;

import javax.swing.*;

public class JRadioButtonMenu extends JMenu {

	private ButtonGroup mButtonGroup;

	public JRadioButtonMenu(String title) {
		super(title);
		mButtonGroup = new ButtonGroup();
	}

	@Override
	public void removeAll() {
		super.removeAll();
		mButtonGroup = new ButtonGroup();
	}

	public void add(JRadioButtonMenuItem menuItem) {
		super.add(menuItem);
		mButtonGroup.add(menuItem);
	}

	public void setSelectedIndex(int index) {
		mButtonGroup.clearSelection();
		((JRadioButtonMenuItem) getMenuComponent(index)).setSelected(true);
	}

}
