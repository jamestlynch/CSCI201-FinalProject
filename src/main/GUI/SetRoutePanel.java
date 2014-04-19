package main.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.freeway.FreewaySegment;
import main.map.GeoMapModel;

public class SetRoutePanel extends JPanel{
	int width;
	int height;
	
	JComboBox<String> freewayList;
	JComboBox<String> rampList;
	JComboBox<String> endingFreeway;
	JComboBox<String> endingFreewayRamp;
	GeoMapModel geoMapModel;
	//Pass in the String of freeway names
	public boolean isValidEntry()
	{
		return true;
	}
	private String[] FreewayNames = new String[]{"10", "101", "105", "405"}; 
	public SetRoutePanel(int width, int height, GeoMapModel geoMapModel)//String[] f1, String[] f2)
	{
		this.geoMapModel = geoMapModel;
		this.width = width;
		this.height = height;
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JLabel startingFreewayLabel = new JLabel("Select Starting Freeway:");
		freewayList = new JComboBox<String>(FreewayNames);
		
		JLabel startingFreewayRampLabel = new JLabel("Select Starting Freeway Ramp:");
		rampList = new JComboBox<String>();
		
		RampPopulate startPopulate = new RampPopulate(freewayList, rampList);
		freewayList.addActionListener(startPopulate);
		
		JLabel endingFreewayLabel = new JLabel("Select Ending Freeway:");
		endingFreeway = new JComboBox<String>(FreewayNames);
		JLabel endingFreewayRampLabel = new JLabel("Select Ending Freeway Ramp:");
		endingFreewayRamp = new JComboBox<String>();
		
		RampPopulate endPopulate = new RampPopulate(endingFreeway, endingFreewayRamp);
		endingFreeway.addActionListener(endPopulate);
		
		setLayout(null);
		setPreferredSize(new Dimension(width, height));
		
		startingFreewayLabel.setBounds(
				width/2 - startingFreewayLabel.getPreferredSize().width,
				5, 
				width - 10,
				startingFreewayLabel.getPreferredSize().height);
		
		freewayList.setBounds(
				5, 
				startingFreewayLabel.getPreferredSize().height+10,
				width - 10,
				freewayList.getPreferredSize().height);
		startingFreewayRampLabel.setBounds(
				width/2 - startingFreewayRampLabel.getPreferredSize().width,
				freewayList.getY()+freewayList.getPreferredSize().height + 5,
				width - 10,
				startingFreewayRampLabel.getPreferredSize().height);
		rampList.setBounds(
				5,
				startingFreewayRampLabel.getY()+startingFreewayRampLabel.getPreferredSize().height + 5, 
				width - 10,
				rampList.getPreferredSize().height);
		
		
		endingFreewayLabel.setBounds(
				width/2 - endingFreewayLabel.getPreferredSize().width,
				rampList.getY() + rampList.getPreferredSize().height+90, 
				width - 10,
				endingFreewayLabel.getPreferredSize().height);
		
		endingFreeway.setBounds(
				5, 
				endingFreewayLabel.getY() + endingFreewayLabel.getPreferredSize().height+5,
				width - 10,
				endingFreeway.getPreferredSize().height);
		endingFreewayRampLabel.setBounds(
				width/2 - endingFreewayRampLabel.getPreferredSize().width,
				endingFreeway.getY() + endingFreeway.getPreferredSize().height + 5,
				width - 10,
				endingFreewayRampLabel.getPreferredSize().height);
		endingFreewayRamp.setBounds(
				5,
				endingFreewayRampLabel.getY()+endingFreewayRampLabel.getPreferredSize().height + 5, 
				width - 10,
				endingFreewayRamp.getPreferredSize().height);
		
		add(startingFreewayLabel);
		add(freewayList);
		
		add(startingFreewayRampLabel);
		add(rampList);
		
		add(endingFreewayLabel);
		add(endingFreeway);
		
		add(endingFreewayRampLabel);
		add(endingFreewayRamp);
		
	}
	class RampPopulate implements ActionListener
	{
		JComboBox<String> parentComboBox;
		JComboBox<String> childComboBox;
		public RampPopulate(JComboBox<String> parentComboBox, JComboBox<String> selfComboBox)
		{
			this.parentComboBox = parentComboBox;
			this.childComboBox = selfComboBox;
			
		}
		public void actionPerformed(ActionEvent arg0) {
			childComboBox.removeAllItems();
			String freewayNameSelected = (String)parentComboBox.getSelectedItem();
			System.out.println(freewayNameSelected);
			
			ArrayList<FreewaySegment> tempSeg = null;
			if (freewayNameSelected.equals("10"))
			{
				tempSeg = geoMapModel.getListOf10Segments();
			}
			else if (freewayNameSelected.equals("101"))
			{
				tempSeg = geoMapModel.getListOf101Segments();
			}
			else if (freewayNameSelected.equals("105"))
			{
				tempSeg = geoMapModel.getListOf105Segments();
			}
			else if (freewayNameSelected.equals("405"))
			{
				tempSeg = geoMapModel.getListOf405Segments();
			}
			if (tempSeg == null)
			{
				System.out.println("hello world");
			}
			int i;
			for (i = 0 ; i < tempSeg.size(); i++)
			{
				childComboBox.addItem(tempSeg.get(i).getStartRamp().getRampName());
			}
			childComboBox.addItem(tempSeg.get(i-1).getEndRamp().getRampName());
		}
		
	}
}

