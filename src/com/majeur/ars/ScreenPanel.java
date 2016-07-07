package com.majeur.ars;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.imageio.ImageIO;
import javax.management.loading.PrivateMLet;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.omg.CORBA.PRIVATE_MEMBER;

public class ScreenPanel extends JPanel implements MouseListener, KeyListener {
	
	private static final long MIN_SCREEN_REFRESH_INTERVAL = 250;
	private AdbHelper mAdbHelper;
	private BufferedImage mImage;
	private int mScreenWidth = 0, mScreenHeight = 0;
	private double mRatio;
	private boolean mFirstDraw = true, mUpdateFrame = false;
	private double mScale = 1;
	private int mDownX, mDownY;
	private long mSwipeStartTime;
	private boolean mLandscape = false;
	
	private boolean mPaused;

	protected Thread mUpdateThread;

	public ScreenPanel(AdbHelper helper) {
		mAdbHelper = helper;
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
			}
		});
	}

	public void startUpdate() {
		if (mPaused)
			return;
		
		Logger.i("Start rendering device screen");
		if (mUpdateThread != null && !mUpdateThread.isInterrupted())
			stopUpdate();

		mUpdateThread = new UpdateThread();
		mUpdateThread.start();
	}

	public void stopUpdate() {
		if (mUpdateThread == null)
			return;
		
		Logger.i("Stop rendering device screen");
		mUpdateThread.interrupt();
		mUpdateThread = null;
	}
	
	public void setPaused(boolean paused) {
		mPaused = paused;
		
		if (paused)
			stopUpdate();
		else 
			startUpdate();
	}

	public void setScale(double scale) {
		mScale = scale;
		setPreferredSize(new Dimension((int) (mScreenWidth * scale),
				(int) (mScreenHeight * scale)));
		MainFrame mainFrame = (MainFrame) getTopLevelAncestor();
		mainFrame.pack();
	}

	public void setLandscape(boolean b) {
		mLandscape = b;
		mUpdateFrame = true;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (mImage != null) {
			mScreenWidth = mLandscape ? mImage.getHeight() : mImage.getWidth();
			mScreenHeight = mLandscape ? mImage.getWidth() : mImage.getHeight();

			if (mFirstDraw) {
				setScale(0.5);
				mFirstDraw = false;
			}
			
			if (mUpdateFrame) {
				setScale(mScale);
				mUpdateFrame = false;
			}

			int width = getWidth();
			int height = getHeight();

			double ratioX = (double) width / (double) mScreenWidth;
			double ratioY = (double) height / (double) mScreenHeight;

			mRatio = Math.min(1, Math.min(ratioX, ratioY));

			double scaledWidth = (double) mScreenWidth * mRatio;
			double scaledHeight = (double) mScreenHeight * mRatio;

			if (mLandscape) {
				AffineTransform transform = new AffineTransform();
				transform.rotate(Math.PI / 2, mImage.getWidth() / 2, mImage.getHeight() / 2);
				AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
				mImage = op.filter(mImage, null);
			}
			
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
			mAdbHelper.performSwipe(mDownX / mRatio, mDownY / mRatio, upX
					/ mRatio, upY / mRatio, duration);
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
	
	private class UpdateThread extends Thread {

		@Override
		public void run() {
			super.run();			
			long previousFrameTime = System.currentTimeMillis();
			
			while (!Thread.interrupted()) {
				long currentFrameTime = System.currentTimeMillis();
				long dT = currentFrameTime - previousFrameTime;
				previousFrameTime = currentFrameTime;
				
				if (LocalProperties.limitFrameRate && dT < MIN_SCREEN_REFRESH_INTERVAL) 
					Utils.sleep(MIN_SCREEN_REFRESH_INTERVAL - dT);

				mImage = mAdbHelper.retrieveScreenShot();
				
				if (mImage == null)
					abort();
				
				repaintPanel();
			}
		}
		
		private void abort() {
			interrupt();
			SwingUtilities.invokeLater(new Runnable() {				
				@Override
				public void run() {
					stopUpdate();					
				}
			});
		}
		
		private void repaintPanel() {			
			try {
				SwingUtilities.invokeAndWait(new Runnable() {						
					@Override
					public void run() {
						repaint();							
					}
				});
			} catch (InvocationTargetException | InterruptedException e) {
				abort();
				e.printStackTrace();
			}
		}
	}

}
