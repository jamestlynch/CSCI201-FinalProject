package main.automobile;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;

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

	FreewaySegment freeway;
	Coordinate currentLocation;
	//FuturePoint holds the index of the array element that is upcoming. If futurepoint == Araylistsize, then we've reached the end.
	int nextPointNumber = 1;
	int numberOfSegmentPointsInThisPath;
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
	
	public Automobile(int id, double speed, FreewaySegment.Direction direction, String ramp, FreewaySegment freeway, GeoMapModel geoMapModel)
	{	
		this.freeway = freeway;
		numberOfSegmentPointsInThisPath = freeway.getSegmentPath().size();
		this.id = id;
		this.speed = speed;
		this.direction = direction;
		this.ramp = ramp;
		currentLocation = freeway.getSegmentPath().get(0);
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
		this.freeway = freeway;
	}
	/**updateLocation updates the current location according to three different test cases
	 * Case 1: the distance moved is within the same 2 points as defined in the segment path
	 * Case 2: the distance moved is past the same 2 points and has moved onto the next path, but hasn't gone onto the next segment
	 * Case 3: the distance moved is into the next segment.
	 * Case 3 invokes case 1 and 2 after moving into the new segment.
	 * @param time_elapse_milliseconds - the time we've elapsed
	 */
	//time_elapse is in milliseconds
	private void updateLocation(double time_elapse_milliseconds)
	{
		double hour_travelled = time_elapse_milliseconds/3600000;//3.6 million milliseoncds per hour
		double milesToTravel = hour_travelled*speed; //mph * h = m
		Coordinate nextDestinationCoord = freeway.getSegmentPath().get(nextPointNumber);
		double DistanceToNextCheckpoint = distance (currentLocation.getLat(), currentLocation.getLon(), nextDestinationCoord.getLat(), nextDestinationCoord.getLon());
		numberOfSegmentPointsInThisPath = freeway.getSegmentPath().size();
		
		if (StaysSameSegment(DistanceToNextCheckpoint, milesToTravel))
		{
			if (false) 
				System.out.println("This car is on the same segment!");
		}
		//if (DistanceToNextCheckpoint - milesTravelled <= 0 && (currentSegmentPointsCount-1 == nextPointNumber))
		else 
		{
			nextPointNumber = 1;
			if (geoMapModel.getNextFreewaySegment(freeway) != null) {
				freeway = geoMapModel.getNextFreewaySegment(freeway);
			}
			numberOfSegmentPointsInThisPath = freeway.getSegmentPath().size();
			nextDestinationCoord = freeway.getSegmentPath().get(nextPointNumber);
			DistanceToNextCheckpoint = distance (currentLocation.getLat(), currentLocation.getLon(), nextDestinationCoord.getLat(), nextDestinationCoord.getLon());
			StaysSameSegment(DistanceToNextCheckpoint, milesToTravel);
		}
		this.updateCarColor();
	}
	private boolean StaysSameSegment(double _DistanceToCheckpoint, double _milesToTravel)
	{
		double milesToTravel = _milesToTravel;// = hour_travelled*speed; //mph * h = m
		
		//SegmentList holds all the upcoming segment points
		ArrayList<Coordinate> SegmentList = freeway.getSegmentPath();
		Coordinate dest = SegmentList.get(nextPointNumber);
		numberOfSegmentPointsInThisPath = SegmentList.size();
		double DistanceToNextCheckpoint = _DistanceToCheckpoint;

		//This is saying if the distance to the checkpoint is within the miles capacity.
		if (DistanceToNextCheckpoint - milesToTravel > 0)
		{
			//Create a latinc and loninc according to the remaining distance and setting a proportion of distance to go.
			double latinc = (dest.getLat() - currentLocation.getLat()) * (milesToTravel/DistanceToNextCheckpoint);
			double loninc = (dest.getLon() - currentLocation.getLon()) * (milesToTravel/DistanceToNextCheckpoint);
			currentLocation.setLat(currentLocation.getLat() + latinc);
			currentLocation.setLon(currentLocation.getLon() + loninc);
			return true;
		}
		else if (DistanceToNextCheckpoint - milesToTravel <= 0 && (numberOfSegmentPointsInThisPath-1 > nextPointNumber))
		{
			//keep moving the base location until the miles remaining are within the distance to the next checkpoint.
			//Otherwise, move to the next checkpoint.
			Coordinate NewCheckpoint;
			do
			{
				dest = SegmentList.get(nextPointNumber);
				milesToTravel = milesToTravel - DistanceToNextCheckpoint;
				nextPointNumber++;
				NewCheckpoint = SegmentList.get(nextPointNumber);
				DistanceToNextCheckpoint = distance (dest.getLat(), dest.getLon(), NewCheckpoint.getLat(), NewCheckpoint.getLon());
			}
			while(DistanceToNextCheckpoint - milesToTravel <= 0 && (numberOfSegmentPointsInThisPath-1 > nextPointNumber));

			double latinc = (NewCheckpoint.getLat() - dest.getLat()) * (milesToTravel/DistanceToNextCheckpoint);
			double loninc = (NewCheckpoint.getLon() - dest.getLon()) * (milesToTravel/DistanceToNextCheckpoint);
			currentLocation.setLat(dest.getLat() + latinc);
			currentLocation.setLon(dest.getLon() + loninc);
			return true;
		}
		else
		{
			return false;
		}
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
				Thread.sleep(130);
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
