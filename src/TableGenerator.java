import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


public class TableGenerator {
	
	private HashMap<String, Symbol> nonTerminalSymbols;
	private HashMap<String, Symbol> terminalSymbols;
	private ArrayList<Production> productions;
	
	private HashMap<Symbol, HashSet<Symbol>> firstTable;
	private HashMap<Symbol, HashSet<Symbol>> followTable;
	private HashMap<Production, HashSet<Symbol>> firstPlusTable;
	
	public TableGenerator(ArrayList<Production> productions,
			HashMap<String,Symbol> terminalSymbols, HashMap<String,Symbol> nonTerminalSymbols)
	{
		this.productions = productions;
		this.terminalSymbols = terminalSymbols;
		this.nonTerminalSymbols = nonTerminalSymbols;
	}
	
	public void generate()
	{
		firstTable = generateFirst();
		followTable = generateFollow();
		firstPlusTable = generateFirstPlus();
		Printer.print(firstPlusTable);
	}
	
	private HashMap<Symbol, HashSet<Symbol>> generateFirst()
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
		
		//Printer.print("First Table",firstTable);
		return firstTable;
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
	
	private HashMap<Symbol, HashSet<Symbol>> generateFollow()
	{
		HashMap<Symbol, HashSet<Symbol>> followTable = new HashMap<Symbol, HashSet<Symbol>>();
		
		Iterator<String> iterNonTerminal = nonTerminalSymbols.keySet().iterator();
		while(iterNonTerminal.hasNext())
		{
			Symbol symbol = nonTerminalSymbols.get(iterNonTerminal.next());
			HashSet<Symbol> follow = new HashSet<Symbol>();
			followTable.put(symbol, follow);
		}
		
		//TODO EOF -> Follow(S)
		
		while(generateFollowForOneIteration(followTable)>0)
		{
			
		}
		
		return followTable;
	}
	
	private int generateFollowForOneIteration(HashMap<Symbol, HashSet<Symbol>> followTable)
	{
		int count = 0;
		HashSet<Symbol> trailer = new HashSet<Symbol>();
		for(int i=0; i<productions.size(); i++)
		{
			Production p = productions.get(i);
			trailer = followTable.get(p.getLeftHandSide());
			for (int j=p.getRightHandSideCount()-1; j>=0; j--)
			{
				Symbol Bj = p.getRightHandSide(j);
				if(Bj.getType() == Symbol.Type.NONTERMINAL)
				{
					HashSet<Symbol> followBj = followTable.get(Bj);
					if(followBj.addAll(trailer))
					{
						count++;
					}
					followTable.put(Bj, followBj);
					
					if(firstTable.get(Bj).contains(Parser.EPSILON))
					{
						HashSet<Symbol> firstBj = firstTable.get(Bj);
						firstBj.remove(Parser.EPSILON);
						trailer.addAll(firstBj);
					}
					else
					{
						trailer = firstTable.get(Bj);
					}
				}
				else
				{
					trailer = firstTable.get(Bj);
				}
			}
		}
		
		return count;
	}
	
	private HashMap<Production, HashSet<Symbol>> generateFirstPlus()
	{
		HashMap<Production, HashSet<Symbol>> firstPlusTable = new HashMap<Production, HashSet<Symbol>>();
		
		for (int i=0; i<productions.size(); i++)
		{
			Production production = productions.get(i);
			HashSet<Symbol> firstBeta = getFirstFromRightHandSide(production.getRightHandSide());
			if(firstBeta.contains(Parser.EPSILON))
			{
				firstBeta.addAll(followTable.get(production.getLeftHandSide()));
			}
			HashSet<Symbol> firstPlusADerivesB = firstBeta;
			firstPlusTable.put(production, firstPlusADerivesB);
		}
		
		return firstPlusTable;
	}
	
	private HashSet<Symbol> getFirstFromRightHandSide(ArrayList<Symbol> rightHandSide)
	{
		int index = 0;
		HashSet<Symbol> firstSet = firstTable.get(rightHandSide.get(index));
		
		while(firstTable.get(rightHandSide.get(index)).contains(Parser.EPSILON))
		{
			index++;
			firstSet.addAll(firstTable.get(rightHandSide.get(index)));
		}
		
		return firstSet;
	}

}
