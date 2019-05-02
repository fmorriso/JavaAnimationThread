// Simple Java Animation Example using Swing components.
// Reference: http://zetcode.com/tutorials/javagamestutorial/animation/
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

public class Driver {

	public static void main(String[] args) {

		// capture size of screen we're using
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		// Define the size of the JFrame as a square that is a percentage of the
		// available screen area and a multiple of 100.
		final int frameHeight = (int) (screenSize.height * 90.00 / 100) / 100 * 100;
		final int frameWidth = frameHeight;

		Dimension frameSize = new Dimension(frameWidth, frameHeight);
		//System.out.format("frame width=%d, height=%d%n", frameWidth, frameHeight);

		EventQueue.invokeLater(() -> {
			ThreadAnimationExample ex = new ThreadAnimationExample(frameSize);
            ex.setVisible(true);
        });

	}

}
