package jsonFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


// Referenced classes of package mapGUI:
//            JSONFileParser

public class JSONFileGetter
{

    public JSONFileGetter()
    {
        try
        {
            URL url = new URL("http://www-scf.usc.edu/~csci201/mahdi_project/test.json");
            URLConnection connection = url.openConnection();
            java.io.InputStream is = connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String JSONfile = in.readLine();
            new JSONFileParser(JSONfile);
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

    public static void main(String args[])
    {
        new JSONFileGetter();
        System.out.println("HELLO WORLD");
    }
}