package main.sql;
import java.util.ArrayList;

import main.freeway.FreewaySegment;
import main.freeway.FreewayRamp;
import main.map.GeoMapModel;
public class FastestPathh {

	GeoMapModel gmm;
	ArrayList<FreewaySegment> segments101;
	ArrayList<FreewaySegment> segments105;
	ArrayList<FreewaySegment> segments405;
	ArrayList<FreewaySegment> segments10;
	
	public FastestPathh(GeoMapModel geomapmodel)
	{
		this.gmm = geomapmodel;
		segments101 = gmm.getListOf101Segments();
		segments105 = gmm.getListOf105Segments();
		segments405 = gmm.getListOf405Segments();
		segments10 = gmm.getListOf10Segments();
			
	}
	public FreewayRamp findRampFromName(String rampName, String freewayName)
	{
		FreewayRamp fr=null;
		//use ramp name to find corresponding startRamp
		if (freewayName.equals("105")){
			for (int i=0; i<segments105.size(); i++){
				if (segments105.get(i).getStartRamp().getRampName().equals(rampName)){
					fr = segments105.get(i).getStartRamp();
					return fr;
				}
			}
		}
		else if (freewayName.equals("405")){
			for (int i=0; i<segments405.size(); i++){
				if (segments405.get(i).getStartRamp().getRampName().equals(rampName)){
					fr = segments405.get(i).getStartRamp();
					return fr;
				}
			}
		}
		else if (freewayName.equals("10")){
			for (int i=0; i<segments10.size(); i++){
				if (segments10.get(i).getStartRamp().getRampName().equals(rampName)){
					fr = segments10.get(i).getStartRamp();
					return fr;
				}
			}
		}
		else{// if (freewayName.equals("101"))
			for (int i=0; i<segments101.size(); i++){
				if (segments101.get(i).getStartRamp().getRampName().equals(rampName)){
					fr = segments101.get(i).getStartRamp();
					return fr;
				}
			}
		}
		return fr;
				
	}
	
