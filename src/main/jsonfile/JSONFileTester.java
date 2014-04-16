package main.jsonfile;

import javax.swing.JFrame;

public class JSONFileTester {
    public static void main(String[] args)
    {
    	while(true)
    	{
    		JSONFileGetter jfg = new JSONFileGetter("http://www-scf.usc.edu/~csci201/mahdi_project/test.json");
    		
    		jfg.start();
    		try
    		{
    			Thread.sleep(180000);
    		}
    		catch( InterruptedException ie)
    		{
    			System.out.println("IE " + ie.getMessage());
    		}
    	}
    }
}
