package tempfiles;

import jsonFile.JSONFileGetter;

public class Main {

    public static void main(String args[])
    {
        while (true)
        {
        	
        	JSONFileGetter a = new JSONFileGetter();
        	a.start();
        	try
        	{
        		Thread.sleep(180000);
        	}
        	catch(InterruptedException ie)
        	{
        		System.out.println("IE: " + ie.getMessage());
        	}
        }
    }
}
