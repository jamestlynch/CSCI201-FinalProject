package main.jsonfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


// Referenced classes of package jsonFile:
//            JSONFileParser
//THIS Runs every 3 minutes which can be written as Thread.sleep(180000)
public class JSONFileGetter extends Thread
{
	URL url;
	String JSONfile;
    public JSONFileGetter(String urllink)
    {
        try
        {
        	url = new URL(urllink);
            URLConnection connection = url.openConnection();
            java.io.InputStream is = connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            JSONfile = in.readLine();
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
    	new JSONFileParser(JSONfile);
    }
}