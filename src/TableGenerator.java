import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


public class TableGenerator {
	
	private HashMap<String, Symbol> nonTerminalSymbols;
	private HashMap<String, Symbol> terminalSymbols;
	private ArrayList<Production> productions;
	
	public TableGenerator(ArrayList<Production> productions,
			HashMap<String,Symbol> terminalSymbols, HashMap<String,Symbol> nonTerminalSymbols)
	{
		this.productions = productions;
		this.terminalSymbols = terminalSymbols;
		this.nonTerminalSymbols = nonTerminalSymbols;
	}
	
	public void generate()
	{
		generateFirst();
		generateFollow();
		generateFirstPlus();
	}
	
	private void generateFirst()
	{
		HashMap<Symbol, HashSet<Symbol>> firstTable = generateFirstTerminal();
		
		Iterator<String> iterNonTerminal = nonTerminalSymbols.keySet().iterator();
		while(iterNonTerminal.hasNext())
		{
			Symbol symbol = nonTerminalSymbols.get(iterNonTerminal.next());
			HashSet<Symbol> first = new HashSet<Symbol>();
			firstTable.put(symbol, first);
		}
		
		while(generateFirstNonTerminalForOneIteration(firstTable) >0)
		{
			//Printer.print("first",firstTable);
		}
		
		Printer.print("First Table",firstTable);
	}
	
	private int generateFirstNonTerminalForOneIteration(HashMap<Symbol, 
			HashSet<Symbol>> firstTable)
	{
		int count = 0;
		for(int i=0; i<productions.size(); i++)
		{
			Production p = productions.get(i);
			int index=0;
			HashSet<Symbol> rhs = new HashSet<Symbol>();
			if(!p.getRightHandSide(0).equals(Parser.EPSILON))
			{
				Symbol s = p.getRightHandSide(0);
				HashSet<Symbol> B0 = firstTable.get(p.getRightHandSide(0));
				B0.remove(Parser.EPSILON);
				rhs = B0;
				index = 0;
				while(firstTable.get(p.getRightHandSide(0)).contains(Parser.EPSILON) && index <= p.getRightHandSideCount()-2)
				{
					HashSet<Symbol> BIndexPlus1 = firstTable.get(p.getRightHandSide(index+1));
					rhs.addAll(BIndexPlus1);
					rhs.remove(Parser.EPSILON);
					index++;
				}
			}
			
			if(index == p.getRightHandSideCount()-1 && firstTable.get(p.getRightHandSide(index)).contains(Parser.EPSILON) )
			{
				rhs.add(Parser.EPSILON);
			}
			HashSet<Symbol> A = firstTable.get(p.getLeftHandSide());
			if(A.addAll(rhs))
			{
				count ++;
			}
			firstTable.put(p.getLeftHandSide(), A);
		}
		return count;
	}
	
	private HashMap<Symbol, HashSet<Symbol>> generateFirstTerminal()
	{
		HashMap<Symbol, HashSet<Symbol>> firstTable = new HashMap<Symbol, HashSet<Symbol>>();
		
		Iterator<String> iterTerminal = terminalSymbols.keySet().iterator();
		while(iterTerminal.hasNext())
		{
			Symbol symbol = terminalSymbols.get(iterTerminal.next());
			HashSet<Symbol> first = new HashSet<Symbol>();
			first.add(symbol);
			firstTable.put(symbol, first);
		}
		
		return firstTable;
	}
	
	private void generateFollow()
	{
		
	}
	
	private void generateFirstPlus()
	{
		
	}

}
