package panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import conversionHelpers.DFA;
import conversionHelpers.State;

@SuppressWarnings("serial")
public class MainPanel extends JPanel implements ActionListener{
	public TapePanel tObj;
	public ControlPanel cObj;
	public BarPanel bObj;
	
	ArrayList<TapeItemPanel> tape;
	TapeItemPanel tapeSlice[], items[];
	
	Timer timer;
	
	String state;
	String val;
	
	int sliceIndex;
	int stepNo;
	int colorState = 0;
	int animSpeed = 1000;
	
	public MainPanel()
	{
		
		items = new TapeItemPanel[13];
		
		for(int a = 0; a < 13; a++)
			items[a] = new TapeItemPanel("~");
		
		tObj = new TapePanel(items);
		cObj = new ControlPanel();
		bObj = new BarPanel();
		
		tape = new ArrayList<TapeItemPanel>();
		tapeSlice = new TapeItemPanel[13];
		
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(tObj, gbc);
		gbc.gridy++;
		add(bObj, gbc);
		gbc.gridy++;
		add(cObj, gbc);
		
		timer = new Timer(1000, this);
		
		Thread watch = new Thread(new Runnable(){
			public void run( ) {
				while(true) {
					if(cObj.comp) {
						bObj.enable();
						tObj.updateSteps(0);
						stepNo = 0;
						tObj.updateState(cObj.inState);
						cObj.comp = false;
					}
					
					if(cObj.ref) {
						bObj.refresh();
						bObj.disable();
						tape.clear();
						for(int a = 0; a < 13; a++) {
							items[a] = new TapeItemPanel("~");
							if(!(a % 2 == 0))
								items[a].setColor(new Color(26, 93, 151));
						}
						colorState = 0;
						tObj.refreshPanel(items);
						tObj.updateState("");
						tObj.updateSteps(-1);
						revalidate();
						cObj.ref = false;
					}
					
					if(bObj.loaded) {
						load();
						bObj.loaded = false;
					}
					
					if(bObj.started) {
						timer.start();
						bObj.started = false;
					}
					
					if(bObj.speedChanged) {
						animSpeed = bObj.animSpeed;
						timer.setDelay(animSpeed);
						bObj.speedChanged = false;
					}
					
					if(bObj.paused) {
						timer.stop();
						bObj.paused = false;
					}
					
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {}
				}
			}
		});
		
		watch.start();
		
		setPreferredSize(new Dimension(700, 500));
	}
	
	public void load() {
		tape.clear();
		colorState = 0;
		stepNo = 0;
		tObj.updateSteps(0);
		String loadStr = bObj.loadString;
		for(int a = 0; a < loadStr.length(); a++) {
			TapeItemPanel item = new TapeItemPanel(loadStr.charAt(a) + "");
			tape.add(item);
		}
		
		for(int a = 0; a < 7; a++) {
			TapeItemPanel item = new TapeItemPanel("~");
			tape.add(item);
		}
		
		for(int a = 0; a < 6; a++) {
			TapeItemPanel item = new TapeItemPanel("~");
			tape.add(0, item);
		}
		
		for(int a = 0; a < 13; a++) {
			tapeSlice[a] = tape.get(a);
			if(!(a % 2 == 0))
				tapeSlice[a].setColor(new Color(26, 93, 151));
		}
		
		tObj.refreshPanel(tapeSlice);
		revalidate();
		
		state = cObj.inState;
		val = tObj.getPanelItem();
		sliceIndex = 0;
	}
	
	public void loadMachine(String filePath) {
		if(!filePath.equals("")) {
			try {
				FileInputStream fstream = new FileInputStream(filePath);
				BufferedReader reader = new BufferedReader(new InputStreamReader(fstream));

				String strLine;

				cObj.states.setText(reader.readLine());
				cObj.initState.setText(reader.readLine());
				
				while ((strLine = reader.readLine()) != null)   {
				  cObj.transition.addItem(strLine);
				  String prod[] = strLine.split(",");
					 for(int a = 0; a < prod.length; a++)
						 prod[a] = prod[a].trim();
				  cObj.tFunc.add(prod);
				}
				
				String fins = (String) cObj.transition.getItemAt(cObj.transition.getItemCount() - 1);
				
				if(fins.charAt(fins.length() - 1) == ',') {
					cObj.transition.removeItemAt(cObj.transition.getItemCount() - 1);
					cObj.finStates.setText(fins.substring(0, fins.length() - 1));
				}
				
				reader.close();
			} catch (IOException e) {
		    	JOptionPane.showMessageDialog(null, "Could not load machine!", "Error", JOptionPane.ERROR_MESSAGE);
		    }
		}
	}
	
