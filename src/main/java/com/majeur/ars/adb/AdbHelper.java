package com.majeur.ars.adb;

import com.majeur.ars.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class AdbHelper {

	public static final int INVALID_DEVICE_INDEX = -1;

	private final String mAdbPath;
	private final AdbCommandBuilder mCommandBuilder;
	private final AdbDevicesWatcher mDevicesWatcher;

	private OnAttachedDevicesChangedListener mAttachedDevicesChangedListener;
	private List<AdbDevice> mAttachedDevices;
	private int mCurrentDeviceIndex;

	public AdbHelper(File adbFile) {
		mAdbPath = adbFile.getAbsolutePath();
		mCommandBuilder = new AdbCommandBuilder(this);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> executeCommand(mCommandBuilder.buildKillServerCommand())));

		mDevicesWatcher = new AdbDevicesWatcher(this);
		mDevicesWatcher.startWatch();
	}

	public void release() {
		mDevicesWatcher.stopWatch();
	}

	public AdbDevice getCurrentDevice() {
		return mAttachedDevices.get(mCurrentDeviceIndex);
	}

	public List<AdbDevice> getAttachedDevices() {
		return Collections.unmodifiableList(mAttachedDevices);
	}

	public void setAttachedDevicesChangedListener(OnAttachedDevicesChangedListener listener) {
		mAttachedDevicesChangedListener = listener;
	}

	public void setCurrentDevice(int index) {
		mCurrentDeviceIndex = index;
	}

	String getAdbPath() {
		return mAdbPath;
	}

	AdbCommandBuilder getCommandBuilder() {
		return mCommandBuilder;
	}

	void onDevicesChanged(List<AdbDevice> newDevices) {
		mCurrentDeviceIndex = INVALID_DEVICE_INDEX;
		mAttachedDevices = newDevices;
		if (mAttachedDevicesChangedListener != null)
			mAttachedDevicesChangedListener.onAttachedDevicesChanged();
	}

	public void performInputKey(int keyCode) {
		if (mCurrentDeviceIndex == INVALID_DEVICE_INDEX)
			return;

		Logger.i("Key pressed (code: %d)", keyCode);
		executeCommand(mCommandBuilder.buildKeyEventCommand(keyCode));
	}

	public void performClick(double x, double y) {
		if (mCurrentDeviceIndex == INVALID_DEVICE_INDEX)
			return;

		Logger.i("Click at %.1fx%.1f", x, y);
		executeCommand(mCommandBuilder.buildTapCommand(x, y));
	}

	public void performSwipe(double x1, double y1, double x2, double y2, long duration) {
		if (mCurrentDeviceIndex == INVALID_DEVICE_INDEX)
			return;

		Logger.i("Swipe from %.1fx%.1f to %.1fx%.1f during %d ms", x1, y1, x2, y2, duration);
		executeCommand(mCommandBuilder.buildSwipeCommand(x1, y1, x2, y2, duration));
	}

	public BufferedImage retrieveScreenShot() throws IOException {
		if (mCurrentDeviceIndex == INVALID_DEVICE_INDEX) {
			Logger.e("No device selected, screenshot aborted");
			return null;
		}

		byte[] data = CommandExec.exec(mCommandBuilder.buildScreencapCommand());
		if (data == null) {
			Logger.e("Fatal error retrieving screenshot");
			return null;
		}
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		// No need to call close() for ByteArrayInputStream
		return ImageIO.read(inputStream);
	}

	private void executeCommand(List<String> command) {
		CommandExec.execAsync(command, null);
	}

	public interface OnAttachedDevicesChangedListener {
		void onAttachedDevicesChanged();
	}

}
