package com.majeur.ars;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ControlPanel extends JPanel implements MouseListener, KeyListener {
	
	private AdbHelper mAdbHelper;
	private BufferedImage mImage;
	private File mImageFile;
	private int mScreenWidth = 0, mScreenHeight = 0;
	private double mRatio;
	private boolean mFirstDraw = true;
	private double mScale = 1;
	private int mDownX, mDownY;
	private long mSwipeStartTime;
	
	protected Thread mUpdateThread;

	public ControlPanel(AdbHelper helper) {
		mAdbHelper = helper;
		mImageFile = new File("screen.png");
		addMouseListener(this);
		addKeyListener(this);
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent e) {
				stopUpdate();
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				requestFocus();
				requestFocusInWindow();
				
				startUpdate();
			}
		});
	}
	
	public void startUpdate() {
		System.out.println("startUpdate");
		if (mUpdateThread != null && !mUpdateThread.isInterrupted())
			stopUpdate();
		
		mUpdateThread = new Thread(new Runnable() {			
			@Override
			public void run() {
				while(!Thread.interrupted()) {
					mAdbHelper.performScreenShot(mImageFile);
					
					try {
						mImage = ImageIO.read(mImageFile);	
						repaint();
					} catch(IOException e) {
						
					}
					
					try {
						Thread.sleep(1000);
					} catch(InterruptedException ex) {
						break;
					}
				}
			}
		});
		mUpdateThread.start();
	}
	
	public void stopUpdate() {
		System.out.println("stopUpdate");
		mUpdateThread.interrupt();
		mUpdateThread = null;
	}
	
	public void setScale(double scale) {
		mScale = scale;
		setPreferredSize(new Dimension((int) (mScreenWidth * scale), (int) (mScreenHeight * scale)));
		MainFrame mainFrame = (MainFrame) getTopLevelAncestor();
		mainFrame.pack();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(mImage != null) {
			mScreenWidth = mImage.getWidth();
			mScreenHeight = mImage.getHeight();
			
			if (mFirstDraw) {
				setScale(0.5);
				mFirstDraw = false;
			}
									
			int width = getWidth();
			int height = getHeight();
			
			double ratioX = (double) width / (double) mScreenWidth;
			double ratioY = (double) height / (double) mScreenHeight;
			
			mRatio = Math.min(1, Math.min(ratioX, ratioY));
			
			double scaledWidth = (double) mScreenWidth * mRatio;
			double scaledHeight = (double) mScreenHeight * mRatio;
			
			g.drawImage(mImage, 0, 0, (int) scaledWidth, (int) scaledHeight, null);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		mAdbHelper.performClick(e.getX() / mRatio, e.getY() / mRatio);		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mDownX = e.getX();
		mDownY = e.getY();
		mSwipeStartTime = System.currentTimeMillis();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int upX = e.getX(), upY = e.getY();
		
		int dx = (int) Math.abs(mDownX - upX);
		int dy = (int) Math.abs(mDownY - upY);
		
		if (dx > 5 && dy > 5) {
			long duration = System.currentTimeMillis() - mSwipeStartTime;
			mAdbHelper.performSwipe(mDownX / mRatio, mDownY / mRatio, upX / mRatio, upY / mRatio, duration);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
