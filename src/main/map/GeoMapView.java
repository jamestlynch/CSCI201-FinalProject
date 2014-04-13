package main.map;

import java.awt.Dimension;

import javax.swing.JPanel;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

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
}
