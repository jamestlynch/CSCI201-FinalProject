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
	public FreewayRamp startRamp;
	private static ArrayList<FreewaySegment> path1 = new ArrayList<FreewaySegment>();
	private static ArrayList<FreewaySegment> path2 = new ArrayList<FreewaySegment>();
	private static ArrayList<FreewaySegment> fastestPath = new ArrayList<FreewaySegment>();
	public double path1time = 0;
	public double path2time = 0;
	
	public double findFastestPath(String start, String end, GeoMapModel mapModel){
		this.source = start;
		this.destination = end;
		
		//find a FreewaySegment that the source & destination belongs to
		sourceFreewaySegment = mapModel.searchByRampName(source, true);	
		destinationFreewaySegment = mapModel.searchByRampName(destination, true);
		//read in freeway names
		sourceFreewayName = sourceFreewaySegment.getFreewayName();
		destinationFreewayName = destinationFreewaySegment.getFreewayName();
		
			//====================FOR REFERENCE====================
			sourceFreewaySegment = mapModel.searchByRampName(source, false);
			destinationFreewaySegment = mapModel.searchByRampName(destination, false);
			currFreewaySegment = sourceFreewaySegment;
			// path
				path2time += currFreewaySegment.getAverageSpeed();
				path2.add(currFreewaySegment);
				currFreewaySegment = mapModel.getNextFreewaySegment(currFreewaySegment);
			//========================================================
		
		//CASE 1: Source is on the 10
		if(sourceFreewayName == "10"){
			//TODO Case A: Source is on left nubbin segment
			currFreewaySegment = sourceFreewaySegment;
			if(mapModel.isJunction(currFreewaySegment) == 0 && sourceFreewaySegment.getSegmentName() == " "){
				//Go right, continue with path
				//Go right, then down 405
			}
			
			//TODO Case B: Source is on the right nubbin segment
			else if(mapModel.isJunction(currFreewaySegment) == 0 && sourceFreewaySegment.getSegmentName() == " "){
				sourceFreewaySegment = mapModel.searchByRampName(source, false);
				destinationFreewaySegment = mapModel.searchByRampName(destination, false);
				currFreewaySegment = sourceFreewaySegment;
				//Go left, continue with path
				//Go left, down 101 
			}
			
			else{
			//Case C: Source is in middle
				//Start by going right
				sourceFreewaySegment = mapModel.searchByRampName(source, true);
				destinationFreewaySegment = mapModel.searchByRampName(destination, true);
				currFreewaySegment = sourceFreewaySegment;
				startRamp = currFreewaySegment.getStartRamp();
				while (startRamp.getRampName() != destination){
					//Reaches a junction
					if(mapModel.isJunction(currFreewaySegment) == 2){
						path1time += currFreewaySegment.getAverageSpeed();
						path1.add(currFreewaySegment);
						//TODO set getNextFreewaySegment to the 101 segment
							//PROBLEM: does this have to be hard coded every time for each junction?
						continue;
					}
					
					path1time += currFreewaySegment.getAverageSpeed();
					path1.add(currFreewaySegment);
					currFreewaySegment = mapModel.getNextFreewaySegment(currFreewaySegment);
					startRamp = currFreewaySegment.getStartRamp();
				}
				/* 	=============FOR REFERENCE================	
				 * go right until you hit a junction
				 * 	go down 101
				 * 	if(destinationFreewayName == 101)
				 * 		stop at destination
				 * 	else go until you hit a junction
				 * 	go left on 105
				 * 	if(destinationFreewayName == 105)
				 * 		stop at destination
				 * 	else go until you hit a junction
				 * 	go up 405
				 * 	if(destinationFreewayName == 405)
				 * 		stop at destination
				 * 	path1speed = sum of all average speeds of segments on path 
				 */
			
			//Start by going left
				sourceFreewaySegment = mapModel.searchByRampName(source, false);
				destinationFreewaySegment = mapModel.searchByRampName(destination, false);
				currFreewaySegment = sourceFreewaySegment;
				startRamp = currFreewaySegment.getStartRamp();
				while (startRamp.getRampName() != destination){
					//Reaches a junction
					if(mapModel.isJunction(currFreewaySegment) == 2){
						path1time += currFreewaySegment.getAverageSpeed();
						path1.add(currFreewaySegment);
						//TODO set getNextFreewaySegment to the 405 segment
							//PROBLEM: does this have to be hard coded every time for each junction?
						continue;
					}
					
					path2time += currFreewaySegment.getAverageSpeed();
					path2.add(currFreewaySegment);
					currFreewaySegment = mapModel.getNextFreewaySegment(currFreewaySegment);
					startRamp = currFreewaySegment.getStartRamp();
				}
				/* ==========FOR REFERENCE===========
				 * go left until you hit a junction
				 * 	go down 405
				 *	if(destinationFreewayName == 405)
				 * 	stop at destination
			 	*	else go until you hit a junction
			 	* 	go right on 105
			 	* 	if(destinationFreewayName == 105)
			 	* 		stop at destination
			 	* 	else go until you hit a junction
			 	* 	go up 101
			 	* 	if(destinationFreewayName == 101)
			 	* 		stop at destination
			 	* 	path2speed = sum of all average speeds of segments on path
				 */
			}
			
		}//end 10 freeway case
		
		//CASE 2: Source is on the 105
		if(sourceFreewayName == "105"){
			//TODO Case a: source is on left nubbin
			currFreewaySegment = sourceFreewaySegment;
			if(mapModel.isJunction(currFreewaySegment) == 0 && sourceFreewaySegment.getSegmentName() == " "){
				//go right, continue with path
				//go right, go up 405
			}
			
			//TODO Case b: source is on right nubbin
			else if(mapModel.isJunction(currFreewaySegment) == 0 && sourceFreewaySegment.getSegmentName() == " "){
				//go left, continue with path
				//go left, go up on 101
			}
			
			//Case c: source is in the middle
			else{
				
			}
			
		}//end 105 case
			/*
	
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
			
		
		
		//Compare the 2 speeds
		if(path1time > path2time){
			return path1time;
		}
		else{
			return path2time;
		}
		
	}
	
	//TODO Historical Fastest Path

	/*  POSSIBLY NEATER TO USE THIS FUNCTION
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
