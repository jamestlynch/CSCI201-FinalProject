//package main.jsonfile;
//
//import java.util.ArrayList;
//
//import main.automobile.Automobile;
//
//public class JSONFileTester {
//    public static void main(String[] args)
//    {
//    	while(true)
//    	{
//    		JSONFileGetter jfg = new JSONFileGetter("http://www-scf.usc.edu/~csci201/mahdi_project/test.json");
//    		
//    		jfg.start();
//    		try
//    		{
//    			Thread.sleep(1800);
//    			ArrayList<Automobile> TempPrintListOfCars = jfg.getUpdatedCar();
//    			for (int i = 0 ; i < TempPrintListOfCars.size(); i++)
//    			{
//    				System.out.println (TempPrintListOfCars.get(i).toString());
//    			}
//    		}
//    		catch( InterruptedException ie)
//    		{
//    			System.out.println("IE " + ie.getMessage());
//    		}
//    	}
//    }
//}
