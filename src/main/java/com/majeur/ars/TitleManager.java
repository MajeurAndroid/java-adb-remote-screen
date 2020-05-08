package com.majeur.ars;

import com.majeur.ars.Constants.Strings;

import javax.swing.*;

public class TitleManager {

	private JFrame mFrame;
	private boolean toggled;
	private String mDeviceName;
	private double mFps;

	public TitleManager(JFrame frame) {
		mFrame = frame;
		update();
	}

	private void update() {
		if (toggled)
			mFrame.setTitle(String.format(Strings.WINDOW_TILE_DEVICE, mDeviceName, mFps));
		else
			mFrame.setTitle(Strings.WINDOW_TILE_REGULAR);
	}

	void toggleMode(boolean b) {
		toggled = b;
		update();
	}

	void postDeviceName(String deviceName) {
		mDeviceName = deviceName;
		update();
	}

	void postFpsCount(double fpsCount) {
		mFps = fpsCount;
		update();
	}

}
