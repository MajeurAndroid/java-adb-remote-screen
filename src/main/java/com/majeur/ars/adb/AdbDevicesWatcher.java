package com.majeur.ars.adb;

import com.majeur.ars.Utils;

import javax.swing.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AdbDevicesWatcher implements Runnable {

	private final AdbHelper mAdbHelper;
	private Thread mWatchThread;
	private List<AdbDevice> mPreviouslyConnectedDevices;

	public AdbDevicesWatcher(AdbHelper adbHelper) {
		mAdbHelper = adbHelper;
	}

	void startWatch() {
		mWatchThread = new Thread(this);
		mWatchThread.start();
	}

	void stopWatch() {
		mWatchThread.interrupt();
		mWatchThread = null;
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {

			final List<AdbDevice> devices = new LinkedList<>();

			AdbCommandBuilder commandBuilder = mAdbHelper.getCommandBuilder();
			byte[] data = CommandExec.exec(commandBuilder.buildDevicesCommand());
			if (data != null) {
				String[] lines = new String(data).split("\\n");
				for (String line : lines) {
					if (line.startsWith("*") || line.startsWith("List") || Utils.isEmpty(line)) // line.startsWith("adb
						// server")
						continue;
					devices.add(new AdbDevice(line));
				}
			}

			Collections.sort(devices);
			if (!Utils.equalsOrder(devices, mPreviouslyConnectedDevices)) {
				mPreviouslyConnectedDevices = devices;
				SwingUtilities.invokeLater(() -> mAdbHelper.onDevicesChanged(devices));
			}

			Utils.sleep(5000);
		}
	}
}
