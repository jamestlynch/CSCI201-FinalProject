package main.jsonfile;

import main.automobile.Automobile;
import main.freeway.FreewaySegment;
import main.map.FreewaySegmentNotFoundException;
import main.map.GeoMapModel;

public class JSONFileParser implements Runnable
{
	private GeoMapModel geoMapModel;
	private int numCarsDeleted = 0;
	
	private boolean debuggingParser = false;
	private boolean checkingNumCarsDeleted = true;
	
	
	public JSONFileParser(GeoMapModel geoMapModel)
	{
		this.geoMapModel = geoMapModel;
	}
	public GeoMapModel getGeoMapModel()
	{
		return geoMapModel;
	}

	public void parseAutomobiles(String fileToParse)
	{
		// Erase all pre-existing cars; re-parse, re-create, and re-draw
		geoMapModel.eraseAutomobilesInFreewayNetwork();
		
		for(int i = 0; i < fileToParse.length(); i++)
		{
			String one_car = "";
			if(fileToParse.charAt(i) == '{')
			{
				for(i++; fileToParse.charAt(i) != '}'; i++)
					one_car = (new StringBuilder(String.valueOf(one_car))).append(fileToParse.charAt(i)).toString();
			}
			if(!one_car.equals(""))
			{
				if (debuggingParser) System.out.println(one_car);
				Automobile a = parse(one_car);
				if (a != null)
					geoMapModel.addAutomobileToNetwork(a);
				else
					numCarsDeleted++;
			}
		}
		if (debuggingParser || checkingNumCarsDeleted) 
			System.out.println(">> Number of autombiles deleted: " + numCarsDeleted);
		numCarsDeleted = 0;
		
		geoMapModel.runAllAutomobileThreads();
		
		Thread.yield();
	}

	private Automobile parse(String oneCarData)
	{
		if (debuggingParser) System.out.println("=========================================================================================================================");
		if (debuggingParser) System.out.println("  Preparing to parse car: " + oneCarData);
		if (debuggingParser) System.out.println("=========================================================================================================================\n");
		
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
			FreewaySegmentVal = geoMapModel.searchForSegment(RampVal, CarDirection, FreewayVal);
		}
		catch(FreewaySegmentNotFoundException fsnfe)
		{
			System.out.println ("INVALID DATA: " + fsnfe.getMessage());
		}
		if (FreewaySegmentVal == null)
		{
			return null;
		}
		else
		{
			Automobile OneCar = new Automobile(IDNum, SpeedNum, CarDirection, RampVal, FreewaySegmentVal, geoMapModel);
			FreewaySegmentVal.addAutomobileToSegment(OneCar);
			FreewaySegmentVal.addAutomobileToLatestUpdate(OneCar);
			return OneCar;
		}
	}

	public void run() {
		
	}
}