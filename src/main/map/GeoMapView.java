package main.map;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;

import main.freeway.FreewaySegment;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;

public class GeoMapView extends JPanel {
	private int panelWidth;
	private int panelHeight;
	
	private JMapViewer mapViewer = new JMapViewer();
	private Coordinate startLocation = new Coordinate(34.05, -118.25);
	private int startZoom = 12;
	
	public GeoMapView(int width, int height) {
		this.panelWidth = width;
		this.panelHeight = height;
		
		setLayout(null);
		setPreferredSize(new Dimension(panelWidth, panelHeight));
		
		mapViewer.setDisplayPositionByLatLon(startLocation.getLat(), startLocation.getLon(), startZoom);
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

		ArrayList<MapPolygon> polygonsToDraw = new ArrayList<MapPolygon>();
		MapPolygonImpl polygon = new MapPolygonImpl(pathToDraw);
		polygonsToDraw.add(polygon);
		mapViewer.setMapPolygonList(polygonsToDraw);
		mapViewer.setMapPolygonsVisible(true);
	}
}
