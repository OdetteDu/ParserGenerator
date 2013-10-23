import java.util.ArrayList;


public class ProductionSet {
	
	private ArrayList<Token> tokens;
	private Symbol leftHandSide;
	private ArrayList<Production> productions;
	
	public ProductionSet(ArrayList<Token> tokens)
	{
		this.tokens = tokens;
	}
	
	public String toString()
	{
		String s="";
		for (int i=0; i<tokens.size(); i++)
		{
			s+=tokens.get(i);
		}
		return s;
	}
	
	public Token getToken(int index)
	{
		return tokens.get(index);
	}
	
	public int getTokenCount()
	{
		return tokens.size();
	}

	public Symbol getLeftHandSide() {
		return leftHandSide;
	}

	public void setLeftHandSide(Symbol leftHandSide) {
		this.leftHandSide = leftHandSide;
	}

	public ArrayList<Production> getProductions() {
		return productions;
	}

	public void setProductions(ArrayList<Production> productions) {
		this.productions = productions;
	}

}
