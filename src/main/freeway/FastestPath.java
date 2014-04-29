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
	public String sourceFreewayName;
	public String destinationFreewayName;
	public FreewaySegment sourceFreewaySegment;
	public FreewaySegment destinationFreewaySegment;
	public FreewaySegment currFreewaySegment;
	public FreewayRamp startRamp;
	public FreewayRamp endRamp;
	private static ArrayList<FreewaySegment> path1 = new ArrayList<FreewaySegment>();
	private static ArrayList<FreewaySegment> path2 = new ArrayList<FreewaySegment>();
	private static ArrayList<FreewaySegment> path3 = new ArrayList<FreewaySegment>();
	private static ArrayList<FreewaySegment> fastestPath = new ArrayList<FreewaySegment>();
	public double path1time = 0;
	public double path2time = 0;
	public double path3time = 0;
	public double fastestPathTime = 0;
	
	public ArrayList<FreewaySegment> findFastestPath(String sourceRamp, String destinationRamp, String startFreewayName, String endFreewayName, GeoMapModel mapModel){
		//Initialize variables
		this.source = sourceRamp;
		this.destination = destinationRamp;
		this.sourceFreewayName = startFreewayName;
		this.destinationFreewayName = endFreewayName;
		//find a FreewaySegment that the source & destination belongs to (arbitrarily chosen boolean)
		sourceFreewaySegment = mapModel.searchByRampNameAndFreewayName(source, sourceFreewayName, true);	
		destinationFreewaySegment = mapModel.searchByRampNameAndFreewayName(destination, destinationFreewayName, true);
		
		//Hard coding for case where source & destination are on same freeway
		if(sourceFreewayName == destinationFreewayName){
			currFreewaySegment = sourceFreewaySegment;
			endRamp = currFreewaySegment.getEndRamp();
			while(endRamp.getRampName() != destination){
				path1time += currFreewaySegment.getAverageSpeed();
				path1.add(currFreewaySegment);
				currFreewaySegment = mapModel.getNextFreewaySegment(currFreewaySegment);
				endRamp = currFreewaySegment.getEndRamp();
			}
	
		}
		else{	
			//Path 1
			currFreewaySegment = sourceFreewaySegment;
			MoveAlongSection(currFreewaySegment, path1time, path1, mapModel);
			
			//Path 2
			sourceFreewaySegment = mapModel.searchByRampNameAndFreewayName(source, sourceFreewayName, false);
			currFreewaySegment = sourceFreewaySegment;
			MoveAlongSection(currFreewaySegment, path2time, path2, mapModel);
		}
			
		/*TODO Possible logic problem: destination segment may not be the right one, since it's boolean doesn't correspond to source boolean. This would cause
				the startRampName to be incorrect and path could be 1 segment off
		  TODO Possible logic problem: not sure if passing in pathtime & path to MoveAlongSection method actually updates public variables (e.g. path1time, path1)
		*/
		
		//Compare the 2 speeds
		if(path1time > path2time){
			fastestPath = path1;
		}
		else{
			fastestPath = path2;
		}
		return fastestPath;
	}

/*	//Travels along end segment until it reaches a junction
	public void MoveAlongEndSection(FreewaySegment currSegment, double pathTime, ArrayList<FreewaySegment> path, GeoMapModel mapModel){
		startRamp = currSegment.getStartRamp();
		//Stops when destination is reached
		if(startRamp.getRampName() == destination){
			break; //TODO put in while loop?
		}
		//Moves to junction
		else if(mapModel.isJunction((mapModel.getNextFreewaySegment(currSegment))) == 2){
			NavigateJunction(currSegment, mapModel);
		}
		else{
			pathTime += currSegment.getAverageSpeed();
			path.add(currSegment);
			currSegment = mapModel.getNextFreewaySegment(currSegment);
			MoveAlongEndSection(currSegment, pathTime, path, mapModel);
		}
	}
*/
	
	//Travels along middle section until it reaches a junction
	public void MoveAlongSection(FreewaySegment currSegment, double pathTime, ArrayList<FreewaySegment> path, GeoMapModel mapModel){
		startRamp = currSegment.getStartRamp();
		//Goes until destination or junction is reached
		while(startRamp.getRampName() != destination){
			endRamp = currSegment.getEndRamp();
			if(endRamp.getRampName() == "I-405 (San Diego Freeway) Sacramento, LAX Airport, Long Beach" || 
				endRamp.getRampName() == "I-10 (Santa Monica Freeway)   Santa Monica, Los Angeles" ||
				endRamp.getRampName() == "US 101 north (Santa Ana Freeway via San Bernardino Freeway) Los Angeles, Hollywood" ||
				endRamp.getRampName() == "I-10 east (San Bernardino Freeway) San Bernardino" ||
				endRamp.getRampName() == "I-405 (San Diego Freeway) Santa Monica, Sacramento" ||
				endRamp.getRampName() == "US 101 (Ventura Freeway)   Ventura, Los Angeles")
			{
//			if(mapModel.isJunction((mapModel.getNextFreewaySegment(currSegment))) == 2){
//				break;
				NavigateJunction(currSegment, pathTime, path, mapModel);
			}
			else{
				pathTime += currSegment.getAverageSpeed();
				path.add(currSegment);
				currSegment = mapModel.getNextFreewaySegment(currSegment);
				startRamp = currSegment.getStartRamp();
				//TODO Does the while loop automatically stop when it reaches the end?
			}
		}
	}
	
	//Handles junction
	public void NavigateJunction(FreewaySegment currSegment, double pathTime, ArrayList<FreewaySegment> path, GeoMapModel mapModel){
		startRamp = currSegment.getStartRamp();
		//Case A: Stay on the same freeway
			currSegment = mapModel.getNextFreewaySegment(currSegment);
			//If it's an end segment, check for destination. If it's not there, ignore this case
			if(mapModel.isJunction(currSegment) == 0){
				while(startRamp.getRampName() != destination){
					//If the destination is not found
					if(mapModel.isJunction(currSegment) == 0){
						//check if destination is there
					}
				}
			}
			//Otherwise, keep going along the section
			else{
				MoveAlongSection(currSegment, pathTime, path, mapModel);
			}
		
		//Case B: Either go onto middle section or end segment
			currSegment = mapModel.getNextFreewaySegment(currSegment).get(1);
			//If it's an end segment, check for destination. If it's not there, ignore this case
			if(mapModel.isJunction(currSegment) == 0){
				for(list of next segments ){
					if(startRamp.getRampName() == destination){
						
					}
				}
			}
			//Otherwise, keep going along the section
			else{
				MoveAlongSection(currSegment, pathTime, path, mapModel);
			}
		
		//Case C: The other option
			currSegment = mapModel.getNextFreewaySegment(currSegment).get(2);
			//If it's an end segment, check for destination. If it's not there, ignore this case
			if(mapModel.isJunction(currSegment) == 0){
				for(list of next segments ){
					if(startRamp.getRampName() == destination){
						
					}
				}
			}
			//Otherwise, keep going along the section
			else{
				MoveAlongSection(currSegment, pathTime, path, mapModel);
			}
	}
}