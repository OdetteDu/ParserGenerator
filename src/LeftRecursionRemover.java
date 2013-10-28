import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;




public class LeftRecursionRemover {
	
	private final Symbol startSymbol;
	private final HashMap<String, Symbol> nonTerminalSymbols;
	private final HashMap<String, Symbol> terminalSymbols;
	private final ArrayList<Production> productions;

	public LeftRecursionRemover(Symbol startSymbol, ArrayList<Production> productions,
			HashMap<String,Symbol> terminalSymbols, HashMap<String,Symbol> nonTerminalSymbols)
	{
		this.startSymbol = startSymbol;
		this.productions = productions;
		this.terminalSymbols = terminalSymbols;
		terminalSymbols.remove(Symbol.EPSILON.getValue());
		this.nonTerminalSymbols = nonTerminalSymbols;
		//Printer.print("Terminal Symbols: ",terminalSymbols);
		//Printer.print("NonTerminal Symbols: ",nonTerminalSymbols);
		Printer.print("Productions: ",productions);
	}

	@SuppressWarnings("unchecked")
	public void removeLeftRecursion() 
	{
		Symbol[] NT = getNonTerminalSymbols(nonTerminalSymbols);
		Printer.print(NT);
		
		for (int i=0; i<NT.length; i++)
		{
			for (int j=0; j<i; j++)
			{
				//if exist a production Ai -> AjY
				ArrayList<Integer> matches = findProduction(NT[i], NT[j]);
				if(!matches.isEmpty())
				{
					//replace Ai -> AjY with one or more production expands Aj
					Iterator<Integer> iter = matches.iterator();
					while(iter.hasNext())
					{
						int index = iter.next();
						Production toBeReplaced = productions.remove(index);
						
						Printer.print(productions);
						System.out.println("i="+i+" j="+j+" "+toBeReplaced);
						System.out.println(findProduction(toBeReplaced.getRightHandSide(0)));
						ArrayList<Integer> expansions = findProduction(toBeReplaced.getRightHandSide(0));
						Iterator<Integer> iterReplace = expansions.iterator();
						ArrayList<Production> newProductions = new ArrayList<Production>();
						while(iterReplace.hasNext())
						{
							Production sample = productions.get(iterReplace.next());
							Symbol newProductionLHS = NT[i];
							ArrayList<Symbol> newProductionRHS = sample.getRightHandSide();
							ArrayList<Symbol> oldProductionRHS = (ArrayList<Symbol>) toBeReplaced.getRightHandSide().clone();
							oldProductionRHS.remove(0);
							newProductionRHS.addAll(oldProductionRHS);
							Production newProduction = new Production(newProductionLHS, newProductionRHS);
							newProductions.add(newProduction);
							System.out.println(newProduction);
						}
						productions.addAll(newProductions);
						Printer.print(productions);
					}
					
				}
			}
		}
	}
	
	private ArrayList<Integer> findProduction(Symbol lhs)
	{
		ArrayList<Integer> matches = new ArrayList<Integer>();
		
		for(int i=0; i<productions.size(); i++)
		{
			Production p = productions.get(i);
			if(p.getLeftHandSide().equals(lhs))
			{
				matches.add(i);
			}
		}
		
		return matches;
	}
	
	/**
	 * return the index of the field productions which ai = lhs, ai = first of rhs
	 * @param ai
	 * @param aj
	 * @return -1 if production not found
	 */
	private ArrayList<Integer> findProduction(Symbol ai, Symbol aj)
	{
		ArrayList<Integer> matches = new ArrayList<Integer>();
		
		for(int i=0; i<productions.size(); i++)
		{
			Production p = productions.get(i);
			if(p.getLeftHandSide().equals(ai))
			{
				if(p.getRightHandSide(0).equals(aj))
				{
					matches.add(i);
				}
			}
		}
		
		return matches;
	}
	
	private Symbol[] getNonTerminalSymbols(HashMap<String, Symbol> nonTerminalSymbols)
	{
		Symbol [] nonTerminals = new Symbol [nonTerminalSymbols.size()];
		
		Iterator<String> iter = nonTerminalSymbols.keySet().iterator();
		int index = 0;
		
		while (iter.hasNext())
		{
			String key = iter.next();
			Symbol temp = nonTerminalSymbols.get(key);
			nonTerminals[index] = temp;
			index++;
		}
		
		return nonTerminals;
	}

}
