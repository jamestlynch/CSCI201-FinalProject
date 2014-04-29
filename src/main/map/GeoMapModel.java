package main.map;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import main.CSCI201Maps;
import main.automobile.Automobile;
import main.freeway.FreewayRamp;
import main.freeway.FreewaySegment;
import main.jsonfile.JSONFileGetter;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class GeoMapModel implements Runnable {
	/* =========================================================================
	 *   MEMBER VARIABLES
	 * ========================================================================= */

	// HashMap that allows you to look-up a freeway section via its start ramp
	private static FreewayNetwork defaultDirectionFreewayNetwork;
	private static FreewayNetwork oppositeDirectionFreewayNetwork;
	private static ArrayList<FreewaySegment> orderedSegments405 = new ArrayList<FreewaySegment>();
	private static ArrayList<FreewaySegment> orderedSegments105 = new ArrayList<FreewaySegment>();
	private static ArrayList<FreewaySegment> orderedSegments10 = new ArrayList<FreewaySegment>();
	private static ArrayList<FreewaySegment> orderedSegments101 = new ArrayList<FreewaySegment>();
	private static ArrayList<FreewaySegment> reverseSegments405 = new ArrayList<FreewaySegment>();
	private static ArrayList<FreewaySegment> reverseSegments105 = new ArrayList<FreewaySegment>();
	private static ArrayList<FreewaySegment> reverseSegments10 = new ArrayList<FreewaySegment>();
	private static ArrayList<FreewaySegment> reverseSegments101 = new ArrayList<FreewaySegment>();
	private ArrayList<Automobile> automobilesInFreewayNetwork = new ArrayList<Automobile>();

	private final File[] freewayXMLFiles = {
			new File("./Freeway-10/Freeway10Updated.xml"),
			new File("./Freeway-101/Freeway101-1.xml"),
			//new File("./Freeway-101/Freeway101-J.xml"),
			new File("./Freeway-105/Freeway105-1.xml"),
			new File("./Freeway-405/Freeway405.xml") };

	private Calendar timeNow = Calendar.getInstance();
	private long timeInSecondsAfter;
	private long timeInSecondsBefore;
	
	private boolean debuggingSearch = false;
	private boolean debuggingAutomobileMarkers = false;
	private boolean debuggingMapUpdateLock = true;
	private boolean debuggingGetNextSegment = false;
	private boolean debuggingMapModelInit = false;
	private boolean debuggingMapModelRun = false;
	private boolean debuggingNextFreewaySegmentExists = false;
	private boolean debuggingSearchBySegment = false;
	private boolean debuggingPostJSONGrabbing = false;
	private boolean debuggingRemoveDeadAutomobile = false;
	private boolean debuggingInitDestination = false;

	/* =========================================================================
	 *   CONSTRUCTORS
	 * ========================================================================= */

	public GeoMapModel() {
		defaultDirectionFreewayNetwork = new FreewayNetwork();
		oppositeDirectionFreewayNetwork = new FreewayNetwork();

		// Load in the Freeways from the XML parser
		for (int i = 0; i < freewayXMLFiles.length; i++) {
			new FreewayLoader(freewayXMLFiles[i]);
		}
	}

	/* =========================================================================
	 *   ACCESSOR METHODS
	 * ========================================================================= */

	public ArrayList<FreewaySegment> returnAllSegment() {
		ArrayList<FreewaySegment> allSegments = new ArrayList<FreewaySegment>();
		
		for (FreewayRamp key : defaultDirectionFreewayNetwork.keySet()) {

			for (int i = 0; i < defaultDirectionFreewayNetwork.get(key).size(); i++) {
				FreewaySegment tempfs = defaultDirectionFreewayNetwork.get(key)
						.get(i);
				allSegments.add(tempfs);
			}
		}
		
		for (FreewayRamp key : oppositeDirectionFreewayNetwork.keySet()) {

			for (int i = 0; i < oppositeDirectionFreewayNetwork.get(key).size(); i++) {
				FreewaySegment tempfs = oppositeDirectionFreewayNetwork.get(key)
						.get(i);
				allSegments.add(tempfs);
			}
		}
		return allSegments;
	}

	public static ArrayList<FreewaySegment> getListOf405Segments() {
		return orderedSegments405;
	}

	public static ArrayList<FreewaySegment> getListOf105Segments() {
		return orderedSegments105;
	}

	public static ArrayList<FreewaySegment> getListOf10Segments() {
		return orderedSegments10;
	}

	public static ArrayList<FreewaySegment> getListOf101Segments() {
		return orderedSegments101;
	}

	public static ArrayList<FreewaySegment> getReverseSegments405() {
		return reverseSegments405;
	}

	public static ArrayList<FreewaySegment> getReverseSegments105() {
		return reverseSegments105;
	}

	public static ArrayList<FreewaySegment> getReverseSegments10() {
		return reverseSegments10;
	}

	public static ArrayList<FreewaySegment> getReverseSegments101() {
		return reverseSegments101;
	}

	/* =========================================================================
	 *   JUNCTION METHODS
	 * ========================================================================= */
	
	public int isJunction(FreewaySegment currentSegment){
		/* 0 = end segment
		 * 1 = regular segment
		 * 2 = junction
		 */
		return defaultDirectionFreewayNetwork.get(currentSegment.getEndRamp()).size();
	}
	
	public boolean isJunctionRamp(FreewayRamp currentRamp)
	{
		if (defaultDirectionFreewayNetwork.get(currentRamp).size() > 1)
			return true;
		else
			return false;
	}
	
	/**
	 * This takes the junction ramp and returns the junction segment
	 * @param junctionRamp
	 * @return junction segment between 2 freeways. If it isn't a junction ramp, then it returns null
	 */
	public FreewaySegment getJunction(FreewayRamp junctionRamp)
	{
		if (isJunctionRamp(junctionRamp))
		{
			return defaultDirectionFreewayNetwork.get(junctionRamp).get(1);
		}
		
		return null;
	}

	
	/* =========================================================================
	 *   SEGMENT SEARCH METHODS
	 * ========================================================================= */

	public FreewaySegment searchByRampName(String rampName, String freewayName, FreewaySegment.Direction direction) {
		FreewaySegment segmentToReturn = null;
		
		for (FreewayRamp ramp  :  defaultDirectionFreewayNetwork.keySet()) 
		{
			if (ramp.getRampName().equals(rampName)) // If the ramp is the same as the next ramp's start (current endRamp)
			{
				for (int i = 0; i < defaultDirectionFreewayNetwork.get(ramp).size(); i++) // Loop through all different segments in the ArrayList
				{	
					if ((direction.equals(defaultDirectionFreewayNetwork.get(ramp).get(i).getDirectionEW())
					  || direction.equals(defaultDirectionFreewayNetwork.get(ramp).get(i).getDirectionNS()))
					 && freewayName.equals(defaultDirectionFreewayNetwork.get(ramp).get(i).getFreewayName())) 
					{
						segmentToReturn = defaultDirectionFreewayNetwork.get(ramp).get(i);
					}
				}
			}
		}
		
		if (segmentToReturn == null) { // If it wasn't found inside of the default network
			for (FreewayRamp ramp  :  oppositeDirectionFreewayNetwork.keySet()) 
			{
				if (ramp.getRampName().equals(rampName)) // If the ramp is the same as the next ramp's start (current endRamp)
				{
					for (int i = 0; i < oppositeDirectionFreewayNetwork.get(ramp).size(); i++) // Loop through all different segments in the ArrayList
					{	
						if ((direction.equals(oppositeDirectionFreewayNetwork.get(ramp).get(i).getDirectionEW())
						  || direction.equals(oppositeDirectionFreewayNetwork.get(ramp).get(i).getDirectionNS()))
						 && freewayName.equals(oppositeDirectionFreewayNetwork.get(ramp).get(i).getFreewayName())) 
						{
							segmentToReturn = oppositeDirectionFreewayNetwork.get(ramp).get(i);
						}
					}
				}
			}
		}
		
		return segmentToReturn;
	}
		
	public FreewaySegment searchByRampAndFreewayName(String startRampName, String freewayName, boolean isDefaultNetwork) {
		HashMap<FreewayRamp, ArrayList<FreewaySegment>> freewayNetwork = 
				isDefaultNetwork ? defaultDirectionFreewayNetwork : oppositeDirectionFreewayNetwork;
		
		for (FreewayRamp ramp  :  freewayNetwork.keySet()) 
		{
			if (ramp.getRampName().equals(startRampName)) // If the ramp is the same as the next ramp's start (current endRamp)
			{
				for (int i = 0; i < freewayNetwork.get(ramp).size(); i++) // Loop through all different segments in the ArrayList
				{	
					if (freewayName.equals(freewayNetwork.get(ramp).get(i).getFreewayName()))
					{
						return freewayNetwork.get(ramp).get(i);
					}
				}
			}
		}
		
		return null; // If we were unable to find a segment beginning at startRamp on the freeway
	}
	
	public FreewaySegment searchBySegment(FreewaySegment segmentToCheck, boolean isDefaultNetwork) {
		String endRampName = segmentToCheck.getEndRamp().getRampName();
		String freewayName = segmentToCheck.getFreewayName();
		
		HashMap<FreewayRamp, ArrayList<FreewaySegment>> freewayNetwork = 
				isDefaultNetwork ? defaultDirectionFreewayNetwork : oppositeDirectionFreewayNetwork;
		
		for (FreewayRamp ramp  :  freewayNetwork.keySet()) 
		{
			if (ramp.getRampName().equals(endRampName)) // If the ramp is the same as the next ramp's start (current endRamp)
			{
				for (int i = 0; i < freewayNetwork.get(ramp).size(); i++) // Loop through all different segments in the ArrayList
				{	
					if ((segmentToCheck.getDirectionEW().equals(freewayNetwork.get(ramp).get(i).getDirectionEW())
					 || segmentToCheck.getDirectionNS().equals(freewayNetwork.get(ramp).get(i).getDirectionNS()))
					 && freewayName.equals(freewayNetwork.get(ramp).get(i).getFreewayName())) 
					{
						return freewayNetwork.get(ramp).get(i);
					}
				}
			}
		}
		
		if (debuggingSearchBySegment) System.out.println("Returning null anyways...");
		return null; // If we were unable to find a segment beginning at our current endRamp with the correct directions and freeway
	}
	
	public boolean existsInFreewayNetwork(FreewaySegment segmentToCheck, boolean isDefaultNetwork) {
		String startRampName = segmentToCheck.getStartRamp().getRampName();
		String endRampName = segmentToCheck.getEndRamp().getRampName();
		String freewayName = segmentToCheck.getFreewayName();
		
		boolean segmentBeginningAtSegmentsStartRamp = false;
		boolean segmentBeginningAtSegmentsEndRamp = false;
		boolean segmentGoingInSameDirection = false;
		boolean segmentOnSameFreeway = false;
		
		HashMap<FreewayRamp, ArrayList<FreewaySegment>> freewayNetwork = 
				isDefaultNetwork ? defaultDirectionFreewayNetwork : oppositeDirectionFreewayNetwork;
		
		for (FreewayRamp ramp  :  freewayNetwork.keySet()) 
		{
			if (ramp.getRampName().equals(startRampName)) 
			{
				segmentBeginningAtSegmentsStartRamp = true;

				if (segmentToCheck.getDirectionEW().equals(freewayNetwork.get(ramp).get(0).getDirectionEW())
				||  segmentToCheck.getDirectionNS().equals(freewayNetwork.get(ramp).get(0).getDirectionNS())) 
				{
					segmentGoingInSameDirection = true;
				}

				if (freewayName.equals(freewayNetwork.get(ramp).get(0).getFreewayName())) 
				{
					segmentOnSameFreeway = true;
				}
			} if (ramp.getRampName().equals(endRampName)) 
			{
				segmentBeginningAtSegmentsEndRamp = true;
			}
		}
		
		return segmentBeginningAtSegmentsStartRamp && segmentBeginningAtSegmentsEndRamp && segmentGoingInSameDirection && segmentOnSameFreeway;
	}
		
	public boolean nextFreewaySegmentExists(FreewaySegment oldSegment) {
		//Checks to see if there is a following segment after this ramp (End of the Freeway)
		if(debuggingNextFreewaySegmentExists) 
			System.out.println(oldSegment.getStartRamp().getRampName() + " " + oldSegment.getDirectionEW().toString() + 
				oldSegment.getDirectionNS().toString() + " checking if next exists...\n");
		
		if (existsInFreewayNetwork(oldSegment, /* isDefaultNetwork */ true)
		&& (searchBySegment(oldSegment,  /* isDefaultDirection */ true)) != null) // Default network
		{ // Checks for existence of network starting at same start ramp, segment starting at this segment's end ramp, and that the directions are the same
			return true;
		} 
		else if (existsInFreewayNetwork(oldSegment, /* isDefaultNetwork */ false)
			 && (searchBySegment(oldSegment,  /* isDefaultDirection */ false)) != null) // Opposite network
		{ // Checks for existence of network starting at same start ramp, segment starting at this segment's end ramp, and that the directions are the same
			return true;
		} 
		
		// Could not find the segment in either the default network OR the opposite network which has the same start ramp and has a segment starting at the segment's end ramp, going in the same direction
		if (debuggingGetNextSegment) System.out.println("[GET NEXT SEGMENT] Can't get a freeway beginning at " + oldSegment.getEndRamp().getRampName());
		return false;
	}
	
	public FreewaySegment getNextFreewaySegment(FreewaySegment oldSegment) {
		
		if (existsInFreewayNetwork(oldSegment, /* isDefaultNetwork */ true)
		&& (searchBySegment(oldSegment,  /* isDefaultDirection */ true)) != null) // Default network
		{ // Checks for existence of network starting at same start ramp, segment starting at this segment's end ramp, and that the directions are the same
			return searchBySegment(oldSegment,  /* isDefaultDirection */ true);
		} 
		else if (existsInFreewayNetwork(oldSegment, /* isDefaultNetwork */ false)
			 && (searchBySegment(oldSegment,  /* isDefaultDirection */ false)) != null) // Opposite network
		{ // Checks for existence of network starting at same start ramp, segment starting at this segment's end ramp, and that the directions are the same
			return searchBySegment(oldSegment,  /* isDefaultDirection */ false);
		}
		
		// Could not find the segment in either the default network OR the opposite network which has the same start ramp and has a segment starting at the segment's end ramp, going in the same direction
		if (debuggingGetNextSegment) System.out.println("[GET NEXT SEGMENT] Can't get a freeway beginning at " + oldSegment.getEndRamp().getRampName());
		return null;
	}
	
	public void addAutomobileToNetwork(Automobile newAutomobile) {
		synchronized(automobilesInFreewayNetwork) 
		{
			automobilesInFreewayNetwork.add(newAutomobile);
		}
	}

	public ArrayList<Automobile> getAutomobilesInFreewayNetwork() {
		synchronized(automobilesInFreewayNetwork) 
		{
			for (int i = 0; i < automobilesInFreewayNetwork.size(); i++)
				if (debuggingAutomobileMarkers) 
					System.out.println("[GET AUTOMOBILES] " + automobilesInFreewayNetwork.get(i)
						.getCarMarker().toString());
			return automobilesInFreewayNetwork;	
		}
	}
	
	public void removeAutomobilesInFreewayNetwork() {
		synchronized(automobilesInFreewayNetwork) 
		{
			automobilesInFreewayNetwork.clear();
		}
	}
	
	public void removeDeadAutomobilesInFreewayNetwork()
	{
		for (int i = 0 ; i < automobilesInFreewayNetwork.size(); i++)
		{
			if (automobilesInFreewayNetwork.get(i).getDestination() == null)
			{
				if (debuggingRemoveDeadAutomobile) System.out.println("[REMOVING NULL AUTOMOBILE] NULL CAR'S IDNUM:" + i);
				//automobilesInFreewayNetwork.get(i).getCarMarker().setVisible(false);
				automobilesInFreewayNetwork.remove(i);
			}
		}
		
	}
	
	public void init() {
		CSCI201Maps.grabMapUpdateLock();
		
		// After structuring the network, initialize all of the car's destinations
		for (int i = 0; i < automobilesInFreewayNetwork.size(); i++) {
			automobilesInFreewayNetwork.get(i).initDestination();
		}
		
		if (debuggingMapUpdateLock) System.out.println("[MAP UPDATE LOCK] Map Model grabbed lock.");
		
		timeInSecondsAfter = System.currentTimeMillis();
		for (int i = 0; i < automobilesInFreewayNetwork.size(); i++)
		{	
			if (debuggingMapModelInit) System.out.println("[MAPMODEL INIT] Time Before (in sec): " + timeInSecondsBefore + " Time After (in sec): " + timeInSecondsAfter);
			automobilesInFreewayNetwork.get(i).updateLocation(timeInSecondsBefore - timeInSecondsAfter);
		}
		timeInSecondsBefore = timeInSecondsAfter;
		CSCI201Maps.giveUpMapUpdateLock();
		if (debuggingMapUpdateLock) System.out.println("[MAP UPDATE LOCK] Map Model gave up lock.");
		CSCI201Maps.startMapViewThread();
	}
	
	public void run() {
		timeInSecondsBefore = System.currentTimeMillis();
		
		this.init();
		
		while (true) {
			try {
				Thread.sleep(CSCI201Maps.automobileUpdateRate);
				
				CSCI201Maps.grabMapUpdateLock();
				if (debuggingMapUpdateLock) System.out.println("[MAP UPDATE LOCK] Map Model grabbed lock.");
				
				if (debuggingPostJSONGrabbing) System.out.println("[MAPMODEL RUN] Size of automobile's list: " + automobilesInFreewayNetwork.size());
				
//				if (JSONFileGetter.getJustGrabbedFromServer())
//				{
					if (debuggingInitDestination) System.out.println("[MAPMODEL RUN] Reininitializing the automobile destinations.");
					
					// After structuring the network, initialize all of the car's destinations
					for (int i = 0; i < automobilesInFreewayNetwork.size(); i++) {
						automobilesInFreewayNetwork.get(i).initDestination();
					}
					
					JSONFileGetter.setJustGrabbedFromServer(false);
//				}
				
				
				timeInSecondsAfter = System.currentTimeMillis();
				for (int i = 0; i < automobilesInFreewayNetwork.size(); i++)
				{	
					if (debuggingMapModelRun)System.out.println("[MAPMODEL RUN] Time Before (in sec): " + timeInSecondsBefore + " Time After (in sec): " + timeInSecondsAfter);
					automobilesInFreewayNetwork.get(i).updateLocation(timeInSecondsAfter - timeInSecondsBefore);
				}
				timeInSecondsBefore = timeInSecondsAfter;
				CSCI201Maps.giveUpMapUpdateLock();
				if (debuggingMapUpdateLock) System.out.println("[MAP UPDATE LOCK] Map Model gave up lock.");
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}
	
	/*
	 * =========================================================================
	 * FREEWAY NETWORK: Custom version of Java's HashMap that overrides the
	 * put() method so that if multiple FreewaySegments derive from the same
	 * FreewayRamp, we store both.
	 * =========================================================================
	 */

	private class FreewayNetwork extends
			HashMap<FreewayRamp, ArrayList<FreewaySegment>> {
		public void put(FreewayRamp ramp, FreewaySegment segment) {
			ArrayList<FreewaySegment> freewaySegmentsStartingAtRamp = new ArrayList<FreewaySegment>();

			if (defaultDirectionFreewayNetwork.get(ramp) == null) {
				freewaySegmentsStartingAtRamp.add(segment);
				this.put(ramp, freewaySegmentsStartingAtRamp);
			} else {
				for (int i = 0; i < this.get(ramp).size(); i++) {
					freewaySegmentsStartingAtRamp.add(this.get(ramp).get(i));
				}
				freewaySegmentsStartingAtRamp.add(segment);
				this.put(ramp, freewaySegmentsStartingAtRamp);
			}
		}
	}

	/*
	 * =========================================================================
	 * FREEWAY XML LOADER: Private, internal class for loading in the Segment
	 * path data.
	 * =========================================================================
	 */

	private class FreewayLoader {
		private boolean debuggingFreewayLoader = false;
		
		public FreewayLoader(File file) {
			// DocumentBuilderFactory creates a DocumentBuilder instance which
			// parses the XML DOM for manipulation
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory
					.newInstance();
			try {
				DocumentBuilder documentBuilder = builderFactory
						.newDocumentBuilder();

				// Add exception handler for XML parse errors
				documentBuilder.setErrorHandler(new ParseErrorHandler(
						new PrintWriter(System.out)));

				Document document = documentBuilder.parse(file);
				document.normalize(); // Checks for DOM errors in the XML
				// (combines adjacent text nodes,
				// removes empty nodes)

				// Capture the root node (freeway); Assumption that there is
				// only one per XML File
				Element freewayElement = (Element) (document
						.getElementsByTagName("freeway").item(0));

				// Name of the Freeway (e.g., 10, 110, 105, 405, etc.)
				String freewayName = freewayElement
						.getElementsByTagName("name").item(0).getTextContent();
				// if (debuggingFreewayLoader) System.out.println("\nFREEWAY NAME TEST: " + freewayName +
				// "\n");

				// List of all segment objects; Loops through to create the
				// segment objects
				NodeList segmentNodeList = freewayElement
						.getElementsByTagName("segment");
				for (int segmentNumber = 0; segmentNumber < segmentNodeList
						.getLength(); segmentNumber++) {
					Element segmentElement = (Element) segmentNodeList
							.item(segmentNumber);

					Element rampsElement = (Element) segmentElement
							.getElementsByTagName("ramps").item(0);
					Element distanceElement = (Element) segmentElement
							.getElementsByTagName("distance").item(0);
					Element speedLimitElement = (Element) segmentElement
							.getElementsByTagName("speed-limit").item(0);

					ArrayList<Coordinate> segmentPoints = new ArrayList<Coordinate>();
					ArrayList<Coordinate> reversedSegmentPoints = new ArrayList<Coordinate>();
					// Only one points object in DOM (houses all point objects);
					// Grab the point node list from the single <points> DOM
					// object
					NodeList pointNodeList = ((Element) segmentElement
							.getElementsByTagName("points").item(0))
							.getElementsByTagName("point");


					// Loop through each <point> DOM object and add the coordinates to the Segment's path
					for (int pointNumber = 0; pointNumber < pointNodeList.getLength(); pointNumber++) {
						Coordinate point = new Coordinate(
								Double.parseDouble(((Element) pointNodeList
										.item(pointNumber)).getAttribute("x")),
										Double.parseDouble(((Element) pointNodeList
												.item(pointNumber)).getAttribute("y")));
						segmentPoints.add(point);
					}

					// Part of the Segment's definition; first and last <path>
					// DOM object are the starting and ending ramps for the
					// segment
					FreewayRamp startRamp = new FreewayRamp(rampsElement
							.getAttribute("begin").toLowerCase()
							.replaceAll("\\s+", ""), segmentPoints.get(0));
					FreewayRamp endRamp = new FreewayRamp(rampsElement
							.getAttribute("end").toLowerCase()
							.replaceAll("\\s+", ""),
							segmentPoints.get(segmentPoints.size() - 1));

					// Increase in longitude = EAST | Increase in latitude =
					// NORTH
					double latitudeDifference = segmentPoints.get(0).getLat()
							- segmentPoints.get(segmentPoints.size() - 1)
									.getLat();
					double longitudeDifference = segmentPoints.get(0).getLon()
							- segmentPoints.get(segmentPoints.size() - 1)
									.getLon();

					FreewaySegment.Direction directionEW;
					FreewaySegment.Direction directionNS;

					if (longitudeDifference < 0) {
						directionEW = FreewaySegment.Direction.EAST;
					} else {
						directionEW = FreewaySegment.Direction.WEST;
					}

					if (latitudeDifference < 0) {
						directionNS = FreewaySegment.Direction.NORTH;
					} else {
						directionNS = FreewaySegment.Direction.SOUTH;
					}

					String segmentName = "DF" + freewayName + "S" + segmentNumber;

					FreewaySegment defaultFreewaySegment = new FreewaySegment(
							segmentName,
							freewayName,
							Double.parseDouble(distanceElement
									.getAttribute("d")),
									Integer.parseInt(speedLimitElement.getTextContent()),
									directionEW, directionNS, segmentPoints, startRamp,
									endRamp);

					if (debuggingFreewayLoader) System.out.println(" +  Creating " + segmentName
							+ "\tBegin: " + startRamp.getRampName() + "\tEnd: "
							+ endRamp.getRampName() + "\tFreeway: "
							+ freewayName + "\tDirection: <"
							+ directionEW.toString() + ", "
							+ directionNS.toString() + ">");
					// if (debuggingFreewayLoader) System.out.println(defaultFreewaySegment.toString());
					// =========================================================================
					// Store the opposite lane's data
					// =========================================================================
					latitudeDifference = segmentPoints.get(
							segmentPoints.size() - 1).getLat()
							- segmentPoints.get(0).getLat();
					longitudeDifference = segmentPoints.get(
							segmentPoints.size() - 1).getLon()
							- segmentPoints.get(0).getLon();

					if (longitudeDifference < 0) {
						directionEW = FreewaySegment.Direction.EAST;
					} else {
						directionEW = FreewaySegment.Direction.WEST;
					}

					if (latitudeDifference < 0) {
						directionNS = FreewaySegment.Direction.NORTH;
					} else {
						directionNS = FreewaySegment.Direction.SOUTH;
					}

					segmentName = "OF" + freewayName + "S" + segmentNumber;
					//Collections.reverse(segmentPoints);
					for (int i=segmentPoints.size()-1; i > -1; i--)
						reversedSegmentPoints.add(segmentPoints.get(i));


					FreewaySegment oppositeFreewaySegment = new FreewaySegment(
							segmentName,
							freewayName,
							Double.parseDouble(distanceElement
									.getAttribute("d")),
									Integer.parseInt(speedLimitElement.getTextContent()),
									directionEW, directionNS, reversedSegmentPoints, endRamp,
									startRamp);

					if (debuggingFreewayLoader) System.out.println(" +  Creating " + segmentName
							+ "\tBegin: " + endRamp.getRampName() + "\tEnd: "
							+ startRamp.getRampName() + "\tFreeway: "
							+ freewayName + "\tDirection: <"
							+ directionEW.toString() + ", "
							+ directionNS.toString() + ">");

					if (freewayName.equals("405"))
					{
						orderedSegments405.add(defaultFreewaySegment);
						reverseSegments405.add(0, defaultFreewaySegment);
					}
					else if (freewayName.equals("105"))
					{
						orderedSegments105.add(defaultFreewaySegment);
						reverseSegments105.add(0, defaultFreewaySegment);
					}
					else if (freewayName.equals("10"))
					{
						orderedSegments10.add(defaultFreewaySegment);
						reverseSegments10.add(0, defaultFreewaySegment);
					}
					else if (freewayName.equals("101"))
					{
						orderedSegments101.add(defaultFreewaySegment);
						reverseSegments101.add(0, defaultFreewaySegment);
					}
					defaultDirectionFreewayNetwork.put(startRamp,
							defaultFreewaySegment);
					oppositeDirectionFreewayNetwork.put(endRamp,
							oppositeFreewaySegment);
				} // [Close] Segment List loop
			} catch (ParserConfigurationException pce) {
				System.out.println("EXCEPTION: ParserConfigurationException: "
						+ pce.getMessage());
			} catch (SAXException saxe) {
				System.out.println("EXCEPTION: SAXException: "
						+ saxe.getMessage());
			} catch (IOException ioe) {
				System.out.println("EXCEPTION: IOException: "
						+ ioe.getMessage());
			}
		}

		// From Java API Documentation:
		// http://docs.oracle.com/javase/tutorial/jaxp/dom/readingXML.html#gestm
		private class ParseErrorHandler implements ErrorHandler {
			/*
			 * ==================================================================
			 * ======= Member Variables
			 * ==========================================
			 * ===============================
			 */

			private PrintWriter out;

			/*
			 * ==================================================================
			 * ======= Constructors
			 * ==============================================
			 * ===========================
			 */

			ParseErrorHandler(PrintWriter out) {
				this.out = out;
			}

			/*
			 * ==================================================================
			 * ======= Exception Info Reporting: Provides a descriptive report
			 * of the SAX Exception that is thrown.
			 * ==============================
			 * ===========================================
			 */

			private String getParseExceptionInfo(SAXParseException spe) {
				String systemId = spe.getSystemId();
				if (systemId == null) {
					systemId = "null";
				}

				String info = "URI = " + systemId + " Line = "
						+ spe.getLineNumber() + ": " + spe.getMessage();
				return info;
			}

			/*
			 * ==================================================================
			 * ======= Exceptions/Warnings: If there is a parse error, throw an
			 * SAX (Simple API for XML)Exception. Not using an SAX parser, but
			 * leveraging the standards that it put into place
			 * ==================
			 * =======================================================
			 */

			public void warning(SAXParseException spe) throws SAXException {
				out.println("Warning: " + getParseExceptionInfo(spe));
			}

			public void error(SAXParseException spe) throws SAXException {
				String message = "Error: " + getParseExceptionInfo(spe);
				throw new SAXException(message);
			}

			public void fatalError(SAXParseException spe) throws SAXException {
				String message = "Fatal Error: " + getParseExceptionInfo(spe);
				throw new SAXException(message);
			}
		}
	}
}
