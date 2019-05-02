import java.awt.Dimension;
//
import javax.swing.JFrame;

public class ThreadAnimationExample extends JFrame {

	private static final long serialVersionUID = 1L;
	private Dimension frameSize;

	public ThreadAnimationExample(Dimension frameSize) {
		this.frameSize = frameSize;
		initUI();
	}

	private void initUI() {

		setSize(frameSize);
		setPreferredSize(frameSize);
		
		add(new Board(frameSize));

		setResizable(false);
		pack();

		setTitle("Star");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
