import java.util.ArrayList;


public class ProductionSet {
	
	private ArrayList<Token> tokens;
	
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

}
