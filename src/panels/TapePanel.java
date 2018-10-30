package panels;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class TapePanel extends JPanel {
	int siz = 13;
	int width = 700, height =200;
	String state = "   State: ", steps = "Steps:  " + "     ";
	TapeItemPanel items[] = new TapeItemPanel[siz];
	JLabel stateLabel, stepsLabel;
	
	public TapePanel(TapeItemPanel items[]) {
		for(int a = 0; a < siz; a++) {
			this.items[a] = items[a];
			if(!(a % 2 == 0))
				this.items[a].setColor(new Color(26, 93, 151));
		}
			
		setPreferredSize(new Dimension(width, height));
		Border loweredEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
 		setBorder(loweredEtched);
 		
 		stateLabel = new JLabel(state);
 		stateLabel.setFont(new Font(stateLabel.getFont().getFontName(), Font.BOLD, stateLabel.getFont().getSize()));
 		stepsLabel = new JLabel(steps);
 		stepsLabel.setFont(new Font(stepsLabel.getFont().getFontName(), Font.BOLD, stepsLabel.getFont().getSize()));
 		
 		setLayout(new GridBagLayout());
		
		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(3,3,10,3);
		
		g.anchor = GridBagConstraints.LINE_START;
		g.gridx = 0;
		g.gridy = 0;
		g.gridwidth = siz/2;
		add(stateLabel, g);
		
		g.anchor = GridBagConstraints.LINE_END;
		g.gridx = siz/2 + 1;
		add(stepsLabel, g);
		
		g.insets = new Insets(3,3,3,3);
		
		g.anchor = GridBagConstraints.CENTER;
		g.gridx = 0;
		g.gridy++;
		g.gridwidth = 1;
		add(items[0], g);
		
		for(int a = 1; a < siz; a++) {
			g.gridx++;
			add(items[a], g);
		}
	}
	
	public TapePanel() {
		for(int a = 0; a < siz; a++) {
			items[a] = new TapeItemPanel("~");
			if(!(a % 2 == 0))
				this.items[a].setColor(new Color(26, 93, 151));
		}
		
		setPreferredSize(new Dimension(width, height));
		Border loweredEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
 		setBorder(loweredEtched);
 		
 		stateLabel = new JLabel(state);
 		stateLabel.setFont(new Font(stateLabel.getFont().getFontName(), Font.BOLD, stateLabel.getFont().getSize()));
 		stepsLabel = new JLabel(steps);
 		stepsLabel.setFont(new Font(stepsLabel.getFont().getFontName(), Font.BOLD, stepsLabel.getFont().getSize()));
 		
 		setLayout(new GridBagLayout());
		
		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(3,3,10,3);
		
		g.anchor = GridBagConstraints.LINE_START;
		g.gridx = 0;
		g.gridy = 0;
		g.gridwidth = siz/2;
		add(stateLabel, g);
		
		g.anchor = GridBagConstraints.LINE_END;
		g.gridx = siz/2 + 1;
		add(stepsLabel, g);
		
		g.insets = new Insets(3,3,3,3);
		
		g.anchor = GridBagConstraints.CENTER;
		g.gridx = 0;
		g.gridy = 0;
		add(items[0], g);
				
		for(int a = 1; a < siz; a++) {
			g.gridx++;
			add(items[a], g);
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		Point location = new Point(70 + (9 * 30), 152);
		Point point2 = new Point(location.x + 20, location.y);
		Point point3 = new Point(location.x + 10, location.y - 20);
		
		int xpoints[] = {location.x, point2.x, point3.x};
	    int ypoints[] = {location.y, point2.y, point3.y};
	    int npoints = 3;
	    
	    g.fillPolygon(xpoints, ypoints, npoints);

	}
	
	public void updateState(String state) {
		this.state = "   State: " + state;
		stateLabel.setText(this.state);
	}
	
	public void updateSteps(int step) {
		if(step == -1)
			steps = "Steps:  " + "     ";
		else
			steps = "Steps: " + step + "     ";
		stepsLabel.setText(steps);
	}
	
	public void setPanelItem(String val) {
		items[siz/2].setVal(val);
	}
	
	public String getPanelItem() {
		return items[siz/2].getVal();
	}
	
	public void refreshPanel(TapeItemPanel items[]) {
		for(int a = 0; a < siz; a++)
			this.items[a] = items[a];
		
		removeAll();
		
		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(3,3,10,3);
		
		g.anchor = GridBagConstraints.LINE_START;
		g.gridx = 0;
		g.gridy = 0;
		g.gridwidth = siz/2;
		add(stateLabel, g);
		
		g.anchor = GridBagConstraints.LINE_END;
		g.gridx = siz/2 + 1;
		add(stepsLabel, g);
		
		g.insets = new Insets(3,3,3,3);
		
		g.anchor = GridBagConstraints.CENTER;
		g.gridx = 0;
		g.gridy++;
		g.gridwidth = 1;
		add(items[0], g);
		
		for(int a = 1; a < siz; a++) {
			g.gridx++;
			add(items[a], g);
		}
	}
}
