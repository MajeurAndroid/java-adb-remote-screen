package com.majeur.ars;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class AdbHelper {
	
	private DevicesWatcher mDevicesWatcher;	
	private final String mAdbPath;
	
	private String mDevice;
	
	public AdbHelper(String path) {
		mAdbPath = path;
	}
	
	public void registerDevicesChangedListener(OnDevicesChangedListener listener) {
		mDevicesWatcher = new DevicesWatcher(listener);
		mDevicesWatcher.startWach();
	}
	
	public void unregisterDevicesChangedListener() {
		mDevicesWatcher.stopWatch();
		mDevicesWatcher = null;
	}
	
	public void setTargetDevice(String device) {
		mDevice = device;
	}
	
	public void performInputKey(int keyCode) {
		Logger.i("Key pressed (code: %d)", keyCode);
		executeDeviceShellCommand(String.format(Constants.Adb.CMD_KEY, keyCode));
	}
	
	public void performClick(double x, double y) {
		Logger.i("Click at %.1fx%.1f", x, y);
		executeDeviceShellCommand(String.format(Constants.Adb.CMD_TAP, x, y).replace(',', '.'));
	}
	
	public void performSwipe(double x1, double y1, double x2, double y2, long duration) {
		Logger.i("Swipe from %.1fx%.1f to %.1fx%.1f during %d ms", x1, y1, x2, y2, duration);
		executeDeviceShellCommand("input swipe " + x1 + " " + y1 + " " + x2 + " " + y2 + " " + duration);
	}
	
	private String executeDeviceShellCommand(String command) {
		if (mDevice == null) {
			Logger.e("No device selected, unable to execute '%s' command", command);
			return null;
		}
		
		return executeAdbCommand(String.format("-s %s shell %s", mDevice, command));
	}
	
	public String executeAdbCommand(String command) {
		return Utils.executeCommand(mAdbPath + " " + command);
	}
	
	public String[] getConnectedDevices() {
		String[] lines = executeAdbCommand("devices").split("\n");
		
		if (lines.length == 0)
			return new String[0];
		
		List<String> devices = new LinkedList<>();
		for (String line : lines) {
			if (line.startsWith("adb server") || line.startsWith("List of devices attached"))
				continue;
			
			devices.add(line.split("\t")[0]);
		}		
		
		return devices.toArray(new String[devices.size()]);	
	}
	
	public BufferedImage retrieveScreenShot() {
		if (mDevice == null) {
			Logger.e("No device selected, screenshot aborted");
			return null;
		}
		
		final String command = String.format(Constants.Adb.CMD_SCREENCAP, mAdbPath, mDevice);
		
		try {
			return ImageIO.read(Utils.executeCommandGetInputStream("/bin/sh", "-c", command));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean saveScreenShotToFile(File file) {
		if (mDevice == null)
			return false;
		
		String commandLine = String.format(Constants.Adb.CMD_SCREENCAP, mAdbPath, mDevice) + " > " + file.getAbsolutePath();
		Utils.executeCommand("/bin/sh", "-c", commandLine);
		return true;
	}
	
	interface OnDevicesChangedListener {
		void onDevicesChanged(String[] devices);
	}
	
	private class DevicesWatcher implements Runnable {
		
		Thread mWatchThread;
		private String[] mOldDevices;
		private OnDevicesChangedListener mListener;
		
		public DevicesWatcher(OnDevicesChangedListener listener) {
			mListener = listener;
		}
		
		void startWach() {
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
				final String[] newDevices = getConnectedDevices();
				if (!Arrays.equals(newDevices, mOldDevices)) {
					mOldDevices = newDevices;
					SwingUtilities.invokeLater(new Runnable() {					
						@Override
						public void run() {
							mListener.onDevicesChanged(newDevices);
						}
					});
				}
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}			
		}		
	}
}
