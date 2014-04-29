package main.freeway;

import java.util.ArrayList;

import main.map.GeoMapModel;
import main.map.GeoMapView;

public class FastestPath_ {
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
	
	public ArrayList<FreewaySegment> FastestPath_(){
		
		//Compare the 2 speeds
		if(path1time > path2time){
			fastestPath = path1;
		}
		else{
			fastestPath = path2;
		}
		return fastestPath;
	}

	//Travels along end segment until it reaches a junction
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
	
	//Travels along middle section until it reaches a junction
	public void MoveAlongMidSection(FreewaySegment currSegment, double pathTime, ArrayList<FreewaySegment> path, GeoMapModel mapModel){
		startRamp = currSegment.getStartRamp();
		//Goes until destination or junction is reached
		while(startRamp.getRampName() != destination){
			if(mapModel.isJunction((mapModel.getNextFreewaySegment(currSegment))) == 2){
				break;
//				NavigateJunction(currSegment, mapModel);
			}
			else{
				pathTime += currSegment.getAverageSpeed();
				path.add(currSegment);
				currSegment = mapModel.getNextFreewaySegment(currSegment);
			}
		}
	}
	
	//Handles junction
	public void NavigateJunction(FreewaySegment currSegment, GeoMapModel mapModel){
		
	}
}