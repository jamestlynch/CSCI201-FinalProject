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
	JLabel liveTimeLabel = new JLabel();
	JLabel limitTimeLabel= new JLabel();
	JComboBox freewayList;
	JComboBox rampList;
	JComboBox endingFreeway;
	JComboBox endingRampList;
	GeoMapModel geoMapModel;
	//Pass in the String of freeway names
	public String getBeginningLocation()
	{
		return (String)rampList.getSelectedItem();
	}
	public String getEndingLocation()
	{
		return (String)endingRampList.getSelectedItem();
	}
	public String getBeginFreeway()
	{
		return (String)freewayList.getSelectedItem();
	}
	public String getEndFreeway()
	{
		return (String)endingFreeway.getSelectedItem();
	}
	public boolean isValidEntry()
	{
		String endingFreewayRampString = (String)endingRampList.getSelectedItem();
		String rampListString = (String)rampList.getSelectedItem();
		String startFreewayName = (String)freewayList.getSelectedItem();
		String endFreewayName = (String)endingFreeway.getSelectedItem();
		if (startFreewayName.equals("105") && !endFreewayName.equals("105"))
			return false;
		if (!startFreewayName.equals("105") && endFreewayName.equals("105"))
			return false;
		if (rampListString != null && endingFreewayRampString != null &&
				rampListString != endingFreewayRampString)
		{
			return true;
		}
		else
			return false;
			
	}
	boolean firstTime = true;
	private String[] FreewayNames = new String[]{"", "10", "101", "105", "405"}; 
	public void displayTotalTime(double liveTime, double limitTime)
	{
		if (firstTime)
		{
			firstTime = false;
		}
		else
		{
			liveTimeLabel.removeAll();
			limitTimeLabel.removeAll();
		}
		String time1 = String.format("%.3g%n", liveTime);
		time1 = time1 + " minutes";
		String time2 = String.format("%.3g%n", limitTime);
		time2 = time2 + " minutes";
		
		liveTimeLabel.setText("Live Data Fastest Path's Time: " + time1);
		limitTimeLabel.setText("Speed Limit Fastest Path's Time: " + time2);
		liveTimeLabel.setBounds(5,
		endingRampList.getY()+endingRampList.getPreferredSize().height + 5, 
		width - 10,
		liveTimeLabel.getPreferredSize().height);
		limitTimeLabel.setBounds(5,
				liveTimeLabel.getY()+liveTimeLabel.getPreferredSize().height + 5, 
				width - 10,
				limitTimeLabel.getPreferredSize().height);
		add(liveTimeLabel);
		add(limitTimeLabel);
	}
	public SetRoutePanel(int width, int height, GeoMapModel geoMapModel)//String[] f1, String[] f2)
	{
		this.geoMapModel = geoMapModel;
		this.width = width;
		this.height = height;
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		//Starting Freeway Declaration and Action Listener
		JLabel startingFreewayLabel = new JLabel("Select Starting Freeway:");
		freewayList = new JComboBox (FreewayNames);
		
		JLabel startingFreewayRampLabel = new JLabel("Select Starting Freeway Ramp:");
		rampList = new JComboBox ();
		
		RampPopulate startPopulate = new RampPopulate(freewayList, rampList);
		freewayList.addActionListener(startPopulate);
		rampList.setEnabled(false);
		
		//Ending freeway Declaration and Action Listener
		JLabel endingFreewayLabel = new JLabel("Select Ending Freeway:");
		endingFreeway = new JComboBox (FreewayNames);
		JLabel endingFreewayRampLabel = new JLabel("Select Ending Freeway Ramp:");
		endingRampList = new JComboBox ();
		
		RampPopulate endPopulate = new RampPopulate(endingFreeway, endingRampList);
		endingFreeway.addActionListener(endPopulate);
		endingRampList.setEnabled(false);
		
		setLayout(null);
		setPreferredSize(new Dimension(width, height));
		
		//Set Location for each component.
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
		endingRampList.setBounds(
				5,
				endingFreewayRampLabel.getY()+endingFreewayRampLabel.getPreferredSize().height + 5, 
				width - 10,
				endingRampList.getPreferredSize().height);
		
		add(startingFreewayLabel);
		add(freewayList);
		
		add(startingFreewayRampLabel);
		add(rampList);
		
		add(endingFreewayLabel);
		add(endingFreeway);
		
		add(endingFreewayRampLabel);
		add(endingRampList);
		
	}
	class RampPopulate implements ActionListener
	{
		JComboBox parentComboBox;
		JComboBox childComboBox;
		public RampPopulate(JComboBox parentComboBox, JComboBox selfComboBox)
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
			else //This means they chose the null option.
			{
				childComboBox.setEnabled(false);
				return;
			}
			int i;
			childComboBox.setEnabled(true);
			for (i = 0 ; i < tempSeg.size(); i++)
			{
				childComboBox.addItem(tempSeg.get(i).getStartRamp().getRampName());
			}
			childComboBox.addItem(tempSeg.get(i-1).getEndRamp().getRampName());
		}
		
	}
}

