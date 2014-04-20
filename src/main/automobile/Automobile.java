package main.automobile;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;

import main.CSCI201Maps;
import main.freeway.FreewaySegment;
import main.map.GeoMapModel;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;


public class Automobile implements Runnable
{
	int id;
	double speed;
	FreewaySegment.Direction direction;
	
	String ramp;
	//String freeway;
	MapMarkerCircle carMarker;

	FreewaySegment freewaySegment;
	
	Coordinate currentLocation;
	//FuturePoint holds the index of the array element that is upcoming. If futurepoint == Araylistsize, then we've reached the end.
	int nextPointNumber = 1;
	int numberOfSegmentPointsInThisPath;
	private int locationPointNumber;
	final double carRadius = 0.001;
	
	Color darkGreen = new Color(0x0B610B);
	Color green = new Color(0x04B404);
	Color lightGreen = new Color(0x58FA82);
	Color yellowGreen = new Color(0xC8FE2E);
	Color yellow = new Color(0xFFFF00);
	Color yellowOrange = new Color(0xF7D358);
	Color orange = new Color(0xFF8000);
	Color redOrange = new Color(0xFF4000);
	Color red = new Color(0xFF0000);
	Color darkRed = new Color(0x8A0808);
	
	private GeoMapModel geoMapModel;

	private boolean debuggingAutomobileRunnable = false;
	private boolean debuggingUpdateFunction = false;
	
	int repaintCount = 0;
	
	public Automobile(int id, double speed, FreewaySegment.Direction direction, String ramp, FreewaySegment freeway, GeoMapModel geoMapModel)
	{	
		this.freewaySegment = freeway;
		numberOfSegmentPointsInThisPath = freeway.getSegmentPath().size();
		this.id = id;
		this.speed = speed;
		this.direction = direction;
		this.ramp = ramp;
		currentLocation = freeway.getSegmentPath().get(0);
		locationPointNumber = 0;
		this.geoMapModel = geoMapModel;
		carMarker = new MapMarkerCircle(currentLocation, carRadius);
		carMarker.setColor(Color.BLACK);
		this.updateCarColor();
		carMarker.setVisible(true);
	}
	public Automobile()
	{
	}
	
	public String toString()
	{
		return ("Car #" + id + " is moving at " + speed);
	}
	
	public MapMarkerCircle getCarMarker() {
		return carMarker;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}

	public void setSpeed(double speed)
	{
		this.speed = speed;
	}

	public void setDirection(FreewaySegment.Direction direction)
	{
		this.direction = direction;
	}

	public void setRamp(String ramp)
	{
		this.ramp = ramp;
	}
	public void updateCarColor()
	{
		if (this.speed>70)
		{
			carMarker.setBackColor(darkGreen);
		}
		else if (this.speed>65)
		{
			carMarker.setBackColor(green);
		}
		else if (this.speed>60)
		{
			carMarker.setBackColor(lightGreen);
		}
		else if (this.speed>50)
		{
			carMarker.setBackColor(yellowGreen);
		}
		else if (this.speed>40)
		{
			carMarker.setBackColor(yellow);
		}
		else if (this.speed>35)
		{
			carMarker.setBackColor(yellowOrange);
		}
		else if (this.speed>30)
		{
			carMarker.setBackColor(orange);
		}
		else if (this.speed>25)
		{
			carMarker.setBackColor(redOrange);
		}
		else if (this.speed>15)
		{
			carMarker.setBackColor(red);
		}
		else
		{
			carMarker.setBackColor(darkRed);
		}
		
	}

	public void setFreeway(FreewaySegment freeway)
	{
		this.freewaySegment = freeway;
	}
	/**updateLocation updates the current location according to three different test cases
	 * Case 1: the distance moved is within the same 2 points as defined in the segment path
	 * Case 2: the distance moved is past the same 2 points and has moved onto the next path, but hasn't gone onto the next segment
	 * Case 3: the distance moved is into the next segment.
	 * Case 3 invokes case 1 and 2 after moving into the new segment.
	 * @param timeElapsedInMilliseconds - the time we've elapsed
	 */
	
