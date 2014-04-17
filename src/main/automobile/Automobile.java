package main.automobile;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;

import main.freeway.FreewaySegment;

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
	MapMarkerCircle carsprite;

	FreewaySegment freeway;
	Coordinate currentLocation;
	//FuturePoint holds the index of the array element that is upcoming. If futurepoint == Araylistsize, then we've reached the end.
	int FuturePoint = 1;
	int ArrayListSize;
	final double carradius = 0.003;

	public Automobile(int id, double speed, FreewaySegment.Direction direction, String ramp, FreewaySegment freeway)
	{	
		this.freeway = freeway;
		ArrayListSize = freeway.getSegmentPath().size();
		this.id = id;
		this.speed = speed;
		this.direction = direction;
		this.ramp = ramp;
		currentLocation = freeway.getSegmentPath().get(0);
		carsprite = new MapMarkerCircle(currentLocation, carradius);
		carsprite.setColor(Color.BLACK);
		carsprite.setBackColor(Color.GREEN);
		carsprite.setVisible(true);
	}
	public Automobile()
	{
	}
	public String toString()
	{
		return ("Car #" + id + " is moving at " + speed);
	}
	
	public MapMarkerCircle getCarsprite() {
		return carsprite;
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
		double miles = hour_travelled*speed; //mph * h = m
		ArrayList<Coordinate> SegmentList = freeway.getSegmentPath();
		Coordinate dest = SegmentList.get(FuturePoint);
		double DistanceToCheckpoint = distance (currentLocation.getLat(), currentLocation.getLon(), dest.getLat(), dest.getLon());
		if (StaysSameSegment(time_elapse_milliseconds))
		{}
		else if (DistanceToCheckpoint - miles <= 0 && (ArrayListSize == FuturePoint))
		{
			FuturePoint = 0;
			freeway = freeway.getAdjacentSections().get(0);
			ArrayListSize = freeway.getAdjacentSections().size();
			miles = miles - DistanceToCheckpoint;
			StaysSameSegment(time_elapse_milliseconds);
		}
	}
	private boolean StaysSameSegment(double time_elapse_milliseconds)
	{
		double hour_travelled = time_elapse_milliseconds/3600000;//3.6 million milliseoncds per hour
		double miles = hour_travelled*speed; //mph * h = m
		ArrayList<Coordinate> SegmentList = freeway.getSegmentPath();
		Coordinate dest = SegmentList.get(FuturePoint);
		double DistanceToCheckpoint = distance (currentLocation.getLat(), currentLocation.getLon(), dest.getLat(), dest.getLon());

		//This is saying if the distance to the checkpoint is within the miles capacity.
		if (DistanceToCheckpoint - miles > 0)
		{
			//Create a latinc and loninc according to the remaining distance and setting a proportion of distance to go.
			double latinc = (dest.getLat() - currentLocation.getLat()) * (miles/DistanceToCheckpoint);
			double loninc = (dest.getLon() - currentLocation.getLon()) * (miles/DistanceToCheckpoint);
			currentLocation.setLat(currentLocation.getLat() + latinc);
			currentLocation.setLon(currentLocation.getLon() + loninc);
			return true;
		}
		else if (DistanceToCheckpoint - miles <= 0 && (ArrayListSize > FuturePoint))
		{
			//keep moving the base location until the miles remaining are within the distance to the next checkpoint.
			//Otherwise, move to the next checkpoint.
			Coordinate NewDest;
			do
			{
				dest = SegmentList.get(FuturePoint);
				miles = miles - DistanceToCheckpoint;
				FuturePoint++;
				NewDest = SegmentList.get(FuturePoint);
				DistanceToCheckpoint = distance (dest.getLat(), dest.getLon(), NewDest.getLat(), NewDest.getLon());
			}
			while(DistanceToCheckpoint - miles <= 0 && (ArrayListSize > FuturePoint));

			double latinc = (NewDest.getLat() - dest.getLat()) * (miles/DistanceToCheckpoint);
			double loninc = (NewDest.getLon() - dest.getLon()) * (miles/DistanceToCheckpoint);
			currentLocation.setLat(dest.getLat() + latinc);
			currentLocation.setLon(dest.getLon() + loninc);
			return true;
		}
		return false;
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
	@Override
	public void run() {
		try
		{
			Calendar now = Calendar.getInstance();
			long timebefore = now.get(Calendar.MILLISECOND);
			long timeafter;
			while(true)
			{
				carsprite.setLat(currentLocation.getLat());
				carsprite.setLon(currentLocation.getLon());
				now = Calendar.getInstance();
				timeafter = now.get(Calendar.MILLISECOND);
				updateLocation(timebefore-timeafter);
				timebefore = timeafter;
				Thread.sleep(13);
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
}
