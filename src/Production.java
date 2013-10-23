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
			s += "("+temp.getType()+","+temp.getValue()+") ";
		}
		return s;
	}
	
	public HashMap<String, Symbol> assignSymbolType(HashMap<String, Symbol> nonTerminalSymbols)
	{
		HashMap<String, Symbol> terminalSymbols = new HashMap<String, Symbol>();
		
		for (int i=0; i<rightHandSide.size(); i++)
		{
			Symbol temp = rightHandSide.get(i);
			if(nonTerminalSymbols.get(temp.getValue())!=null)
			{
				temp.setType(Symbol.Type.NONTERMINAL);
			}
			else
			{
				temp.setType(Symbol.Type.TERMINAL);
				terminalSymbols.put(temp.getValue(), temp);
			}
			rightHandSide.set(i, temp);
		}
		
		return terminalSymbols;
	}
	
	public Symbol getRightHandSide(int index)
	{
		return rightHandSide.get(index);
	}

}
