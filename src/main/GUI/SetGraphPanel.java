package main.GUI;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

public class SetGraphPanel extends JPanel{
	int width;
	int height;
	public SetGraphPanel(int width, int height)
	{
		this.width = width;
		this.height = height;
		setOpaque(false);
	}
	protected void paintComponent(Graphics g)
	{
		g.drawLine(10, 10, 10, height/2);
		g.drawLine(10, height/2, width - 10, height/2);
		int inc = (width-20)/24;
		for (int i = 0; i < 24; i++)
		{
			String y; 
			if ( i < 10)
				y = " "+i;
			else
				y = "" + i;
			FontMetrics fm = g.getFontMetrics();
			g.setFont(new Font("Arial", Font.PLAIN, 10));
			
			int yPixelSize = fm.stringWidth(""+i);
			g.drawString(y, inc*(i+1) + 5, height/2+14);
			g.drawLine(10+(inc*(i+1)), height/2-5, 10 + (inc*(i+1)), height/2+5);
		}
	}
	
}
