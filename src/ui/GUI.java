package ui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import oracle.jrockit.jfr.JFR;

@SuppressWarnings("serial")
public class GUI extends JPanel implements ActionListener {

		JTextField text;
		JTextArea area;
		Controller controller;
		
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String in = text.getText();
			area.append(in + "\n");
			text.selectAll();
			
			area.append(controller.command(in) + "\n\n");
			
			
			area.setCaretPosition(area.getDocument().getLength());
			
		}
		
		public GUI(){
		
			super(new GridBagLayout());
			
			text = new JTextField(150);
			text.addActionListener(this);
			
			area = new JTextArea(50, 150);
			area.setEditable(false);
			JScrollPane pane = new JScrollPane(area);
			
			GridBagConstraints c = new GridBagConstraints();
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(text, c);
			
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1.0;
			c.weighty = 1.0;
			add(pane, c);
			
			controller = new Controller();
			
		}
		
		private static void createAndShowGUI(){
			JFrame f = new JFrame("eBook Library");
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			f.add(new GUI());
			
			f.pack();
			f.setVisible(true);
		}
		
		
		public static void main(String[] args){
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                createAndShowGUI();
	            }
	        });
		}
		
		
}
	

