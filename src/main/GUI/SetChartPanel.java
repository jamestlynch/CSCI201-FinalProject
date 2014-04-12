package main.GUI;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

public class SetChartPanel extends JPanel{
	int width;
	int height;
	final int total_rows = 25;
	public SetChartPanel(int width, int height)
	{
		setPreferredSize(new Dimension(width, height));
		this.width = width;
		this.height = height;
		DefaultTableModel dtm = new DefaultTableModel(total_rows, 2);
		JTable tables = new JTable(dtm);
		tables.setEnabled(false);
		tables.setValueAt("Hour", 0, 0);
		tables.setValueAt("ETA", 0, 1);
		for (int i = 1 ; i < 25; i++)
		{
			tables.setValueAt(i +":00", i , 0);
		}
		
		
		Dimension tableDimensions = tables.getPreferredSize();
		add(tables);
	}
}
