package main.GUI;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class SetGraphPanel extends JPanel{
	int width;
	int height;
	ArrayList<Double> TimeVal;
	public SetGraphPanel(int width, int height)
	{
		this.width = width;
		this.height = height;
		setOpaque(false);
	}
	public void setGraphValues(ArrayList<Double> TimeVal)
	{
		this.TimeVal = TimeVal;
	}
	protected void paintComponent(Graphics g)
	{
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		for (int i = 0 ; i < TimeVal.size();i++)
		{
			if (TimeVal.get(i) > max)
				max = TimeVal.get(i);
			if (TimeVal.get(i) < min)
				min = TimeVal.get(i);
		}
		//Left Line
		g.drawLine(10, 10, 10, height/2);
		//Bottom Line
		g.drawLine(10, height/2, width - 10, height/2);
		int inc = (width-20)/24;
		//Increments along the bottom
		for (int i = 0; i < 24; i++)
		{
			String y; 
			if ( i < 10)
				y = " "+i;
			else
				y = "" + i;
			g.setFont(new Font("Arial", Font.PLAIN, 10));

			g.drawString(y, inc*(i+1) + 5, height/2+14);
			g.drawLine(10+(inc*(i+1)), height/2-5, 10 + (inc*(i+1)), height/2+5);
		}
		//Increments along the left
		int bottomCorner = (height/2);
		inc = (bottomCorner - 10) / 24;
		double timeinc = (max - min / 24);
		for (int i = 0 ; i < 24 ; i++)
		{
			double currentVal = (min + (timeinc * i));
			double nextVal = max;
			String y;
			if (currentVal < 10)
			{
				y = " " + currentVal;
			}
			else
				y = "" + currentVal;
			g.drawString(y, 10, bottomCorner - (inc * i+1));
				
		}
	}
	
}
