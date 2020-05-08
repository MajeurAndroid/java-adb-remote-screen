package com.majeur.ars;

import com.majeur.ars.adb.AdbHelper;

import javax.imageio.IIOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class AdbDeviceScreenComponent extends JComponent implements MouseListener, KeyListener {

	private static final long MIN_SCREEN_REFRESH_INTERVAL = 250;
	private static final Dimension DEFAULT_SCREEN_SIZE = new Dimension(720, 1080);

	private AdbHelper mAdbHelper;
	private volatile BufferedImage mImage;
	private int mDownX, mDownY;
	private long mSwipeStartTime;

	protected volatile Thread mUpdateThread;

	public AdbDeviceScreenComponent(AdbHelper helper) {
		mAdbHelper = helper;
		addMouseListener(this);
		addKeyListener(this);

		setPreferredSize(new Dimension((int) (DEFAULT_SCREEN_SIZE.getWidth() * 0.5),
				(int) (DEFAULT_SCREEN_SIZE.getHeight() * 0.5)));

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent e) {
				if (isRendering())
					stopUpdate();
			}

			@Override
			public void componentResized(ComponentEvent e) {
				requestFocus();
				requestFocusInWindow();
			}
		});
	}

	boolean isRendering() {
		return mUpdateThread != null && !mUpdateThread.isInterrupted();
	}

	public void startUpdate() {
		if (isRendering())
			throw new IllegalStateException("Already started");

		Logger.i("Start rendering device screen");
		mUpdateThread = new UpdateThread();
		mUpdateThread.start();
	}

	public void stopUpdate() {
		if (!isRendering())
			throw new IllegalStateException("Already stoped");

		Logger.i("Stop rendering device screen");
		mUpdateThread.interrupt();
		mUpdateThread = null;
		mImage = null;
		repaint();
	}

	public void setScale(double scale) {
		Dimension d;
		if (mImage == null)
			d = new Dimension((int) (DEFAULT_SCREEN_SIZE.getWidth() * scale),
					(int) (DEFAULT_SCREEN_SIZE.getHeight() * scale));
		else
			d = new Dimension((int) (mImage.getWidth() * scale), (int) (mImage.getHeight() * scale));

		if (!getPreferredSize().equals(d)) {
			setPreferredSize(d);
			((JFrame) getTopLevelAncestor()).pack();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (mImage != null) {
			double dRatio = mImage.getWidth() / (double) mImage.getHeight();
			double fRatio = getWidth() / (double) getHeight();
			if (!Double.valueOf(dRatio).equals(fRatio))
				setScale(0.5);

			g.drawImage(mImage, 0, 0, getWidth(), getHeight(), null);

		} else {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());

			String text = "Nothing to show";
			g.setColor(Color.WHITE);
			int textWidth = g.getFontMetrics().stringWidth(text);
			g.drawString(text, (getWidth() - textWidth) / 2, getHeight() / 2);
		}
	}

	private double computeTransformValue() {
		return (mImage == null ? DEFAULT_SCREEN_SIZE.getWidth() : mImage.getWidth()) / (double) getWidth();
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
		double transform = computeTransformValue();
		mAdbHelper.performClick(e.getX() * transform, e.getY() * transform);
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

		int dx = Math.abs(mDownX - upX);
		int dy = Math.abs(mDownY - upY);

		if (dx > 5 && dy > 5) {
			double transform = computeTransformValue();
			long duration = System.currentTimeMillis() - mSwipeStartTime;
			mAdbHelper.performSwipe(mDownX * transform, mDownY * transform, upX * transform, upY * transform, duration);
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

				if (dT < MIN_SCREEN_REFRESH_INTERVAL)
					Utils.sleep(MIN_SCREEN_REFRESH_INTERVAL - dT);

				if (Thread.interrupted())
					break;

				try {
					mImage = mAdbHelper.retrieveScreenShot();

					long t2 = System.currentTimeMillis();
					postFpsCount((t2 - currentFrameTime) / 1000d);

					if (Thread.interrupted())
						mImage = null;

					if (mImage == null)
						abort();
					else
						repaintPanel();

				} catch (IIOException e) {
					e.printStackTrace();
					Logger.w("Corrupted data received from device, skipping one frame");
					// Screencap is partially corrupted for that frame, let's try at the next one
				} catch (IOException e) {
					e.printStackTrace();
					abort();
				}
			}
		}

		private void abort() {
			interrupt();
			mUpdateThread = null;
			mImage = null;

			Utils.executeOnUiThread(() -> {
				Logger.e("Unable to retrieve screen capture, aborting rendering...");
				repaint();
			});
		}

		private void repaintPanel() {
			Utils.executeOnUiThread(AdbDeviceScreenComponent.this::repaint);
		}

		private void postFpsCount(final double dt) {
			Utils.executeOnUiThread(() -> ((MainFrame) getTopLevelAncestor()).getTitleManger().postFpsCount(1d / dt));
		}
	}

}
