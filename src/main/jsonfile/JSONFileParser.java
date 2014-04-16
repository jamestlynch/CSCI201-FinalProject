package main.jsonfile;

import java.util.ArrayList;

import main.automobile.Automobile;
import main.freeway.FreewaySegment;
import main.map.FreewaySegmentNotFoundException;
import main.map.GeoMapModel;

// Referenced classes of package objects:
//            Automobile

public class JSONFileParser
{
	ArrayList<Automobile> updated_cars;
	public JSONFileParser()
	{}
	
	public ArrayList<Automobile> updateCars(String filetoparse)
    {
        updated_cars = new ArrayList<Automobile>();
        for(int i = 0; i < filetoparse.length(); i++)
        {
            String one_car = "";
            if(filetoparse.charAt(i) == '{')
            {
                for(i++; filetoparse.charAt(i) != '}'; i++)
                    one_car = (new StringBuilder(String.valueOf(one_car))).append(filetoparse.charAt(i)).toString();
            }
            if(!one_car.equals(""))
            {
                Automobile a = parse(one_car);
                updated_cars.add(a);              
            }
        }
        return updated_cars;
    }

    private Automobile parse(String car)
    {
    	String rampname = "";
        Automobile one_car = new Automobile();
        for(int i = 0; i < car.length(); i++)
        {
            if(car.charAt(i) == '"')
            {
                String id = "";
                for(i++; car.charAt(i) != '"'; i++)
                    id = (new StringBuilder(String.valueOf(id))).append(car.charAt(i)).toString();

                if(id.equals("id") || id.equals("ID") || id.equals("Id"))
                {
                    String idnum = "";
                    i++;
                    for(i++; car.charAt(i) != ','; i++)
                        idnum = (new StringBuilder(String.valueOf(idnum))).append(car.charAt(i)).toString();

                    one_car.setId(Integer.parseInt(idnum));
                }
            }
            i++;
            if(car.charAt(i) == '"')
            {
                String speed = "";
                for(i++; car.charAt(i) != '"'; i++)
                    speed = (new StringBuilder(String.valueOf(speed))).append(car.charAt(i)).toString();

                if(speed.equals("speed"))
                {
                    String speedval = "";
                    i++;
                    for(i++; car.charAt(i) != ','; i++)
                        speedval = (new StringBuilder(String.valueOf(speedval))).append(car.charAt(i)).toString();

                    one_car.setSpeed(Double.parseDouble(speedval));
                }
            }
            i++;
            if(car.charAt(i) == '"')
            {
                String direction = "";
                for(i++; car.charAt(i) != '"'; i++)
                    direction = (new StringBuilder(String.valueOf(direction))).append(car.charAt(i)).toString();

                if(direction.equals("direction"))
                {
                    String directionval = "";
                    i++;
                    i++;
                    for(i++; car.charAt(i) != '"'; i++)
                        directionval = (new StringBuilder(String.valueOf(directionval))).append(car.charAt(i)).toString();
                    i++;
                    FreewaySegment.Direction CarDirection = FreewaySegment.Direction.NORTH;
                    if (directionval.equals("N"))
                    	CarDirection = FreewaySegment.Direction.NORTH;
                    if (directionval.equals("S"))
                    	CarDirection = FreewaySegment.Direction.SOUTH;
                    if (directionval.equals("E"))
                    	CarDirection = FreewaySegment.Direction.EAST;
                    if (directionval.equals("W"))
                    	CarDirection = FreewaySegment.Direction.WEST;
                    one_car.setDirection(CarDirection);
                }
            }
            i++;
            if(car.charAt(i) == '"')
            {
                String ramp = "";
                for(i++; car.charAt(i) != '"'; i++)
                    ramp = (new StringBuilder(String.valueOf(ramp))).append(car.charAt(i)).toString();

                if(ramp.equals("on\\/off ramp"))
                {
                	rampname = "";
                    i++;
                    i++;
                    for(i++; car.charAt(i) != '"'; i++)
                        rampname = (new StringBuilder(String.valueOf(rampname))).append(car.charAt(i)).toString();
                }
            }
            i++;
            i++;
            if(car.charAt(i) == '"')
            {
                String freeway = "";
                for(i++; car.charAt(i) != '"'; i++)
                    freeway = (new StringBuilder(String.valueOf(freeway))).append(car.charAt(i)).toString();

                if(freeway.equals("freeway"))
                {
                    String freewayval = "";
                    i++;
                    i++;
                    for(i++; car.charAt(i) != '"'; i++)
                        freewayval = (new StringBuilder(String.valueOf(freewayval))).append(car.charAt(i)).toString();
//                    try
//                    {
//                    	FreewaySegment tempfs = GeoMapModel.searchForSegment(rampname, one_car.getDirection(), freewayval);
//                    	one_car.setFreeway(tempfs);
//                    }
//                    catch(FreewaySegmentNotFoundException fsnfe)
//                    {
//                    	System.out.println("Freeway Segment Not Found Exception: " + fsnfe.getMessage());
//                    }
                    //one_car.setFreeway(freewayval);
                }
            }
        }

        return one_car;
    }

    public ArrayList<Automobile> getUpdated_cars() {
		return updated_cars;
	}
}