	public ArrayList<FreewaySegment> calculateFastestPath(String sourceRampName, String destRampName, String sourceFreewayName, String destFreewayName)
	{
		
		boolean defaultDirection;
		FreewayRamp sourceRamp;
		FreewayRamp destRamp;
		int startIndex=0;
		int endIndex=0;
		boolean startIndexFound = false;
		ArrayList<FreewaySegment> listToCheck;
	
		ArrayList<FreewaySegment> fastestPathSegments = new ArrayList<FreewaySegment>();
		ArrayList<FreewaySegment> altFastestPathSegments = new ArrayList<FreewaySegment>();
		
		sourceRamp = this.findRampFromName(sourceRampName, sourceFreewayName);
		destRamp = this.findRampFromName(destRampName, destFreewayName);
		
		if (sourceFreewayName.equals("105") && destFreewayName.equals("105"))
		{
			if (sourceRamp.getRampLocation().getLon() < destRamp.getRampLocation().getLon())//destination is east of source
			{
				listToCheck = gmm.getListOf105Segments();
			}
			else {//destination is west of source (oppositeFreewayDirection)
				listToCheck = gmm.getReverseSegments105();
			}
			//locate start and end ramps in ordered list of segments
			for (int i = 0; i < listToCheck.size(); i++){
				if (listToCheck.get(i).getStartRamp().getRampName().equals(sourceRamp.getRampName())){
					startIndex = i;
				}
				else if (listToCheck.get(i).getEndRamp().getRampName().equals(destRamp.getRampName())){
					endIndex = i;
				}
			}
			for (int j = startIndex; j < endIndex+1; j++){
				fastestPathSegments.add(listToCheck.get(j));
			}

		}
		else if (sourceFreewayName.equals("405") && destFreewayName.equals("405"))
		{
			//direct path
			if (sourceRamp.getRampLocation().getLat() < destRamp.getRampLocation().getLat())//destination is north of source
			{
				listToCheck = gmm.getListOf405Segments();
			}
			else {//destination is south of source (oppositeFreewayDirection)
				listToCheck = gmm.getReverseSegments405();
			}
			//locate start and end ramps in ordered list of segments
			for (int i = 0; i < listToCheck.size(); i++){
				if (listToCheck.get(i).getStartRamp().getRampName().equals(sourceRamp.getRampName())){
					startIndex = i;
				}
				else if (listToCheck.get(i).getEndRamp().getRampName().equals(destRamp.getRampName())){
					endIndex = i;
				}
			}
			for (int j = startIndex; j < endIndex+1; j++){
				fastestPathSegments.add(listToCheck.get(j));
			}
			
		}
		else if (sourceFreewayName.equals("101") && destFreewayName.equals("101"))
		{
			//direct path
			if (sourceRamp.getRampLocation().getLon() < destRamp.getRampLocation().getLon())//destination is east of source
			{
				listToCheck = gmm.getReverseSegments101();
			}
			else {//destination is west of source (default direction)
				listToCheck = gmm.getListOf101Segments();
			}
			//locate start and end ramps in ordered list of segments
			for (int i = 0; i < listToCheck.size(); i++){
				if (listToCheck.get(i).getStartRamp().getRampName().equals(sourceRamp.getRampName())){
					startIndex = i;
				}
				else if (listToCheck.get(i).getEndRamp().getRampName().equals(destRamp.getRampName())){
					endIndex = i;
				}
			}
			for (int j = startIndex; j < endIndex+1; j++){
				fastestPathSegments.add(listToCheck.get(j));
			}
			
		}
		else if (sourceFreewayName.equals("10") && destFreewayName.equals("10"))
		{
			//direct path
			if (sourceRamp.getRampLocation().getLon() < destRamp.getRampLocation().getLon()){//destination is east of source
				listToCheck = gmm.getListOf10Segments();	
			}
			else {//destination is west of source (default direction)
				listToCheck = gmm.getReverseSegments10();
			}
			//locate start and end ramps in ordered list of segments
			for (int i = 0; i < listToCheck.size(); i++){
				if (listToCheck.get(i).getStartRamp().getRampName().equals(sourceRamp.getRampName())){
					startIndex = i;}
				else if (listToCheck.get(i).getEndRamp().getRampName().equals(destRamp.getRampName())){
					endIndex = i;}
			}
			for (int j = startIndex; j < endIndex+1; j++){
				fastestPathSegments.add(listToCheck.get(j));
			}
			
		}
		else if (sourceFreewayName.equals("10") && destFreewayName.equals("405"))
		{
			if (sourceRamp.getRampLocation().getLon() < segments10.get(8).getStartRamp().getRampLocation().getLon()){//if source is left of 10/405 junction
				listToCheck = segments10;
				for (int i = 0; i < listToCheck.size(); i++){
					if (listToCheck.get(i).getStartRamp().getRampName().equals(sourceRamp.getRampName())){
						startIndex = i;
					}
				}
				for (int i = startIndex; i< 9; i++){
					fastestPathSegments.add(listToCheck.get(i));
					//altFastestPathSegments.add(listToCheck.get(i));
				}
				if (destRamp.getRampLocation().getLat() <= segments405.get(9).getStartRamp().getRampLocation().getLat()){//destination is south of 10/405 junction
					listToCheck = gmm.getReverseSegments405();
					for (int i = 8; i >= 0; i--){
						if (listToCheck.get(i).getEndRamp().getRampName().equals(destRampName))
							endIndex = i;
					}
					for (int i = 8; i>= endIndex; i--){
						fastestPathSegments.add(listToCheck.get(i));
						//altFastestPathSegments.add(listToCheck.get(i));
					}
				}
				else{//destination is north of 10/405 junction
					listToCheck = segments405;
					int i = 9;
					while (!listToCheck.get(i-1).getEndRamp().getRampName().equals(destRampName) && i<listToCheck.size())
					{
						fastestPathSegments.add(listToCheck.get(i));
						i++;
					}
				}
				
			}
			else{//source ramp is right of 10/405 junction
				listToCheck = gmm.getReverseSegments10();
				for (int i = 0; i < listToCheck.size(); i++){
					if (listToCheck.get(i).getStartRamp().getRampName().equals(sourceRamp.getRampName())){
						startIndex = i;
					}
				}
				for (int i= startIndex; i>=9; i--){
					fastestPathSegments.add(listToCheck.get(i));
				}
				if (destRamp.getRampLocation().getLat() <= segments405.get(9).getStartRamp().getRampLocation().getLat()){//destination is south of 10/405 junction
					listToCheck = gmm.getReverseSegments405();
					for (int i = 8; i >= 0; i--){
						if (listToCheck.get(i).getEndRamp().getRampName().equals(destRampName))
							endIndex = i;
					}
					for (int i = 8; i>= endIndex; i--){
						fastestPathSegments.add(listToCheck.get(i));
						//altFastestPathSegments.add(listToCheck.get(i));
					}
				}
				else{//destination is north of 10/405 junction
					listToCheck = segments405;
					int i = 9;
					while (!listToCheck.get(i-1).getEndRamp().getRampName().equals(destRampName) && i<listToCheck.size())
					{
						fastestPathSegments.add(listToCheck.get(i));
						i++;
					}
				}
				
				
			}
		}
		else if (sourceFreewayName.equals("10") && destFreewayName.equals("101"))
		{
			if (sourceRamp.getRampLocation().getLon() < segments10.get(33).getStartRamp().getRampLocation().getLon()){//if source is left of 10/101 junction
				listToCheck = segments10;
				for (int i = 0; i < listToCheck.size(); i++){
					if (listToCheck.get(i).getStartRamp().getRampName().equals(sourceRamp.getRampName())){
						startIndex = i;
					}
				}
				for (int i = startIndex; i< 34; i++){//33 is the junction segment index for 10/100
					fastestPathSegments.add(listToCheck.get(i));
					//altFastestPathSegments.add(listToCheck.get(i));
				}
				//WHAT IF IS JUNCTION (i-10 east san bernardino)
				if (destRamp.getRampLocation().getLon() >= segments101.get(7).getEndRamp().getRampLocation().getLat()){//destination is east of 10/101 junction
					listToCheck = gmm.getReverseSegments101();
					for (int i = 7; i >= 0; i--){
						if (listToCheck.get(i).getEndRamp().getRampName().equals(destRampName))
							endIndex = i;
					}
					for (int i = 6; i>= endIndex; i--){
						fastestPathSegments.add(listToCheck.get(i));
						//altFastestPathSegments.add(listToCheck.get(i));
					}
				}
				else{//destination is west of 10/101 junction
					listToCheck = segments101;
					int i = 7;
					while (!listToCheck.get(-1).getEndRamp().getRampName().equals(destRampName) && i<listToCheck.size())
					{
						fastestPathSegments.add(listToCheck.get(i));
						i++;
					}
				}
				
			}
			else{//source ramp is right of 10/101 junction
				listToCheck = gmm.getReverseSegments10();
				for (int i = 0; i < listToCheck.size(); i++){
					if (listToCheck.get(i).getStartRamp().getRampName().equals(sourceRamp.getRampName())){
						startIndex = i;
					}
				}
				for (int i= listToCheck.size()-1; i>=33; i--){
					fastestPathSegments.add(listToCheck.get(i));
				}
				if (destRamp.getRampLocation().getLon() >= segments101.get(7).getEndRamp().getRampLocation().getLat()){//destination is east of 10/101 junction
					listToCheck = gmm.getReverseSegments101();
					for (int i = 7; i >= 0; i--){
						if (listToCheck.get(i).getEndRamp().getRampName().equals(destRampName))
							endIndex = i;
					}
					for (int i = 6; i>= endIndex; i--){
						fastestPathSegments.add(listToCheck.get(i));
						//altFastestPathSegments.add(listToCheck.get(i));
					}
				}
				else{//destination is west of 10/101 junction
					listToCheck = segments101;
					int i = 7;
					while (!listToCheck.get(i-1).getEndRamp().getRampName().equals(destRampName) && i<listToCheck.size())
					{
						fastestPathSegments.add(listToCheck.get(i));
						i++;
					}
				}
				
				
			}
		}
		else if (sourceFreewayName.equals("101") && destFreewayName.equals("10"))
		{
			if (sourceRamp.getRampLocation().getLon() < segments101.get(6).getStartRamp().getRampLocation().getLon()){//if source is left of 10/101 junction
				listToCheck = gmm.getReverseSegments101();
				for (int i = 0; i < listToCheck.size(); i++){
					if (listToCheck.get(i).getStartRamp().getRampName().equals(sourceRamp.getRampName())){
						startIndex = i;
					}
				}
				for (int i = startIndex; i> 6; i--){//7/6 is the junction segment index for 10/100
					fastestPathSegments.add(listToCheck.get(i));
					//altFastestPathSegments.add(listToCheck.get(i));
				}
				//WHAT IF IS JUNCTION (i-10 east san bernardino)
				if (destRamp.getRampLocation().getLon() >= segments10.get(32).getStartRamp().getRampLocation().getLon()){//destination is east of 10/101 junction
					listToCheck = segments10;
					for (int i = 0; i < listToCheck.size(); i++){
						if (listToCheck.get(i).getEndRamp().getRampName().equals(destRampName))
							endIndex = i;
					}
					for (int i = 33; i<= endIndex; i++){
						fastestPathSegments.add(listToCheck.get(i));
						//altFastestPathSegments.add(listToCheck.get(i));
					}
				}
				else{//destination is west of 10/101 junction
					listToCheck = gmm.getReverseSegments10();
					for (int i = 0; i < listToCheck.size(); i++){
						if (listToCheck.get(i).getEndRamp().getRampName().equals(destRampName))
							endIndex = i;
					}
					for (int i=33; i>=endIndex; i--)
					{	fastestPathSegments.add(listToCheck.get(i));
					}
				}
				
			}
			else{//source ramp is right of 10/101 junction
				listToCheck = gmm.getReverseSegments101();
				for (int i = 0; i < listToCheck.size(); i++){
					if (listToCheck.get(i).getStartRamp().getRampName().equals(sourceRamp.getRampName())){
						startIndex = i;
					}
				}
				for (int i=startIndex; i<=6; i++){
					fastestPathSegments.add(listToCheck.get(i));
				}
				//WHAT IF IS JUNCTION (i-10 east san bernardino)
				if (destRamp.getRampLocation().getLon() >= segments10.get(32).getStartRamp().getRampLocation().getLon()){//destination is east of 10/101 junction
					listToCheck = segments10;
					for (int i = 0; i < listToCheck.size(); i++){
						if (listToCheck.get(i).getEndRamp().getRampName().equals(destRampName))
							endIndex = i;
					}
					for (int i = 33; i<= endIndex; i++){
						fastestPathSegments.add(listToCheck.get(i));
						//altFastestPathSegments.add(listToCheck.get(i));
					}
				}
				else{//destination is west of 10/101 junction
					listToCheck = gmm.getReverseSegments10();
					for (int i = 0; i < listToCheck.size(); i++){
						if (listToCheck.get(i).getEndRamp().getRampName().equals(destRampName))
							endIndex = i;
					}
					for (int i=33; i>=endIndex; i--)
					{	fastestPathSegments.add(listToCheck.get(i));
					}
				}
				
			}
		}
		else if (sourceFreewayName.equals("101") && destFreewayName.equals("405"))
		{
			if (sourceRamp.getRampLocation().getLon() < segments101.get(41).getEndRamp().getRampLocation().getLon()){//if source is left of 101/405 junction
				listToCheck = gmm.getReverseSegments101();
				for (int i = 0; i < listToCheck.size(); i++){
					if (listToCheck.get(i).getStartRamp().getRampName().equals(sourceRamp.getRampName())){
						startIndex = i;
					}
				}
				for (int i = startIndex; i>=42; i--){//42 is the junction segment on the 101
					fastestPathSegments.add(listToCheck.get(i));
					altFastestPathSegments.add(listToCheck.get(i));//COMPLETE ALT LATER
				}
				//should check if junction! add later
				if (destRamp.getRampLocation().getLat() <= segments405.get(19).getStartRamp().getRampLocation().getLat()){//destination is south of 101/405 junction
					listToCheck = gmm.getReverseSegments405();
					for (int i = 19; i >= 0; i--){
						if (listToCheck.get(i).getEndRamp().getRampName().equals(destRampName)){
							endIndex = i;
						}
					}
					for (int i = 18; i>= endIndex; i--){
						fastestPathSegments.add(listToCheck.get(i));
						//altFastestPathSegments.add(listToCheck.get(i));
					}
				}
				else{//destination is north of 101/405 junction
					listToCheck = segments405;
					int i = 19;
					while (!listToCheck.get(i-1).getEndRamp().getRampName().equals(destRampName) && i<listToCheck.size())
					{
						fastestPathSegments.add(listToCheck.get(i));
						i++;
					}
				}
				
			}
			else{//source ramp is right of 101/405 junction
				listToCheck = segments101;
				for (int i = 0; i < listToCheck.size(); i++){
					if (listToCheck.get(i).getStartRamp().getRampName().equals(sourceRamp.getRampName())){
						startIndex = i;
					}
				}
				for (int i= startIndex; i<=42; i++){
					fastestPathSegments.add(listToCheck.get(i));
				}
				//should check if junction! add later
				if (destRamp.getRampLocation().getLat() <= segments405.get(19).getStartRamp().getRampLocation().getLat()){//destination is south of 101/405 junction
					listToCheck = gmm.getReverseSegments405();
					for (int i = 19; i >= 0; i--){
						if (listToCheck.get(i).getEndRamp().getRampName().equals(destRampName))
							endIndex = i;
					}
					for (int i = 18; i>= endIndex; i--){
						fastestPathSegments.add(listToCheck.get(i));
						//altFastestPathSegments.add(listToCheck.get(i));
					}
				}
				else{//destination is north of 101/405 junction
					listToCheck = segments405;
					int i = 19;
					while (!listToCheck.get(i-1).getEndRamp().getRampName().equals(destRampName) && i<listToCheck.size())
					{
						fastestPathSegments.add(listToCheck.get(i));
						i++;
					}
				}
				
			}
		}
			else if (sourceFreewayName.equals("405") && destFreewayName.equals("101"))
			{//add if junction
				if (sourceRamp.getRampLocation().getLat() >= segments405.get(19).getStartRamp().getRampLocation().getLat()){//if source is north of 101/405 junction
					listToCheck = gmm.getReverseSegments405();
					for (int i = 0; i < listToCheck.size(); i++){
						if (listToCheck.get(i).getStartRamp().getRampName().equals(sourceRamp.getRampName())){
							startIndex = i;
						}
					}
					for (int i = startIndex; i>=19; i--){
						fastestPathSegments.add(listToCheck.get(i));
						altFastestPathSegments.add(listToCheck.get(i));//COMPLETE ALT LATER
					}
					//should check if junction! add later
					if (destRamp.getRampLocation().getLon() <= segments101.get(41).getEndRamp().getRampLocation().getLon()){//destination is west of 101/405 junction
						listToCheck = segments101;
						for (int i = 0; i < listToCheck.size(); i++){
							if (listToCheck.get(i).getEndRamp().getRampName().equals(destRampName)){
								endIndex = i;
							}
						}
						for (int i = 42; i<= endIndex; i++){
							fastestPathSegments.add(listToCheck.get(i));
							//altFastestPathSegments.add(listToCheck.get(i));
						}
					}
					else{//destination is east of 101/405 junction
						listToCheck = gmm.getReverseSegments101();
						for (int i = 0; i < listToCheck.size(); i++){
							if (listToCheck.get(i).getEndRamp().getRampName().equals(destRampName)){
								endIndex = i;
							}
						}
						for (int i = 42; i>= endIndex; i--){
							fastestPathSegments.add(listToCheck.get(i));
							//altFastestPathSegments.add(listToCheck.get(i));
						}
					}
					
				}
				else{//source ramp is south of 101/405 junction
					listToCheck = segments405;
					for (int i = 0; i < listToCheck.size(); i++){
						if (listToCheck.get(i).getStartRamp().getRampName().equals(sourceRamp.getRampName())){
							startIndex = i;
						}
					}
					for (int i= startIndex; i<=18; i++){
						fastestPathSegments.add(listToCheck.get(i));
					}
					//should check if junction! add later
					//should check if junction! add later
					if (destRamp.getRampLocation().getLon() <= segments101.get(41).getEndRamp().getRampLocation().getLon()){//destination is west of 101/405 junction
						listToCheck = segments101;
						for (int i = 0; i < listToCheck.size(); i++){
							if (listToCheck.get(i).getEndRamp().getRampName().equals(destRampName)){
								endIndex = i;
							}
						}
						for (int i = 42; i<= endIndex; i++){
							fastestPathSegments.add(listToCheck.get(i));
							//altFastestPathSegments.add(listToCheck.get(i));
						}
					}
					else{//destination is east of 101/405 junction
						listToCheck = gmm.getReverseSegments101();
						for (int i = 0; i < listToCheck.size(); i++){
							if (listToCheck.get(i).getEndRamp().getRampName().equals(destRampName)){
								endIndex = i;
							}
						}
						for (int i = 42; i>= endIndex; i--){
							fastestPathSegments.add(listToCheck.get(i));
							//altFastestPathSegments.add(listToCheck.get(i));
						}
					}
					
				}
		}
			else if (sourceFreewayName.equals("405") && destFreewayName.equals("10"))
			{//add if junction
				if (sourceRamp.getRampLocation().getLat() <= segments405.get(8).getStartRamp().getRampLocation().getLat()){//if source is south of 10/405 junction
					listToCheck = segments405;
					for (int i = 0; i < listToCheck.size(); i++){
						if (listToCheck.get(i).getStartRamp().getRampName().equals(sourceRamp.getRampName())){
							startIndex = i;
						}
					}
					for (int i = startIndex; i<=8; i++){
						fastestPathSegments.add(listToCheck.get(i));
						altFastestPathSegments.add(listToCheck.get(i));//COMPLETE ALT LATER
					}
					//should check if junction! add later
					if (destRamp.getRampLocation().getLon() <= segments101.get(8).getStartRamp().getRampLocation().getLon()){//destination is west of 10/405 junction
						listToCheck = gmm.getReverseSegments10();
						for (int i = 0; i < listToCheck.size(); i++){
							if (listToCheck.get(i).getEndRamp().getRampName().equals(destRampName)){
								endIndex = i;
							}
						}
						for (int i = 8; i>= endIndex; i--){
							fastestPathSegments.add(listToCheck.get(i));
							//altFastestPathSegments.add(listToCheck.get(i));
						}
					}
					else{//destination is east of 10/405 junction
						listToCheck = segments10;
						for (int i = 0; i < listToCheck.size(); i++){
							if (listToCheck.get(i).getEndRamp().getRampName().equals(destRampName)){
								endIndex = i;
							}
						}
						for (int i = 8; i<= endIndex; i++){
							fastestPathSegments.add(listToCheck.get(i));
							//altFastestPathSegments.add(listToCheck.get(i));
						}
					}
					
				}
				else{//source ramp is north of 101/405 junction
					listToCheck = gmm.getReverseSegments405();
					for (int i = 0; i < listToCheck.size(); i++){
						if (listToCheck.get(i).getStartRamp().getRampName().equals(sourceRamp.getRampName())){
							startIndex = i;
						}
					}
					for (int i= startIndex; i>=9; i++){
						fastestPathSegments.add(listToCheck.get(i));
					}
					//should check if junction! add later
					if (destRamp.getRampLocation().getLon() <= segments101.get(8).getStartRamp().getRampLocation().getLon()){//destination is west of 10/405 junction
						listToCheck = gmm.getReverseSegments10();
						for (int i = 0; i < listToCheck.size(); i++){
							if (listToCheck.get(i).getEndRamp().getRampName().equals(destRampName)){
								endIndex = i;
							}
						}
						for (int i = 8; i>= endIndex; i--){
							fastestPathSegments.add(listToCheck.get(i));
							//altFastestPathSegments.add(listToCheck.get(i));
						}
					}
					else{//destination is east of 10/405 junction
						listToCheck = segments10;
						for (int i = 0; i < listToCheck.size(); i++){
							if (listToCheck.get(i).getEndRamp().getRampName().equals(destRampName)){
								endIndex = i;
							}
						}
						for (int i = 8; i<= endIndex; i++){
							fastestPathSegments.add(listToCheck.get(i));
							//altFastestPathSegments.add(listToCheck.get(i));
						}
					}
					
				}
		}
		System.out.println("Start INDEX: " + startIndex);
		System.out.println("End INDEX" + endIndex);
		return fastestPathSegments;
	}
	
	public double getCurrentSpeedTimeToTravel(ArrayList<FreewaySegment> fps)
	{
		double time=0;
		for (int i=0; i<fps.size(); i++)
		{
			//time = distance/rate
			time += fps.get(i).getDistance()/fps.get(i).getAverageSpeed();
		}
		time*=60;//convert from hours to minutes
		return time;
	}
	public double getSpeedLimitTimeToTravel(ArrayList<FreewaySegment> fps)
	{
		double time=0;
		for (int i=0; i<fps.size(); i++)
		{
			//time = distance/rate
			time += fps.get(i).getDistance()/fps.get(i).getSpeedLimit();
		}
		time*=60;//convert from hours to minutes
		return time;
	}
	
}
