package com.majeur.ars;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.MenuBar;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.majeur.ars.AdbHelper.OnDevicesChangedListener;

import static com.majeur.ars.Constants.Strings;

public class MainFrame extends JFrame implements OnDevicesChangedListener {

	private AdbHelper mAdbHelper;
	private ScreenPanel mScreenPanel;
	private String[] mDevices;

	private RadioButtonGroup mRadioButtonGroup;

	private JDialog mNoDeviceDialog;
	
	private JFrame mInputKeysFrame;
	private JFrame mLogFrame;

	public MainFrame(final AdbHelper adbHelper) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}		
		
		setTitle(Strings.WINDOW_TILE_REGULAR);
		setSize(200, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		JOptionPane optionPane = new JOptionPane();
		optionPane.setMessage(Strings.MESSAGE_NO_DEVICE);
		mNoDeviceDialog = optionPane.createDialog(this, Strings.TITLE_NO_DEVICE);
		mNoDeviceDialog.setModal(false);
		
		mAdbHelper = adbHelper;
		mScreenPanel = new ScreenPanel(adbHelper);

		mAdbHelper.registerDevicesChangedListener(this);

		initMenu();

		final int togglesWidth = 180;
		JPanel togglesPanel = new JPanel();
		togglesPanel.setPreferredSize(new Dimension(togglesWidth, 400));

		togglesPanel.add(buildDevicesPanel(togglesWidth));
		togglesPanel.add(buildRenderingOptionPanel(togglesWidth));
		togglesPanel.add(buildInfoPanel(togglesWidth));

		getContentPane().add(togglesPanel, BorderLayout.WEST);
		getContentPane().add(mScreenPanel);
		
		pack();
	}

	private void setCurrentDevice(String device) {
		if (device == null) {
			mScreenPanel.stopUpdate();
			setTitle(Strings.WINDOW_TILE_REGULAR);
			mAdbHelper.setTargetDevice(device);
			
			Logger.i("No device found");
			
			mNoDeviceDialog.show();
		} else {
			mScreenPanel.startUpdate();
			setTitle(String.format(Strings.WINDOW_TILE_DEVICE, device));
			mAdbHelper.setTargetDevice(device);
			mRadioButtonGroup.setSelectedRadio(Arrays.asList(mDevices).indexOf(
					device));
			mNoDeviceDialog.hide();
		}
	}

	@Override
	public void onDevicesChanged(String[] devices) {
		Logger.i("Connected devices changed");
		mDevices = devices;
		mRadioButtonGroup.setRadioButtons(devices);

		if (devices.length > 0)
			setCurrentDevice(devices[0]);
		else
			setCurrentDevice(null);
	}

	private void initMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("About");

		JMenuItem menuItem2 = new JMenuItem("Version 2.0: View Github project");
		menuItem2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openUrlInBrowser(Strings.GITHUB_PROJECT_URL);
			}
		});
		menu.add(menuItem2);

		menu.addSeparator();

		JMenuItem menuItem3 = new JMenuItem("Developed by Majeur: View Github page");
		menuItem3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openUrlInBrowser(Strings.GITHUB_MAJEUR_URL);
			}
		});
		menu.add(menuItem3);

		menuBar.add(menu);
		setJMenuBar(menuBar);
	}

	private void openUrlInBrowser(String url) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop()
				: null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(new URI(url));
			} catch (IOException | URISyntaxException exception) {
				exception.printStackTrace();
			}
		}
	}

	private JPanel buildDevicesPanel(int width) {
		JPanel devicesPanel = new JPanel();
		devicesPanel.setPreferredSize(new Dimension(width, 100));
		devicesPanel.setBorder(BorderFactory.createTitledBorder("Devices"));

		mRadioButtonGroup = new RadioButtonGroup();
		mRadioButtonGroup
				.setOnRadioSelectedListener(new RadioButtonGroup.OnRadioSelectedListener() {
					@Override
					public void onSelected(int index) {
						setCurrentDevice(mDevices[index]);
					}
				});
		devicesPanel.add(mRadioButtonGroup);

		return devicesPanel;
	}

	private JPanel buildRenderingOptionPanel(int width) {
		JPanel renderingPanel = new JPanel();
		renderingPanel.setPreferredSize(new Dimension(width, 165));
		renderingPanel.setBorder(BorderFactory.createTitledBorder("Rendering"));

		JCheckBox updateCheckBox = new JCheckBox();
		updateCheckBox.setText("Refresh screen");
		updateCheckBox.setSelected(true);
		updateCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED)
					mScreenPanel.setPaused(true);
				else if (e.getStateChange() == ItemEvent.SELECTED)
					mScreenPanel.setPaused(false);
			}
		});
		renderingPanel.add(updateCheckBox);

		JCheckBox landscapeCheckBox = new JCheckBox();
		landscapeCheckBox.setText("Landscape");
		landscapeCheckBox.setSelected(false);
		landscapeCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED)
					mScreenPanel.setLandscape(false);
				else if (e.getStateChange() == ItemEvent.SELECTED)
					mScreenPanel.setLandscape(true);
			}
		});
		renderingPanel.add(landscapeCheckBox);

		NumberChooserPanel numberChooser = new NumberChooserPanel("Scale");
		numberChooser.setValue(50);
		numberChooser
				.setOnValueChangedListener(new NumberChooserPanel.OnValueChangedListener() {
					@Override
					public void onValueChanged(int newValue) {
						mScreenPanel.setScale(newValue / 100.0);
					}
				});
		renderingPanel.add(numberChooser);

		final JButton saveScreenShot = new JButton();
		saveScreenShot.setText("Save screenshot");
		saveScreenShot.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int result = fileChooser.showOpenDialog(saveScreenShot);

				if (result == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					mAdbHelper.saveScreenShotToFile(file);
				}
			}
		});
		renderingPanel.add(saveScreenShot);

		return renderingPanel;
	}

	private JPanel buildInfoPanel(int width) {
		JPanel extraPanel = new JPanel();
		extraPanel.setPreferredSize(new Dimension(width, 110));
		extraPanel.setBorder(BorderFactory.createTitledBorder("Extra"));
		
		final JButton inputKeysWindowButton = new JButton();
		inputKeysWindowButton.setText("Open Input Keys window");
		inputKeysWindowButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (mInputKeysFrame == null) {
					mInputKeysFrame = new InputFrame(mAdbHelper);
					mInputKeysFrame.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent windowEvent) {
							mInputKeysFrame = null;
						}
					});
				} else {
					mInputKeysFrame.toFront();
				}
			}
		});
		extraPanel.add(inputKeysWindowButton);
		
		final JButton logWindowButton = new JButton();
		logWindowButton.setText("Open Log window");
		logWindowButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (mLogFrame == null) {
					mLogFrame = new LogFrame();
					mLogFrame.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent windowEvent) {
							mLogFrame = null;
						}
					});
				} else {
					mLogFrame.toFront();
				}
			}
		});
		extraPanel.add(logWindowButton);

		return extraPanel;
	}

}
