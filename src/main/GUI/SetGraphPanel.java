package main.GUI;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class SetGraphPanel extends JPanel{
	int width;
	int height;
	double min = Double.MAX_VALUE;
	double max = Double.MIN_VALUE;
	ArrayList<Double> TimeVal;
	int bottomCornery;
	int topCornery = 10;
	int bottomCornerx = 10;
	int rightCornerx;
	int leftDashfirst;
	int leftDashlast;
	int inc; //This defines the increase in pixels when analyzing the next point.
	double timeinc; //This represents the amount of time in between each y axis point
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
		inc = (rightCornerx - bottomCornerx)/24;
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
		inc = (bottomCornery - topCornery) / 23;
		timeinc = (max - min) / 23;
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
			g.drawLine(5, bottomCornery - (inc * (i)), 15, bottomCornery - (inc * (i)));
			if ( i == 0)
				leftDashfirst = bottomCornery - (inc * (i));
			if (i == 23)
				leftDashlast = bottomCornery - (inc * (i));
		}
		
		for (int i = 1 ; i < TimeVal.size(); i++)
		{
			inc = (rightCornerx - bottomCornerx)/24;
			determineLine(g, TimeVal.get(i), TimeVal.get(i-1), i);
			//g.drawLine(x1, y1, x2, y2);
		}
	}
	public void determineLine(Graphics g, Double p2, Double p1, int numberEntry)
	{
		
		double ProportionUpP1 = p1/(max - min);
		double ProportionUpP2 = p2/(max - min);
		
		double yINC1 = (leftDashfirst - leftDashlast) * (ProportionUpP1); 
		double yINC2 = (leftDashfirst - leftDashlast) * (ProportionUpP2);
		int point1y = (int) (leftDashfirst - yINC1);
		int point2y = (int) (leftDashfirst - yINC2);
//		g.drawLine(30, bottomCornerx + (numberEntry-1 * inc));
		g.drawLine(bottomCornerx + ((numberEntry) * inc), point1y, bottomCornerx + ((numberEntry+1) * inc), point2y);
	}
}
