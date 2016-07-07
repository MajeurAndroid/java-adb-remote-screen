package com.majeur.ars;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class RadioButtonGroup  extends JPanel {
	
		interface OnRadioSelectedListener {
			void onSelected(int index);
		}
		
		private OnRadioSelectedListener mListener;
	
		RadioButtonGroup (){
		     setLayout(new FlowLayout());
		     setVisible(true);
		}
		
		void setOnRadioSelectedListener(OnRadioSelectedListener listener) {
			mListener = listener;
		}
		
		void setRadioButtons(String[] titles) {
			removeAll();
			revalidate();
			repaint();
			
			if (titles == null || titles.length == 0)
				return;
			
			Box box = Box.createVerticalBox();
			ButtonGroup buttonGroup = new ButtonGroup();
			setSize(new Dimension(400, titles.length * 100));
			
			for (int i = 0; i < titles.length; i++) {
				JRadioButton radioButton = new JRadioButton(titles[i]);
				final int index = i;
				radioButton.addActionListener(new ActionListener() {					
					@Override
					public void actionPerformed(ActionEvent e) {
						if (mListener != null)
							mListener.onSelected(index);						
					}
				});
				buttonGroup.add(radioButton);
				box.add(radioButton);
			}
			
			add(box);
		}
		
		void setSelectedRadio(int index) {
			Box box = (Box) getComponent(0);
			((JRadioButton) box.getComponent(index)).setSelected(true);		
		}
}