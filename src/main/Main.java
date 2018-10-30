package main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.filechooser.FileNameExtensionFilter;

import conversionHelpers.DFA;
import conversionHelpers.NFA;
import conversionHelpers.RegularExpression;
import panels.MainPanel;

public class Main {
	static JFrame frame;
	static MainPanel mpanel;
	
	static JMenuBar bar = new JMenuBar();
    static JMenu mfile = new JMenu("     File          ");
    static JMenuItem load = new JMenuItem("    Load          ");
    static JMenuItem save = new JMenuItem("    Save          ");
    static JMenuItem exit = new JMenuItem("    Exit          ");
    static JMenu mhelp = new JMenu("     Help          ");
    static JMenuItem help = new JMenuItem("    Help          ");
    static JMenuItem about = new JMenuItem("    About          ");
    static JMenuItem regex = new JMenuItem("    Create From Regex          ");
	
	public static void main(String args[]) {
		frame = new JFrame("Turing Machine Simulation");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		
		frame.setJMenuBar(bar);
        bar.add(mfile);
        mfile.add(load);
        mfile.add(new JSeparator());
        mfile.add(save);
        mfile.add(new JSeparator());
        mfile.add(regex);
        mfile.add(new JSeparator());
        mfile.add(exit);
        bar.add(mhelp);
        mhelp.add(help);
        mhelp.add(new JSeparator());
        mhelp.add(about);
		
		frame.setLayout(new BorderLayout());
		mpanel = new MainPanel();
		frame.add(new JScrollPane(mpanel), BorderLayout.CENTER);
		
		addListeners();
				
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public static void addListeners() {
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(mpanel.bObj.isEnabled()) {
					JOptionPane.showMessageDialog(null, "A machine is already compiled. Please refresh first!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Turing Machine File (*.tm)", "tm");
				fileChooser.setFileFilter(filter);
				fileChooser.setAcceptAllFileFilterUsed(false);
				
				String filePath = "";
				int returnVal = fileChooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION)
					filePath = fileChooser.getSelectedFile().getAbsolutePath();
				
				mpanel.loadMachine(filePath);
			}
		});
		
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!mpanel.bObj.isEnabled()) {
					JOptionPane.showMessageDialog(null, "Cannot save machine before compilation!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Turing Machine File (*.tm)", "tm");
				fileChooser.setFileFilter(filter);
				fileChooser.setAcceptAllFileFilterUsed(false);
				
				String filePath = "";
				int returnVal = fileChooser.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION)
					filePath = fileChooser.getSelectedFile().getAbsolutePath();
				mpanel.saveMachine(filePath);
			}
		});
		
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "To create a turing machine, first fill in the set of states, input state, \n"
		  			    + "set of final states, transition function and click on compile. A set of \n"
					    + "values must always be comma separated. The productions of the \n"
					    + "transition function should be in the following form: (qcur, varcur, \n"
					    + "qres, varres, move). The simulation can then be run on different \n"
					    + "input strings which can be loaded using the load bar. Once \n"
					    + "compiled, turing machines can also be saved and loaded later.", "Help", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Created by Sourish Banerjee.\n"
						+ "Achieved with Java.", "About", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		regex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(mpanel.bObj.isEnabled()) {
					JOptionPane.showMessageDialog(null, "A machine is already compiled. Please refresh first!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				String input = JOptionPane.showInputDialog(null, "Enter regex: ", "Input", JOptionPane.INFORMATION_MESSAGE);
	            if (input != null) {
	            	NFA nfa = RegularExpression.generateNFA(input);
	            	DFA dfa = RegularExpression.generateDFA(nfa);
	            	mpanel.loadRegex(dfa);
	            }
			}
		});
	}
}
