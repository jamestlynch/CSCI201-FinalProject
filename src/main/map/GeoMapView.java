package main.map;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;

import main.CSCI201Maps;
import main.automobile.Automobile;
import main.freeway.FreewaySegment;
import main.jsonfile.JSONFileGetter;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;

public class GeoMapView extends JPanel implements Runnable {
	private int panelWidth;
	private int panelHeight;
	
	private JMapViewer mapViewer = new JMapViewer();
	private Coordinate startLocation = new Coordinate(34.05, -118.25);
	private int startZoom = 12;
	
	private ArrayList<MapPolygon> polygonsToDraw = new ArrayList<MapPolygon>();//can move to inside of drawPath method if you want a new path to be drawn every time
	
	private GeoMapModel geoMapModel;
	
	private boolean debuggingDrawPath = true;
	private boolean debuggingDrawAutomobiles = true;
	private boolean debuggingMapUpdateLock = true;
	
	public GeoMapView(int width, int height, GeoMapModel geoMapModel) 
	{
		this.geoMapModel = geoMapModel;
		this.panelWidth = width;
		this.panelHeight = height;
		
		//setLayout(null);
		setPreferredSize(new Dimension(panelWidth, panelHeight));
		
		mapViewer.setDisplayPositionByLatLon(startLocation.getLat(), startLocation.getLon(), startZoom);
//		MapMarkerCircle circle = new MapMarkerCircle(34.05, -118.25, .005);
//		circle.setColor(Color.RED);
//		circle.setBackColor(Color.GREEN);
//		
//		mapViewer.addMapMarker(circle);
//		mapViewer.setMapMarkerVisible(true);
		//drawAutomobiles();
		add(mapViewer);
	}

	public JMapViewer getMapViewer() {
		return mapViewer;
	}
	
	//Draws fastest path on map
	public void drawPath(ArrayList<FreewaySegment> freewaysegments)
	{
		ArrayList<Coordinate> pathToDraw = new ArrayList<Coordinate>();
		for (int i = 0; i < freewaysegments.size(); i++)
		{
			for (int j = 0; j < freewaysegments.get(i).getSegmentPath().size(); j++)
			{
				pathToDraw.add(new Coordinate(freewaysegments.get(i).getSegmentPath().get(j).getLat(),freewaysegments.get(i).getSegmentPath().get(j).getLon() ));
			}

		}
		for (int i = pathToDraw.size()-1; i>=0; i--)
		{
			pathToDraw.add(new Coordinate(pathToDraw.get(i).getLat(), pathToDraw.get(i).getLon()));
		}

		if (debuggingDrawPath) System.out.println("[DRAW PATH] Number freeway segments: " + freewaysegments.size());
		if (debuggingDrawPath) System.out.println("[DRAW PATH] Number of points on pathToDraw: " + pathToDraw.size());
		
		//ArrayList<MapPolygon> polygonsToDraw = new ArrayList<MapPolygon>(); 
		MapPolygonImpl polygon = new MapPolygonImpl(pathToDraw);
		polygon.setColor(Color.RED);
		polygonsToDraw.add(polygon);
	
		mapViewer.setMapPolygonList(polygonsToDraw);
		mapViewer.setMapPolygonsVisible(true);
	}
	
	public void setAutomobileMarkers()
	{
		if (debuggingDrawAutomobiles) System.out.println("[SET AUTOMOBILES] Method called.");
		ArrayList<Automobile> automobilesToDisplay = geoMapModel.getAutomobilesInFreewayNetwork();
		if (debuggingDrawAutomobiles) System.out.println("[SET AUTOMOBILES] Amount of automobiles to draw: " + automobilesToDisplay.size());
		
		for (int i = 0 ; i < automobilesToDisplay.size(); i++)
		{
			synchronized(geoMapModel.getAutomobilesInFreewayNetwork())
			{
				mapViewer.addMapMarker(automobilesToDisplay.get(i).getCarMarker());
			}
		}
	}
	
	//This method should be called in this MapView's threading
	public void drawAutomobiles()
	{
		if (debuggingDrawAutomobiles) System.out.println("[DRAW AUTOMOBILES] Method called.");
		ArrayList<Automobile> automobilesToDisplay = geoMapModel.getAutomobilesInFreewayNetwork();
		if (debuggingDrawAutomobiles) System.out.println("[DRAW AUTOMOBILES] Amount of automobiles to draw: " + automobilesToDisplay.size());
				
		if (debuggingDrawAutomobiles) System.out.println("DRAW AUTOMOBILES] Size of MapMarkerList: " + mapViewer.getMapMarkerList().size());
		
		for (int i = 0 ; i < mapViewer.getMapMarkerList().size(); i++)
		{
			synchronized(geoMapModel.getAutomobilesInFreewayNetwork())
			{
				try {
					mapViewer.getMapMarkerList().get(i);
				} catch (IndexOutOfBoundsException ioobe) {
					System.out.println("IndexOutOfBounds");
				}
			}
		}
		
		mapViewer.setMapMarkerVisible(true);
	}
	
	public void redrawSingleAutomobile(MapMarkerCircle automobileMarker)
	{
		mapViewer.removeMapMarker(automobileMarker);
		mapViewer.addMapMarker(automobileMarker);
	}
	
	public void eraseAutomobiles()
	{
		mapViewer.removeAllMapMarkers();
	}

	public void run() {
		while(true) {
			try {
				CSCI201Maps.grabMapUpdateLock();
				if (debuggingMapUpdateLock) System.out.println("[MAP UPDATE LOCK] Map View grabbed lock.");
				drawAutomobiles();
				CSCI201Maps.giveUpMapUpdateLock();
				if (debuggingMapUpdateLock) System.out.println("[MAP UPDATE LOCK] Map View gave up lock.");
				Thread.sleep(13000);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}
}
