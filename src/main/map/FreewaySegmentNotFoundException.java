package main.map;

public class FreewaySegmentNotFoundException extends Exception {	
	public FreewaySegmentNotFoundException(String rampName, String freewayName, String directionName) {
		super("[LOOKUP ERROR] (GeoMapModel) Could not find segment starting at " + rampName + " on the " + freewayName + " freeway heading in the " + directionName + " direction.");
	}
}