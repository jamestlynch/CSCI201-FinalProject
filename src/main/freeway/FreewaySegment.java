package main.freeway;

import java.util.ArrayList;

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
	private ArrayList<Coordinate> segmentPath;
	private ArrayList<FreewaySegment> adjacentSections;
	private FreewayRamp startRamp = null;
	private FreewayRamp endRamp = null;
	
	//private ArrayList<Automobile> carsOnSection;
	
	public FreewaySegment(
		String name, String freewayName,
		double distance, 
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
		this.startRamp = start;
		this.endRamp = end;
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
}
