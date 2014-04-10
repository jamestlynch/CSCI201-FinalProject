package jsonFile;

import java.util.ArrayList;

import src.Automobile;

// Referenced classes of package mapGUI:
//            Automobile

public class JSONFileParser
{

    public JSONFileParser(String filetoparse)
    {
        updated_cars = new ArrayList();
        for(int i = 0; i < filetoparse.length(); i++)
        {
            String one_car = "";
            if(filetoparse.charAt(i) == '{')
                for(i++; filetoparse.charAt(i) != '}'; i++)
                    one_car = (new StringBuilder(String.valueOf(one_car))).append(filetoparse.charAt(i)).toString();

            if(!one_car.equals(""))
            {
                Automobile a = parse(one_car);
                updated_cars.add(a);
                a.print();
            }
        }

    }

    private Automobile parse(String car)
    {
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
                    one_car.setDirection(directionval);
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
                    String rampval = "";
                    System.out.println(car.substring(i));
                    i++;
                    i++;
                    for(i++; car.charAt(i) != '"'; i++)
                        rampval = (new StringBuilder(String.valueOf(rampval))).append(car.charAt(i)).toString();

                    one_car.setRamp(rampval);
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

                    one_car.setFreeway(freewayval);
                }
            }
        }

        return one_car;
    }

    ArrayList updated_cars;
}