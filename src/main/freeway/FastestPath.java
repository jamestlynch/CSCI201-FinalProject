package main.freeway;

import java.util.ArrayList;

import main.map.GeoMapModel;
import main.map.GeoMapView;

public class FastestPath {
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
	public FreewayRamp endRamp;
	private static ArrayList<FreewaySegment> path1 = new ArrayList<FreewaySegment>();
	private static ArrayList<FreewaySegment> path2 = new ArrayList<FreewaySegment>();
	private static ArrayList<FreewaySegment> fastestPath = new ArrayList<FreewaySegment>();
	public double path1time = 0;
	public double path2time = 0;
	
	public double FastestPath(String start, String end, GeoMapModel mapModel){
		this.source = start;
		this.destination = end;
		
		//find a FreewaySegment that the source & destination belongs to
		sourceFreewaySegment = mapModel.searchByRampName(source, true);		//TODO move this into CASE 1
		destinationFreewaySegment = mapModel.searchByRampName(destination, true);
		//read in freeway names
		sourceFreewayName = sourceFreewaySegment.getFreewayName();
		destinationFreewayName = destinationFreewaySegment.getFreewayName();
		
		//CASE 1: source and end are on same freeway
		if(sourceFreewayName == destinationFreewayName){
			//Case a	TODO something that forces it to find path on same freeway
			
			currFreewaySegment = sourceFreewaySegment;
			endRamp = currFreewaySegment.getEndRamp();
			while(endRamp.getRampName() != destination){ 					//TODO PROBLEM: doesn't include last segment
				path1time += currFreewaySegment.getAverageSpeed();
				path1.add(currFreewaySegment);
				currFreewaySegment = mapModel.getNextFreewaySegment(currFreewaySegment);
				endRamp = currFreewaySegment.getEndRamp();
			}
			//Case b- go the other way
			sourceFreewaySegment = mapModel.searchByRampName(source, false);
			destinationFreewaySegment = mapModel.searchByRampName(destination, false);
			currFreewaySegment = sourceFreewaySegment;
			//TODO path
				path2time += currFreewaySegment.getAverageSpeed();
				path2.add(currFreewaySegment);
				currFreewaySegment = mapModel.getNextFreewaySegment(currFreewaySegment);
			
		}
		
		//CASE 2: source and end are on different freeways
		else{
			if(sourceFreewayName == "10"){
				//Case a:
				sourceFreewaySegment = mapModel.searchByRampName(source, true);
				destinationFreewaySegment = mapModel.searchByRampName(destination, true);
			}
			/* 		go right until you hit a junction
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
		if(path1time > path2time){
			return path1time;
		}
		else{
			return path2time;
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
			//TODO
//		mapview.draw(fastestPath);
	}
}