	//time_elapse is in milliseconds
	public void updateLocation(double timeElapsedInMilliseconds)
	{		
		double timeElapsedInHours = timeElapsedInMilliseconds / 3600000; //3.6 million milliseconds per hour
		//double milesToTravel = timeElapsedInHours * speed; //mph * h = m
		//Coordinate nextDestinationCoord = freewaySegment.getSegmentPath().get(nextPointNumber);
		//double DistanceToNextCheckpoint = distance (currentLocation.getLat(), currentLocation.getLon(), nextDestinationCoord.getLat(), nextDestinationCoord.getLon());
		numberOfSegmentPointsInThisPath = freewaySegment.getSegmentPath().size();	
		// Get the destination coordinate
		//locationPointNumber starts at 0  according to the location of the current point.
		Coordinate destination;
		if (locationPointNumber < numberOfSegmentPointsInThisPath - 2) { // Same segment, different target point. -2 because Moving thats the one right before. Last Segment moving forward
			destination = freewaySegment.getSegmentPath().get(++ locationPointNumber);
		} else { // If it needs to switch onto next segment
			if (geoMapModel.getNextFreewaySegment(freewaySegment) == null) { // If it's at the end of a highway
				return; // Keep the currentLocation the same
			} else {
				//setting a new FreewaySegment value over the current one.
				freewaySegment = geoMapModel.getNextFreewaySegment(freewaySegment);
				//Establishing new free segment number and new starting location on the new segment.
				numberOfSegmentPointsInThisPath = freewaySegment.getSegmentPath().size();
				locationPointNumber = 0;
				
				if (locationPointNumber < numberOfSegmentPointsInThisPath - 2) {
					destination = freewaySegment.getSegmentPath().get(locationPointNumber + 1);
				} else if (geoMapModel.getNextFreewaySegment(freewaySegment) == null) {
					return;
				} else {
					destination = geoMapModel.getNextFreewaySegment(freewaySegment).getSegmentPath().get(0);
				}
				this.updateCarColor();
			}
		}
		
		Coordinate newLocationAfterUpdate = getTargetCoordinate(timeElapsedInHours, currentLocation, destination);
		if (freewaySegment == null)
		{
			System.out.println ("reached the end of a freeway.");
			this.carMarker.setVisible(false);
			return;
			
		}
		
		
		if (currentLocation.getLat() == destination.getLat() && currentLocation.getLon() == destination.getLon() )
		{
			System.out.println("THIS SHOULD NEVER BE PRINTING--------------------");
		}
		else
		{
			System.out.println(" THIS IS THE DIFFERENCE IN LOCATION: " + newLocationAfterUpdate.toString() + "," + destination.toString());
		}
		System.out.println(freewaySegment.getSegmentName());
		currentLocation = newLocationAfterUpdate;
		this.carMarker.setLat(newLocationAfterUpdate.getLat());
		this.carMarker.setLon(newLocationAfterUpdate.getLon());
		System.out.println("[UPDATE AUTOMOBILES] Updating Car ID #" + id);

	}
		
	private double distance(double lat1, double lon1, double lat2, double lon2) 
	{
		double theta = lon1 - lon2;
		double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
		dist = Math.acos(dist);
		dist = Math.toDegrees(dist);
		dist = dist * 60 * 1.1515;
		return (dist);
	}
	
