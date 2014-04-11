package main.GUI;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class UserInterface extends JPanel {
	public UserInterface()
	{
		JTabbedPane ThreeOpts = new JTabbedPane();
		/*ThreeOpts.add("Set Route",); //Add the name of the JPanel of the chart);
		ThreeOpts.add("Chart", );//Add the name of the JPanel);
		ThreeOpts.add("Graph", );//Add the name of the JPanel);*/
		add(ThreeOpts, BorderLayout.NORTH);
		
	}
	public static void main2(String[] args)
	{
		JFrame a = new JFrame();
		a.add(new UserInterface());
	}
}
