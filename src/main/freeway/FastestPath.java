package main.freeway;

import main.map.GeoMapModel;

public class FastestPath {
	/*TODO
	 * find the corresponding freeway for source
	 * find the corresponding freeway for destination
	 * find the junctions
	 * 
	 * FIND ACTUAL AVERAGE SPEED
	 * add up the average speeds of all the segments from one path
	 * add up the average speeds of all the segments from the alternate path
	*/
	
	/*
	 * =========================================================================
	 * MEMBER VARIABLES
	 * =========================================================================
	 */
	public static String source;
	public static String destination;
	public FreewaySegment freeway;
	public String sourceFreeway = " ";
	public String destinationFreeway = " ";
	public double averageSpeeds = 0;
	public double path1speed = 0;
	public double path2speed = 0;
	
	public double FastestPath(String start, String end, GeoMapModel mapModel){
		this.source = start;
		this.destination = end;
		
//		findFreeways(source, destination);
		mapModel.searchByRampName(source, true);
		mapModel.searchByRampName(destination, false);
		
		//CASE 1: source and end are on same freeway
		if(sourceFreeway == destinationFreeway){
			//iterate through list of segments in between points
			path1speed += averageSpeeds;
		}
		
		//CASE 2: source and end are on different freeways
		else{
			//figure out which freeways are connected 
			path2speed = 55;
		}
		
		//Compare the 2 speeds
		if(path1speed > path2speed){
			return path1speed;
		}
		else{
			return path2speed;
		}
		
	}
	
	//finds which freeway the given location belongs to
	public void findFreeways(FreewaySegment start, FreewaySegment end){
		//search in list of ramps for match
		
		sourceFreeway = start.getFreewayName();
		destinationFreeway = end.getFreewayName();
	}
	
	
	public static void main(String args[]){
		//Draw Fastest Path
	}
}
