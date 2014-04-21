package main.GUI;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class SetGraphPanel extends JPanel{
	int width;
	int height;
	double timeinc;
	double min = Double.MAX_VALUE;
	double max = Double.MIN_VALUE;
	ArrayList<Double> TimeVal;
	int bottomCornery;
	int topCornery = 10;
	int bottomCornerx = 10;
	int rightCornerx;
	public SetGraphPanel(int width, int height)
	{
		this.width = width;
		this.height = height;
		bottomCornery = height/2;
		rightCornerx = width - 10;
		setOpaque(false);
	}
	public void setGraphValues(ArrayList<Double> TimeVal)
	{
		this.TimeVal = TimeVal;
		
		for (int i = 0 ; i < TimeVal.size();i++)
		{
			if (TimeVal.get(i) > max)
				max = TimeVal.get(i);
			if (TimeVal.get(i) < min)
				min = TimeVal.get(i);
		}
	}
	protected void paintComponent(Graphics g)
	{
		
		//Left Line
		g.drawLine(bottomCornerx, topCornery, bottomCornerx, bottomCornery);
		//Bottom Line
		g.drawLine(bottomCornerx, bottomCornery, rightCornerx, bottomCornery);
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
			g.drawLine(bottomCornerx+(inc*(i+1)), bottomCornery-5, bottomCornerx+ (inc*(i+1)), bottomCornery+5);
		}
		//Increments along the left along with dashes
		bottomCornery = (height/2);
		inc = (bottomCornery - 10) / 23;
		double timeinc = (max - min) / 23;
		for (int i = 0 ; i < 24; i++)
		{
			double currentVal = (min + (timeinc * i));			
			String y;
			currentVal = (double)Math.round(currentVal* 10) / 10;
			if (currentVal < 10)
				y = " " + currentVal;
			else
				y = "" + currentVal;
			g.drawString(y, -1, bottomCornery - (inc * i+1));
			g.drawLine(5, bottomCornery - (inc * i+2), 15, bottomCornery - (inc * i+2));
		}
		
		for (int i = 1 ; i < TimeVal.size(); i++)
		{
			determineLine();
			//g.drawLine(x1, y1, x2, y2);
		}
	}
	public void determineLine()
	{
		
	}
}
