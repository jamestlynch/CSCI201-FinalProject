package main.automobile;

import java.awt.Color;
import java.util.Calendar;

import main.CSCI201Maps;
import main.freeway.FreewaySegment;
import main.map.GeoMapModel;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;


public class Automobile
{
	int id;
	double speed;
	FreewaySegment.Direction direction;
	
	String ramp;
	MapMarkerCircle carMarker;

	FreewaySegment freewaySegment;
	
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
	
	private Coordinate currentLocation;
	private Coordinate destination;
	private FreewaySegment destinationSegment;
	
	private GeoMapModel geoMapModel;

	private boolean debuggingAutomobileRunnable = false;
	private boolean debuggingUpdateFunction = false;
	private boolean debuggingSetNextDestination = false;
	private boolean debuggingUpdateLocation = false;
	private boolean debuggingInitDestination = false;
	private boolean debuggingAutomobileUpdated = true;
	
	private final double milesPerHour_to_milesPerSeconds = 0.000277777778; // (1 / 60 / 60):  Used for converting for distance calculations with current time's milliseconds
	
	int repaintCount = 0;
	
	public Automobile(int id, double speed, FreewaySegment.Direction direction, String ramp, FreewaySegment freeway, GeoMapModel geoMapModel)
	{	
		this.freewaySegment = freeway;
		numberOfSegmentPointsInThisPath = freeway.getSegmentPath().size();
		this.id = id;
		this.speed = speed;
		this.direction = direction;
		this.ramp = ramp;
		this.currentLocation = freeway.getSegmentPath().get(0);
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
	
	public FreewaySegment.Direction getDirection() {
		return direction;
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public Coordinate getDestination() {
		return destination;
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
	
	public void initDestination() {
		if (debuggingInitDestination) System.out.println("[INIT DESTINATION] Initializing destination for car ID #" + id);
		
		if (locationPointNumber < numberOfSegmentPointsInThisPath - 2) { // -2 because that's the one right before last point on the segment
			this.destination = freewaySegment.getSegmentPath().get(locationPointNumber + 1); // Get next on same segment
			destinationSegment = freewaySegment;
		} else if (geoMapModel.getNextFreewaySegment(freewaySegment) != null) { // If is next to last or last, set destination as next freeway
			this.destination = geoMapModel.getNextFreewaySegment(freewaySegment).getStartRamp().getRampLocation();
		} else { // CHECK: If it's at the end of a highway, 
			if (debuggingInitDestination) System.out.println("[INIT DESTINATION] Could not find destination for car ID #" + id);
			this.destination = null;
			return; // Keep the currentLocation the same
		}
	}
	
	public void setNextDestinationPoint(double totalDistanceOnPath, double distanceToTravel) 
	{
		if (distanceToTravel < totalDistanceOnPath) {
			double portionOfPath = distanceToTravel / totalDistanceOnPath;
			
			// Changes of position
			double dLongitude = (this.destination.getLon() - this.currentLocation.getLon()) * portionOfPath;
			double dLatitude  = (this.destination.getLat() - this.currentLocation.getLat()) * portionOfPath;
			
			this.currentLocation = new Coordinate(currentLocation.getLat() + dLatitude,
												  currentLocation.getLon() + dLongitude); 
			// Keep the destination the same, still on same path
		} else if (locationPointNumber < numberOfSegmentPointsInThisPath - 2) { // -2 because that's the one right before last point on the segment
			this.currentLocation = this.destination; // Destination before update
			this.destination = freewaySegment.getSegmentPath().get(++locationPointNumber); // Get next on same segment
		} else if (geoMapModel.nextFreewaySegmentExists(freewaySegment)) { // If is next to last or last, set destination as next freeway
			setFreewaySegmentToNextSegment(); // FreewaySegment = next freeway segment on network
			this.currentLocation = this.destination; // Destination before update
			
			if (debuggingSetNextDestination) System.out.println("[SET NEXT DEST] does the next freeway segment exist? " + (geoMapModel.getNextFreewaySegment(freewaySegment) != null));
			
			try {
				destinationSegment = geoMapModel.getNextFreewaySegment(freewaySegment);
				if (debuggingSetNextDestination) System.out.println("[SET NEXT DEST] Car ID #" + id + ": " + destinationSegment.toString());
			} catch (NullPointerException npe) {
				if (debuggingSetNextDestination) System.out.println("[SET NEXT DEST] NULLPOINTEREXCEPTION: Car ID #" + id + " could not get destination FREEWAY SEGMENT");
			}
			
			
			try {//CHANGE SOMETHING HERE?
				destination = destinationSegment.getSegmentPath().get(0); // Get first point along segment path
			} catch (NullPointerException npe) {
				if (debuggingSetNextDestination) System.out.println("[SET NEXT DEST] NULLPOINTEREXCEPTION: Car ID #" + id + " could not get destination RAMP");
			}
		} else { // CHECK: If it's at the end of a highway, 
			this.destination = null;
			return; // Keep the currentLocation the same
		}
	}

	public void setFreewaySegmentToNextSegment() {
		// Setting a new FreewaySegment value over the current one.
		this.freewaySegment = geoMapModel.getNextFreewaySegment(freewaySegment);
	
		// Establishing new free segment number and new starting location on the new segment.
		numberOfSegmentPointsInThisPath = freewaySegment.getSegmentPath().size();
		locationPointNumber = 0;

		this.updateCarColor();
	}
	
	/**updateLocation updates the current location according to three different test cases
	 * Case 1: the distance moved is within the same 2 points as defined in the segment path
	 * Case 2: the distance moved is past the same 2 points and has moved onto the next path, but hasn't gone onto the next segment
	 * Case 3: the distance moved is into the next segment.
	 * Case 3 invokes case 1 and 2 after moving into the new segment.
	 * @param timeElapsedInMilliseconds - the time we've elapsed
	 */
	public void updateLocation(double timeElapsedInMilliseconds)
	{		
		double timeRemaining = timeElapsedInMilliseconds / 1000; //3.6 million milliseconds per hour
		numberOfSegmentPointsInThisPath = freewaySegment.getSegmentPath().size();	

		while (timeRemaining > 0 && destination != null) { // Until the time runs out keep updating position while updating the speed at the same time
			if (debuggingUpdateLocation) System.out.println("[UPDATE LOCATION] timeRemaining: " + timeRemaining);
			
			this.currentLocation = this.getCarMarker().getCoordinate();
			
			double distanceAlongPath = 0; 
			distanceAlongPath = coordinatesToMiles(currentLocation.getLat(), currentLocation.getLon(), destination.getLat(), destination.getLon());
			double timeToCompleteSegment = distanceAlongPath  				// s = m / (m/s)
					/ /*      ------------------------------------------ 	*/ 
							  (speed * milesPerHour_to_milesPerSeconds); 	// Convert speed to m/millisecond
			
			if (debuggingUpdateLocation) System.out.println("[UPDATE LOCATION] CAR ID #" + id + " Difference in times: " + (timeRemaining - timeToCompleteSegment));

			double distanceToTravel;
			if (timeRemaining > timeToCompleteSegment) {
				if (debuggingUpdateLocation) System.out.println("[UPDATE LOCATION] Car ID #" + id + " Time Remaing (" + timeRemaining + ") is greater than Time to complete (" + timeToCompleteSegment + ") segment: " + (timeRemaining > timeToCompleteSegment));
				distanceToTravel = distanceAlongPath; // Enough time to travel full path
				timeRemaining = timeRemaining - timeToCompleteSegment; // Take away the timeToCompleteSegment from the total time remaining
				
				if (debuggingUpdateLocation) System.out.println("[UPDATE LOCATION] timeRemaining: " + timeRemaining);
				this.setNextDestinationPoint(distanceAlongPath, distanceToTravel); // Set next destination point based on current location
			} else { // Not enough time to go the full distance on the path, find where new location along path he should be at.
				if (debuggingUpdateLocation) System.out.println("[UPDATE LOCATION] CAR ID #" + id + " !!!: " + timeRemaining);
				distanceToTravel = timeRemaining * (speed * milesPerHour_to_milesPerSeconds); // Convert speed to m/millisecond
				timeRemaining = 0; // No more time left to travel
				this.setNextDestinationPoint(distanceAlongPath, distanceToTravel); // Only travel remainder of path
			}
		}
		if (carMarker.getLat() == currentLocation.getLat() && carMarker.getLon() == currentLocation.getLon() && (destination != null))
		{
			System.out.println(id + "INVALID LOCATION: ");
		}
		if (destination == null)
		{
			carMarker.setVisible(false);
			carMarker.setBackColor(Color.BLACK);
			carMarker.setLat(0);
			carMarker.setLon(0);
			return;
		}
		this.updateCarColor();
	
		this.carMarker.setLat(currentLocation.getLat());
		this.carMarker.setLon(currentLocation.getLon());
		
		if (debuggingAutomobileUpdated && id % 100 == 0) System.out.println("[AUTOMOBILE UPDATED] Automobile #" + id + " was updated");
	}
	private double coordinatesToMiles(double lat1, double lon1, double lat2, double lon2) 
	{
		//source: http://www.geodatasource.com/developers/java
		  double theta = lon1 - lon2;
		  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		  dist = Math.acos(dist);
		  dist = rad2deg(dist);
		  dist = dist * 60 * 1.1515;
		  return (dist);
	
	}
	private double deg2rad(double deg) 
	{

		return (deg * Math.PI / 180.0);

	}
	private double rad2deg(double rad) 
	{
		return (rad * 180 / Math.PI);
	}

}
