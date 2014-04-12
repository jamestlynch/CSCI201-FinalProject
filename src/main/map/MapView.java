/**
 * ==========================================================================================================================
 * @fileName 			MapView.java
 * @createdBy 			JamesLynch
 * @teamMembers 		Joseph Lin (josephml), Sarah Loui (sloui), James Lynch (jamestly), Ivana Wang (ivanawan)
 * @date 				Apr 10, 2014 6:24:17 PM
 * @professor			Jeffrey Miller
 * @project   			CSCI201 Final Project
 * @projectDescription	Map Transportation Network for Spring 2014 CSCI201 ("Principles of Software Development") at USC
 * @version				0.10
 * @versionCreated		0.10
 * ==========================================================================================================================
 *
 * ==========================================================================================================================
 */

package main.map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;
import org.jdesktop.swingx.mapviewer.WaypointRenderer;

public class MapView extends JPanel  {

	public MapView() {
		org.jdesktop.swingx.JXMapKit jXMapKit = new org.jdesktop.swingx.JXMapKit();
		jXMapKit.setDefaultProvider(org.jdesktop.swingx.JXMapKit.DefaultProviders.OpenStreetMaps);
		jXMapKit.setDataProviderCreditShown(true);
		//jXMapKit.setName("jXMapKit1");
		//add(jXMapKit);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.add(new MapView());
		frame.setVisible(true);
	}
}
