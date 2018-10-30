package panels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class BarPanel extends JPanel{
	JTextField init;
	JButton load, play, pause;
	JSlider speed;
	int width = 700, height = 50;
	public int animSpeed = 500;
	public boolean loaded = false, started = false, speedChanged = false, paused = false;
	public String loadString = "";
	
	public BarPanel() {
		setPreferredSize(new Dimension(width, height));
		Border loweredEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
 		setBorder(loweredEtched);
		
		init = new JTextField(10);
		load = new JButton("Load");
		play = new JButton("Play");
		pause = new JButton("Pause");
		speed =  new JSlider(JSlider.HORIZONTAL, 70, 3000, 1000);
		
		setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
 		g.insets = new Insets(3,3,3,3);
		
		g.gridx = 0;
		g.gridy = 0;
		add(init, g);
		
		g.gridx++;
		add(load, g);
		
		g.gridx++;
		add(play, g);
		
		g.gridx++;
		add(pause, g);
		
		g.gridx++;
		add(new JLabel("Speed:  "), g);
		
		g.gridx++;
		add(speed, g);
		
		disable();
		
		addListeners();
	}
	
	public void addListeners() {
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadString = init.getText();
				loaded = true;
			}
		});
		
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				load.setEnabled(false);
				play.setEnabled(false);
				started = true;			
			}
		});
		
		pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				load.setEnabled(true);
				play.setEnabled(true);
				paused = true;
			}
		});
		
		speed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				animSpeed = speed.getValue();
				speedChanged = true;
			}
		});
	}
	
	public void disable() {
		init.setEnabled(false);
		load.setEnabled(false);
		play.setEnabled(false);
		pause.setEnabled(false);
		speed.setEnabled(false);
	}
	
	public void enable() {
		init.setEnabled(true);
		load.setEnabled(true);
		play.setEnabled(true);
		pause.setEnabled(true);
		speed.setEnabled(true);
	}
	
	public void refresh() {
		init.setText("");
		loadString = "";
		animSpeed = 500;
		speed.setValue(500);
	}
	
	public boolean isEnabled() {
		if(init.isEnabled())
			return true;
		
		else
			return false;
	}
}
