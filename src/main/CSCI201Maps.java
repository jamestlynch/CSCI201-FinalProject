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
import java.util.ConcurrentModificationException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import main.freeway.FreewaySegment;
import main.jsonfile.JSONFileGetter;
import main.map.GeoMap;
import main.map.GeoMapModel;
import main.map.GeoMapView;

public class CSCI201Maps {
	private GeoMapModel geoMapModel;
	private GeoMapView geoMapView;
	private GeoMap geoMap;
	
	private Thread jsonGetterThread;
	private Thread mapModelThread;
	private static Thread mapViewThread;
	
	private static Semaphore mapUpdateSemaphore = new Semaphore(1);
	
	public static final long automobileUpdateRate = 13000; // 13 milliseconds to wait between updates
	public static final long automobilePaintDelay = 13000; // 13 milliseconds to fully paint
	public static final long jsonFileFetchingDelay = 3 * 60 * 1000; // 3 minutes to wait between grabbing JSON file
	
	// Call the user interface
	// Instantiate all objects
	public CSCI201Maps() {
		ExecutorService configurationExecutor = Executors.newFixedThreadPool(1);
		
		geoMapModel = new GeoMapModel();
		geoMapView = new GeoMapView(500, 500, geoMapModel);
		geoMap = new GeoMap(geoMapView, geoMapModel);
		
		ArrayList<FreewaySegment> segments405 = new ArrayList<FreewaySegment>();
		ArrayList<FreewaySegment> segments105 = new ArrayList<FreewaySegment>();
		ArrayList<FreewaySegment> segments10 = new ArrayList<FreewaySegment>();
		ArrayList<FreewaySegment> segments101 = new ArrayList<FreewaySegment>();

		segments405 = geoMapModel.getListOf405Segments();
		segments105 = geoMapModel.getListOf105Segments();
		segments10  = geoMapModel.getListOf10Segments();
		segments101 = geoMapModel.getListOf101Segments();
		
		jsonGetterThread = new Thread(
			(new JSONFileGetter("http://www-scf.usc.edu/~csci201/mahdi_project/project_data.json", geoMapModel, geoMapView)));
		jsonGetterThread.setPriority(Thread.MAX_PRIORITY);
		jsonGetterThread.start();
		
		mapModelThread = new Thread(geoMapModel);
		mapModelThread.setPriority(Thread.MAX_PRIORITY - 1);
		mapModelThread.start();
		
		mapViewThread = new Thread(geoMapView);
		mapViewThread.setPriority(Thread.MAX_PRIORITY - 1);
		
		geoMapView.drawPath(segments105);
		
		// Instantiate the GUI
		//new UICSCI201Maps();
		JFrame temporaryframethatwewillreplacewithUICSCI201MapJFrame = new JFrame();
		temporaryframethatwewillreplacewithUICSCI201MapJFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		temporaryframethatwewillreplacewithUICSCI201MapJFrame.setSize(600,  600);
		temporaryframethatwewillreplacewithUICSCI201MapJFrame.add(geoMapView);
		temporaryframethatwewillreplacewithUICSCI201MapJFrame.setVisible(true);
	}

	public static void grabMapUpdateLock()
	{
		try {
			mapUpdateSemaphore.acquire();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}
	
	public static void giveUpMapUpdateLock()
	{
		mapUpdateSemaphore.release();
	}
	
	public static void startMapViewThread() 
	{
		mapViewThread.start();
	}
	
	public static void main(String [] args) {
		new CSCI201Maps();
	}
}
