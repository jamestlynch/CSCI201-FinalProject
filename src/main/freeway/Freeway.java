package main.freeway;

import java.util.HashMap;

import main.freeway.ramp.FreewayRamp;
import main.freeway.section.FreewaySegment;

public class Freeway {
	private String freewayName;
	// HashMap that allows you to look-up a freeway section via its start ramp
	private HashMap<FreewayRamp, FreewaySegment> freeway;
	
	public Freeway(String name, HashMap<FreewayRamp, FreewaySegment> freeway) {
		this.freewayName = name;
		this.freeway = freeway;
	}
}
