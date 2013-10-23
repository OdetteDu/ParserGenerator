import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
	
	public static Symbol EOF = new Symbol(Symbol.Type.TERMINAL, "EOF");
	public static Symbol EPSILON = new Symbol(Symbol.Type.TERMINAL, "EPSILON");
	
	private HashMap<String, Symbol> nonTerminalSymbols;
	private HashMap<String, Symbol> terminalSymbols;
	
	public Parser()
	{
		nonTerminalSymbols = new HashMap<String, Symbol>();
		terminalSymbols = new HashMap<String, Symbol>();
		terminalSymbols.put(EOF.getValue(), EOF);
	}
	
	public ArrayList<Production> parse(ArrayList<ProductionSet> productionSets) throws ParseException
	{
		ArrayList<Production> productions = new ArrayList<Production>();
		
		//parse production set
		for (int i=0; i<productionSets.size(); i++)
		{
			productions.addAll(parseProductionSet(productionSets.get(i)));
		}
		
		//sets symbol type
		for (int i=0; i<productions.size(); i++)
		{
			terminalSymbols.putAll(productions.get(i).assignSymbolType(nonTerminalSymbols));
		}
		
		Printer.print(nonTerminalSymbols);
		Printer.print(terminalSymbols);
		
		return productions;
	}
	
	private ArrayList<Production> parseProductionSet(ProductionSet productionSet) throws ParseException
	{
		ArrayList<Production> productions = new ArrayList<Production>();
		
		if(productionSet.getTokenCount() < 4)
		{
			throw new ParseException("There are less than 4 tokens in this production set");
		}
		
		Token token = productionSet.getToken(0);
		if(token.getType() == Token.Type.SYMBOL)
		{
			Symbol leftHandSide = new Symbol(Symbol.Type.NONTERMINAL, token.getValue());
			productionSet.setLeftHandSide(leftHandSide);
			nonTerminalSymbols.put(token.getValue(), leftHandSide);
			
			token = productionSet.getToken(1);
			if(token.getType() == Token.Type.DERIVES)
			{
				ArrayList<Token> rightHandSide = new ArrayList<Token>();
				for(int i=2; i<productionSet.getTokenCount(); i++)
				{
					token = productionSet.getToken(i);
					if(token.getType() == Token.Type.ALSODERIVES)
					{
						//handle right hand side
						productions.add(parseRightHandSide(leftHandSide, rightHandSide));
						rightHandSide = new ArrayList<Token>();
					}
					else if(token.getType() == Token.Type.SEMICOLON)
					{
						productions.add(parseRightHandSide(leftHandSide, rightHandSide));
						//end of the production set
						if(i < productionSet.getTokenCount()-1)
						{
							throw new ParseException("There are extra token in this production set: "+productionSet.getToken(i+1));
						}
					}
					else
					{
						rightHandSide.add(token);
					}
				}
			}
			else
			{
				throw new ParseException("The second token of the production set is not a derive");
			}
		}
		else
		{
			throw new ParseException("The first token of the production set is not a symbol");
		}
		
		productionSet.setProductions(productions);
		return productions;
	}
	
	private Production parseRightHandSide(Symbol leftHandSide, ArrayList<Token> rightHandSide) throws ParseException
	{
		if (rightHandSide.size() == 0)
		{
			throw new ParseException("The right hand side is empty");
		}
		
		if (rightHandSide.get(0).getType() == Token.Type.EPSILON)
		{
			if(rightHandSide.size() != 1 )
			{
				throw new ParseException("There are extra tokens in right hand side followed by an epsilon");
			}
			else
			{
				ArrayList<Symbol> symbols = new ArrayList<Symbol>();
				symbols.add(EPSILON);
				return new Production(leftHandSide, symbols);
			}
		}
		else 
		{
			return new Production(leftHandSide, parseSymbolList(rightHandSide));
		}
	}
	
	private ArrayList<Symbol> parseSymbolList(ArrayList<Token> symbolList) throws ParseException
	{
		ArrayList<Symbol> symbols = new ArrayList<Symbol>();
		
		if (symbolList.size() == 1)
		{
			symbols.add(parseSymbol(symbolList.get(0)));
		}
		else
		{
			symbols.add(parseSymbol(symbolList.remove(0)));
			symbols.addAll(parseSymbolList(symbolList));
		}
		return symbols;
	}
	
	private Symbol parseSymbol(Token symbol) throws ParseException
	{
		if(symbol.getType() != Token.Type.SYMBOL)
		{
			throw new ParseException("Unexpected token type, should be type symbol, actually type "+symbol.getType());
		}
		
		return new Symbol(symbol.getValue());
	}

}
