package main.automobile;

import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;


public class Automobile
{
    int id;
    double speed;
    String direction;
    String ramp;
    String freeway;
    MapMarkerCircle carsprite;
    //FreewaySegment freeway;
    public Automobile(int id, double speed, String direction, String ramp, String freeway)
    {	
    	this.freeway = freeway;
        this.id = id;
        this.speed = speed;
        this.direction = direction;
        this.ramp = ramp;
        
    }
    public Automobile()
    {
    }

    public void print()
    {
        System.out.println((new StringBuilder("ID: ")).append(id).append(", SPEED: ").append(speed).append(", DIRECTION: ").append(direction).append(", RAMP: ").append(ramp).append(", FREEWAY: ").append(freeway).toString());
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

    public void setFreeway(String freeway)
    {
        this.freeway = freeway;
    }
    
    public void updateLocation(double time_elapse)
    {
    	
    }

}