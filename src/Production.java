import java.util.ArrayList;
import java.util.HashMap;


public class Production {
	
	private Symbol leftHandSide;
	private ArrayList<Symbol> rightHandSide;
	
	public Production(Symbol leftHandSide, ArrayList<Symbol> rightHandSide)
	{
		this.leftHandSide = leftHandSide;
		this.rightHandSide = rightHandSide;
	}
	
	public String toString()
	{
		String s = leftHandSide.getValue() + "->";
		for (int i=0; i<rightHandSide.size(); i++)
		{
			//s += rightHandSide.get(i).getValue() + " ";
			Symbol temp = rightHandSide.get(i);
			s += temp+" ";
		}
		return s;
	}
	
	//return the NT which appear on the right hand side
	public HashMap<String, Symbol> assignSymbolType(HashMap<String, Symbol> nonTerminalSymbols, HashMap<String, Symbol> terminalSymbols )
	{
		HashMap<String, Symbol> nonTerminalsWhichAppearOnTheRightSide = new HashMap<String, Symbol>();
		
		for (int i=0; i<rightHandSide.size(); i++)
		{
			Symbol temp = rightHandSide.get(i);
			if(nonTerminalSymbols.get(temp.getValue())!=null)
			{
				temp.setType(Symbol.Type.NONTERMINAL);
				nonTerminalsWhichAppearOnTheRightSide.put(temp.getValue(), temp);
			}
			else
			{
				temp.setType(Symbol.Type.TERMINAL);
				terminalSymbols.put(temp.getValue(), temp);
			}
			rightHandSide.set(i, temp);
		}
		
		return nonTerminalsWhichAppearOnTheRightSide;
	}
	
	public Symbol getLeftHandSide() {
		return leftHandSide;
	}

	public ArrayList<Symbol> getRightHandSide() {
		return rightHandSide;
	}

	public Symbol getRightHandSide(int index)
	{
		return rightHandSide.get(index);
	}
	
	public int getRightHandSideCount()
	{
		return rightHandSide.size();
	}

}
