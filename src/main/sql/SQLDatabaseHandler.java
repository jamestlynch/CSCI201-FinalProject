package main.sql;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.freeway.FreewaySegment;
import main.map.GeoMapModel;

public class SQLDatabaseHandler {

   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver"; 
   static final String DB_URL = "jdbc:mysql://localhost:3306/test";
   
   static final String USER = "root";
   static final String PASS = "";
   Connection conn = null;
   Statement stmt = null;
   public boolean printErrors = false;
   
   int cars=5;
   public SQLDatabaseHandler()
   {
	   try{
		   //Register JDBC driver
		   Class.forName("com.mysql.jdbc.Driver");

		   //connect to database
		   conn = DriverManager.getConnection(DB_URL,USER,PASS);

		   //Create AllFreewaySegmentsTable
		   stmt = conn.createStatement();
		   String createtablequery="CREATE TABLE IF NOT EXISTS AllFreewaySegments(Id INT PRIMARY KEY AUTO_INCREMENT, FreewaySegmentTableName VARCHAR(20), StartRamp VARCHAR(120), EndRamp VARCHAR(120), Distance DOUBLE) Engine=InnoDB";
		   stmt.executeUpdate(createtablequery);
		   System.out.println("Created AllFreewaySegments Table");

	   }catch(SQLException se){
		   //Handle errors for JDBC
		   se.printStackTrace();
	   }catch(Exception e){
		   //Handle errors for Class.forName
		   e.printStackTrace();
	   }	    
	   
	   try {
		   String createtable;
		   String tablename = "carcounts";
		   createtable = "CREATE TABLE IF NOT EXISTS " + tablename + " (aaa INT, Cars INT) Engine=InnoDB";
		   stmt.executeUpdate(createtable);


	   } catch (SQLException ex) {
		
	   }
   }
   public ArrayList<Double> getAverageSpeeds(FreewaySegment fs)
   {
	   ArrayList<Double> speedsByHour = new ArrayList<Double>();
	   try{
		   PreparedStatement pst = conn.prepareStatement("SELECT AverageSpeed from " + fs.getSegmentName());   
		   ResultSet resultSet = pst.executeQuery();
		   while (resultSet.next())
		   {		   
			  speedsByHour.add(resultSet.getDouble("AverageSpeed"));   
		   }
		
	   }
	   catch (SQLException ex) {
		   if (printErrors) System.out.println("[DATABASE] Couldn't get average speeds for " + fs.getSegmentName());
	   }
	   return speedsByHour;
   }
   public void createFreewaySegmentTable(FreewaySegment fs)
   {
	   try {
		   String createtable;
		   String tablename = fs.getSegmentName();
		   createtable = "CREATE TABLE IF NOT EXISTS " + tablename + "(Time INT PRIMARY KEY, DataCount INT, AverageSpeed DOUBLE, Distance DOUBLE) Engine=InnoDB";
		   stmt.executeUpdate(createtable);
		   //initialize table
		   for (int i=0; i<24; i++)
		   {
			   PreparedStatement pst;
			   pst = conn.prepareStatement("INSERT INTO " + tablename +" VALUES(?, ?, ?, ?)");
			   pst.setInt(1,i);
			   pst.setInt(2,0);
			   pst.setDouble(3, fs.getAverageSpeed());
			   pst.setDouble(4, fs.getDistance());
			   pst.executeUpdate();
		   }

	   } catch (SQLException ex) {
		   if (printErrors) System.out.println("[DATABASE] Unable to create FreewaySegmentTable for " + fs.getSegmentName());
	   }
   }
   public void createFreewaySegmentTables(ArrayList<FreewaySegment> freewaysegments)
   {
	   for (int j = 0; j<freewaysegments.size(); j++)
	   { 
		   try {
			   String createtable;
			   String tablename = freewaysegments.get(j).getSegmentName();
			   createtable = "CREATE TABLE IF NOT EXISTS " + tablename + " (Time INT PRIMARY KEY, DataCount INT, AverageSpeed DOUBLE, Distance DOUBLE) Engine=InnoDB";
			   stmt.executeUpdate(createtable);

			   //initialize table--make one entry for each hour, set initial average speed to the speed limit
			   for (int i=0; i<24; i++)
			   {
				   PreparedStatement pst;
				   pst = conn.prepareStatement("INSERT INTO " + tablename +" VALUES(?, ?, ?, ?)");
				   pst.setInt(1,i);
				   pst.setInt(2,0);
				   pst.setDouble(3, freewaysegments.get(j).getAverageSpeed());
				   pst.setDouble(4, freewaysegments.get(j).getDistance());
				   pst.executeUpdate();
			   }

		   } catch (SQLException ex) {
			   if (printErrors) System.out.println("[DATABASE] Unable to create table for " + freewaysegments.get(j).getStartRamp().getRampName()+ j);
		   }
	   }
   }
   public void insertFreewaySegment(FreewaySegment fs)
   {
	   try {
		   PreparedStatement pst;
		   pst = conn.prepareStatement("INSERT INTO AllFreewaySegments(Id, FreewaySegmentTableName, StartRamp, EndRamp, Distance) VALUES(default, ?, ?, ?, ?)");

		   pst.setString(1,fs.getSegmentName());
		   pst.setString(2, fs.getStartRamp().getRampName());
		   pst.setString(3, fs.getEndRamp().getRampName());
		   pst.setDouble(4, fs.getDistance());
		   pst.executeUpdate();

	   } catch (SQLException ex) {
		  if (printErrors) System.out.println("Unable to insert freeway segment " + fs.getSegmentName() + "into AllFreewaySegments Table" );
	   }
   }
   public void insertListOfFreewaySegments(ArrayList<FreewaySegment> freewaySegments)
   {
	   PreparedStatement pst = null;
	   try {
		   pst = conn.prepareStatement("INSERT INTO AllFreewaySegments(Id, FreewaySegmentTableName, StartRamp, EndRamp, Distance) VALUES(default, ?, ?, ?, ?)");
	   } catch (SQLException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	   }

	   for (FreewaySegment fs: freewaySegments)
	   {
		   try {
			   pst.setString(1,fs.getSegmentName());
			   pst.setString(2, fs.getStartRamp().getRampName());
			   pst.setString(3, fs.getEndRamp().getRampName());
			   pst.setDouble(4, fs.getDistance());
			   pst.executeUpdate();

		   } catch (SQLException ex) {
			   if (printErrors) System.out.println("[DATABASE] Unable to insert freeway segment " + fs.getSegmentName() + "into AllFreewaySegments Table" );
		   }
	   }
   }
   public void updateAverageSpeedOfSegment(FreewaySegment fs, int hour)
   {
	   try{
		   double newAverageSpeed = fs.getAverageSpeed();
		   double oldAverageSpeed;
		   int dataCount;
		   PreparedStatement statement = conn.prepareStatement("SELECT * from " + fs.getSegmentName() + " WHERE Time = ?");    
		   statement.setInt(1, hour);    
		   ResultSet resultSet = statement.executeQuery();
		   while (resultSet.next())
		   {		   
			   //assumes that fs.getAverageSpeed() is returning the average speed that is ONLY based on the most recent json file data
			   //ASK: are the automobiles being removed from segments at the end of each 3 minute cycle?
			   //removeAutomobileFromSegment never called?
			   dataCount = resultSet.getInt("DataCount");
			   oldAverageSpeed = resultSet.getDouble("AverageSpeed");
			   newAverageSpeed = (oldAverageSpeed * dataCount + newAverageSpeed*fs.getAutomobilesOnSegment().size())/(dataCount+fs.getAutomobilesOnSegment().size());
			   dataCount +=fs.getAutomobilesOnSegment().size();

			   String query = "UPDATE " + fs.getSegmentName() + " SET AverageSpeed = ?, DataCount = ? where Time = ?";
			   PreparedStatement preparedStmt = conn.prepareStatement(query);
			   preparedStmt.setDouble(1, newAverageSpeed);
			   preparedStmt.setInt(2, dataCount);
			   preparedStmt.setInt(3, hour);
			   preparedStmt.executeUpdate();
			   
			   cars+=fs.getAutomobilesOnSegment().size();
				 
			   String query2 = "INSERT INTO carcounts VALUES(2, ?)";
			   PreparedStatement preparedStmt2 = conn.prepareStatement(query2);
			   preparedStmt2.setInt(1, cars);
			   preparedStmt2.executeUpdate();
		
			   
		   }
	   }
	   catch (SQLException ex) {
		   if (printErrors) System.out.println("Unable to update average speed for " + fs.getSegmentName());
	   }

   }
   public void updateAverageSpeedOfSegments(ArrayList<FreewaySegment> freewaySegments, int time)
   {
	   try{
		   for (FreewaySegment fs: freewaySegments)
		   {
			   //Pull old data to calculate new average speed
			   double newAverageSpeed = fs.getAverageSpeed();
			   double oldAverageSpeed;
			   int dataCount;
			   
			   PreparedStatement statement = conn.prepareStatement("SELECT * from " + fs.getSegmentName() + " WHERE Time = ?");    
			   statement.setInt(1, time);    
			   
			   ResultSet resultSet = statement.executeQuery();
			   while (resultSet.next())
			   { 
				   dataCount = resultSet.getInt("DataCount");
				   oldAverageSpeed = resultSet.getDouble("AverageSpeed");
				   newAverageSpeed = (oldAverageSpeed * dataCount + newAverageSpeed*fs.getAutomobilesOnSegment().size())/(dataCount+fs.getAutomobilesOnSegment().size());
				   dataCount +=fs.getAutomobilesOnSegment().size();

				   String query = "UPDATE " + fs.getSegmentName() + " SET AverageSpeed = ?, DataCount = ? where Time = ?";
				   PreparedStatement preparedStmt = conn.prepareStatement(query);
				   preparedStmt.setDouble(1, newAverageSpeed);
				   preparedStmt.setInt(2, dataCount);
				   preparedStmt.setInt(3, time);
				   preparedStmt.executeUpdate();
				   
				   cars+=fs.getAutomobilesOnSegment().size();
				 
				   String query2 = "INSERT INTO carcounts VALUES(2, ?)";
				   PreparedStatement preparedStmt2 = conn.prepareStatement(query2);
				   preparedStmt2.setInt(1, cars);
				   preparedStmt2.executeUpdate();
			   }
		   }
		
	   }
	   catch (SQLException ex) {
		   if (printErrors) System.out.println("[DATABASE] Unable to update average speeds");
	   }

   }
   public void printAllFreewaySegmentsTableData()
   {
	   String sql = "SELECT * FROM AllFreewaySegments";
	   ResultSet rs;
	   try {
		   rs = stmt.executeQuery(sql);
		   while(rs.next()){
			   //get entry by column name
			   int id = rs.getInt("Id");
			   String segmentName = rs.getString("FreewaySegmentTableName");
			   String start = rs.getString("StartRamp");
			   String end = rs.getString("EndRamp");
			   double distance = rs.getDouble("Distance");

			   //Display values
			   System.out.println("Id: " + id + " FreewaySegmentTableName: " + segmentName+ " Start Ramp: " + start + " End Ramp: " + end + " Distance: " + distance);
		   }
	   } catch (SQLException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	   }
   }
   public void printAverageSpeedData(FreewaySegment fs)
   {
	   String sql = "SELECT * FROM " + fs.getSegmentName();
	   ResultSet rs;
	   try {
		   rs = stmt.executeQuery(sql);
		   while(rs.next()){
			   int time = rs.getInt("Time");
			   int dataCount = rs.getInt("DataCount");
			   double speed = rs.getDouble("AverageSpeed");

			   System.out.println("FreewaySegmentTableName: " + fs.getSegmentName() + " Time: " + time + " DataCount:  " + dataCount + " Speed: " + speed);
		   }
	   } catch (SQLException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	   }
   }
   
