package main.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SetRoutePanel extends JPanel{
	int width;
	int height;
	//Pass in the String of freeway names
	public SetRoutePanel(int width, int height)//String[] f1, String[] f2)
	{
		this.width = width;
		this.height = height;
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JLabel flheader = new JLabel("Select a freeway:");
		JComboBox<String> freewayList = new JComboBox<String>();
		JLabel flheader2 = new JLabel("Select a freeway exit:");
		JComboBox<String> rampList = new JComboBox<String>();
		
		setLayout(null);
		setPreferredSize(new Dimension(width, height));
		
		flheader.setBounds(width/2 - flheader.getPreferredSize().width,0, width - 10, flheader.getPreferredSize().height);
		freewayList.setBounds(5, flheader.getPreferredSize().height+5, width - 10, freewayList.getPreferredSize().height);
		flheader2.setBounds(width/2 - flheader2.getPreferredSize().width, freewayList.getY()+freewayList.getPreferredSize().height + 5, width - 10, flheader2.getPreferredSize().height);
		rampList.setBounds(5, flheader2.getY()+flheader2.getPreferredSize().height + 5, width- 10, rampList.getPreferredSize().height);
		add(flheader);
		add(freewayList);
		
		add(flheader2);
		add(rampList);
		JButton jb = new JButton ("ACTIVATE");
		jb.setBounds(5, height - jb.getPreferredSize().height - 10, width- 10, jb.getPreferredSize().height);
		add (jb);
	}
}
