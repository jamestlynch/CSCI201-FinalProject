/**
 * ==========================================================================================================================
 * @fileName 			CSCI201Maps.java
 * @createdBy 			JamesLynch
 * @teamMembers 		Joseph Lin (josephml), Sarah Loui (sloui), James Lynch (jamestly), Ivana Wang (ivanawan)
 * @date 				Apr 12, 2014 4:45:04 PM
 * @professor			Jeffrey Miller
 * @project   			CSCI201 Final Project
 * @projectDescription	Map Transportation Network for Spring 2014 CSCI201 ("Principles of Software Development") at USC
 * @version				0.10
 * @versionCreated		0.10
 * ==========================================================================================================================
 *
 * ==========================================================================================================================
 */

package main;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import main.freeway.FreewaySegment;
import main.map.GeoMap;
import main.map.GeoMapModel;
import main.map.GeoMapView;

public class CSCI201Maps {
	private GeoMapModel geoMapModel;
	private GeoMapView geoMapView;
	private GeoMap geoMap;
	
	// Call the user interface
	// Instantiate all objects
	public CSCI201Maps() {
		geoMapModel = new GeoMapModel();
		geoMapView = new GeoMapView(500, 500, geoMapModel);
		geoMap = new GeoMap(geoMapView, geoMapModel);
		ArrayList<FreewaySegment> temp = new ArrayList<FreewaySegment>();
		//temp = geoMapModel.returnAllSegment();
		//System.out.println();
		ArrayList<FreewaySegment> temp2 = new ArrayList<FreewaySegment>();
		ArrayList<FreewaySegment> temp3 = new ArrayList<FreewaySegment>();
		ArrayList<FreewaySegment> temp4 = new ArrayList<FreewaySegment>();
		//temp = geoMapModel.returnAllSegment();
		temp = geoMapModel.getListOf405Segments();
		temp2 = geoMapModel.getListOf105Segments();
		temp3 = geoMapModel.getListOf10Segments();
		temp4 = geoMapModel.getListOf101Segments();
		System.out.println();
		try
		{
			geoMapView.getMapViewer();
			geoMapView.drawPath(temp);
			//geoMapView.drawPath(temp2);
			//geoMapView.drawPath(temp3);
			//geoMapView.drawPath(temp4);
			
		}
		catch(NullPointerException npe)
		{
			System.out.println("NPE: " + npe.getMessage());
		}
		JFrame temporaryframethatwewillreplacewithUICSCI201MapJFrame = new JFrame();
		temporaryframethatwewillreplacewithUICSCI201MapJFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		temporaryframethatwewillreplacewithUICSCI201MapJFrame.setSize(600,  600);
		temporaryframethatwewillreplacewithUICSCI201MapJFrame.add(geoMapView);
		temporaryframethatwewillreplacewithUICSCI201MapJFrame.setVisible(true);
		/*while(true)
    	{
    		JSONFileGetter jfg = new JSONFileGetter("http://www-scf.usc.edu/~csci201/mahdi_project/test.json");    		
    		try
    		{
    			jfg.start();
    			ArrayList<Automobile> UpdatedCarsArrayList;
    			UpdatedCarsArrayList = jfg.updatecar();
    			System.out.println("SIZE: ");
    			for (int i = 0; i < UpdatedCarsArrayList.size(); i++)
    			{
    				geoMapView.getMapViewer().addMapMarker(UpdatedCarsArrayList.get(i).getCarsprite());
    			}
    			Thread.sleep(180000);
    		}
    		catch( InterruptedException ie)
    		{
    			System.out.println("IE " + ie.getMessage());
    		}
    	}*/
// 		Instantiate the GUI
//		new UICSCI201Maps();
//		new UICSCI201Maps();
	}
	
	public static void main(String [] args) {
		new CSCI201Maps();
		
	}
}