	private Coordinate getTargetCoordinate(double timeRemaining, Coordinate currentLocation, Coordinate destination)
	{	
		if (debuggingUpdateFunction && id % 100 == 0) System.out.println("[AUTOMOBILE UPDATE] Values at the start of update "
				+ "call for Automobile ID #" + id + 
				" Time remaining: " + timeRemaining + " Current Location: " + currentLocation.toString() + 
				" Destination: " + destination.toString());
		double dLat_dLon = (destination.getLat() - currentLocation.getLat()) 
					/ /*   -------------------------------------------------- 	*/
						   (destination.getLon() - currentLocation.getLon());
		
		double dLon_dLat = (destination.getLon() - currentLocation.getLon())
					/ /*   -------------------------------------------------- 	*/
						   (destination.getLat() - currentLocation.getLat());
		
		double distanceDestinationAndLocation = Math.sqrt(Math.pow(currentLocation.getLat() + destination.getLat(), 2)
														+ Math.pow(currentLocation.getLon() + destination.getLon(), 2));
		
		double timeElapsedAlongPath = distanceDestinationAndLocation / speed;
		
		double distanceToTravelAlongPath;
		if ((timeRemaining - timeElapsedAlongPath) > 0) { // Still timeRemaining after taking off time to travel along path
			timeRemaining -= timeElapsedAlongPath;
			distanceToTravelAlongPath = distanceDestinationAndLocation;
		} else { // No remainder time so just use what's left of timeRemaining to travel a portion of the segment
			distanceToTravelAlongPath = timeRemaining * speed;
			timeRemaining = 0;
		}
		
		if (distanceDestinationAndLocation - distanceToTravelAlongPath > 0 && timeRemaining <= 0) { // If you did not travel down full path,
			double portionOfPathDistance = (distanceDestinationAndLocation - distanceToTravelAlongPath)
									/ /*   ------------------------------------------------------------ 	*/
														(distanceDestinationAndLocation);
			
			repaintCount++;
			
			double newLatitude  = destination.getLat();// 	(dLat_dLon * speed * timeRemaining) * portionOfPathDistance; // x = (dx/dy) * y
			double newLongitude = destination.getLon();//	(dLon_dLat * speed * timeRemaining) * portionOfPathDistance; // y = (dy/dx) * x
			if (debuggingUpdateFunction && id % 100 == 0) System.out.println("[AUTOMOBILE UPDATE] ID #" + id+ " dLat_dLon: " + dLat_dLon + " Destination longitude: " + destination.getLon() + " Portion of path's distance: " + portionOfPathDistance);
			if (debuggingUpdateFunction && id % 100 == 0) System.out.println("[AUTOMOBILE UPDATE] ID #" + id+ " dLon_dLat: " + dLon_dLat + " Destination latitude: " + destination.getLat() + " Portion of path's distance: " + portionOfPathDistance + "\n");
			return new Coordinate(newLatitude, newLongitude);
		} else if (distanceDestinationAndLocation == distanceToTravelAlongPath && timeRemaining <= 0) {
			return destination;
		} else { // Else, there is distance remaining passed going to the end of path
			Coordinate newLocation;
			Coordinate newDestination;
			if (freewaySegment.getSegmentPath().get(++ locationPointNumber) != null) { // Same segment, different target point
				newLocation = destination;
				newDestination = freewaySegment.getSegmentPath().get(locationPointNumber);
			} if (geoMapModel.getNextFreewaySegment(freewaySegment) == null) {
				System.out.println("1. Return currentLocation");
				return currentLocation;
			} else {
				newLocation = destination;
				freewaySegment = geoMapModel.getNextFreewaySegment(freewaySegment);
				numberOfSegmentPointsInThisPath = freewaySegment.getSegmentPath().size();
				locationPointNumber = 0;
				if (locationPointNumber < numberOfSegmentPointsInThisPath - 2) {
					newDestination = freewaySegment.getSegmentPath().get(locationPointNumber + 1);
				}  else if (geoMapModel.getNextFreewaySegment(freewaySegment) == null) {
					System.out.println("2. Return currentLocation");
					return currentLocation;
				} else {
					newDestination = geoMapModel.getNextFreewaySegment(freewaySegment).getSegmentPath().get(0);
				}
				speed = freewaySegment.getAverageSpeed();
				this.updateCarColor();
			}
			System.out.println("3. Return recursive call");
			return getTargetCoordinate(timeRemaining, newLocation, newDestination);
		}
	}

	public void run() {
		try
		{
			Calendar now = Calendar.getInstance();
			long timeBefore = now.get(Calendar.MILLISECOND);
			long timeAfter;
			while(true)
			{
				if (debuggingAutomobileRunnable) System.out.println("[AUTOMOBILE RUN] Car #" + id + "'s thread is run");
				
				carMarker.setLat(currentLocation.getLat());
				carMarker.setLon(currentLocation.getLon());
				now = Calendar.getInstance();
				timeAfter = now.get(Calendar.MILLISECOND);
				updateLocation(timeBefore - timeAfter);
				timeBefore = timeAfter;
				Thread.sleep(CSCI201Maps.automobileUpdateRate);
			}
		}
		catch(InterruptedException ie)
		{
			System.out.println ("INTERUPTED EXCEPTION: " + ie.getMessage());
		}
	}
	
	public FreewaySegment.Direction getDirection() {
		return direction;
	}
	
	public double getSpeed()
	{
		return speed;
	}
}
