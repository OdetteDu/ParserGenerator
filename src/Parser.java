import java.util.ArrayList;

public class Parser {
	
	private ArrayList<Token> tokens;
	
	public Parser()
	{
		
	}
	
	public void parse(ArrayList<Token> tokens)
	{
		this.tokens = tokens;
	}

}