	public void loadRegex(DFA dfa) {
		cObj.refresh();
		LinkedList<State> states = dfa.getDfa();
		ArrayList<String> acceptNos = new ArrayList<String>(); 
		for(State state: states) {
			if(state.isAcceptState()) {
				acceptNos.add(state.getStateID() + "");
			}
		}
		
		for(State state: states) {
			String stateName = state.getStateID()+"";
			for(String st: acceptNos) {
		    	if(stateName.equals(st)) {
		    		stateName = "accept" + stateName;
		    		break;
		    	}
		    }
			cObj.states.setText(cObj.states.getText() + "," + stateName);
			if(state.getStateID() == 0)
				cObj.initState.setText(stateName + "");
			for (Map.Entry<Character, ArrayList<State>> entry : state.nextState.entrySet())
			{
			    char ch = entry.getKey();
			    String id = entry.getValue().get(0).getStateID() + "";
			    for(String st: acceptNos) {
			    	if(id.equals(st)) {
			    		id = "accept" + id;
			    		break;
			    	}
			    }
			    String prod = stateName + "," + ch + "," + id + "," + ch + ",R";
			    cObj.transition.addItem(prod);
			    cObj.tFunc.add(prod.split(","));
			}
		}
		cObj.states.setText(cObj.states.getText().substring(1));
	}
	
	public void saveMachine(String filePath) {
			File file;

			if(!filePath.equals("")) {
				file = new File(filePath + ".tm");

			    try {
			    	file.createNewFile();
			      
				    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				    
					for(int a = 0; a < cObj.statesList.length; a ++) {
						if(a + 1 == cObj.statesList.length) {
							writer.write(cObj.statesList[a]);
							writer.newLine();
						}
						else
							writer.write(cObj.statesList[a] + ",");
					}
					
					writer.write(cObj.inState);
					writer.newLine();
					
					for(String[] prod : cObj.tFunc) {
						for(int a = 0; a < prod.length; a ++) {
							if(a + 1 == prod.length) {
								writer.write(prod[a]);
								writer.newLine();
							}
							else
								writer.write(prod[a] + ",");
						}
					}
				
					for(int a = 0; a < cObj.finStatesList.length; a ++) {
						if(a + 1 == cObj.finStatesList.length) {
							writer.write(cObj.finStatesList[a] + ",");
							writer.newLine();
						}
						else
							writer.write(cObj.finStatesList[a] + ",");
					}
					
					writer.flush();
					writer.close();
					
					JOptionPane.showMessageDialog(null, "Machine successfully saved!", "Message", JOptionPane.INFORMATION_MESSAGE);
			    } catch (IOException e) {
			    	JOptionPane.showMessageDialog(null, "Could not save machine!", "Error", JOptionPane.ERROR_MESSAGE);
			    }
			}
	}
	
	public void actionPerformed(ActionEvent e) {
		Thread animate = new Thread(new Runnable(){
			public void run( ) {
				boolean encountered = false;
				for(String prod[]: cObj.tFunc) {
					if(prod.length != 5)
						continue;
					
					if(state.equals(prod[0]) && val.equals(prod[1])) {
						encountered = true;
						state = prod[2];
						tObj.setPanelItem(prod[3]);
						tape.set(sliceIndex + 6, new TapeItemPanel(prod[3]));
				
						try {
							Thread.sleep(animSpeed/2);
						} catch (InterruptedException e1) {}
						
						if(prod[4].equals("L")) {
							if(sliceIndex == 0) {
								tape.add(0, new TapeItemPanel("~"));
							} else {
								sliceIndex--;
							}
						} else if(prod[4].equals("R")) {
							if(sliceIndex + 13 == tape.size()) {
								tape.add(new TapeItemPanel("~"));
								sliceIndex++;
							} else {
								sliceIndex++;
							}
						}
						
						if(prod[4].equals("-")) {
							if(colorState == 0)
								colorState = 1;
							else
								colorState = 0;
						}
						
						for(int a = sliceIndex; a < sliceIndex + 13; a++) {
							tapeSlice[a - sliceIndex] = tape.get(a);
							if(colorState == 0 && (a - sliceIndex) % 2 == 0) 
								tapeSlice[a - sliceIndex].setColor(new Color(26, 93, 151));
							if(colorState == 1 && (a - sliceIndex) % 2 != 0) 
								tapeSlice[a - sliceIndex].setColor(new Color(26, 93, 151));
						}
								
						if(colorState == 0)
							colorState = 1;
						else
							colorState = 0;
						
						tObj.refreshPanel(tapeSlice);
						revalidate();
						
						val = tObj.getPanelItem();
						
						tObj.updateState(state);
						
						for(int a = 0; a < cObj.finStatesList.length; a++)
							if(state.equals(cObj.finStatesList[a])) {
								timer.stop();
								bObj.load.setEnabled(true);
								bObj.play.setEnabled(true);
							}
						break;
					}
				}
				if(!encountered) {
					timer.stop();
					bObj.load.setEnabled(true);
					bObj.play.setEnabled(true);
				}
				stepNo++;
				tObj.updateSteps(stepNo);
			}
		});
		animate.start();
	}
}
