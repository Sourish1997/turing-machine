package conversionHelpers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;

public class RegularExpression {
	private static int stateID = 0;
	
	private static Stack<NFA> stackNfa = new Stack<NFA> ();
	private static Stack<Character> operator = new Stack<Character> ();	

	private static Set<State> set1 = new HashSet <State> ();
	private static Set<State> set2 = new HashSet <State> ();
	
	private static Set <Character> input = new HashSet <Character> ();
	
	public static NFA generateNFA(String regular) {
		regular = AddConcat (regular);
		
		input.add('a');
		input.add('b');
		
		stackNfa.clear();
		operator.clear();

		for (int i = 0 ; i < regular.length(); i++) {	

			if (isInputCharacter (regular.charAt(i))) {
				pushStack(regular.charAt(i));
				
			} else if (operator.isEmpty()) {
				operator.push(regular.charAt(i));
				
			} else if (regular.charAt(i) == '(') {
				operator.push(regular.charAt(i));
				
			} else if (regular.charAt(i) == ')') {
				while (operator.get(operator.size()-1) != '(') {
					doOperation();
				}				
		
				operator.pop();
				
			} else {
				while (!operator.isEmpty() && 
						Priority (regular.charAt(i), operator.get(operator.size() - 1)) ){
					doOperation ();
				}
				operator.push(regular.charAt(i));
			}		
		}		
		
		while (!operator.isEmpty()) {	doOperation(); }
		
		NFA completeNfa = stackNfa.pop();
		
		completeNfa.getNfa().get(completeNfa.getNfa().size() - 1).setAcceptState(true);
		
		return completeNfa;
	}
	
	private static boolean Priority (char first, Character second) {
		if(first == second) {	return true;	}
		if(first == '*') 	{	return false;	}
		if(second == '*')  	{	return true;	}
		if(first == '.') 	{	return false;	}
		if(second == '.') 	{	return true;	}
		if(first == '|') 	{	return false;	} 
		else 				{	return true;	}
	}

	private static void doOperation () {
		if (RegularExpression.operator.size() > 0) {
			char charAt = operator.pop();

			switch (charAt) {
				case ('|'):
					union ();
					break;
	
				case ('.'):
					concatenation ();
					break;
	
				case ('*'):
					star ();
					break;
	
				default :
					System.out.println("Unkown Symbol !");
					System.exit(1);
					break;			
			}
		}
	}
		
	private static void star() {
		NFA nfa = stackNfa.pop();
		
		State start = new State (stateID++);
		State end	= new State (stateID++);
		
		start.addTransition(end, 'e');
		start.addTransition(nfa.getNfa().getFirst(), 'e');
		
		nfa.getNfa().getLast().addTransition(end, 'e');
		nfa.getNfa().getLast().addTransition(nfa.getNfa().getFirst(), 'e');
		
		nfa.getNfa().addFirst(start);
		nfa.getNfa().addLast(end);
		
		stackNfa.push(nfa);
	}

	private static void concatenation() {
		NFA nfa2 = stackNfa.pop();
		NFA nfa1 = stackNfa.pop();
		
		nfa1.getNfa().getLast().addTransition(nfa2.getNfa().getFirst(), 'e');
		
		for (State s : nfa2.getNfa()) {	nfa1.getNfa().addLast(s); }

		stackNfa.push (nfa1);
	}
	
	private static void union() {
		NFA nfa2 = stackNfa.pop();
		NFA nfa1 = stackNfa.pop();
		
		State start = new State (stateID++);
		State end	= new State (stateID++);

		start.addTransition(nfa1.getNfa().getFirst(), 'e');
		start.addTransition(nfa2.getNfa().getFirst(), 'e');

		nfa1.getNfa().getLast().addTransition(end, 'e');
		nfa2.getNfa().getLast().addTransition(end, 'e');

		nfa1.getNfa().addFirst(start);
		nfa2.getNfa().addLast(end);
		
		for (State s : nfa2.getNfa()) {
			nfa1.getNfa().addLast(s);
		}
		stackNfa.push(nfa1);		
	}
	
