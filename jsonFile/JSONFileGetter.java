package jsonFile;


// Referenced classes of package mapGUI:
//            JSONFileParser

public class JSONFileGetter extends Thread 
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