import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


public class TableGenerator {

	private Symbol startSymbol;
	private HashMap<String, Symbol> nonTerminalSymbols;
	private HashMap<String, Symbol> terminalSymbols;
	private ArrayList<Production> productions;

	private HashMap<Symbol, HashSet<Symbol>> firstTable;
	private HashMap<Symbol, HashSet<Symbol>> followTable;
	private HashMap<Production, HashSet<Symbol>> firstPlusTable;
	private HashMap<String, HashMap<String, Integer>> parseTable;

	public TableGenerator(Symbol startSymbol, ArrayList<Production> productions,
			HashMap<String,Symbol> terminalSymbols, HashMap<String,Symbol> nonTerminalSymbols)
	{
		this.startSymbol = startSymbol;
		this.productions = productions;
		this.terminalSymbols = terminalSymbols;
		terminalSymbols.remove(Symbol.EPSILON.getValue());
		this.nonTerminalSymbols = nonTerminalSymbols;
		Printer.print("Terminal Symbols: ",terminalSymbols);
		Printer.print("NonTerminal Symbols: ",nonTerminalSymbols);
		Printer.print("Productions: ",productions);
	}

	public void generate()
	{
		firstTable = generateFirst();
		Printer.print("First Table: ",firstTable);
		followTable = generateFollow();
		Printer.print("Follow Table: ",followTable);
		firstPlusTable = generateFirstPlus();
		Printer.print("First Plus: ",firstPlusTable);
		parseTable = generateTable();
		Printer.print("Parse Table: ", parseTable);
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

		}

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
			if(!p.getRightHandSide(0).equals(Symbol.EPSILON))
			{
				HashSet<Symbol> B0 = firstTable.get(p.getRightHandSide(0));
				B0.remove(Symbol.EPSILON);
				rhs = B0;
				index = 0;
				while(firstTable.get(p.getRightHandSide(0)).contains(Symbol.EPSILON) && index <= p.getRightHandSideCount()-2)
				{
					HashSet<Symbol> BIndexPlus1 = firstTable.get(p.getRightHandSide(index+1));
					rhs.addAll(BIndexPlus1);
					rhs.remove(Symbol.EPSILON);
					index++;
				}
			}

			if(index == p.getRightHandSideCount()-1 && firstTable.get(p.getRightHandSide(index)).contains(Symbol.EPSILON))
			{
				rhs.add(Symbol.EPSILON);
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
		
		HashSet<Symbol> firstEOF = new HashSet<Symbol>();
		firstEOF.add(Symbol.EOF);
		firstTable.put(Symbol.EOF, firstEOF);
		
		HashSet<Symbol> firstEPSILON = new HashSet<Symbol>();
		firstEPSILON.add(Symbol.EPSILON);
		firstTable.put(Symbol.EPSILON, firstEPSILON);
		
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

		//EOF -> Follow(S)
		HashSet<Symbol> followStart = new HashSet<Symbol>();
		followStart.add(Symbol.EOF);
		followTable.put(startSymbol, followStart);

		while(generateFollowForOneIteration(followTable)>0)
		{

		}

		return followTable;
	}

	@SuppressWarnings("unchecked")
	private int generateFollowForOneIteration(HashMap<Symbol, HashSet<Symbol>> followTable)
	{
		int count = 0;
		HashSet<Symbol> trailer = new HashSet<Symbol>();
		for(int i=0; i<productions.size(); i++)
		{
			Production p = productions.get(i);
			trailer = (HashSet<Symbol>) followTable.get(p.getLeftHandSide()).clone();
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

					if(firstTable.get(Bj).contains(Symbol.EPSILON))
					{
						HashSet<Symbol> firstBj = (HashSet<Symbol>) firstTable.get(Bj).clone();
						firstBj.remove(Symbol.EPSILON);
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
			if(firstBeta.contains(Symbol.EPSILON))
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

		while(firstTable.get(rightHandSide.get(index)).contains(Symbol.EPSILON) && index < rightHandSide.size()-2)
		{
			index++;
			firstSet.addAll(firstTable.get(rightHandSide.get(index)));
		}

		return firstSet;
	}

	private HashMap<String, HashMap<String, Integer>> generateTable()
	{
		terminalSymbols.remove(Symbol.EPSILON.getValue());
		
		HashMap<String, HashMap<String, Integer>> parseTable = new HashMap<String, HashMap<String, Integer>>();

		Iterator<String> iterNonTerminal = nonTerminalSymbols.keySet().iterator();
		while(iterNonTerminal.hasNext())
		{
			HashMap<String, Integer> row = new HashMap<String, Integer>();

			Iterator<String> iterTerminal = terminalSymbols.keySet().iterator();
			while (iterTerminal.hasNext())
			{
				row.put(terminalSymbols.get(iterTerminal.next()).getValue(), -1);
			}

			parseTable.put(nonTerminalSymbols.get(iterNonTerminal.next()).getValue(), row);
		}

		int p = 0;
		while(p < productions.size())
		{
			Production production = productions.get(p);
			String A = production.getLeftHandSide().getValue();
			HashSet<Symbol> firstPlusADerivesB = firstPlusTable.get(production);
			Iterator<Symbol> iterFirstPlusADB = firstPlusADerivesB.iterator();
			while(iterFirstPlusADB.hasNext())
			{
				Symbol w = iterFirstPlusADB.next();
				HashMap<String, Integer> row = parseTable.get(A);
				row.put(w.getValue(), p);
				parseTable.put(A, row);
			}
			
			if(firstPlusADerivesB.contains(Symbol.EOF))
			{
				HashMap<String, Integer> row = parseTable.get(A);
				row.put(Symbol.EOF.getValue(), p);
				parseTable.put(A, row);
			}
			p++;
		}

		return parseTable;
	}

}
