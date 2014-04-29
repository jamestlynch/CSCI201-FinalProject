package main.GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class SetChartPanel extends JPanel{
	int width;
	int height;
	final int total_rows = 25;
	String BeginLocation, EndLocation;
	JTable tables;
	public void setLocations(String BeginLocation, String EndLocation, ArrayList<Double> TimeValues)//)
	{
		this.BeginLocation = BeginLocation;
		this.EndLocation = EndLocation;
		JLabel begin = new JLabel ("Starting from "+ BeginLocation + "\n");
		JLabel end = new JLabel ("Ending at " + EndLocation);
		JPanel beginAndEnd = new JPanel();
		beginAndEnd.setLayout(new BorderLayout());
		beginAndEnd.add(begin, BorderLayout.WEST);
		beginAndEnd.add(end, BorderLayout.SOUTH);
		
		for (int i = 1; i < tables.getRowCount(); i++)
		{
			String y = String.format("%.3g%n", TimeValues.get(i-1));
			tables.setValueAt(y, i, 1);
		}
		//begin.setBounds(10, 0, begin.getWidth(), begin.getHeight());
		//end.setBounds(5, 5+begin.getHeight(), end.getWidth(), end.getHeight());
		//tables.setBounds(width/2 - tables.getWidth()/2, end.getY()+end.getHeight(), tables.getWidth(), tables.getHeight());
		add(beginAndEnd, BorderLayout.NORTH);
		add(tables, BorderLayout.CENTER);
	}
	public SetChartPanel(int width, int height)
	{
		setPreferredSize(new Dimension(width, height));
		setLayout(new BorderLayout());
		this.width = width;
		this.height = height;
		DefaultTableModel dtm = new DefaultTableModel(total_rows, 2);
		tables = new JTable(dtm);
		tables.setEnabled(false);
		tables.setValueAt("Hour", 0, 0);
		tables.setValueAt("ETA(minutes)", 0, 1);
		for (int i = 1 ; i < 25; i++)
		{
			tables.setValueAt(i +":00", i , 0);
		}
		
		
		Dimension tableDimensions = tables.getPreferredSize();
		
		
		//http://stackoverflow.com/questions/3548140/how-to-open-and-save-using-java
		JButton exportToCSV = new JButton("Export data to CSV file");
		//exportToCSV.setBounds(5, 500 - exportToCSV.getPreferredSize().height - 30, 400- 10, exportToCSV.getPreferredSize().height);
		exportToCSV.addActionListener(new ExportButton(this));
		add(exportToCSV, BorderLayout.SOUTH);
	}
	class ExportButton implements ActionListener
	{
		JPanel parentPanel;
		public ExportButton(JPanel parentPanel)
		{
			this.parentPanel = parentPanel;
		}
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			
			int returnVal = fileChooser.showSaveDialog(parentPanel);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
			  File file = fileChooser.getSelectedFile();
			  generateCSV(file);
			}
			
		}
	}
	public void generateCSV(File file)
	{
		try
		{
			FileWriter fw = new FileWriter(file + ".csv");
			PrintWriter pw = new PrintWriter(fw);
			pw.println(BeginLocation + "," + EndLocation);
			for (int i = 0 ; i < tables.getRowCount(); i++)
				pw.println(tables.getValueAt(i, 0)+"," +tables.getValueAt(i,1));
			pw.close();
		}
		catch(IOException ioe)
		{
			System.out.println("IOE: " + ioe.getMessage());
		}
	}
	
}
class CSVSaveFileView implements FileFilter
{

	public boolean accept(File arg0) {
		return false;
	}
	public String getDescription()
	{
		return ".CSV files";
	}
	
}
