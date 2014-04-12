package main.freeway.section;

import java.util.ArrayList;

import main.freeway.ramp.FreewayRamp;

import org.openstreetmap.gui.jmapviewer.Coordinate;

public class FreewaySegment {
	private String segmentName;
	
	public enum Direction {
		NORTH, SOUTH, EAST, WEST
	}
	
	private Direction direction;
	private double distance;
	private ArrayList<Coordinate> segmentPath;
	private ArrayList<FreewaySegment> adjacentSections;
	private FreewayRamp startRamp = null;
	private FreewayRamp endRamp = null;
	
	//private ArrayList<Automobile> carsOnSection;
	
	public FreewaySegment(
		String name, double distance, 
		ArrayList<FreewaySegment> adjacentSections, 
		Direction direction, 
		ArrayList<Coordinate> segmentPath, 
		FreewayRamp start, FreewayRamp end
	) {
		this.segmentName = name;
		this.adjacentSections = adjacentSections;
		this.segmentPath = segmentPath;
		this.direction = direction;
		this.distance = distance;
		this.startRamp = start;
		this.endRamp = end;
	}
}
