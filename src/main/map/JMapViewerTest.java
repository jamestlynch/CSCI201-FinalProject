package main.map;


import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;


public class JMapViewerTest extends JFrame {
	public JMapViewerTest() {
		super("JMapViewerTest");
		this.setSize(new Dimension(600, 600));
		
		JMapViewer map = new JMapViewer();
		Coordinate losangeles = new Coordinate(34.05, 118.25);
		//map.setCenter(new Point(300,300));
		//0, 0 is london. +x is north, + y is east
		System.out.println(JMapViewer.MAX_ZOOM);
		map.setDisplayPositionByLatLon(34.05, -118.25, 12);
		
		MapMarkerCircle circle = new MapMarkerCircle(34.05, -118.25, .0005);
		circle.setColor(Color.RED);
		circle.setBackColor(Color.GREEN);
		
		map.addMapMarker(circle);
		//map.setDisplayPosition(1500,3000, 5);
	
		add(map);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void recenterMapAndStorePointInXML() {
		
	}
	
	public static void main(String[] args) {
		new JMapViewerTest();
	}
}
