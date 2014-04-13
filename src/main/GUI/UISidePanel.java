package main.GUI;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import main.map.GeoMap;

public class UISidePanel extends JPanel {
	
	public UISidePanel()
	{
		
		JTabbedPane ThreeOpts = new JTabbedPane();
		SetRoutePanel SetRoute = new SetRoutePanel(300, 450);
		
		ThreeOpts.addTab("Set Route", SetRoute); //Add the name of the JPanel of the chart
		
		SetChartPanel DisplayChart = new SetChartPanel(300, 450);
		ThreeOpts.addTab("Chart", DisplayChart);//Add the name of the JPanel
		
		SetGraphPanel DisplayGraph = new SetGraphPanel();
		ThreeOpts.addTab("Graph", DisplayGraph);//Add the name of the JPanel
		
		//add(new Map(600, 800), BorderLayout.EAST);
		
		add(ThreeOpts, BorderLayout.WEST);
		
	}
	public static void main(String[] args)
	{
		JFrame a = new JFrame();
		a.setSize(500, 500);
		a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		a.add(new UISidePanel(), BorderLayout.EAST);
		a.setVisible(true);
	}
}
