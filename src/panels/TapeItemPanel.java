package panels;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class TapeItemPanel extends JPanel {
	String val;
	JLabel label;
	int width = 30, height = 30;
	
	public TapeItemPanel(String val) {
		this.val = val;
		label = new JLabel("  " + val + "  ");
		label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, label.getFont().getSize()));
		setPreferredSize(new Dimension(width, height));
		Border loweredEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
 		setBorder(loweredEtched);
 		setLayout(new BorderLayout());
 		setBackground(new Color(66, 139, 202));
		add(label, BorderLayout.CENTER);
	}
	
	public TapeItemPanel() {
		val = "~";
		label = new JLabel("  " + val + "  ");
		label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, label.getFont().getSize()));
		setPreferredSize(new Dimension(width, height));
		Border loweredEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
 		setBorder(loweredEtched);
 		setLayout(new GridBagLayout());
 		setBackground(new Color(66, 139, 202));
		add(label);
	}
	
	public void setVal(String val) {
		this.val = val;
		label.setText("  " + val + "  ");
	}
	
	public String getVal() {
		return val;
	}
	
	public void setColor(Color color) {
		setBackground(color);
	}
}
