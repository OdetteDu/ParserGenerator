import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;




public class LeftRecursionRemover {

	private final HashMap<String, Symbol> nonTerminalSymbols;
	private final ArrayList<Production> productions;

	public LeftRecursionRemover(ArrayList<Production> productions, HashMap<String,Symbol> nonTerminalSymbols)
	{
		this.productions = productions;
		this.nonTerminalSymbols = nonTerminalSymbols;
		Printer.print(productions);
	}

	@SuppressWarnings("unchecked")
	public void removeLeftRecursion() 
	{
		Symbol[] NT = getNonTerminalSymbols(nonTerminalSymbols);

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
						}
						productions.addAll(newProductions);
					}

				}
			}

			//Eliminate direct left recursion for Ai
			ArrayList<Integer> productionsWithAi = findProduction(NT[i]);
			Iterator<Integer> iter = productionsWithAi.iterator();
			ArrayList<Production> oldProductions = new ArrayList<Production>();
			while(iter.hasNext())
			{
				oldProductions.add(productions.get(iter.next()));
			}
			ArrayList<Production> newProductions = transform(oldProductions);
			productions.removeAll(oldProductions);
			productions.addAll(newProductions);
		}
		
		//Printer.print(productions);
		
//		for (int i=0; i<NT.length; i++)
//		{
//			
//		}
		Printer.print(productions);
	}

	@SuppressWarnings("unchecked")
	private ArrayList<Production> transform(ArrayList<Production> oldProductions)
	{
		//find Alpha & Beta
		ArrayList<ArrayList<Symbol>> alpha = new ArrayList<ArrayList<Symbol>>();
		ArrayList<ArrayList<Symbol>> beta = new ArrayList<ArrayList<Symbol>>();

		for (int i=0; i<oldProductions.size(); i++)
		{
			Production p = (Production) oldProductions.get(i);
			if(p.getLeftHandSide().equals(p.getRightHandSide(0)))
			{
				//alpha
				ArrayList<Symbol> temp = (ArrayList<Symbol>) p.getRightHandSide().clone();
				temp.remove(0);
				alpha.add(temp);
			}
			else
			{
				//beta
				beta.add((ArrayList<Symbol>) p.getRightHandSide().clone());
			}
		}

		if(alpha.isEmpty())
		{
			return oldProductions;
		}

		Symbol oldNT = oldProductions.get(0).getLeftHandSide();
		Symbol newNT = new Symbol(Symbol.Type.NONTERMINAL, oldProductions.get(0).getLeftHandSide().getValue()+"Prime");
		nonTerminalSymbols.put(newNT.getValue(), newNT);

		ArrayList<Production> newProductions = new ArrayList<Production>();

		//add Fee -> Beta Fee'
		Iterator<ArrayList<Symbol>> iterBeta = beta.iterator();
		while(iterBeta.hasNext())
		{
			ArrayList<Symbol> b = iterBeta.next();
			b.add(newNT);
			Production newProduction = new Production(oldNT, b);
			newProductions.add(newProduction);
		}

		//add Fee'-> Alpha Fee'
		Iterator<ArrayList<Symbol>> iterAlpha = alpha.iterator();
		while(iterAlpha.hasNext())
		{
			ArrayList<Symbol> a = iterAlpha.next();
			a.add(newNT);
			Production newProduction = new Production(newNT, a);
			newProductions.add(newProduction);
		}

		//add Fee'-> E
		ArrayList<Symbol> e = new ArrayList<Symbol>();
		e.add(Symbol.EPSILON);
		Production newProduction = new Production(newNT, e);
		newProductions.add(newProduction);
		return newProductions;
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

	public ArrayList<Production> getProductions() {
		return productions;
	}

	public HashMap<String, Symbol> getNonTerminalSymbols() {
		return nonTerminalSymbols;
	}

}
