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
	public double fastestPathTime = 0;
	
	public ArrayList<FreewaySegment> findFastestPath(String start, String end, GeoMapModel mapModel){
		this.source = start;
		this.destination = end;
		
		//find a FreewaySegment that the source & destination belongs to (arbitrarily chosen boolean)
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
		
		//CASE 1: Source is on a middle segment		
				
		if(sourceFreewayName == "10"){
			//Case A: Source is on end segment
			currFreewaySegment = sourceFreewaySegment;
			MoveAlongExtraSection(currFreewaySegment, fastestPath, fastestPathTime, mapModel);
			NavigateJunction(currFreewaySegment, mapModel);
			
			//TODO Case B: Source is on the right nubbin segment
			(mapModel.isJunction(currFreewaySegment) == 0 && sourceFreewaySegment.getSegmentName() == " "){
				sourceFreewaySegment = mapModel.searchByRampName(source, false);
				destinationFreewaySegment = mapModel.searchByRampName(destination, false);
				currFreewaySegment = sourceFreewaySegment;
				//Go left, continue with path
				//Go left, down 101 
			}
			
			if{
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
			}
			
		}//end 105 case		
		
		//Compare the 2 speeds
		if(path1time > path2time){
			fastestPath = path1;
		}
		else{
			fastestPath = path2;
		}
		return fastestPath;
		
	}
	
	//Move along freeway in middle until it reaches a junction
	public void MoveAlongMidSection(FreewaySegment currentSegment, ArrayList<FreewaySegment> path, double pathTime, GeoMapModel mapModel){
		startRamp = currentSegment.getStartRamp();
		while (startRamp.getRampName() != destination){
			//Goes until right before a junction
			if(mapModel.isJunction((mapModel.getNextFreewaySegment(currFreewaySegment))) == 2){
				NavigateJunction(currentSegment, mapModel);
			}
			pathTime += currFreewaySegment.getAverageSpeed();
			path.add(currFreewaySegment);
			currFreewaySegment = mapModel.getNextFreewaySegment(currFreewaySegment);
			startRamp = currFreewaySegment.getStartRamp();
		}
	}
	
	//Move along an end segment
	public void MoveAlongExtraSection(FreewaySegment currentSegment, ArrayList<FreewaySegment> path, double pathTime, GeoMapModel mapModel){
		//move until junction
		while(startRamp.getRampName() != destination){
			pathTime += currentSegment.getAverageSpeed();
			path.add(currentSegment);
			currentSegment = mapModel.getNextFreewaySegment(currentSegment);
		}
		//transition to middle section
	}
	
	//At a junction
	public void NavigateJunction(FreewaySegment currFreewaySegment, GeoMapModel mapModel){
		//Case a: Keep going on current freeway
		currFreewaySegment = mapModel.getNextFreewaySegment(currFreewaySegment);
		//Leads to an end segment
		if(mapModel.isJunction(currFreewaySegment) == 0){
			//destination is on end segment
			if(currFreewaySegment.getFreewayName() == destinationFreewayName){
				path1time += currFreewaySegment.getAverageSpeed();
				path1.add(currFreewaySegment);
			}
		}
		

		//Case b: Transition to next freeway
		
		
		//Case c: Transition to next freeway, but backwards
	}
	
}