   public static void main(String[] args) throws SQLException 
   {
	   SQLDatabaseHandler sqlhandler = new SQLDatabaseHandler();
	  // GeoMapModel gmm = new GeoMapModel();
	   //System.out.println("# Total Segments: " + gmm.returnAllSegment().size());
	
	   String createtable;
	   String tablename = "aaa";
	   createtable = "CREATE TABLE IF NOT EXISTS " + tablename + " (Time INT, DataCount INT, AverageSpeed DOUBLE) Engine=InnoDB";
	   sqlhandler.stmt.executeUpdate(createtable);

	   //initialize table--make one entry for each hour
	   for (int i=0; i<2; i++)
	   {
		   PreparedStatement pst;
		   pst = sqlhandler.conn.prepareStatement("INSERT INTO " + tablename +" VALUES(?, ?, ?)");
		   pst.setInt(1,i);
		   pst.setInt(2,0);
		   pst.setDouble(3, 25);
		   pst.executeUpdate();
	   }
	   PreparedStatement statement = sqlhandler.conn.prepareStatement("SELECT * from " + tablename + " WHERE Time = ?");    
	   statement.setInt(1, 1);    
	   ResultSet resultSet = statement.executeQuery();
	   while (resultSet.next())
	   {   
		   int dataCount = resultSet.getInt("DataCount");
		   double oldAverageSpeed = resultSet.getDouble("AverageSpeed");
		   double newAverageSpeed = 5;
		   newAverageSpeed = (oldAverageSpeed * dataCount + newAverageSpeed)/(dataCount+1);
		   String query = "UPDATE " + tablename + " SET AverageSpeed = ?, DataCount = ? where Time = ?";
		   PreparedStatement preparedStmt = sqlhandler.conn.prepareStatement(query);
		   preparedStmt.setDouble(1, newAverageSpeed);
		   preparedStmt.setInt(2, 5);
		   preparedStmt.setInt(3, 1);
		   preparedStmt.executeUpdate();

	   }
	   //sqlhandler.insertListOfFreewaySegments(gmm.returnAllSegment());
	  // sqlhandler.printAllFreewaySegmentsTableData();
	   //sqlhandler.createFreewaySegmentTables(gmm.getListOf105Segments());
	  // sqlhandler.printAverageSpeedData(gmm.returnAllSegment().get(0));
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