	private static void pushStack(char symbol) {
		State s0 = new State (stateID++);
		State s1 = new State (stateID++);
		
		s0.addTransition(s1, symbol);
		
		NFA nfa = new NFA ();
		
		nfa.getNfa().addLast(s0);
		nfa.getNfa().addLast(s1);		
		
		stackNfa.push(nfa);
	}

	private static String AddConcat(String regular) {
		String newRegular = new String ("");

		for (int i = 0; i < regular.length() - 1; i++) {
			if ( isInputCharacter(regular.charAt(i))  && isInputCharacter(regular.charAt(i+1)) ) {
				newRegular += regular.charAt(i) + ".";
				
			} else if ( isInputCharacter(regular.charAt(i)) && regular.charAt(i+1) == '(' ) {
				newRegular += regular.charAt(i) + ".";
				
			} else if ( regular.charAt(i) == ')' && isInputCharacter(regular.charAt(i+1)) ) {
				newRegular += regular.charAt(i) + ".";
				
			} else if (regular.charAt(i) == '*'  && isInputCharacter(regular.charAt(i+1)) ) {
				newRegular += regular.charAt(i) + ".";
				
			} else if ( regular.charAt(i) == '*' && regular.charAt(i+1) == '(' ) {
				newRegular += regular.charAt(i) + ".";
				
			} else if ( regular.charAt(i) == ')' && regular.charAt(i+1) == '(') {
				newRegular += regular.charAt(i) + ".";			
				
			} else {
				newRegular += regular.charAt(i);
			}
		}
		newRegular += regular.charAt(regular.length() - 1);
		return newRegular;
	}

	private static boolean isInputCharacter(char charAt) {
		if 		(charAt == 'a')	return true;
		else if (charAt == 'b')	return true;
		else if (charAt == 'e')	return true;
		else					return false;
	}

	
	public static DFA generateDFA(NFA nfa) {
		DFA dfa = new DFA ();

		stateID = 0;

		LinkedList <State> unprocessed = new LinkedList<State> ();
		
		set1 = new HashSet <State> ();
		set2 = new HashSet <State> ();

		set1.add(nfa.getNfa().getFirst());

		removeEpsilonTransition ();

		State dfaStart = new State (set2, stateID++);
		
		dfa.getDfa().addLast(dfaStart);
		unprocessed.addLast(dfaStart);
		
		while (!unprocessed.isEmpty()) {
			State state = unprocessed.removeLast();

			for (Character symbol : input) {
				set1 = new HashSet<State> ();
				set2 = new HashSet<State> ();

				moveStates (symbol, state.getStates(), set1);
				removeEpsilonTransition ();

				boolean found = false;
				State st = null;

				for (int i = 0 ; i < dfa.getDfa().size(); i++) {
					st = dfa.getDfa().get(i);

					if (st.getStates().containsAll(set2)) {
						found = true;
						break;
					}
				}

				if (!found) {
					State p = new State (set2, stateID++);
					unprocessed.addLast(p);
					dfa.getDfa().addLast(p);
					state.addTransition(p, symbol);

				} else {
					state.addTransition(st, symbol);
				}
			}			
		}
		return dfa;
	}

	private static void removeEpsilonTransition() {
		Stack <State> stack = new Stack <State> ();
		set2 = set1;

		for (State st : set1) { stack.push(st);	}

		while (!stack.isEmpty()) {
			State st = stack.pop();

			ArrayList <State> epsilonStates = st.getAllTransitions ('e');

			for (State p : epsilonStates) {
				if (!set2.contains(p)) {
					set2.add(p);
					stack.push(p);
				}				
			}
		}		
	}

	private static void moveStates(Character c, Set<State> states,	Set<State> set) {
		ArrayList <State> temp = new ArrayList<State> ();

		for (State st : states) {	temp.add(st);	}
		for (State st : temp) {			
			ArrayList<State> allStates = st.getAllTransitions(c);

			for (State p : allStates) {	set.add(p);	}
		}
	}	
}