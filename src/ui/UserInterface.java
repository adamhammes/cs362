package ui;

import java.awt.FlowLayout;

import javax.swing.*;

public class UserInterface {

	@SuppressWarnings("deprecation")
	public static void main(String args[]){
		
		JFrame f = new JFrame();
		f.setSize(500, 500);
		f.setTitle("eBook Library");
		f.setLocation(800, 100);
		f.setVisible(true);
		f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
		
		
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		JTextField in = new JTextField();
		p.add(in);
		f.add(p);
		
		
	}
	
}
