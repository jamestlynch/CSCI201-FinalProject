package main.jsonfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import main.automobile.Automobile;
import main.map.GeoMapModel;


// Referenced classes of package jsonFile:
//            JSONFileParser
//THIS Runs every 3 minutes which can be written as Thread.sleep(180000)
public class JSONFileGetter extends Thread
{
	URL url;
	String JSONfile;
	JSONFileParser jfp;
    public JSONFileGetter(String urllink, GeoMapModel parserMapModel)
    {
    	jfp = new JSONFileParser(parserMapModel);
    	try
        {
        	System.out.println("TEST " + urllink);
        	url = new URL(urllink);
            URLConnection connection = url.openConnection();
            java.io.InputStream is = connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            JSONfile = in.readLine();
            jfp.updateCars(JSONfile);
        }
        catch(MalformedURLException mue)
        {
            System.out.println((new StringBuilder("MUE:")).append(mue.getMessage()).toString());
        }
        catch(IOException ioe)
        {
            System.out.println((new StringBuilder("IOE:")).append(ioe.getMessage()).toString());
        }
    	//String asdf = jfp.getUpdated_cars().toString();
    	//System.out.println();
    }
    public ArrayList<Automobile> getUpdatedCar()
    {
    	
    	return jfp.getUpdatedCars();
    }
    
    public void run()
    {
    	jfp.updateCars(JSONfile);
    }
}