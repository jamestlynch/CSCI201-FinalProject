package main.GUI;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class UICSCI201Maps extends JFrame {
	// Add the side panel and map panel
	
	public UICSCI201Maps() {
		setSize(500, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(new UIMapPanel(), BorderLayout.WEST);
		//add(new UISidePanel(), BorderLayout.EAST);
		setVisible(true);
	}
	public static void main(String[] args)
	{
		new UICSCI201Maps();
	}
}
