package main.automobile;

import main.freeway.FreewaySegment;

import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;


public class Automobile 
{

    int id;
    double speed;
    String direction;
    String ramp;
    //String freeway;
    MapMarkerCircle carsprite;
    FreewaySegment freeway;
    JMapViewer map;
    public Automobile(int id, double speed, String direction, String ramp, FreewaySegment freeway, JMapViewer map)
    {	
    	this.freeway = freeway;
        this.id = id;
        this.speed = speed;
        this.direction = direction;
        this.ramp = ramp;
        this.map = map;
    }
    public Automobile()
    {
    }


    public void setId(int id)
    {
        this.id = id;
    }

    public void setSpeed(double speed)
    {
        this.speed = speed;
    }

    public void setDirection(String direction)
    {
        this.direction = direction;
    }

    public void setRamp(String ramp)
    {
        this.ramp = ramp;
    }

    public void setFreeway(FreewaySegment freeway)
    {
        this.freeway = freeway;
    }
    //time_elapse is in milliseconds
    public void updateLocation(double time_elapse_milliseconds)
    {
    	double hour_travelled = time_elapse_milliseconds/3600000;//3.6 million milliseoncds per hour
    	double miles = hour_travelled*speed; //mph * h = m
    	//map.get
    	
    }

}