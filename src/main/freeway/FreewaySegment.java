package main.freeway;

import java.util.ArrayList;

import main.automobile.Automobile;

import org.openstreetmap.gui.jmapviewer.Coordinate;

public class FreewaySegment {
	private String segmentName;
	private String freewayName;
	
	public enum Direction {
		NORTH, SOUTH, EAST, WEST
	}
	
	private Direction directionEW;
	private Direction directionNS;
	private double distance;
	private int speedLimit;
	private ArrayList<Coordinate> segmentPath = new ArrayList<Coordinate>();
	private ArrayList<FreewaySegment> adjacentSections;
	private FreewayRamp startRamp = null;
	private FreewayRamp endRamp = null;
	
	private ArrayList<Automobile> automobilesOnSegment = new ArrayList<Automobile>();
	private ArrayList<Automobile> automobilesFromLatestUpdate = new ArrayList<Automobile>();
	private double latestAverageSpeed;
	
	private double averageSpeed;
	
	private boolean debuggingAverageSpeed = false;
	
	//private ArrayList<Automobile> carsOnSection;
	public FreewaySegment(
		String name, String freewayName,
		double distance, 
		int speedLimit,
		/* ArrayList<FreewaySegment> adjacentSections ,*/ 
		Direction directionEW,
		Direction directionNS,
		ArrayList<Coordinate> segmentPath, 
		FreewayRamp start, FreewayRamp end
	) {
		this.segmentName = name;
		this.freewayName = freewayName;
		//this.adjacentSections = adjacentSections;
		this.segmentPath = segmentPath;
		this.directionEW = directionEW;
		this.directionNS = directionNS;
		this.distance = distance;
		this.setSpeedLimit(speedLimit);
		this.averageSpeed = speedLimit;
		this.startRamp = start;
		this.endRamp = end;
	}
	
	public void addAutomobileToSegment(Automobile newAutomobile)
	{
		if (debuggingAverageSpeed) System.out.println("\n\n=========================================================================================================================");
		if (debuggingAverageSpeed) System.out.println("\tOld average speed on " + startRamp.getRampName() + " segment: " + averageSpeed);
		
		if (automobilesOnSegment.size() != 0)
		{
			double totalSpeed = averageSpeed * automobilesOnSegment.size(); // Size BEFORE adding the newAutomobile
			automobilesOnSegment.add(newAutomobile);
			averageSpeed = (newAutomobile.getSpeed() + totalSpeed) / automobilesOnSegment.size(); // Size AFTER adding newAutomobile
		} else
		{
			automobilesOnSegment.add(newAutomobile);
			averageSpeed = newAutomobile.getSpeed();
		}
		
		if (debuggingAverageSpeed) System.out.println("\tNew average speed on " + startRamp.getRampName() + " segment: " + averageSpeed);
		if (debuggingAverageSpeed) System.out.println("=========================================================================================================================\n\n");
	}
	
	public void removeAutomobileFromSegment(Automobile automobileToRemove)
	{
		if (debuggingAverageSpeed) System.out.println("\n\n=========================================================================================================================");
		if (debuggingAverageSpeed) System.out.println("\tOld average speed on " + startRamp.getRampName() + " segment: " + averageSpeed);

		double totalSpeed = averageSpeed * automobilesOnSegment.size(); // Size BEFORE removing the automobileToRemove
		automobilesOnSegment.remove(automobileToRemove);
		averageSpeed = (totalSpeed - automobileToRemove.getSpeed()) / automobilesOnSegment.size(); // Size AFTER removing automobileToRemove
		
		if (debuggingAverageSpeed) System.out.println("\tNew average speed on " + startRamp.getRampName() + " segment: " + averageSpeed);
		if (debuggingAverageSpeed) System.out.println("=========================================================================================================================\n\n");
	}

	public String getSegmentName() {
		return segmentName;
	}
	
	public String getFreewayName() {
		return freewayName;
	}
	
	public ArrayList<Coordinate> getSegmentPath()
	{
		return segmentPath;
	}

	public Direction getDirectionEW() {
		return directionEW;
	}

	public Direction getDirectionNS() {
		return directionNS;
	}

	public double getDistance() {
		return distance;
	}

	public ArrayList<FreewaySegment> getAdjacentSections() {
		return adjacentSections;
	}

	public FreewayRamp getStartRamp() {
		return startRamp;
	}

	public FreewayRamp getEndRamp() {
		return endRamp;
	}
	
	public double getAverageSpeed() {
		return averageSpeed;
	}
	public ArrayList<Automobile> getAutomobilesOnSegment()
	{
		return automobilesOnSegment;
	}

	public ArrayList<Automobile> getAutomobilesFromLatestUpdate() {
		return automobilesFromLatestUpdate;
	}

	public void setAutomobilesFromLatestUpdate(
			ArrayList<Automobile> automobilesFromLatestUpdate) {
		this.automobilesFromLatestUpdate = automobilesFromLatestUpdate;
	}

	public double getLatestAverageSpeed() {
		return latestAverageSpeed;
	}

	public void setLatestAverageSpeed(double latestAverageSpeed) {
		this.latestAverageSpeed = latestAverageSpeed;
	}
	public void addAutomobileToLatestUpdate(Automobile auto)
	{
		this.automobilesFromLatestUpdate.add(auto);
		this.latestAverageSpeed += (auto.getSpeed() - latestAverageSpeed)/(automobilesFromLatestUpdate.size());
	}
	public void clearAutomobilesFromLatestUpdate()
	{
		this.automobilesFromLatestUpdate.clear();
		this.latestAverageSpeed = 0;
	}
	public String getStringDirectionEW()
	{
		if (this.directionEW==Direction.EAST)
			return "East";
		else if (this.directionEW == Direction.WEST)
			return "West";
		else 
			return "Invalid";
			
	}
	public String getStringDirectionNS()
	{
		if (this.directionNS==Direction.NORTH)
			return "North";
		else if (this.directionNS == Direction.SOUTH)
			return "South";
		else 
			return "Invalid";
			
	}

	public int getSpeedLimit() {
		return speedLimit;
	}

	public void setSpeedLimit(int speedLimit) {
		this.speedLimit = speedLimit;
	}
}
