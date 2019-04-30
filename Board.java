import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Board extends JPanel implements Runnable {

	private int B_WIDTH = 350;
	private int B_HEIGHT = 350;
	private final int INITIAL_X = -40;
	private final int INITIAL_Y = -40;
	private final int DELAY = 25;

	private Image star;
	private Thread animator;
	private int x, y;
	private Dimension frameSize;

	public Board(Dimension frameSize) {
		this.frameSize = frameSize;
		initBoard();
	}

	private void loadImage() {
		System.out.println("loadImage-top");
		ImageIcon ii = new ImageIcon("/images/star.png");
		star = ii.getImage();		
		System.out.println("loadImage-bottom");
	}

	private void initBoard() {
		this.B_HEIGHT = this.frameSize.height;
		this.B_WIDTH = this.frameSize.width;

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

		g.drawImage(star, x, y, this);
		Toolkit.getDefaultToolkit().sync();
	}

	private void cycle() {

		x += 1;
		y += 1;

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

			cycle();
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
