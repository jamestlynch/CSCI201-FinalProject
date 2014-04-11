package jsonFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


// Referenced classes of package jsonFile:
//            JSONFileParser

public class JSONFileGetter extends Thread
{
	URL url;
	String JSONfile;
    public JSONFileGetter()
    {
        try
        {
        	url = new URL("http://www-scf.usc.edu/~csci201/mahdi_project/test.json");
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