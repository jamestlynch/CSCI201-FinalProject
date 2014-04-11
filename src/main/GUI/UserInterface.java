package main.GUI;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class UserInterface extends JPanel {
	public UserInterface()
	{
		setSize(10000,10000);
		JTabbedPane ThreeOpts = new JTabbedPane();
		JPanel SetRoute = new JPanel();
		SetRoute.add();
		SetRoute.setSize(200, 200);
		ThreeOpts.addTab("Set Route", SetRoute); //Add the name of the JPanel of the chart);
		JPanel DisplayChart = new JPanel();
		
		ThreeOpts.addTab("Chart", DisplayChart);//Add the name of the JPanel);
		JPanel DisplayGraph = new JPanel();
		ThreeOpts.addTab("Graph", DisplayGraph);//Add the name of the JPanel);
		add(ThreeOpts, BorderLayout.NORTH);
		
	}
	public static void main(String[] args)
	{
		JFrame a = new JFrame();
		a.setSize(1000, 1000);
		a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		a.add(new UserInterface(), BorderLayout.EAST);
		a.setVisible(true);
	}
}
