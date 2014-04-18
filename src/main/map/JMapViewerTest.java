package main.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import main.freeway.FreewaySegment;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;


public class JMapViewerTest extends JFrame {
	int count = 0;
	final double markersize = .001;
	final double maplat = 34.05;
	final double maplon = -118.25;
	
	double the_distance = 0;
	static String segbegin;
	static PrintWriter br;
	ArrayList<Double> latpt = new ArrayList<Double>();
	ArrayList<Double> lonpt = new ArrayList<Double>();
	ArrayList<MapMarkerCircle> mmc = new ArrayList<MapMarkerCircle>();
	static Scanner fin = new Scanner(System.in);
	JMapViewer map;
	public JMapViewerTest() {
		//latpt.
		super("JMapViewerTest");
		this.setSize(new Dimension(600, 600));

		map = new JMapViewer();
		//map.setCenter(new Point(300,300));
		//0, 0 is london. +x is north, + y is east
		//System.out.println(JMapViewer.MAX_ZOOM);
		map.setDisplayPositionByLatLon(34.05, -118.25, 10);
		map.addMouseListener(new PointListener(map));
		map.addKeyListener(new UndoPoint(map));
		map.setFocusable(true);
		//map.setDisplayPosition(1500,3000, 5);


		//		ArrayList<Coordinate> coords = new ArrayList<Coordinate>();
		//		coords.add(new Coordinate(35, -118.36));
		//		coords.add(new Coordinate(34.0316607900439, -118.36));
		//		coords.add(new Coordinate(34.0316607900439, -130));
		add(map);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new SaveBeforeClosing(this));
		this.setVisible(true);
	}
	//Draws fastest path on map
	public void drawPath(ArrayList<FreewaySegment> freewaysegments)
	{
		ArrayList<Coordinate> pathToDraw = new ArrayList<Coordinate>();
		for (int i=0; i< freewaysegments.size(); i++)
		{
			for (int j=0; j<freewaysegments.get(i).getSegmentPath().size(); j++)
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
		map.setMapPolygonList(polygonsToDraw);
		map.setMapPolygonsVisible(true);
	}

	public static void main(String[] args) {
		System.out.println("WHAT DO YOU WANT TO NAME THE FILE?");
		String y;
		try
		{

			y = fin.nextLine();
			FileWriter out = new FileWriter(y+".xml");
			br = new PrintWriter(out);
			System.out.println("WHAT FREEWAY WILL YOU BE GOING OVER?");
			String fwy = fin.nextLine();
			System.out.println("What ramp will you be starting with?");
			String segbegin = fin.nextLine();
			System.out.println("What ramp are you ending with?");
			String segend = fin.nextLine();
			br.println("<freeway>");	
			br.println("	<name>"+fwy+"</name>");
			br.println("	<segment>");
			br.println("		<number begin = "+segbegin+" end = " + segend + "></number>");
			segbegin = segend;
			br.println("		<points>");
			br.flush();
			JMapViewerTest a = new JMapViewerTest();

		}
		catch (IOException ioe)
		{
			System.out.println("IOE: " + ioe.getMessage());

		}
		br.flush();
		//br.close();
	}
	class UndoPoint extends KeyAdapter
	{
		JMapViewer map;
		public UndoPoint(JMapViewer map)
		{
			this.map = map;
		}
		public void keyReleased(KeyEvent e)
		{
			if (e.getKeyChar() == ' ')
			{
				if (!(count == 0))
				{	
					System.out.println("DELETING LAST ADDITION");
					map.removeMapMarker(mmc.get(count-1));
					latpt.remove(count-1);
					lonpt.remove(count-1);
					count--;
				}
			}
			if (e.getKeyChar() == '.')
			{
				for (int i = 0 ; i < count; i++)
					br.println("			<point x="+ latpt.get(i)+ " y=" + lonpt.get(i)+ " num=" +i +"></point>");
				double x = distance(latpt.get(0), lonpt.get(0), latpt.get(count-1), lonpt.get(count-1));
				br.println("        <distance d=" + x + "> </distance>");
				latpt.clear();
				lonpt.clear();
				count = 0;
				mmc.clear();
				map.removeAllMapMarkers();

				br.println("		</points>");
				br.println("	</segment>");
				System.out.println("What is the next segment?");
				String segend = fin.nextLine();
				br.println("	<segment>");
				br.println("		<number begin = "+segbegin+" end = " + segend + "></number>");
				br.println("		<points>");
				segbegin = segend;
				br.flush();

			}
		}
	}
	class PointListener extends MouseAdapter
	{
		JMapViewer map;
		PointListener(JMapViewer map)
		{
			this.map = map;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (SwingUtilities.isLeftMouseButton(e))
			{
				 
				Coordinate co = map.getPosition(e.getPoint());
				MapMarkerCircle mm = new MapMarkerCircle(co, markersize);
				mmc.add(mm);
				map.addMapMarker(mm);
				System.out.println(co.toString());
				latpt.add(co.getLat());
				lonpt.add(co.getLon());
				the_distance += distance(latpt.get(count), lonpt.get(count), latpt.get(count+1), lonpt.get(count+1));
				count++;
			}
		}
	}
	class SaveBeforeClosing extends WindowAdapter
	{
		JFrame jf;
		SaveBeforeClosing(JFrame frame)
		{
			jf = frame;
		}
		@Override
		public void windowClosing(WindowEvent e) {
			jf.dispose();
			if (count <= 1)
				return;
			for (int i = 0 ; i < count; i++)
				br.println("			<point x="+ latpt.get(i)+ " y=" + lonpt.get(i)+ " num=" +i +"></point>");
			br.flush();
			br.println("        <distance d=" + the_distance + "> </distance>");
			br.println("		</points>");
			br.println("	</segment>");
			br.flush();
			br.close();
		}
	}
	//http://www.geodatasource.com/developers/java
	private double distance(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
		dist = Math.acos(dist);
		dist = Math.toDegrees(dist);
		dist = dist * 60 * 1.1515;
		return (dist);
	}

}
