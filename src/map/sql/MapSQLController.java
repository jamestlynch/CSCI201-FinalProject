/**
 * ==========================================================================================================================
 * @fileName 			MapSQLController.java
 * @createdBy 			JamesLynch
 * @teamMembers 		Joseph Lin (josephml), Sarah Loui (sloui), James Lynch (jamestly), Ivana Wang (ivanawan)
 * @date 				Apr 8, 2014 3:07:48 PM
 * @professor			Jeffrey Miller
 * @project   			CSCI201 Final Project
 * @projectDescription	Map Transportation Network for Spring 2014 CSCI201 ("Principles of Software Development") at USC
 * @version				0.10
 * @versionCreated		0.10
 * ==========================================================================================================================
 *
 * ==========================================================================================================================
 */

package map.sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.util.PriorityQueue;
import java.util.Properties;


public class MapSQLController implements Runnable {
	// =========================================================================
	//   MEMBER VARIABLES
	// =========================================================================
	
	/**
	 *  Queue for holding MapSQLController's many statements (MapSQLStatementModels) and is in charge of executing the SQL 
	 *    commands and using the returned data to update the Map's Model.
	 */
	private PriorityQueue<PreparedStatement> statementQueue = new PriorityQueue<PreparedStatement>();
	
	private final String filePath = new File("").getAbsolutePath();
	private final String sqlStatementsFileName = filePath + "/src/map/sql/map-sql-statements.properties";
	
	// =========================================================================
	//   CONSTRUCTORS
	// =========================================================================
	/**
	 * Creates an instance of the MapSQLController, instantiated with the SQL statements from MapSQLStatements.properties
	 *   so that the SQL statements are stored separately from the Java code.
	 * @source http://www.mkyong.com/java/java-properties-file-examples/ 
	 */
	public MapSQLController() 
	{
		// Read in all of the SQL commands from properties file and create a collection MapSQLModel statements
		Properties sqlStatements = new Properties();
		FileInputStream input = null;

		try 
		{
			input = new FileInputStream(sqlStatementsFileName);

			// Load the properties file
			sqlStatements.load(input);

			// 
			String tableName = "xyz";
			String createTable = sqlStatements.getProperty("createTable");
			System.out.println(createTable);
			
			
			/*
			 * Backup what they give us
			 * 
			 * Time stamp
			 * 
			 */
			
			//System.out.println(sqlStatements.getProperty("dbuser"));
			//System.out.println(sqlStatements.getProperty("dbpassword"));

		} catch (IOException ioe) 
		{
			ioe.printStackTrace();
		} finally 
		{
			if (input != null) 
			{
				try 
				{
					input.close();
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 */
	public void run() 
	{
		try 
		{
			Thread.sleep(5000);
		} catch (InterruptedException ie) {
			System.out.println("[ERROR] MapSQLController thread interrupted");
			ie.printStackTrace();
		}
	}
	
	// =========================================================================
	//   MAP MODEL UPDATES
	// =========================================================================

	public static void main(String[] args) {
		new MapSQLController();
	}
}
