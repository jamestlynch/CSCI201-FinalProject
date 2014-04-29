package main.jsonfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import main.CSCI201Maps;
import main.freeway.FreewaySegment;
import main.map.GeoMapModel;
import main.map.GeoMapView;
import main.sql.SQLDatabaseHandler;


// THIS Runs every 3 minutes which can be written as Thread.sleep(180000)
public class JSONFileGetter implements Runnable
{
	private URL url;
	private String jsonFile;
	private JSONFileParser jfp;
	private GeoMapView geoMapView;
	private GeoMapModel geoMapModel;
	private SQLDatabaseHandler sqlDatabaseHandler;
	
	public SQLDatabaseHandler getSqlDatabaseHandler() {
		return sqlDatabaseHandler;
	}
	
	private boolean runningDatabase = false;
	private boolean tablesCreated = true;
	private static boolean justGrabbedFromServer = false;

	private boolean debuggingJSONFileGetter = false;
	private boolean debuggingMapUpdateLock = true;
	
	private static Calendar cal;
	
    public JSONFileGetter(String urlLink, GeoMapModel parserMapModel, GeoMapView geoMapView)
    {
    	cal = Calendar.getInstance();
    	System.out.println("Hour: " + cal.get(Calendar.HOUR_OF_DAY));
    	this.jfp = new JSONFileParser(parserMapModel);
    	geoMapModel = parserMapModel;
    	this.geoMapView = geoMapView;
    	if (runningDatabase)
    	{
    		sqlDatabaseHandler = new SQLDatabaseHandler();
    		if (!tablesCreated)
    		{
    			//sqlDatabaseHandler.createFreewaySegmentTables(jfp.getGeoMapModel().returnAllSegment());
    			sqlDatabaseHandler.insertListOfFreewaySegments(jfp.getGeoMapModel().returnAllSegment());
    		}
    	}

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
    
    
    public static boolean getJustGrabbedFromServer()
    {
    	return justGrabbedFromServer;
    }
    
    public static void setJustGrabbedFromServer(boolean justGrabbed)
    {
    	justGrabbedFromServer = justGrabbed;
    }
    
    
    
    public void reInit() 
    {
    	if (debuggingMapUpdateLock) System.out.println("[MAP UPDATE LOCK] JSON File Getter grabbed lock.");
    	CSCI201Maps.grabMapUpdateLock();
		geoMapView.eraseAutomobiles();
		for (FreewaySegment fs: jfp.getGeoMapModel().returnAllSegment())
			fs.clearAutomobilesFromLatestUpdate();
		cal.add(Calendar.MINUTE, 3);
		
		justGrabbedFromServer = true;
    }
    
    public void run()
    {
    	if (debuggingMapUpdateLock) System.out.println("[MAP UPDATE LOCK] JSON File Getter grabbed lock.");
    	CSCI201Maps.grabMapUpdateLock();
    	while(true) {
    		jfp.parseAutomobiles(jsonFile);
    		
    		if (runningDatabase) 
    		{
    			for (FreewaySegment fs: jfp.getGeoMapModel().returnAllSegment())
    				sqlDatabaseHandler.updateAverageSpeedOfSegment(fs, cal.get(Calendar.HOUR_OF_DAY));
    		}
    		
    		geoMapView.setAutomobileMarkers();
    		
    		java.util.Date date = new java.util.Date();
    		System.out.println("[JSONFileGetter] Last pulled from the server: " + new Timestamp(date.getTime()));
    		
    		try {
    			CSCI201Maps.giveUpMapUpdateLock();
    			if (debuggingMapUpdateLock) System.out.println("[MAP UPDATE LOCK] JSON File Getter gave up lock.");
    			Thread.sleep(CSCI201Maps.jsonFileFetchingDelay); // Sleep for 3 minutes (conversion to millis shown)
    		} catch (InterruptedException ie) {
    			ie.printStackTrace();
    		}
    		
    		if (debuggingJSONFileGetter) System.out.println("[JSONFileGetter] Thread woken up.");
    		
    		// After woken up for the first time, it has been 3 minutes so everything that happens to reinit the parser is done here.
    		this.reInit();
    	}
    }
}
