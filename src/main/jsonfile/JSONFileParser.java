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
			System.out.println(updated_cars.size());
		}
		System.out.println(updated_cars.size());
		return updated_cars;
	}

	private Automobile parse(String oneCarData)
	{
		System.out.println("=========================================================================================================================");
		System.out.println("  Preparing to parse car: " + oneCarData);
		System.out.println("=========================================================================================================================\n");
		
		String IDVal = "";
		int IDNum = 0;
		String SpeedVal = "";
		double SpeedNum = 0;
		String DirectionVal = "";
		FreewaySegment.Direction CarDirection = null;
		String RampVal = "";
		String FreewayVal = "";    

		int i = 0;
		if(oneCarData.charAt(i) == '"')
		{
			String id = "";
			for(i++; oneCarData.charAt(i) != '"'; i++)
				id = (new StringBuilder(String.valueOf(id))).append(oneCarData.charAt(i)).toString();

			if(id.equals("id") || id.equals("ID") || id.equals("Id"))
			{

				i++;
				for(i++; oneCarData.charAt(i) != ','; i++)
					IDVal = (new StringBuilder(String.valueOf(IDVal))).append(oneCarData.charAt(i)).toString();

				IDNum = (Integer.parseInt(IDVal));
			}
		}
		i++;
		if(oneCarData.charAt(i) == '"')
		{
			String speed = "";
			for(i++; oneCarData.charAt(i) != '"'; i++)
				speed = (new StringBuilder(String.valueOf(speed))).append(oneCarData.charAt(i)).toString();

			if(speed.equals("speed"))
			{
				i++;
				for(i++; oneCarData.charAt(i) != ','; i++)
					SpeedVal = (new StringBuilder(String.valueOf(SpeedVal))).append(oneCarData.charAt(i)).toString();

				SpeedNum = (Double.parseDouble(SpeedVal));
			}
		}
		i++;
		if(oneCarData.charAt(i) == '"')
		{
			String direction = "";
			for(i++; oneCarData.charAt(i) != '"'; i++)
				direction = (new StringBuilder(String.valueOf(direction))).append(oneCarData.charAt(i)).toString();

			if(direction.equals("direction"))
			{
				i++;
				i++;
				for(i++; oneCarData.charAt(i) != '"'; i++)
					DirectionVal = (new StringBuilder(String.valueOf(DirectionVal))).append(oneCarData.charAt(i)).toString();
				i++;

				if (DirectionVal.equals("North"))
					CarDirection = FreewaySegment.Direction.NORTH;
				if (DirectionVal.equals("South"))
					CarDirection = FreewaySegment.Direction.SOUTH;
				if (DirectionVal.equals("East"))
					CarDirection = FreewaySegment.Direction.EAST;
				if (DirectionVal.equals("West"))
					CarDirection = FreewaySegment.Direction.WEST;
			}
		}
		i++;
		if(oneCarData.charAt(i) == '"')
		{
			String ramp = "";
			for(i++; oneCarData.charAt(i) != '"'; i++)
				ramp = (new StringBuilder(String.valueOf(ramp))).append(oneCarData.charAt(i)).toString();

			if(ramp.equals("on\\/off ramp"))
			{
				RampVal = "";
				i++;
				i++;
				for(i++; oneCarData.charAt(i) != '"'; i++)
					RampVal = (new StringBuilder(String.valueOf(RampVal))).append(oneCarData.charAt(i)).toString();
				RampVal = RampVal.toLowerCase().replaceAll("\\s+","");
			}
		}
		i++;
		i++;
		if(oneCarData.charAt(i) == '"')
		{
			String freeway = "";
			for(i++; oneCarData.charAt(i) != '"'; i++)
				freeway = (new StringBuilder(String.valueOf(freeway))).append(oneCarData.charAt(i)).toString();

			if(freeway.equals("freeway"))
			{
				i++;
				i++;
				for(i++; oneCarData.charAt(i) != '"'; i++)
					FreewayVal = (new StringBuilder(String.valueOf(FreewayVal))).append(oneCarData.charAt(i)).toString();
			}
		}
		FreewaySegment FreewaySegmentVal = null;
		try
		{
//			System.out.println (RampVal);
//			System.out.println (CarDirection);
//			System.out.println (FreewayVal);
			GeoMapModel a = new GeoMapModel();
			FreewaySegmentVal = GeoMapModel.searchForSegment(RampVal, CarDirection, FreewayVal);
		}
		catch(FreewaySegmentNotFoundException fsnfe)
		{
			System.out.println ("INVALID DATA: " + fsnfe.getMessage());
		}
		Automobile OneCar = new Automobile(IDNum, SpeedNum, CarDirection, RampVal, FreewaySegmentVal);
		return OneCar;
	}

	public ArrayList<Automobile> getUpdatedCars() {
		System.out.println("HELLO WORLD" + updated_cars.size());
		for (int i = 0 ; i < updated_cars.size(); i++)
			System.out.println(updated_cars.get(i).getCarsprite().toString());
		return updated_cars;
	}
}