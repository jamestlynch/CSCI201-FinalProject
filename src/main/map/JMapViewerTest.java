
package main.map;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;


public class JMapViewerTest extends JFrame {
	int count = 0;
	final double markersize = .005;
	final double maplat = 34.05;
	final double maplon = -118.25;
	static PrintWriter br;
	ArrayList<Double> latpt = new ArrayList<Double>();
	ArrayList<Double> lonpt = new ArrayList<Double>();
	public JMapViewerTest() {
		//latpt.
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
		System.out.println("WHAT DO YOU WANT TO NAME THE FILE?");
		String y;
		try
		{
			Scanner fin = new Scanner(System.in);
			y = fin.nextLine();
			FileWriter out = new FileWriter(y+".xml");
			br = new PrintWriter(out);
			//System.out.println("WHAT FREEWAY WILL YOU BE GOING OVER?");
			//String fwy = fin.nextLine();
			System.out.println("What ramp will you be starting with?");
			String segbegin = fin.nextLine();
			System.out.println("What ramp are you ending with?");
			String segend = fin.nextLine();
			//br.println("<freeway>");	
			//br.println("	<name>"+fwy+"</name>");
			br.println("	<segment>");
			br.println("		<number begin = "+segbegin+" end = " + segend + "</number>");
			br.println("		<points>");
			br.flush();
			JMapViewerTest a = new JMapViewerTest();
			br.println("		</points>");
			br.println("	</segment>");
		}
		catch (IOException ioe)
		{
			System.out.println("IOE: " + ioe.getMessage());
			
		}
		br.flush();
		//br.close();
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
			MapMarkerCircle mm = new MapMarkerCircle(co, markersize);
			map.addMapMarker(mm);
			System.out.println(co.toString());
			latpt.add(co.getLat());
			lonpt.add(co.getLon());
			br.println("			<point x="+co.getLat() + " y=" + co.getLon() + " num=" +count +"></point>");
			br.flush();
			count++;
			
		}
	}
}