
package main.map;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;


public class JMapViewerTest extends JFrame {
	final double markersize = .01;
	final double maplat = 34.05;
	final double maplon = -118.25;
	public JMapViewerTest() {
		super("JMapViewerTest");
		this.setSize(new Dimension(600, 600));

		JMapViewer map = new JMapViewer();
		//map.setCenter(new Point(300,300));
		//0, 0 is london. +x is north, + y is east
		System.out.println(JMapViewer.MAX_ZOOM);
		map.setDisplayPositionByLatLon(maplat, maplon, 12);
		map.addMouseListener(new PointListener(map));
		//map.setDisplayPosition(1500,3000, 5);
		MapMarkerCircle circle = new MapMarkerCircle(34.05, -118.25, .0005);
		circle.setColor(Color.RED);
		circle.setBackColor(Color.GREEN);
		
		map.addMapMarker(circle);
		
		add(map);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new JMapViewerTest();
	}
	class PointListener implements MouseListener
	{
		JMapViewer map;
		PointListener(JMapViewer map)
		{
			this.map = map;
		}
		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {

			Coordinate co = map.getPosition(e.getPoint());
			MapMarker mm = new MapMarkerCircle(co, markersize);
			map.addMapMarker(mm);
			System.out.println(co.toString());
		}
	}
}