import java.awt.Dimension;

import javax.swing.JFrame;

import org.openstreetmap.gui.jmapviewer.JMapViewer;


public class JMapViewerTest extends JFrame {
	public JMapViewerTest() {
		super("JMapViewerTest");
		this.setSize(new Dimension(600, 600));
		
		JMapViewer map = new JMapViewer();
		
		add(map);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new JMapViewerTest();
	}
}
