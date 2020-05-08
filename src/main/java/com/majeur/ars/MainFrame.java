package com.majeur.ars;

import com.majeur.ars.adb.AdbDevice;
import com.majeur.ars.adb.AdbHelper;
import com.majeur.ars.adb.AdbHelper.OnAttachedDevicesChangedListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.List;

public class MainFrame extends JFrame implements OnAttachedDevicesChangedListener {

	private AdbHelper mAdbHelper;
	private AdbDeviceScreenComponent mScreenPanel;
	private TitleManager mTitleManager;

	private JRadioButtonMenu mDevicesMenu;

	private JDialog mInputKeysDialog;
	private JDialog mLogDialog;

	public MainFrame(final AdbHelper adbHelper) {
		setSize(200, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		URL iconURL = getClass().getResource("/app_icon.png");
		setIconImage(new ImageIcon(iconURL).getImage());
		initMenu();

		mTitleManager = new TitleManager(this);

		mAdbHelper = adbHelper;
		mAdbHelper.setAttachedDevicesChangedListener(this);

		mScreenPanel = new AdbDeviceScreenComponent(adbHelper);
		getContentPane().add(mScreenPanel);

		pack();
	}

	TitleManager getTitleManger() {
		return mTitleManager;
	}

	private void setCurrentDevice(int deviceIndex) {
		if (deviceIndex == AdbHelper.INVALID_DEVICE_INDEX) {
			Logger.i("Selected device: None");

			if (mScreenPanel.isRendering())
				mScreenPanel.stopUpdate();

			mAdbHelper.setCurrentDevice(AdbHelper.INVALID_DEVICE_INDEX);
			mDevicesMenu.setSelectedIndex(0); // None menuItem index
			mTitleManager.toggleMode(false);

		} else {
			mAdbHelper.setCurrentDevice(deviceIndex);
			AdbDevice device = mAdbHelper.getCurrentDevice();
			Logger.i("Selected device: " + device.getReadableName());
			mTitleManager.postDeviceName(device.getReadableName());
			mTitleManager.toggleMode(true);

			mDevicesMenu.setSelectedIndex(deviceIndex + 1); // Offset to match menuItem index
			mScreenPanel.startUpdate();
			mScreenPanel.setScale(0.5);
		}
	}

	@Override
	public void onAttachedDevicesChanged() {
		Logger.i("Attached devices changed");
		MenuSelectionManager.defaultManager().clearSelectedPath();
		ButtonGroup buttonGroup = new ButtonGroup();
		mDevicesMenu.removeAll();
		JMenuItem noneMenuItem = new JRadioButtonMenuItem("None");
		buttonGroup.add(noneMenuItem);
		mDevicesMenu.add(noneMenuItem);
		noneMenuItem.addActionListener(e -> setCurrentDevice(AdbHelper.INVALID_DEVICE_INDEX));

		List<AdbDevice> devices = mAdbHelper.getAttachedDevices();
		for (int i = 0; i < devices.size(); i++) {
			AdbDevice device = devices.get(i);
			JMenuItem menuItem = new JRadioButtonMenuItem(device.getReadableName());
			menuItem.setEnabled(device.isAvailable());
			buttonGroup.add(menuItem);
			mDevicesMenu.add(menuItem);
			final int deviceIndex = i;
			menuItem.addActionListener(e -> setCurrentDevice(deviceIndex));
			Logger.i(String.format("Device #%d: ", i) + device.toString());
		}
		setCurrentDevice(AdbHelper.INVALID_DEVICE_INDEX);
	}

	private void initMenu() {
		JMenuBar menuBar = new JMenuBar();

		menuBar.add(buildDevicesMenu());
		menuBar.add(buildInputMenu());
		menuBar.add(buildViewMenu());
		menuBar.add(buildAboutMenu());
		setJMenuBar(menuBar);
	}

	private JMenu buildDevicesMenu() {
		mDevicesMenu = new JRadioButtonMenu("Devices");
		return mDevicesMenu;
	}

	private JMenu buildInputMenu() {
		JMenu inputMenu = new JMenu("Inputs");

		JMenuItem menuItem = new JMenuItem("Send KeyInputs");
		menuItem.addActionListener(e -> {
			if (mInputKeysDialog == null) {
				mInputKeysDialog = new InputKeysDialog(MainFrame.this, mAdbHelper);
				mInputKeysDialog.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent windowEvent) {
						mInputKeysDialog.dispose();
						mInputKeysDialog = null;
					}
				});
			} else {
				mInputKeysDialog.toFront();
			}
		});
		inputMenu.add(menuItem);
		return inputMenu;
	}

	private JMenu buildViewMenu() {
		JMenu viewMenu = new JMenu("View");
		JMenu scaleSubMenu = new JMenu("Scale");
		for (final Object[] entry : Constants.VIEW_SCALE_KEY_MAP) {
			JMenuItem scaleMenuItem = new JMenuItem((String) entry[1]);
			scaleMenuItem.addActionListener(e -> mScreenPanel.setScale((double) entry[0]));
			scaleSubMenu.add(scaleMenuItem);
		}
		viewMenu.add(scaleSubMenu);
		return viewMenu;
	}

	private JMenu buildAboutMenu() {
		JMenu helpMenu = new JMenu("Help");

		JMenuItem logMenuItem = new JMenuItem("Debugging LOG");
		logMenuItem.addActionListener(e -> {
			if (mLogDialog == null) {
				mLogDialog = new LogDialog(MainFrame.this);
				mLogDialog.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent windowEvent) {
						mLogDialog.dispose();
						mLogDialog = null;
					}
				});
			} else {
				mLogDialog.toFront();
			}
		});
		helpMenu.add(logMenuItem);
		helpMenu.addSeparator();

		JMenuItem aboutMenuItem = new JMenuItem("About Adb Remote Screen");
		aboutMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				URL iconURL = getClass().getResource("/app_icon.png");
				JOptionPane.showMessageDialog(MainFrame.this, Constants.Strings.ABOUT_MESSAGE, "About",
						JOptionPane.PLAIN_MESSAGE, new ImageIcon(iconURL));
			}
		});
		helpMenu.add(aboutMenuItem);

		return helpMenu;
	}
}
