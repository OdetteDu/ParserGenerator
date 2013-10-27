import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


public class TableGenerator {

	private final Symbol startSymbol;
	private final HashMap<String, Symbol> nonTerminalSymbols;
	private final HashMap<String, Symbol> terminalSymbols;
	private final ArrayList<Production> productions;

	private HashMap<Symbol, HashSet<Symbol>> firstTable;
	private HashMap<Symbol, HashSet<Symbol>> followTable;
	private HashMap<Production, HashSet<Symbol>> firstPlusTable;
	private HashMap<String, HashMap<String, Integer>> parseTable;

	public TableGenerator(Symbol startSymbol, ArrayList<Production> productions,
			HashMap<String,Symbol> terminalSymbols, HashMap<String,Symbol> nonTerminalSymbols) throws NotLLOneGrammarException
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

	public void generate() throws NotLLOneGrammarException
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

		while(generateFirstNonTerminalForOneIteration(firstTable) > 0)
		{

		}

		return firstTable;
	}

	@SuppressWarnings("unchecked")
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
				HashSet<Symbol> B0 = (HashSet<Symbol>) firstTable.get(p.getRightHandSide(0)).clone();
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
						trailer = (HashSet<Symbol>) firstTable.get(Bj).clone();
					}
				}
				else
				{
					trailer = (HashSet<Symbol>) firstTable.get(Bj).clone();
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

	@SuppressWarnings("unchecked")
	private HashSet<Symbol> getFirstFromRightHandSide(ArrayList<Symbol> rightHandSide)
	{
		int index = 0;
		HashSet<Symbol> firstSet = (HashSet<Symbol>) firstTable.get(rightHandSide.get(index)).clone();

		while(firstTable.get(rightHandSide.get(index)).contains(Symbol.EPSILON) && index < rightHandSide.size()-2)
		{
			index++;
			firstSet.addAll(firstTable.get(rightHandSide.get(index)));
		}

		return firstSet;
	}

	private HashMap<String, HashMap<String, Integer>> generateTable() throws NotLLOneGrammarException
	{	
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

			row.put(Symbol.EOF.getValue(), -1);

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
				if(!w.equals(Symbol.EPSILON) && !w.equals(Symbol.EOF) )
				{
					HashMap<String, Integer> row = parseTable.get(A);
					int temp = row.get(w.getValue());
					if(temp !=-1)
					{
						throw new NotLLOneGrammarException();
					}
					else
					{
						row.put(w.getValue(), p);
						parseTable.put(A, row);
					}
				}

			}

			if(firstPlusADerivesB.contains(Symbol.EOF))
			{
				HashMap<String, Integer> row = parseTable.get(A);
				int temp = row.get(Symbol.EOF.getValue());
				if(temp != -1)
				{
					throw new NotLLOneGrammarException();
				}
				else
				{
					row.put(Symbol.EOF.getValue(), p);
					parseTable.put(A, row);
				}

			}
			p++;
		}

		return parseTable;
	}

	public String getLLOneTableInYAML()
	{
		String s = "";

		//terminals: [a, b]
		s += "terminals: [";
		Iterator<String> iterTerminals = terminalSymbols.keySet().iterator();
		while (iterTerminals.hasNext())
		{
			s += iterTerminals.next()+", ";
		}
		if(terminalSymbols.size() != 0)
		{
			s = s.substring(0, s.length()-2);
		}
		s += "]\n";

		//non-terminals: [B]
		s += "non-terminals: [";
		Iterator<String> iterNonTerminals = nonTerminalSymbols.keySet().iterator();
		while (iterNonTerminals.hasNext())
		{
			s += iterNonTerminals.next()+", ";
		}
		if(nonTerminalSymbols.size() != 0)
		{
			s = s.substring(0, s.length()-2);
		}
		s += "]\n";

		//eof-maker: <EOF>
		s += "eof-marker: "+Symbol.EOF.getValue()+"\n";

		//error-maker: --
		s += "error-marker: --\n";

		//start-symbol: B
		s += "start-symbol: "+startSymbol.getValue()+"\n\n";

		//productions: 0: {B: [a, B, b]}
		s += "productions: \n";
		for (int i=0; i<productions.size(); i++)
		{
			s += "  " + i + ": {";
			Production p = productions.get(i);
			s += p.getLeftHandSide().getValue()+": "+"[";
			int j;
			for (j=0; j<p.getRightHandSideCount(); j++)
			{
				Symbol rhsj = p.getRightHandSide(j);
				if (!rhsj.equals(Symbol.EPSILON))
				{
					s += rhsj.getValue() + ", ";
				}
				else
				{
					s += ", ";
				}
			}
			if( j!=0)
			{
				s = s.substring(0, s.length()-2);
			}
			s += "]}\n";
		}
		s += "\n";

		//table: B: {b: 1, a: 0, <EOF>:1}
		s += "table: \n";
		Iterator<String> iterTable = parseTable.keySet().iterator();
		while(iterTable.hasNext())
		{
			String nt = iterTable.next();
			s += "  " + nt+": " + "{";
			HashMap<String, Integer> row = parseTable.get(nt);
			Iterator<String> iterRow = row.keySet().iterator();
			while(iterRow.hasNext())
			{
				String t = iterRow.next();
				s += t + ": ";
				int value = row.get(t);
				if(value == -1)
				{
					s += "--";
				}
				else
				{
					s += value;
				}
				s += ", ";
			}
			if (row.size() != 0)
			{
				s = s.substring(0, s.length()-2);
			}
			s += "}\n";
		}

		return s;
	}

	public String getProductions()
	{
		String s = "";

		s += "Productions: \n";
		for (int i=0; i<productions.size(); i++)
		{
			s += i + ": ";
			Production p = productions.get(i);
			s += p.getLeftHandSide().getValue()+" -> ";
			int j;
			for (j=0; j<p.getRightHandSideCount(); j++)
			{
				Symbol rhsj = p.getRightHandSide(j);
				s += rhsj.getValue() + " ";
			}
			if( j!=0)
			{
				s = s.substring(0, s.length()-1);
			}
			s += "\n";
		}
		//s += "\n";	

		return s;
	}
	
	public String getFirstSets()
	{
		String s = "First Sets: \n";
		Iterator<Symbol> iterFirstTable = firstTable.keySet().iterator();
		while (iterFirstTable.hasNext())
		{
			Symbol A = iterFirstTable.next();
			s += A.getValue() + ": " + "[";
			HashSet<Symbol> set = firstTable.get(A);
			Iterator<Symbol> iterSet = set.iterator();
			while(iterSet.hasNext())
			{
				s += iterSet.next() + ", ";
			}
			if(set.size() != 0)
			{
				s = s.substring(0, s.length()-2);
			}
			s += "]\n";
		}
		return s;
	}
	
	public String getFollowSets()
	{
		String s = "Follow Sets: \n";
		Iterator<Symbol> iterFollowTable = followTable.keySet().iterator();
		while (iterFollowTable.hasNext())
		{
			Symbol A = iterFollowTable.next();
			s += A.getValue() + ": " + "[";
			HashSet<Symbol> set = followTable.get(A);
			Iterator<Symbol> iterSet = set.iterator();
			while(iterSet.hasNext())
			{
				s += iterSet.next() + ", ";
			}
			if(set.size() != 0)
			{
				s = s.substring(0, s.length()-2);
			}
			s += "]\n";
		}
		return s;
	}
	
	public String getFirstPlusSets()
	{
		String s = "First+ Sets: \n";
		for (int i=0; i<productions.size(); i++)
		{
			Production p = productions.get(i);
			s += p +": \n\t[";
			HashSet<Symbol> set = firstPlusTable.get(p);
			Iterator<Symbol> iterSet = set.iterator();
			while(iterSet.hasNext())
			{
				s += iterSet.next() + " ";
			}
			if(set.size() != 0)
			{
				s = s.substring(0, s.length()-1);
			}
			s += "]\n";
		}
		return s;
	}

}
