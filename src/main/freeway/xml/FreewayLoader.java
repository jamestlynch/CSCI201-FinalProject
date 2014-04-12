package main.freeway.xml;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import main.freeway.Freeway;
import main.freeway.ramp.FreewayRamp;
import main.freeway.section.FreewaySegment;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class FreewayLoader {
	/*	=========================================================================
	 *  Member Variables
	 * 	========================================================================= */
	
	private Element freewayElement; // Store the root element because you can grab every other element from it.
	
	
	/*	=========================================================================
	 *  Constructors
	 * 	========================================================================= */
	
	public FreewayLoader(File file) {
		HashMap<FreewayRamp, FreewaySegment> freewayHash = new HashMap<FreewayRamp, FreewaySegment>();
		
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
			
			// Add exception handler for XML parse errors
			documentBuilder.setErrorHandler(new ParseErrorHandler(new PrintWriter(System.out)));
			
			Document document = documentBuilder.parse(file);
			document.normalize(); // Checks for DOM errors in the XML (combines adjacent text nodes, removes empty nodes)
			
			// Capture the root node (restaurant)
			NodeList freewayNodeList = document.getElementsByTagName("freeway");
			Node freewayNode = freewayNodeList.item(0);
			Element freewayElement = (Element) freewayNode;
			
			String freewayName = freewayElement.getElementsByTagName("name").item(0).getTextContent();
			System.out.println(freewayName);
			
			NodeList segmentNodeList = freewayElement.getElementsByTagName("segment");
			for (int segmentNumber = 0; segmentNumber < segmentNodeList.getLength(); segmentNumber++) {
				Element segmentElement = (Element) segmentNodeList.item(segmentNumber);
				
				Element rampsElement = (Element) segmentElement.getElementsByTagName("ramps").item(0);
				Element distanceElement = (Element) segmentElement.getElementsByTagName("distance").item(0);
				
				ArrayList<Coordinate> segmentPoints = new ArrayList<Coordinate>();
				NodeList pointsNodeList = segmentElement.getElementsByTagName("points");
				for (int pointNumber = 0; pointNumber < pointsNodeList.getLength(); pointNumber++) {
					Coordinate point = new Coordinate(
						Double.parseDouble(((Element) pointsNodeList.item(pointNumber)).getAttribute("x")), 
						Double.parseDouble(((Element) pointsNodeList.item(pointNumber)).getAttribute("y"))
					);
					segmentPoints.add(point);
				}
				
				FreewayRamp startRamp = new FreewayRamp(rampsElement.getAttribute("begin"), segmentPoints.get(0));
				FreewayRamp endRamp = new FreewayRamp(rampsElement.getAttribute("end"), segmentPoints.get(segmentPoints.size() - 1));
				
				String segmentName = freewayName + "-" + segmentNumber;
				FreewaySegment freewaySegment = new FreewaySegment(
					segmentName, 
					Double.parseDouble(distanceElement.getAttribute("d")),
					null,
					FreewaySegment.Direction.NORTH,
					segmentPoints,
					startRamp,
					endRamp
				);
				
				freewayHash.put(startRamp, freewaySegment);
			}
			
			Freeway freeway = new Freeway(file.getName().substring(file.getName().indexOf('-')), freewayHash);
			// Pass the freeway reference to the object that holds the references to all freeways
			
		} catch (ParserConfigurationException pce) {
			System.out.println("EXCEPTION: ParserConfigurationException: " + pce.getMessage());
		} catch (SAXException saxe) {
			System.out.println("EXCEPTION: SAXException: " + saxe.getMessage());
		} catch (IOException ioe) {
			System.out.println("EXCEPTION: IOException: " + ioe.getMessage());
		}
	}
	
	public Element getRootElement() {
		return freewayElement;
	}
	
	public static void main(String[] args) {
		
		File freeway10 = new File("./Freeway-10/Freeway-10.xml");
		new FreewayLoader(freeway10);
	}
	
	// From Java API Documentation: http://docs.oracle.com/javase/tutorial/jaxp/dom/readingXML.html#gestm
	private static class ParseErrorHandler implements ErrorHandler {
		/*	=========================================================================
		 *  Member Variables
		 * 	========================================================================= */
	    
		private PrintWriter out;

	    
	    /*	=========================================================================
		 *  Constructors
		 * 	========================================================================= */
	   
		ParseErrorHandler(PrintWriter out) {
	        this.out = out;
	    }

	    
	    /*	=========================================================================
		 *  Exception Info Reporting: Provides a descriptive report of the SAX 
		 *  	Exception that is thrown.
		 * 	========================================================================= */
	    
	    private String getParseExceptionInfo(SAXParseException spe) {
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
