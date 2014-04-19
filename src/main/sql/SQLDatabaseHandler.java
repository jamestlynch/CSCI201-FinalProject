package main.sql;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.freeway.FreewaySegment;
import main.automobile.Automobile;

public class SQLDatabaseHandler {

   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver"; 
   static final String DB_URL = "jdbc:mysql://localhost:3306/test";
   
   static final String USER = "root";
   static final String PASS = "";
   Connection conn = null;
   Statement stmt = null;
   
   public SQLDatabaseHandler()
   {
	   try{
		   //Register JDBC driver
		   Class.forName("com.mysql.jdbc.Driver");

		   System.out.println("Connecting to database...");
		   conn = DriverManager.getConnection(DB_URL,USER,PASS);

		   //Create AllFreewaySegmentsTable
		   stmt = conn.createStatement();
		   String createtablequery="CREATE TABLE IF NOT EXISTS AllFreewaySegments(Id INT PRIMARY KEY AUTO_INCREMENT, FreewaySegmentTableName VARCHAR(100), StartRamp VARCHAR(60), EndRamp VARCHAR(60), FreewayName VARCHAR(10), Distance DOUBLE) Engine=InnoDB";
		   stmt.executeUpdate(createtablequery);

	   }catch(SQLException se){
		   //Handle errors for JDBC
		   se.printStackTrace();
	   }catch(Exception e){
		   //Handle errors for Class.forName
		   e.printStackTrace();
	   }
		    
   }
   
   public void InsertFreewaySegment(FreewaySegment fs)
   {
	   try {
		   PreparedStatement pst;
		   pst = conn.prepareStatement("INSERT INTO AllFreewaySegments(Id, FreewaySegmentTableName, StartRamp, EndRamp, FreewayNumber, Distance) VALUES(default, ?, ?, ?, ?, ?)");

		   pst.setString(1,fs.getSegmentName());
		   pst.setString(2, fs.getStartRamp().getRampName());
		   pst.setString(3, fs.getEndRamp().getRampName());
		   pst.setString(4, fs.getFreewayName());
		   pst.setDouble(5, fs.getDistance());
		   pst.executeUpdate();

	   } catch (SQLException ex) {
		  System.out.println("Unable to insert freeway segment " + fs.getSegmentName() + "into AllFreewaySegments Table" );
	   }
   }
   public void CreateFreewaySegmentTable(FreewaySegment fs)
   {
	   try {
		   String createtable;
		   String tablename = fs.getSegmentName();
		   createtable = "CREATE TABLE IF NOT EXISTS " + tablename + "(Time INT PRIMARY KEY AUTO_INCREMENT, CarCount INT, AverageSpeed DOUBLE, Distance DOUBLE) Engine=InnoDB";
		   stmt.executeUpdate(createtable);

	   } catch (SQLException ex) {
		   System.out.println("Unable to create table for " + fs.getSegmentName());
	   }
   }
   public void InsertAutomobileData(Automobile car)
   {
	   
   }
   public void test()
   {
	   try {
		   String createtable;
		   String tablename = "hello";
		   createtable = "CREATE TABLE IF NOT EXISTS " + tablename + "(Time INT PRIMARY KEY AUTO_INCREMENT, CarCount INT, AverageSpeed DOUBLE, Distance DOUBLE) Engine=InnoDB";
		   stmt.executeUpdate(createtable);

	   } catch (SQLException ex) {
		   System.out.println("Unable to create FreewaySegment table");
	   }
	   try {
		   PreparedStatement pst;
		   pst = conn.prepareStatement("INSERT INTO AllFreewaySegments VALUES(default, ?, ?, ?, ?, ?)");

		   pst.setString(1,"segmentname");
		   pst.setString(2, "samplestartrampname");
		   pst.setString(3, "sample end ramp");
		   pst.setString(4, "105");
		   pst.setDouble(5, 2.44);
		   pst.executeUpdate();

	   } catch (SQLException ex) {
		   // Logger lgr = Logger.getLogger(Prepared.class.getName());
		   //gr.log(Level.SEVERE, ex.getMessage(), ex);
	   }
	   String sql = "SELECT * FROM AllFreewaySegments";
	   ResultSet rs;
	   try {
		   rs = stmt.executeQuery(sql);
		   while(rs.next()){
			   //get entry by column name
			   int id = rs.getInt("Id");
			   String start = rs.getString("StartRamp");
			   String end = rs.getString("EndRamp");
			   double distance = rs.getDouble("Distance");

			   //Display values
			   System.out.println("Id: " + id + " Start Ramp: " + start + " End Ramp: " + end + " Distance: " + distance);
		   }
	   } catch (SQLException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	   }

   }

   
   public static void main(String[] args) 
   {
	   SQLDatabaseHandler sqlhandler = new SQLDatabaseHandler();
	   sqlhandler.test();
	   
	   /*
		   rs.close();
		   stmt.close();
		   conn.close();
	   }catch(SQLException se){
		   //Handle errors for JDBC
		   se.printStackTrace();
	   }catch(Exception e){
		   //Handle errors for Class.forName
		   e.printStackTrace();
	   }finally{
		   //finally block used to close resources
		   try{
			   if(stmt!=null)
				   stmt.close();
		   }catch(SQLException se2){
		   }// nothing we can do
		   try{
			   if(conn!=null)
				   conn.close();
		   }catch(SQLException se){
			   se.printStackTrace();
		   }//end finally try
	   }//end try

   }*/
}
}