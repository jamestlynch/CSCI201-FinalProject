
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
import java.util.Scanner;

import javax.swing.JFrame;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;


public class JMapViewerTest extends JFrame {
	int count = 0;
	final double markersize = .005;
	final double maplat = 34.05;
	final double maplon = -118.25;
	static String segbegin;
	static PrintWriter br;
	ArrayList<Double> latpt = new ArrayList<Double>();
	ArrayList<Double> lonpt = new ArrayList<Double>();
	ArrayList<MapMarkerCircle> mmc = new ArrayList<MapMarkerCircle>();
	static Scanner fin = new Scanner(System.in);
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
		map.addKeyListener(new UndoPoint(map));
		map.setFocusable(true);
		//map.setDisplayPosition(1500,3000, 5);
		MapMarkerCircle circle = new MapMarkerCircle(34.05, -118.25, .0005);
		circle.setColor(Color.RED);
		circle.setBackColor(Color.GREEN);

		map.addMapMarker(circle);

		add(map);

		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new SaveBeforeClosing(this));
		this.setVisible(true);
	}

	public static void main(String[] args) {
		System.out.println("WHAT DO YOU WANT TO NAME THE FILE?");
		String y;
		try
		{
			
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
				br.println("		</points>");
				br.println("	</segment>");
				System.out.println("What is the next segment?");
				String segend = fin.nextLine();
				br.println("		<number begin = "+segbegin+" end = " + segend + "</number>");
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

			Coordinate co = map.getPosition(e.getPoint());
			MapMarkerCircle mm = new MapMarkerCircle(co, markersize);
			mmc.add(mm);
			map.addMapMarker(mm);
			System.out.println(count + "pt have been added in total.");
			latpt.add(co.getLat());
			lonpt.add(co.getLon());
			count++;

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
			br.println("		</points>");
			br.println("	</segment>");
			br.flush();
			br.close();
		}
	}
}
