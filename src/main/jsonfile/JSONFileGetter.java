package main.jsonfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;

import main.CSCI201Maps;
import main.map.GeoMapModel;
import main.map.GeoMapView;


// THIS Runs every 3 minutes which can be written as Thread.sleep(180000)
public class JSONFileGetter implements Runnable
{
	private URL url;
	private String jsonFile;
	private JSONFileParser jfp;
	private GeoMapView geoMapView;
	
	private boolean debuggingJSONFileGetter = true;
	
    public JSONFileGetter(String urlLink, GeoMapModel parserMapModel, GeoMapView geoMapView)
    {
    	this.jfp = new JSONFileParser(parserMapModel);
    	this.geoMapView = geoMapView;
    	
    	try
        {
        	System.out.println("Parsing file from: " + urlLink);
        	
        	url = new URL(urlLink);
            URLConnection urlConnection = url.openConnection();
            java.io.InputStream is = urlConnection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            
            // Store the contents of the JSON file in String and pass to the parser when JSONFileGetter task is run
            jsonFile = in.readLine();
        }
        catch(MalformedURLException mue)
        {
            System.out.println((new StringBuilder("MUE:")).append(mue.getMessage()).toString());
        }
        catch(IOException ioe)
        {
            System.out.println((new StringBuilder("IOE:")).append(ioe.getMessage()).toString());
        }
    }
    
    public void run()
    {
    	while(true) {
    		CSCI201Maps.grabMapUpdateLock();
    		geoMapView.eraseAutomobiles();
    		jfp.parseAutomobiles(jsonFile);
    		geoMapView.drawAutomobiles();
    		
    		java.util.Date date = new java.util.Date();
    		System.out.println("[JSONFileGetter] Last updated: " + new Timestamp(date.getTime()));
    		
    		try {
    			CSCI201Maps.giveUpMapUpdateLock();
    			Thread.sleep((3 * 60 * 1000)); // Sleep for 3 minutes (conversion to millis shown)
    		} catch (InterruptedException ie) {
    			ie.printStackTrace();
    		}
    		if (debuggingJSONFileGetter) System.out.println("[JSONFileGetter] Thread woken up.");
    	}
    }
}