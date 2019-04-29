import java.awt.Dimension;
import java.awt.EventQueue;
//
import javax.swing.JFrame;

public class ThreadAnimationExample extends JFrame {

	private Dimension frameSize;
	
	public ThreadAnimationExample(Dimension frameSize) {
		this.frameSize = frameSize;
		initUI();
	}

	private void initUI() {

		add(new Board(frameSize));

		setResizable(false);
		pack();

		setTitle("Star");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
