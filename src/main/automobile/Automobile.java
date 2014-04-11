package main.automobile;


public class Automobile
{

    public Automobile(int id, double speed, String direction, String ramp, String freeway)
    {
        this.id = id;
        this.speed = speed;
        this.direction = direction;
        this.ramp = ramp;
        this.freeway = freeway;
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

    int id;
    double speed;
    String direction;
    String ramp;
    String freeway;
}