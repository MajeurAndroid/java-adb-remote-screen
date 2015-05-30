package com.majeur.ars;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;

public class AdbRemoteScreen {

	public static void main(String[] args) {
		final AdbHelper adbHelper = new AdbHelper();
				
		MainFrame frame = new MainFrame();
		frame.setVisible(true);
		
		final ControlPanel controlPanel = new ControlPanel(adbHelper);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setPreferredSize(new Dimension(150, panel.getHeight()));
		
		JPanel savePanel = new JPanel();
	    savePanel.setBorder(BorderFactory.createTitledBorder("Save"));
		
		final JButton saveScreenShot = new JButton();
		saveScreenShot.setText("Save screenshot");
		saveScreenShot.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int result = fileChooser.showOpenDialog(saveScreenShot);
				
				if (result == JFileChooser.APPROVE_OPTION) {		            
					File file = fileChooser.getSelectedFile();
					adbHelper.performScreenShot(file);
				}
			}
		});;
		savePanel.add(saveScreenShot);
		panel.add(savePanel);
		
		JPanel updatePanel = new JPanel();
	    updatePanel.setBorder(BorderFactory.createTitledBorder("Screen rendering"));
		JCheckBox checkBox = new JCheckBox();
		checkBox.setText("Update");
		checkBox.setSelected(true);
		checkBox.addItemListener(new ItemListener() {			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED)
					controlPanel.stopUpdate();
				else if (e.getStateChange() == ItemEvent.SELECTED)
					controlPanel.startUpdate();
			}
		});
		updatePanel.add(checkBox);		
		panel.add(updatePanel);
	    
	    JPanel controlPanelScale = new JPanel();
	    NumberChooserPanel numberChooser = new NumberChooserPanel();
	    numberChooser.setValue(50);
	    numberChooser.setOnValueChangedListener(new NumberChooserPanel.OnValueChangedListener() {			
			@Override
			public void onValueChanged(int newValue) {
				controlPanel.setScale(newValue / 100.0);			
			}
		});
	    controlPanelScale.add(numberChooser);
	    controlPanelScale.setBorder(BorderFactory.createTitledBorder("Scale"));
	    panel.add(controlPanelScale);
	    
	    JPanel infoPanel = new JPanel();
	    JTextArea infoTextArea = new JTextArea();
	    adbHelper.setTextArea(infoTextArea);
	    JScrollPane scrollPane = new JScrollPane(infoTextArea);
	    scrollPane.setPreferredSize(new Dimension(130, 50));
	    infoPanel.add(scrollPane);
	    infoPanel.setBorder(BorderFactory.createTitledBorder("Info"));
	    panel.add(infoPanel);
	    adbHelper.setTextArea(infoTextArea);
	    

		System.out.println("start");
		frame.getContentPane().add(panel, BorderLayout.WEST);
		frame.getContentPane().add(controlPanel, BorderLayout.CENTER);
		

		String devices = adbHelper.executeAdbCommand("devices");
		if (!devices.matches(".*\\d.*"))
			JOptionPane.showMessageDialog(null, "No device connected", "Error", JOptionPane.INFORMATION_MESSAGE);
	}
}
