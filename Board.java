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

	private Image star;
	private Thread animator;
	private int x, y;
	private Dimension panelSize;

	public Board(Dimension parentSize) {
		// rememver the size of our parent
		panelSize = parentSize;

		// initial limit of how far the image will move within this JPanel.
		// we will further limit the movement once we get the scaled image.
		B_HEIGHT = panelSize.height * 90 / 100;
		B_WIDTH = panelSize.width * 90 / 100;
		
		INITIAL_X = 1;
		INITIAL_Y = 1;
		
		initBoard();
	}

	private void loadImage() {

		// load and scale the image
		try {
			File f = new File("images/spaceinvaders/player.png");
			if(!f.exists()){
				String errorMessage = String.format("file %s does not exist", f.getAbsolutePath());
				System.out.println(errorMessage);
				this.setVisible(false);				
				throw new FileNotFoundException(errorMessage);

			}
			Image image = ImageIO.read(f);
			System.out.format("Image original width=%d, height=%d%n", image.getWidth(null), image.getHeight(null));
			
			// scale the image to 10% of whatever screen size we are running on.
			int width = (int) (this.panelSize.width / 10.0);
			int height = (int) (this.panelSize.height / 10.0);
			star = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			System.out.format("Image scaled width=%d, height=%d%n", star.getWidth(null), star.getHeight(null));
			
			// adjust the maximum movement limits of the image
			B_WIDTH -= width / 2;
			B_HEIGHT -= height / 2;
		} catch (IOException e) {
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

		// Image simg = new Image(star);
		g.drawImage(star, x, y, this);
		Toolkit.getDefaultToolkit().sync();
	}

	private void moveImage() {

		// change image's (x,y) coordinate so that it will move diagonally from top-left
		// to top-right
		x += B_WIDTH * 0.6 / 100.0;
		y += B_HEIGHT * 0.6 / 100.0;

		if (y > B_HEIGHT) {

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

			timeDiff = System.currentTimeMillis() - beforeTime;
			sleep = DELAY - timeDiff;

			if (sleep < 0) {
				sleep = 2;
			}

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