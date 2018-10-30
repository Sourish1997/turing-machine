package conversionHelpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class State {
	private int stateID;
	public Map<Character, ArrayList<State>> nextState;
	private Set <State> states;
	private boolean acceptState;
	
	public State (int ID) {
		this.setStateID(ID);
		this.setNextState(new HashMap <Character, ArrayList<State>> ());
		this.setAcceptState(false);
	}
	
	public State(Set<State> states, int ID) {
		this.setStates(states);
		this.setStateID(ID);
		this.setNextState(new HashMap <Character, ArrayList<State>> ());
		
		for (State p : states) {
			if (p.isAcceptState()) {
				this.setAcceptState(true);
				break;
			}
		}
	}
	
	public void addTransition (State next, char key) {
		ArrayList <State> list = this.nextState.get(key);
		
		if (list == null) {
			list = new ArrayList<State> ();
			this.nextState.put(key, list);
		}
		
		list.add(next);
	}

	public ArrayList<State> getAllTransitions(char c) {	
		if (this.nextState.get(c) == null)	{	return new ArrayList<State> ();	}
		else 								{	return this.nextState.get(c);	}
	}
	
	public Map<Character, ArrayList<State>> getNextState() {
		return nextState;
	}

	public void setNextState(HashMap<Character, ArrayList<State>> hashMap) {
		this.nextState = hashMap;
	}
	
	public int getStateID() {
		return stateID;
	}

	public void setStateID(int stateID) {
		this.stateID = stateID;
	}

	public boolean isAcceptState() {
		return acceptState;
	}

	public void setAcceptState(boolean acceptState) {
		this.acceptState = acceptState;
	}

	public Set <State> getStates() {
		return states;
	}

	public void setStates(Set <State> states) {
		this.states = states;
	}
}