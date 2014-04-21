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
	public FreewaySegment sourceFreeway;
	public FreewaySegment destinationFreeway;
	public String sourceFreewayName = " ";
	public String destinationFreewayName = " ";
	public double averageSpeeds = 0;
	public double path1speed = 0;
	public double path2speed = 0;
	
	public double FastestPath(String start, String end, GeoMapModel mapModel){
		this.source = start;
		this.destination = end;
		
		//find the FreewaySegment that the source & destination belongs to
		sourceFreeway = mapModel.searchByRampName(source, true);
		destinationFreeway = mapModel.searchByRampName(destination, false);
		//read in freeway names
		sourceFreewayName = sourceFreeway.getFreewayName();
		destinationFreewayName = destinationFreeway.getFreewayName();
		
		//CASE 1: source and end are on same freeway
		if(sourceFreeway == destinationFreeway){
			//iterate through list of segments in between points on same freeway
			path1speed += averageSpeeds;
			//iterate through list of segments on the other freeways
			path2speed += averageSpeeds;
		}
		
		//CASE 2: source and end are on different freeways
		else{
			/*if(source is on the 10)
			 * case a:
			 * 		go right until you hit a junction
			 * 		go down 101
			 * 		if(destination is on 101)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go left on 105
			 * 		if(destination is on 105)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go up 405
			 * 		if(destination is on 405)
			 * 			stop at destination
			 * 		path1speed = sum of all average speeds of segments on path
			 * case b:
			 * 		go left until you hit a junction
			 * 		go down 405
			 * 		if(destination is on 405)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go right on 105
			 * 		if(destination is on 105)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go up 101
			 * 		if(destination is on 101)
			 * 			stop at destination
			 * 		path2speed = sum of all average speeds of segments on path
			 * 
			 * if(source is on the 105)
			 * case a:
			 * 		go right until you hit a junction
			 * 		go up 101
			 * 		if(destination is on 101)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go left on 10
			 * 		if(destination is on 10)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go down 405
			 * 		if(destination is on 405)
			 * 			stop at destination
			 * 		path1speed = sum of all average speeds of segments on path
			 * case b:
			 * 		go left until you hit a junction
			 * 		go up 405
			 * 		if(destination is on 405)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go right on 10
			 * 		if(destination is on 10)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go down 101
			 * 		if(destination is on 101)
			 * 			stop at destination
			 * 		path2speed = sum of all average speeds of segments on path
			 * 
			 * if(source is on the 101)
			 * case a:
			 * 		go up until you hit a junction
			 * 		go left on 10
			 * 		if(destination is on 10)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go down on 405
			 * 		if(destination is on 405)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go right on 105
			 * 		if(destination is on 105)
			 * 			stop at destination
			 * 		path1speed = sum of all average speeds of segments on path
			 * case b:
			 * 		go down until you hit a junction
			 * 		go left on 105
			 * 		if(destination is on 105)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go up on 405
			 * 		if(destination is on 405)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go right on 10
			 * 		if(destination is on 10)
			 * 			stop at destination
			 * 		path2speed = sum of all average speeds of segments on path
			 * 
			 *  if(source is on the 405)
			 * case a:
			 * 		go up until you hit a junction
			 * 		go right on 10
			 * 		if(destination is on 10)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go down on 101
			 * 		if(destination is on 101)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go left on 105
			 * 		if(destination is on 105)
			 * 			stop at destination
			 * 		path1speed = sum of all average speeds of segments on path
			 * case b:
			 * 		go down until you hit a junction
			 * 		go right on 105
			 * 		if(destination is on 105)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go up on 101
			 * 		if(destination is on 101)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go left on 10
			 * 		if(destination is on 10)
			 * 			stop at destination
			 * 		path2speed = sum of all average speeds of segments on path
			 */
			
		}
		
		//Compare the 2 speeds
		if(path1speed > path2speed){
			return path1speed;
		}
		else{
			return path2speed;
		}
		
	}

	/*
	//finds which freeway the given location belongs to
	public void findFreeways(FreewaySegment start, FreewaySegment end){
		//search in list of ramps for match
		
		sourceFreeway = start.getFreewayName();
		destinationFreeway = end.getFreewayName();
	}
	*/
	
	public static void main(String args[]){
		//Draw Fastest Path
	}
}
