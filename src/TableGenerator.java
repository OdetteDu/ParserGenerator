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
		Printer.print(firstTable);
	}
	
	private HashMap<Symbol, HashSet<Symbol>> generateFirstNonTerminalForOneIteration(HashMap<Symbol, 
			HashSet<Symbol>> firstTable)
	{
		for(int i=0; i<productions.size(); i++)
		{
			Production p = productions.get(i);
			if(p.getRightHandSide(0).getValue() != Parser.EPSILON.getValue())
			{
				HashSet<Symbol> b0 = firstTable.get(p.getRightHandSide(0));
				b0.remove(Parser.EPSILON);
				HashSet<Symbol> rhs = b0;
			}
		}
		return firstTable;
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
