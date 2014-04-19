package main.GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import main.map.GeoMapModel;

public class UISidePanel extends JPanel {
	GeoMapModel geoMapModel;
	public UISidePanel(GeoMapModel geoMapModel)
	{
		this.geoMapModel = geoMapModel;
		
		JTabbedPane ThreeOpts = new JTabbedPane();
		
		SetRoutePanel SetRoute = new SetRoutePanel(400, 500, geoMapModel);
		ThreeOpts.addTab("Set Route", SetRoute); //Add the name of the JPanel of the chart

		JButton jb = new JButton ("Calculate Route");
		jb.setBounds(5, 500 - jb.getPreferredSize().height - 30, 400- 10, jb.getPreferredSize().height);
		BtnListener activateRoute = new BtnListener(SetRoute, ThreeOpts);
		jb.addActionListener(activateRoute);
		SetRoute.add(jb);
		
		
		SetChartPanel DisplayChart = new SetChartPanel(400, 500);
		ThreeOpts.addTab("Chart", DisplayChart);//Add the name of the JPanel
		ThreeOpts.setEnabledAt(1, false);
		
		
		SetGraphPanel DisplayGraph = new SetGraphPanel(400, 500);
		ThreeOpts.addTab("Graph", DisplayGraph);//Add the name of the JPanel
		ThreeOpts.setEnabledAt(2, false);
		//add(new Map(600, 800), BorderLayout.EAST);
		
		add(ThreeOpts, BorderLayout.WEST);
	}
	
}
class BtnListener implements ActionListener
{
	SetRoutePanel SetRoute;
	JTabbedPane ThreeOpts;
	public BtnListener(SetRoutePanel SetRoute, JTabbedPane ThreeOpts)
	{
		this.SetRoute = SetRoute;
		this.ThreeOpts = ThreeOpts;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (SetRoute.isValidEntry())
		{
			ThreeOpts.setEnabledAt(1, true);
			ThreeOpts.setEnabledAt(2, true);
		}
	}
	
}
