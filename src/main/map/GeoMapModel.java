package main.map;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import main.freeway.FreewayRamp;
import main.freeway.FreewaySegment;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class GeoMapModel {
	// HashMap that allows you to look-up a freeway section via its start ramp
	private static FreewayNetwork defaultDirectionFreewayNetwork;
	private static FreewayNetwork oppositeDirectionFreewayNetwork;
	
	private final File[] freewayXMLFiles = {
			new File("./Freeway-10/Freeway-10.xml"),
			new File("./Freeway-10/Freeway-10-1.xml"),
			new File("./Freeway-10/Freeway-10-2.xml"),
			new File("./Freeway-10/Freeway10-J.xml"),
			new File("./Freeway-10/Freeway10-J2.xml"),
			new File("./Freeway-101/Freeway101-1.xml"),
			new File("./Freeway-101/Freeway101-J.xml")
	};

	public GeoMapModel() {
		defaultDirectionFreewayNetwork = new FreewayNetwork();
		oppositeDirectionFreewayNetwork = new FreewayNetwork();
		// Load in the Freeways from the XML parser
		for(int i = 0; i < freewayXMLFiles.length; i++) {
			new FreewayLoader(freewayXMLFiles[i]);
		}
		
	}
	public ArrayList<FreewaySegment> returnAllSegment()
	{
		ArrayList<FreewaySegment> allSegments = new ArrayList<FreewaySegment>();
		for (FreewayRamp key: defaultDirectionFreewayNetwork.keySet()) {
			for (int i = 0; i < defaultDirectionFreewayNetwork.get(key).size(); i++)
			{
				FreewaySegment tempfs = defaultDirectionFreewayNetwork.get(key).get(i);
				System.out.println(tempfs.toString());
				allSegments.add(tempfs);
			}
		}
		return allSegments;
	}
	
	public static FreewaySegment searchForSegment(String rampName, FreewaySegment.Direction direction, String freewayName) 
			throws FreewaySegmentNotFoundException 
	{
		while (defaultDirectionFreewayNetwork.keySet().iterator().hasNext()) 
		{
			FreewayRamp currentRamp = defaultDirectionFreewayNetwork.keySet().iterator().next();
			if (rampName == currentRamp.getRampName()) 
			{
				ArrayList<FreewaySegment> currentSegment = defaultDirectionFreewayNetwork.get(currentRamp);
				for(int i = 0; i < currentSegment.size(); i++) 
				{
					if (direction == FreewaySegment.Direction.EAST || direction == FreewaySegment.Direction.WEST) 
					{
						if (currentSegment.get(i).getDirectionEW() == direction 
						   && freewayName.equals(currentSegment.get(i).getFreewayName())) 
						{
							return currentSegment.get(i);
						}
					}
				}
			}
		}
		throw new FreewaySegmentNotFoundException(rampName, freewayName, direction.toString());
	}
	
	
	public ArrayList<FreewaySegment> searchForSegmentWithRamp(FreewayRamp ramp) {
		return defaultDirectionFreewayNetwork.get(ramp);
	}
	
	
	
	/* =========================================================================
	 *   FREEWAY NETWORK: Custom version of Java's HashMap that overrides the 
	 *     put() method so that if multiple FreewaySegment's derive from the 
	 *     same FreewayRamp, we store both.
	 * ========================================================================= */
	
	private class FreewayNetwork extends HashMap<FreewayRamp, ArrayList<FreewaySegment>> {
		public void put(FreewayRamp ramp, FreewaySegment segment) {
			ArrayList<FreewaySegment> freewaySegmentsStartingAtRamp = new ArrayList<FreewaySegment>();
			
			if (defaultDirectionFreewayNetwork.get(ramp) == null) {
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
	
	
	
	/* =========================================================================
	 *   FREEWAY XML LOADER: Private, internal class for loading in the Segment
	 *     path data.
	 * ========================================================================= */
	
	private class FreewayLoader {
		
		public FreewayLoader(File file) 
		{
			// DocumentBuilderFactory creates a DocumentBuilder instance which parses the XML DOM for manipulation
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			try 
			{
				DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
				
				// Add exception handler for XML parse errors
				documentBuilder.setErrorHandler(new ParseErrorHandler(new PrintWriter(System.out)));
				
				Document document = documentBuilder.parse(file);
				document.normalize(); // Checks for DOM errors in the XML (combines adjacent text nodes, removes empty nodes)
				
				// Capture the root node (freeway); Assumption that there is only one per XML File
				Element freewayElement = (Element) (document.getElementsByTagName("freeway").item(0));
				
				// Name of the Freeway (e.g., 10, 110, 105, 405, etc.)
				String freewayName = freewayElement.getElementsByTagName("name").item(0).getTextContent();
				
				// List of all segment objects; Loops through to create the segment objects
				NodeList segmentNodeList = freewayElement.getElementsByTagName("segment");
				for (int segmentNumber = 0; segmentNumber < segmentNodeList.getLength(); segmentNumber++) {
					Element segmentElement = (Element) segmentNodeList.item(segmentNumber);
					
					Element rampsElement = (Element) segmentElement.getElementsByTagName("ramps").item(0);
					Element distanceElement = (Element) segmentElement.getElementsByTagName("distance").item(0);
					
					ArrayList<Coordinate> segmentPoints = new ArrayList<Coordinate>();
					// Only one points object in DOM (houses all point objects); Grab the point node list from the single <points> DOM object
					NodeList pointNodeList = ((Element) segmentElement.getElementsByTagName("points").item(0))
							.getElementsByTagName("point");
					
					// Loop through each <point> DOM object and add the coordinates to the Segment's path
					for (int pointNumber = 0; pointNumber < pointNodeList.getLength(); pointNumber++) {
						Coordinate point = new Coordinate(
							Double.parseDouble(((Element) pointNodeList.item(pointNumber)).getAttribute("x")), 
							Double.parseDouble(((Element) pointNodeList.item(pointNumber)).getAttribute("y"))
						);
						segmentPoints.add(point);
					}
					
					// Part of the Segment's definition; first and last <path> DOM object are the starting and ending ramps for the segment
					FreewayRamp startRamp = new FreewayRamp(rampsElement.getAttribute("begin"), segmentPoints.get(0));
					FreewayRamp endRamp = new FreewayRamp(rampsElement.getAttribute("end"), segmentPoints.get(segmentPoints.size() - 1));
					
					// Increase in longitude = EAST | Increase in latitude = NORTH
					double latitudeDifference = segmentPoints.get(0).getLat() - segmentPoints.get(segmentPoints.size() - 1).getLat();
					double longitudeDifference = segmentPoints.get(0).getLat() - segmentPoints.get(segmentPoints.size() - 1).getLat();
					
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
					
					String segmentName = freewayName + "-0-" + segmentNumber;
						
					FreewaySegment defaultFreewaySegment = new FreewaySegment(
						segmentName,
						freewayName,
						Double.parseDouble(distanceElement.getAttribute("d")),
						directionEW,
						directionNS,
						segmentPoints,
						startRamp,
						endRamp
					);
					
					// =========================================================================
					//   Store the opposite lane's data
					// =========================================================================
					latitudeDifference = segmentPoints.get(segmentPoints.size() - 1).getLat() - segmentPoints.get(0).getLat();
					longitudeDifference = segmentPoints.get(segmentPoints.size() - 1).getLat() - segmentPoints.get(0).getLat();
					
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
					
					segmentName = freewayName + "-1-" + segmentNumber;
					Collections.reverse(segmentPoints);
					
					FreewaySegment oppositeFreewaySegment = new FreewaySegment(
						segmentName,
						freewayName,
						Double.parseDouble(distanceElement.getAttribute("d")),
						directionEW,
						directionNS,
						segmentPoints,
						endRamp,
						startRamp
					);
					
					defaultDirectionFreewayNetwork.put(startRamp, defaultFreewaySegment);
					oppositeDirectionFreewayNetwork.put(endRamp, oppositeFreewaySegment);
				} // [Close] Segment List loop
			} catch (ParserConfigurationException pce) 
			{
				System.out.println("EXCEPTION: ParserConfigurationException: " + pce.getMessage());
			} catch (SAXException saxe) 
			{
				System.out.println("EXCEPTION: SAXException: " + saxe.getMessage());
			} catch (IOException ioe) 
			{
				System.out.println("EXCEPTION: IOException: " + ioe.getMessage());
			}
		}
		
		// From Java API Documentation: http://docs.oracle.com/javase/tutorial/jaxp/dom/readingXML.html#gestm
		private class ParseErrorHandler implements ErrorHandler 
		{
			/*	=========================================================================
			 *  Member Variables
			 * 	========================================================================= */
		    
			private PrintWriter out;
		    
		    /*	=========================================================================
			 *  Constructors
			 * 	========================================================================= */
		   
			ParseErrorHandler(PrintWriter out) 
			{
		        this.out = out;
		    }
		    
		    /*	=========================================================================
			 *  Exception Info Reporting: Provides a descriptive report of the SAX 
			 *  	Exception that is thrown.
			 * 	========================================================================= */
		    
		    private String getParseExceptionInfo(SAXParseException spe) 
		    {
		        String systemId = spe.getSystemId();
		        if (systemId == null) {
		            systemId = "null";
		        }

		        String info = "URI = " + systemId + " Line = " + spe.getLineNumber() + ": " + spe.getMessage();
		        return info;
		    }

		    /*	=========================================================================
			 *  Exceptions/Warnings: If there is a parse error, throw an SAX (Simple API 
			 *  	for XML)Exception. Not using an SAX parser, but leveraging the 
			 *  	standards that it put into place
			 * 	========================================================================= */
		    
		    public void warning(SAXParseException spe) throws SAXException 
		    {
		        out.println("Warning: " + getParseExceptionInfo(spe));
		    }
		        
		    public void error(SAXParseException spe) throws SAXException 
		    {
		        String message = "Error: " + getParseExceptionInfo(spe);
		        throw new SAXException(message);
		    }

		    public void fatalError(SAXParseException spe) throws SAXException 
		    {
		        String message = "Fatal Error: " + getParseExceptionInfo(spe);
		        throw new SAXException(message);
		    }
		}
	}
	
	
	/* =========================================================================
	 *   FREEWAY SEGMENT NOT FOUND EXCEPTION: Gets thrown if there is no segment
	 *     in our HashMap that begins at the ramp passed in.
	 * ========================================================================= */
	
}
