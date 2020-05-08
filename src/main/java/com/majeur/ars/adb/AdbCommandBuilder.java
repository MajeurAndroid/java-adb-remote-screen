package com.majeur.ars.adb;

import com.majeur.ars.Utils;

import java.util.LinkedList;
import java.util.List;

public class AdbCommandBuilder {

	private final AdbHelper mAdbHelper;

	public AdbCommandBuilder(AdbHelper adbHelper) {
		mAdbHelper = adbHelper;
	}

	public List<String> buildTapCommand(double x, double y) {
		List<String> command = buildInputCommand();
		command.add("tap");
		command.add(Utils.toString(x));
		command.add(Utils.toString(y));
		return command;
	}

	public List<String> buildKeyEventCommand(int keyCode) {
		List<String> command = buildInputCommand();
		command.add("keyevent");
		command.add(Integer.toString(keyCode));
		return command;
	}

	public List<String> buildSwipeCommand(double x1, double y1, double x2, double y2, long dt) {
		List<String> command = buildInputCommand();
		command.add("swipe");
		command.add(Utils.toString(x1));
		command.add(Utils.toString(y1));
		command.add(Utils.toString(x2));
		command.add(Utils.toString(y2));
		command.add(Long.toString(dt));
		return command;
	}

	public List<String> buildInputCommand() {
		List<String> command = buildShellCommand();
		command.add("input");
		return command;
	}

	public List<String> buildShellCommand() {
		List<String> command = buildDeviceSpecificCommand();
		command.add("shell");
		return command;
	}

	public List<String> buildScreencapCommand() {
		List<String> command = buildDeviceSpecificCommand();
		command.add("exec-out");
		command.add("screencap");
		command.add("-p");
		return command;
	}

	public List<String> buildDeviceSpecificCommand() {
		List<String> command = buildAdbCommand();
		command.add("-s");
		command.add(mAdbHelper.getCurrentDevice().serial);
		return command;
	}

	public List<String> buildDevicesCommand() {
		List<String> command = buildAdbCommand();
		command.add("devices");
		command.add("-l");
		return command;
	}

	public List<String> buildKillServerCommand() {
		List<String> command = buildAdbCommand();
		command.add("kill-server");
		command.add("-l");
		return command;
	}

	public List<String> buildAdbCommand() {
		List<String> command = new LinkedList<>();
		command.add(mAdbHelper.getAdbPath());
		return command;
	}

}
