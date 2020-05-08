package com.majeur.ars;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class NumberChooserPanel extends JPanel {
	
	private JTextArea mTextArea;
	private JButton mMinusButton, mPlusButton;
	private int mValue;
	private OnValueChangedListener mListener;
	
	public NumberChooserPanel(String title) {
		
		mMinusButton = new JButton();
		mMinusButton.setText("-");
		mMinusButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				setValue(mValue - 10);
			}
		});
		
		mPlusButton = new JButton();
		mPlusButton.setText("+");
		mPlusButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				setValue(mValue + 10);
			}
		});
		
		mTextArea = new JTextArea();
		mTextArea.setEditable(false);
		
		JTextArea titleTextArea = new JTextArea(title);
		titleTextArea.setEditable(false);
		
		add(mMinusButton, BorderLayout.WEST);
		add(mTextArea, BorderLayout.CENTER);
		add(mPlusButton, BorderLayout.EAST);
		add(titleTextArea, BorderLayout.NORTH);
	}

	public void setValue(int value) {
		mValue = value;
		mTextArea.setText(String.valueOf(value));
		if (mListener != null)
			mListener.onValueChanged(value);
		
		mPlusButton.setEnabled(mValue < 100);
		mMinusButton.setEnabled(mValue > 20);
	}
	
	public int getValue() {
		return mValue;
	}
	
	public void setOnValueChangedListener(OnValueChangedListener l) {
		mListener = l;
	}
	
	public interface OnValueChangedListener {
		public void onValueChanged(int newValue);
	}
}
