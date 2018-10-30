package panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel{
	int width = 700, height = 200;
	public boolean comp = false, ref = false;
	public ArrayList<String[]> tFunc;
	public String statesList[], inState, finStatesList[];
	
	JTextField states, initState, finStates;
	JComboBox transition;
	JButton add, compile, refresh;
	
	
	public ControlPanel() {
		setPreferredSize(new Dimension(width, height));
		Border loweredEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
 		setBorder(loweredEtched);
		
 		tFunc = new ArrayList<String[]>();
 		
 		states = new JTextField(10);
 		initState = new JTextField(5);
 		finStates = new JTextField(10);
 		transition = new JComboBox(new String[]{"Transition Function"});
 		add = new JButton("Add");
 		compile = new JButton("Compile");
 		refresh = new JButton("Refresh");
 		
 		setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
 		g.insets = new Insets(3,3,3,3);
		
		g.gridx = 0;
		g.gridy = 0;
		add(new JLabel("States:  "), g);
		
		g.gridx++;
		add(states);
		
		g.gridx++;
		add(new JLabel("     Start State:  "));
		
		g.gridx++;
		add(initState, g);
		
		g.gridx++;
		add(new JLabel("     Final States:  "));
		
		g.gridx++;
		add(finStates, g);
		
		g.gridx = 0;
		g.gridy++;
		g.gridwidth = 6;
		add(transition, g);
		
		g.gridy++;
		g.gridwidth = 2;
		add(add, g);
		
		g.gridx+=2;
		add(compile, g);
		
		g.gridx+=2;
		add(refresh, g);
 		
		refresh.setEnabled(false);
 		addListeners();
	}
	
	public void addListeners() {
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 String production = JOptionPane.showInputDialog("Enter a production: ");
				 if(production != null) {
					 String pr[] = production.split(",");
					 
					 for(int a = 0; a < pr.length; a++) {
						 pr[a] = pr[a].trim();
						 if(pr[a].equals("")) {
							 JOptionPane.showMessageDialog(null, "Invalid production!", "Error", JOptionPane.ERROR_MESSAGE);
							 return;
						 }
					 }
								 
					 if(pr.length != 5) {
						 JOptionPane.showMessageDialog(null, "Invalid production!", "Error", JOptionPane.ERROR_MESSAGE);
						 return;
					 }
					 
					 if(pr[1].length() != 1 || pr[3].length() !=1 || !(pr[4].equals("L") || pr[4].equals("R") || pr[4].equals("-")) ) {
						 JOptionPane.showMessageDialog(null, "Invalid production!", "Error", JOptionPane.ERROR_MESSAGE);
						 return;
					 }
					 
					 String prod[] = production.split(",");
					 for(int a = 0; a < prod.length; a++)
						 prod[a] = prod[a].trim();
					 tFunc.add(prod);
					 transition.addItem(production);
				 }
			}
		});
		
		compile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 if(initState.getText().equals("") || states.getText().equals("") || transition.getItemCount() == 1) {
					 JOptionPane.showMessageDialog(null, "Please fill all essential details!", "Error", JOptionPane.ERROR_MESSAGE);
					 return;
				 }
				 disable();
				 statesList = states.getText().split(",");
				 inState = initState.getText().trim();
				 finStatesList = finStates.getText().split(",");
				 for(int a  = 0; a < statesList.length; a++)
					 statesList[a] = statesList[a].trim();
				 for(int a  = 0; a < finStatesList.length; a++)
					 finStatesList[a] = finStatesList[a].trim();
				 comp = true;
			}
		});
		
		refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 refresh();
				 enable();
				 ref = true;
			}
		});
	}
	
	public void disable() {
		states.setEnabled(false);
		initState.setEnabled(false);
		finStates.setEnabled(false);;
		add.setEnabled(false);
		compile.setEnabled(false);
		refresh.setEnabled(true);
	}
	
	public void enable() {
		states.setEnabled(true);
		initState.setEnabled(true);
		finStates.setEnabled(true);;
		add.setEnabled(true);
		compile.setEnabled(true);
		refresh.setEnabled(false);
	}
	
	public void refresh() {
		tFunc.clear();
		states.setText("");
		initState.setText("");
		finStates.setText("");
		transition.removeAllItems();
		transition.addItem("Transition Function");
	}
}
