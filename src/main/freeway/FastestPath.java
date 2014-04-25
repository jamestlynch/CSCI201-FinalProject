package main.freeway;

import java.util.ArrayList;

import main.map.GeoMapModel;
import main.map.GeoMapView;

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
	public FreewaySegment sourceFreewaySegment;
	public FreewaySegment destinationFreewaySegment;
	public FreewaySegment currFreewaySegment;
	public String sourceFreewayName = " ";
	public String destinationFreewayName = " ";
	private static ArrayList<FreewaySegment> path1 = new ArrayList<FreewaySegment>();
	private static ArrayList<FreewaySegment> path2 = new ArrayList<FreewaySegment>();
	private static ArrayList<FreewaySegment> fastestPath = new ArrayList<FreewaySegment>();
	public double path1speed = 0;
	public double path2speed = 0;
	
	public double averageSpeeds = 0;
	
	public double FastestPath(String start, String end, GeoMapModel mapModel){
		this.source = start;
		this.destination = end;
		
		//find the FreewaySegment that the source & destination belongs to
		sourceFreewaySegment = mapModel.searchByRampName(source, true);
		destinationFreewaySegment = mapModel.searchByRampName(destination, false);
		//read in freeway names
		sourceFreewayName = sourceFreewaySegment.getFreewayName();
		destinationFreewayName = destinationFreewaySegment.getFreewayName();
		
		//CASE 1: source and end are on same freeway
		if(sourceFreewayName == destinationFreewayName){
			//iterate through list of segments in between points on same freeway
			mapModel.getNextFreewaySegment(sourceFreewaySegment);
			path1speed += averageSpeeds;
			//iterate through list of segments on the other freeways
			path2speed += averageSpeeds;
		}
		
		//CASE 2: source and end are on different freeways
		else{
			/*if(sourceFreewayName == the 10)
			 * case a:
			 * 		go right until you hit a junction
			 * 		go down 101
			 * 		if(destinationFreewayName == 101)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go left on 105
			 * 		if(destinationFreewayName == 105)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go up 405
			 * 		if(destinationFreewayName == 405)
			 * 			stop at destination
			 * 		path1speed = sum of all average speeds of segments on path
			 * case b:
			 * 		go left until you hit a junction
			 * 		go down 405
			 * 		if(destinationFreewayName == 405)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go right on 105
			 * 		if(destinationFreewayName == 105)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go up 101
			 * 		if(destinationFreewayName == 101)
			 * 			stop at destination
			 * 		path2speed = sum of all average speeds of segments on path
			 * 
			 * if(sourceFreewayName == the 105)
			 * case a:
			 * 		go right until you hit a junction
			 * 		go up 101
			 * 		if(destinationFreewayName == 101)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go left on 10
			 * 		if(destinationFreewayName == 10)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go down 405
			 * 		if(destinationFreewayName == 405)
			 * 			stop at destination
			 * 		path1speed = sum of all average speeds of segments on path
			 * case b:
			 * 		go left until you hit a junction
			 * 		go up 405
			 * 		if(destinationFreewayName == 405)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go right on 10
			 * 		if(destinationFreewayName == 10)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go down 101
			 * 		if(destinationFreewayName == 101)
			 * 			stop at destination
			 * 		path2speed = sum of all average speeds of segments on path
			 * 
			 * if(sourceFreewayName == the 101)
			 * case a:
			 * 		go up until you hit a junction
			 * 		go left on 10
			 * 		if(destinationFreewyName == 10)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go down on 405
			 * 		if(destinationFreewayName == 405)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go right on 105
			 * 		if(destinationFreewayName == 105)
			 * 			stop at destination
			 * 		path1speed = sum of all average speeds of segments on path
			 * case b:
			 * 		go down until you hit a junction
			 * 		go left on 105
			 * 		if(destinationFreewayName == 105)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go up on 405
			 * 		if(destinationFreewayName == 405)
			 * 			stop at destination
			 * 		else go until you hit a junction
			 * 		go right on 10
			 * 		if(destinationFreewayName == 10)
			 * 			stop at destination
			 * 		path2speed = sum of all average speeds of segments on path
			 * 
			 *  if(sourceFreewayName == the 405)
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
		
//		mapview.draw(fastestPath);
	}
}