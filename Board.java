
// Simple Java Animation Example using Swing components.
// Reference: http://zetcode.com/tutorials/javagamestutorial/animation/
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
//import javax.swing.Icon;
//import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Board extends JPanel implements Runnable {

	private int B_WIDTH = 350;
	private int B_HEIGHT = 350;
	private int INITIAL_X = -40;
	private int INITIAL_Y = -40;
	private final int DELAY = 25;
	private final int MIN_DELAY = 2;

	private Image scaledImage;
	private Thread animator;
	private int x, y;
	private Dimension panelSize;

	public Board(Dimension parentSize) {

		// remember the size of our parent
		panelSize = parentSize;

		// initial limit of how far the image will move within this JPanel.
		// We will further limit the movement once we get the scaled image.
		B_HEIGHT = panelSize.height * 92 / 100;
		B_WIDTH = panelSize.width * 92 / 100;

		INITIAL_X = 2;
		INITIAL_Y = 2;

		initBoard();
	}

	private Image getImageFromFile(String imagePath) {

		// load and scale the image
		try {
			File f = new File(imagePath);
			if (!f.exists()) {
				String errorMessage = String.format("file %s does not exist", f.getAbsolutePath());
				System.out.println(errorMessage);
				throw new FileNotFoundException(errorMessage);
			}
			Image unscaledImage = ImageIO.read(f);
			System.out.format("Image original width=%d, height=%d%n", unscaledImage.getWidth(null),
					unscaledImage.getHeight(null));
			return unscaledImage;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void loadImage() {

		// load and scale the image
		try {
			Image unscaledImage = getImageFromFile("images/star.png");
			System.out.format("Image original width=%d, height=%d%n", unscaledImage.getWidth(null),
					unscaledImage.getHeight(null));

			// scale the image to 10% of whatever screen size we are running on.
			int width = (int) (this.panelSize.width / 10.0);
			int height = (int) (this.panelSize.height / 10.0);
			scaledImage = unscaledImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			System.out.format("Image scaled width=%d, height=%d%n", scaledImage.getWidth(null),
					scaledImage.getHeight(null));

			// adjust the maximum movement limits of the image
			B_WIDTH -= width / 2;
			B_HEIGHT -= height / 2;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initBoard() {

		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));

		loadImage();

		x = INITIAL_X;
		y = INITIAL_Y;
	}

	@Override
	public void addNotify() {
		super.addNotify();
		animator = new Thread(this);
		animator.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		drawStar(g);
	}

	private void drawStar(Graphics g) {
		g.drawImage(scaledImage, x, y, this);
		Toolkit.getDefaultToolkit().sync();
	}

	private void moveImage() {

		// change image's (x,y) coordinate so that it will move diagonally from top-left
		// to top-right
		final double incrementalMove = 0.4 / 100.0;
		x += B_WIDTH * incrementalMove;
		y += B_HEIGHT * incrementalMove;

		if (y > B_HEIGHT || x > B_WIDTH) {
			y = INITIAL_Y;
			x = INITIAL_X;
		}
	}

	@Override
	public void run() {

		long beforeTime, timeDiff, sleep;

		beforeTime = System.currentTimeMillis();

		while (true) {

			moveImage();
			repaint();
			// reduce the amount of the sleep delay by how much time it took to move
			// the image and repaint it:
			timeDiff = System.currentTimeMillis() - beforeTime;
			
			// sleep for a minimum amount of milliseconds, possibly longer
			sleep = Math.max( DELAY - timeDiff, MIN_DELAY);

			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {

				String msg = String.format("Thread interrupted: %s", e.getMessage());

				JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
			}

			beforeTime = System.currentTimeMillis();
		}
	}
}