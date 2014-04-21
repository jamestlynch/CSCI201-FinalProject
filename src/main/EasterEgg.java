package main;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EasterEgg implements ActionListener{
	public void actionPerformed(ActionEvent ae)
	{
		JDialog jd = new JDialog();
		jd.setTitle("ABOUT MENU");
		jd.setSize(500,500);
		jd.setModal(true);
		jd.setResizable(false);
		JPanel text = new JPanel();
		jd.setLayout(new FlowLayout());
		text.add(new JLabel("Hi J-Milly! Our names are Joseph M. Lin, James T. Lynch, Ivana N. Wang, and Sarah A. Loui. Thanks for using our program!"
				+ " I just wanted to say you've been an extraordinary professor. We have tons of suggestions on how you could improve this final project"
				+ " that would hopefully line up under the Computer Science department's requirements for a CS201 project. For now, enjoy these two photos"
				+ " of you we found online."));
		jd.add(text);
		JLabel image1 = new JLabel();
		JLabel image2 = new JLabel();
		ImageIcon i1 = new ImageIcon("Jeff_Miller_photo.jpg");
		ImageIcon i2 = new ImageIcon("Miller_Alcohol.jpg");
		image1.setIcon(i1);
		image2.setIcon(i2);
		jd.add(text);
		jd.add(image1);
		jd.add(image2);
		jd.setVisible(true);
	}
}
