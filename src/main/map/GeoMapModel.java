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
import main.freeway.FreewayRamp;
import main.freeway.FreewaySegment;
import main.automobile.Automobile;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class GeoMapModel implements Runnable {
	/*
	 * =========================================================================
	 * MEMBER VARIABLES
	 * =========================================================================
	 */

	// HashMap that allows you to look-up a freeway section via its start ramp
	private static FreewayNetwork defaultDirectionFreewayNetwork;
	private static FreewayNetwork oppositeDirectionFreewayNetwork;
	private static ArrayList<FreewaySegment> orderedSegments405 = new ArrayList<FreewaySegment>();
	private static ArrayList<FreewaySegment> orderedSegments105 = new ArrayList<FreewaySegment>();
	private static ArrayList<FreewaySegment> orderedSegments10 = new ArrayList<FreewaySegment>();
	private static ArrayList<FreewaySegment> orderedSegments101 = new ArrayList<FreewaySegment>();

	private ArrayList<Automobile> automobilesInFreewayNetwork = new ArrayList<Automobile>();

	private final File[] freewayXMLFiles = {
			new File("./Freeway-10/Freeway10Updated.xml"),
			new File("./Freeway-101/Freeway101-1.xml"),
			new File("./Freeway-101/Freeway101-J.xml"),
			new File("./Freeway-105/Freeway105-1.xml"),
			new File("./Freeway-405/Freeway405.xml") };

	private Calendar timeNow = Calendar.getInstance();
	private long timeInMillisAfter;
	private long timeInMillisBefore;
	
	private boolean debuggingSearch = false;
	private boolean debuggingAutomobileMarkers = false;
	private boolean debuggingMapUpdateLock = true;
	
	/*
	 * =========================================================================
	 * CONSTRUCTORS
	 * =========================================================================
	 */

	public GeoMapModel() {
		defaultDirectionFreewayNetwork = new FreewayNetwork();
		oppositeDirectionFreewayNetwork = new FreewayNetwork();

		// Load in the Freeways from the XML parser
		for (int i = 0; i < freewayXMLFiles.length; i++) {
			new FreewayLoader(freewayXMLFiles[i]);
		}
	}

	/*
	 * =========================================================================
	 * ACCESSOR METHODS
	 * =========================================================================
	 */

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

	public ArrayList<FreewaySegment> getListOf405Segments() {
		return orderedSegments405;
	}

	public ArrayList<FreewaySegment> getListOf105Segments() {
		return orderedSegments105;
	}

	public ArrayList<FreewaySegment> getListOf10Segments() {
		return orderedSegments10;
	}

	public ArrayList<FreewaySegment> getListOf101Segments() {
		return orderedSegments101;
	}

	public FreewaySegment searchByRampName(String rampName, boolean isDefaultDirection) {
		if (isDefaultDirection) {
			for (FreewayRamp ramps  :  defaultDirectionFreewayNetwork.keySet()) {
				if (ramps.getRampName().equals(rampName)) {
					return defaultDirectionFreewayNetwork.get(ramps).get(0);
				}
			}		
		} else {
			for (FreewayRamp ramps  :  oppositeDirectionFreewayNetwork.keySet()) {
				if (ramps.getRampName().equals(rampName)) {
					return oppositeDirectionFreewayNetwork.get(ramps).get(0);
				}
			}
		}
		return null;
	}
	
	public FreewaySegment getNextFreewaySegment(FreewaySegment oldSegment) {
		//Checks to see if there is a following segment after this ramp (End of the Freeway)
		
		if ((searchByRampName(oldSegment.getStartRamp().getRampName(),  /* isDefaultDirection */ true) != null)  // If the oldSegment is in the defaultDirectionFreewayNetwork
	    &&  (searchByRampName(oldSegment.getEndRamp().getRampName(),    /* isDefaultDirection */ true) != null)) // ...and there is a segment starting at its end location
		{ 
			//Checks to see which direction the segment is facing (default vs. opposite)
			if (oldSegment.getDirectionEW().equals(
					(searchByRampName(oldSegment.getStartRamp().getRampName(),  /* isDefaultDirection */ true))
					.getDirectionEW()) 
				||
				oldSegment.getDirectionEW().equals(
					(searchByRampName(oldSegment.getStartRamp().getRampName(),  /* isDefaultDirection */ true))
					.getDirectionEW())) 
			{
				if((searchByRampName(oldSegment.getEndRamp().getRampName(),  /* isDefaultDirection */ true)) != null)
				{
					return searchByRampName(oldSegment.getEndRamp().getRampName(),  /* isDefaultDirection */ true);
				} else {
					System.out.println("[GET NEXT SEGMENT] Should mean it's at the end of a freeway.");
					return null; // CHECK: Must be end of freeway??
				}
			}
		} else 
		if ((searchByRampName(oldSegment.getEndRamp().getRampName(),   /* isDefaultDirection */ false) != null)
		&&  (searchByRampName(oldSegment.getStartRamp().getRampName(), /* isDefaultDirection */ false) != null)) 
		{
			//Checks to see which direction the segment is facing (default vs. opposite)
			if (oldSegment.getDirectionEW().equals(
					(searchByRampName(oldSegment.getStartRamp().getRampName(),  /* isDefaultDirection */ false))
					.getDirectionEW()) 
				||
				oldSegment.getDirectionEW().equals(
					(searchByRampName(oldSegment.getStartRamp().getRampName(),  /* isDefaultDirection */ false))
					.getDirectionEW())) 
			{
				if((searchByRampName(oldSegment.getEndRamp().getRampName(),  /* isDefaultDirection */ false)) != null)
				{
					return searchByRampName(oldSegment.getEndRamp().getRampName(),  /* isDefaultDirection */ false);
				} else {
					System.out.println("[GET NEXT SEGMENT] Should mean it's at the end of a freeway.");
					return null; // CHECK: Must be end of freeway??
				}
			}
		}	

		System.out.println("[GET NEXT SEGMENT] Can't get a freeway beginning at " + oldSegment.getEndRamp().getRampName());
		return null;
	}
	
	public void addAutomobileToNetwork(Automobile newAutomobile) {
		synchronized(automobilesInFreewayNetwork) 
		{
			automobilesInFreewayNetwork.add(newAutomobile);
		}
	}
	
	public void eraseAutomobilesInFreewayNetwork() {
		synchronized(automobilesInFreewayNetwork) 
		{
			automobilesInFreewayNetwork.clear();
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
	
	public void runAllAutomobileThreads() {
		Semaphore automobileSemaphore = new Semaphore(10);
		ExecutorService executor = Executors.newFixedThreadPool(automobilesInFreewayNetwork.size());
		
		for (int i = 0; i < 10/*automobilesInFreewayNetwork.size()*/; i++)
		{	
			try {
				automobileSemaphore.acquire(1);
				executor.execute(automobilesInFreewayNetwork.get(i));
				automobileSemaphore.release(1);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}
	
	public void init() {		
		CSCI201Maps.grabMapUpdateLock();
		
		if (debuggingMapUpdateLock) System.out.println("[MAP UPDATE LOCK] Map Model grabbed lock.");
		
		timeInMillisAfter = System.currentTimeMillis();
		for (int i = 0; i < automobilesInFreewayNetwork.size(); i++)
		{	
			automobilesInFreewayNetwork.get(i).updateLocation(timeInMillisBefore - timeInMillisAfter);
		}
		timeInMillisBefore = timeInMillisAfter;
		CSCI201Maps.giveUpMapUpdateLock();
		if (debuggingMapUpdateLock) System.out.println("[MAP UPDATE LOCK] Map Model gave up lock.");
		CSCI201Maps.startMapViewThread();
	}
	
	public void run() {		
		timeInMillisBefore = System.currentTimeMillis();
		
		this.init();
		
		while (true) {
			try {
				Thread.sleep(CSCI201Maps.automobileUpdateRate);
				
				CSCI201Maps.grabMapUpdateLock();
				if (debuggingMapUpdateLock) System.out.println("[MAP UPDATE LOCK] Map Model grabbed lock.");
				
				timeInMillisAfter = System.currentTimeMillis();
				System.out.println("Time After: " + timeInMillisAfter + " Time Before: " + timeInMillisBefore);
				for (int i = 0; i < automobilesInFreewayNetwork.size(); i++)
				{	
					automobilesInFreewayNetwork.get(i).updateLocation(timeInMillisAfter - timeInMillisBefore);
				}
				timeInMillisBefore = timeInMillisAfter;
				
				CSCI201Maps.giveUpMapUpdateLock();
				if (debuggingMapUpdateLock) System.out.println("[MAP UPDATE LOCK] Map Model gave up lock.");
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}

	/*
	 * =========================================================================
	 * SEGMENT SEARCH METHODS
	 * =========================================================================
	 */

	public FreewaySegment searchForSegment(String rampName,
			FreewaySegment.Direction direction, String freewayName)
			throws FreewaySegmentNotFoundException {
		// Search through the default network, if found return that segment from
		// the defaultDirectionFreewayNetwork
		FreewaySegment segmentDefault = searchForSegmentWithNetwork(rampName,
				direction, freewayName, defaultDirectionFreewayNetwork);
		if (debuggingSearch) System.out.println("[STARTING THE OPPOSITE DIRECTION NETWORK SEARCH]");
		FreewaySegment segmentOpposite = searchForSegmentWithNetwork(rampName,
				direction, freewayName, oppositeDirectionFreewayNetwork);

		if (segmentDefault != null)
			if (debuggingSearch) System.out.println("DEFAULT [SEGMENT BEFORE SEARCHING OP]\t"
					+ segmentDefault.getStartRamp().getRampName());
		else
			if (debuggingSearch) System.out.println("DEFAULT [SEGMENT BEFORE SEARCHING OP]\t"
					+ "UNDEFINED");

		// // If not found in the default network, search the opposite direction
		// segment = (searchForSegment(rampName, direction, freewayName,
		// oppositeDirectionFreewayNetwork) == null)
		// ? segment
		// : searchForSegment(rampName, direction, freewayName,
		// oppositeDirectionFreewayNetwork);

		if (segmentOpposite != null)
			if (debuggingSearch) System.out.println("OPPOSITE [SEGMENT AFTER SEARCHING OP]\t"
					+ segmentOpposite.getStartRamp().getRampName());
		else
			if (debuggingSearch) System.out.println("OPPOSITE [SEGMENT AFTER SEARCHING OP]\t"
					+ "UNDEFINED");

		return (segmentDefault == null) ? segmentOpposite : segmentDefault;
	}

	public FreewaySegment searchForSegmentWithNetwork(String rampName,
			FreewaySegment.Direction direction, String freewayName,
			HashMap<FreewayRamp, ArrayList<FreewaySegment>> networkToSearch)
			throws FreewaySegmentNotFoundException {
		for (FreewayRamp ramp : networkToSearch.keySet()) {
			FreewayRamp currentRamp = ramp;
			/*
			 * if (debuggingSearch) System.out.println(" *  " + currentRamp.getRampName() + ", " +
			 * "<" +
			 * networkToSearch.get(currentRamp).get(0).getDirectionEW().toString
			 * () + ", " +
			 * networkToSearch.get(currentRamp).get(0).getDirectionNS
			 * ().toString() + ">");
			 */

			if (rampName.equals(currentRamp.getRampName())) {
				if (debuggingSearch) System.out.println("[RAMP FOUND]\t\t\t"
						+ currentRamp.getRampName());
				ArrayList<FreewaySegment> currentSegment = networkToSearch
						.get(currentRamp);

				for (int i = 0; i < currentSegment.size(); i++) {
					if (direction == FreewaySegment.Direction.EAST
							|| direction == FreewaySegment.Direction.WEST) {
						if (debuggingSearch) System.out.println(
								"[CHECK FREEWAY & DIRECTION]\tRamp: "
										+ rampName
										+ "\tDirection is E/W: "
										+ direction
										+ "\t (Passed in) "
										+ freewayName
										+ " == "
										+ currentSegment.get(i)
												.getFreewayName()
										+ " (Our value)?\t"
										+ freewayName.equals(currentSegment
												.get(i).getFreewayName())
						);
						
						if (currentSegment.get(i).getDirectionEW() == direction
								&& freewayName.equals(currentSegment.get(i)
										.getFreewayName())) {
							if (debuggingSearch) System.out.println(
									"[CHECK FREEWAY & DIRECTION]\tSEGMENT FOUND: Returning the E/W freeway ramp starting at "
											+ currentSegment.get(i)
													.getStartRamp()
													.getRampName()
											+ " on the "
											+ currentSegment.get(i)
													.getFreewayName()
							);
							
							return currentSegment.get(i);
						}
					}
					if (direction == FreewaySegment.Direction.NORTH
							|| direction == FreewaySegment.Direction.SOUTH) {
						if (debuggingSearch) System.out.println(
								"[CHECK FREEWAY & DIRECTION]\tRamp: "
										+ rampName
										+ "\tDirection is N/S:"
										+ direction
										+ "\t (Passed in) "
										+ freewayName
										+ " == "
										+ currentSegment.get(i)
												.getFreewayName()
										+ " (Our value)?\t"
										+ freewayName.equals(currentSegment
												.get(i).getFreewayName())
						);
						
						if (debuggingSearch) System.out.println("\t\t\t\tOUR Direction: "
								+ currentSegment.get(i).getDirectionNS()
								+ ", \t\t\tTheir Direction " + direction);
						if (currentSegment.get(i).getDirectionNS() == direction
								&& freewayName.equals(currentSegment.get(i)
										.getFreewayName())) {
							if (debuggingSearch) System.out.println(
									"[CHECK FREEWAY & DIRECTION]\tSEGMENT FOUND: Returning the N/S freeway ramp starting at "
											+ currentSegment.get(i)
													.getStartRamp()
													.getRampName()
											+ " on the "
											+ currentSegment.get(i)
													.getFreewayName()
							);
							
							return currentSegment.get(i);
						}
					}
				}

			}
		}
		return null; // If segment not found, return null
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
					// Only one points object in DOM (houses all point objects);
					// Grab the point node list from the single <points> DOM
					// object
					NodeList pointNodeList = ((Element) segmentElement
							.getElementsByTagName("points").item(0))
							.getElementsByTagName("point");


					// Loop through each <point> DOM object and add the coordinates to the Segment's path
					// Changed order that points were added in (decrement instead of increment through list)
					for (int pointNumber = pointNodeList.getLength()-1; pointNumber >= 0; pointNumber--) {
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
					double longitudeDifference = segmentPoints.get(0).getLat()
							- segmentPoints.get(segmentPoints.size() - 1)
									.getLat();

					FreewaySegment.Direction directionEW;
					FreewaySegment.Direction directionNS;

					if (latitudeDifference < 0) {
						directionEW = FreewaySegment.Direction.EAST;
					} else {
						directionEW = FreewaySegment.Direction.WEST;
					}

					if (longitudeDifference < 0) {
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
							segmentPoints.size() - 1).getLat()
							- segmentPoints.get(0).getLat();

					if (latitudeDifference < 0) {
						directionEW = FreewaySegment.Direction.EAST;
					} else {
						directionEW = FreewaySegment.Direction.WEST;
					}

					if (longitudeDifference < 0) {
						directionNS = FreewaySegment.Direction.NORTH;
					} else {
						directionNS = FreewaySegment.Direction.SOUTH;
					}

					segmentName = "OF" + freewayName + "S" + segmentNumber;
					Collections.reverse(segmentPoints);

					FreewaySegment oppositeFreewaySegment = new FreewaySegment(
							segmentName,
							freewayName,
							Double.parseDouble(distanceElement
									.getAttribute("d")),
							Integer.parseInt(speedLimitElement.getTextContent()),
							directionEW, directionNS, segmentPoints, endRamp,
							startRamp);

					if (debuggingFreewayLoader) System.out.println(" +  Creating " + segmentName
							+ "\tBegin: " + endRamp.getRampName() + "\tEnd: "
							+ startRamp.getRampName() + "\tFreeway: "
							+ freewayName + "\tDirection: <"
							+ directionEW.toString() + ", "
							+ directionNS.toString() + ">");

					if (freewayName.equals("405"))
						orderedSegments405.add(defaultFreewaySegment);
					else if (freewayName.equals("105"))
						orderedSegments105.add(defaultFreewaySegment);
					else if (freewayName.equals("10"))
						orderedSegments10.add(defaultFreewaySegment);
					else if (freewayName.equals("101"))
						orderedSegments101.add(defaultFreewaySegment);
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

	/*
	 * =========================================================================
	 * FREEWAY SEGMENT NOT FOUND EXCEPTION: Gets thrown if there is no segment
	 * in our HashMap that begins at the ramp passed in.
	 * =========================================================================
	 */

}
