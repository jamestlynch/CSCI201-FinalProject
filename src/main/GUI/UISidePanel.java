package main.GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;



//import main.freeway.FastestPath;
import main.freeway.FreewaySegment;
import main.map.GeoMapModel;
import main.map.GeoMapView;
import main.sql.FastestPathh;
import main.sql.SQLDatabaseHandler;

public class UISidePanel extends JPanel {
	GeoMapModel geoMapModel;
	GeoMapView geoMapView;
	String BeginLocation;
	String EndLocation;
	String BeginFreeway;
	String EndFreeway;
	SQLDatabaseHandler sql;
	FastestPathh fp;
	
	private boolean runningDatabase;
	
//	FastestPath calculateFastestPath;
	public String getBeginLocation() {
		return BeginLocation;
	}

	public String getEndLocation() {
		return EndLocation;
	}

	public UISidePanel(GeoMapModel geoMapModel, GeoMapView geoMapView, SQLDatabaseHandler sql)
	{
		this.geoMapModel = geoMapModel;
		this.geoMapView = geoMapView;
		this.sql = sql;
		JTabbedPane ThreeOpts = new JTabbedPane();
		
		SetRoutePanel SetRoute = new SetRoutePanel(400, 500, geoMapModel);
		ThreeOpts.addTab("Set Route", SetRoute); //Add the name of the JPanel of the chart
		
		SetChartPanel DisplayChart = new SetChartPanel(400, 500);
		ThreeOpts.addTab("Chart", DisplayChart);//Add the name of the JPanel
		ThreeOpts.setEnabledAt(1, false);
		
		
		SetGraphPanel DisplayGraph = new SetGraphPanel(400, 500);
		ThreeOpts.addTab("Graph", DisplayGraph);//Add the name of the JPanel
		ThreeOpts.setEnabledAt(2, false);
		
		JButton jb = new JButton ("Calculate Route");
		jb.setBounds(5, 500 - jb.getPreferredSize().height - 30, 400- 10, jb.getPreferredSize().height);
		BtnListener activateRoute = new BtnListener(SetRoute, ThreeOpts, DisplayChart, DisplayGraph);
		jb.addActionListener(activateRoute);
		SetRoute.add(jb);
		//add(new Map(600, 800), BorderLayout.EAST);
		
		add(ThreeOpts, BorderLayout.WEST);
	}

	class BtnListener implements ActionListener
	{
		SetRoutePanel SetRoute;
		JTabbedPane ThreeOpts;
		SetChartPanel DisplayChart;
		SetGraphPanel DisplayGraph;
		
		public BtnListener(SetRoutePanel SetRoute, JTabbedPane ThreeOpts, SetChartPanel DisplayChart, SetGraphPanel DisplayGraph)
		{
			this.SetRoute = SetRoute;
			this.ThreeOpts = ThreeOpts;
			this.DisplayChart = DisplayChart;
			this.DisplayGraph = DisplayGraph;
		}
		public void actionPerformed(ActionEvent e) {
			if (SetRoute.isValidEntry())
			{
				ThreeOpts.setEnabledAt(1, true);
				ThreeOpts.setEnabledAt(2, true);
				BeginLocation = SetRoute.getBeginningLocation();
				EndLocation = SetRoute.getEndingLocation();
				BeginFreeway = SetRoute.getBeginFreeway();
				EndFreeway = SetRoute.getEndFreeway();
				fp = new FastestPathh(geoMapModel, geoMapView);
				ArrayList<FreewaySegment> fastestPathSegs = fp.calculateFastestPath(BeginLocation, EndLocation, BeginFreeway, EndFreeway);
				double SpeedLimitTimeToTravel = fp.getSpeedLimitTimeToTravel(fastestPathSegs);
//				calculateFastestPath = new FastestPath(BeginLocation, EndLocation, geoMapModel);
//				ArrayList<FreewaySegment> fastestPathSegments = new ArrayList<FreewaySegment>();// = calculateFastestPath.getFastestPath();
				ArrayList<Double> TimeVal = new ArrayList<Double>();
				for (int i = 0 ; i < 24; i++)
				{
					TimeVal.add(i, 0.0);
				}
				if (runningDatabase) 
				{
					for (int i = 0; i < fastestPathSegs.size(); i++)
					{
						ArrayList<Double> speedsAtTimeI = sql.getAverageSpeeds(fastestPathSegs.get(i));
						for (int j = 0 ; j < 24; j++)
						{
							Double timeAtSegI = TimeVal.get(j) + (fastestPathSegs.get(i).getDistance()*60/speedsAtTimeI.get(j));
							TimeVal.set(j, timeAtSegI);
						}
						//fastestPathSegments.
					}
				}
//				//Establish an ArrayList of Strings with all the time values.
//				
//				for (int i = 0; i < 24; i++)
//				{
//					TimeVal.add(i, 30+Math.random()*50);
//				}
				double currentSpeedTime = fp.getCurrentSpeedTimeToTravel(fastestPathSegs);
				double speedLimitTime = fp.getSpeedLimitTimeToTravel(fastestPathSegs);
				SetRoute.displayTotalTime(currentSpeedTime, speedLimitTime);
				DisplayChart.setLocations(BeginLocation, EndLocation, TimeVal);
				DisplayGraph.setGraphValues(TimeVal);
				DisplayGraph.repaint();
				
				//DisplayGraph
				
			}
		}
		
	}
	
}
