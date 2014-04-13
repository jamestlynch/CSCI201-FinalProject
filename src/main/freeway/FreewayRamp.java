package main.freeway;

import org.openstreetmap.gui.jmapviewer.Coordinate;

public class FreewayRamp {
	private String rampName;
	private Coordinate rampLocation;
	
	public FreewayRamp(String name, Coordinate rampLocation) {
		this.rampName = name;
		this.rampLocation = rampLocation;
	}

	public String getRampName()
	{
		return rampName;
	}
}
