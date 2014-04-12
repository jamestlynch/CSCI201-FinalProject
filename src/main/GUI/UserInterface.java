package main.GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class UserInterface extends JPanel {
	public UserInterface()
	{
		
		JTabbedPane ThreeOpts = new JTabbedPane();
		SetRoutePanel SetRoute = new SetRoutePanel(300, 400);
		
		ThreeOpts.addTab("Set Route", SetRoute); //Add the name of the JPanel of the chart
		JPanel DisplayChart = new JPanel();
		
		ThreeOpts.addTab("Chart", DisplayChart);//Add the name of the JPanel
		JPanel DisplayGraph = new JPanel();
		
		ThreeOpts.addTab("Graph", DisplayGraph);//Add the name of the JPanel
		add(ThreeOpts, BorderLayout.NORTH);
		
	}
	public static void main(String[] args)
	{
		JFrame a = new JFrame();
		a.setSize(500, 500);
		a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		a.add(new UserInterface(), BorderLayout.EAST);
		a.setVisible(true);
	}
}