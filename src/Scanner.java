import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;


public class Scanner {
	
	private ArrayList<Token> tokens;
	
	public Scanner()
	{
		 tokens = new ArrayList<Token>();
	}
	
	public ArrayList<Token> scan(BufferedReader reader) throws InvalidCommandLineArgumentException, IllegalCharacterException
	{
		try 
		{
			String nextLine = reader.readLine();
			while(nextLine != null)
			{
				scanLine(nextLine);
				nextLine = reader.readLine();
			}
			
		} catch (IOException e) 
		{
			throw new InvalidCommandLineArgumentException("The file is unavailable to open correctly. ");
		}
		
		return tokens;
	}
	
	private void scanLine(String currentLine) throws IllegalCharacterException
	{
		String temp="";
		
		for(int i=0; i<currentLine.length(); i++)
		{
			char c=currentLine.charAt(i);
			if(c==' ' || c=='\n' || c==-1)
			{
				Token token = stringToToken(temp);
				if(token != null)
				{
					tokens.add(token);
				}
				temp = "";
			}
			else if(c==';')
			{
				Token token = stringToToken(temp);
				if(token != null)
				{
					tokens.add(token);
				}
				temp = "";
				tokens.add(Token.getToken(Token.Type.SEMICOLON));
			}
			else if(c==':')
			{
				Token token = stringToToken(temp);
				if(token != null)
				{
					tokens.add(token);
				}
				temp = "";
				tokens.add(Token.getToken(Token.Type.DERIVES));
			}
			else if(c=='|')
			{
				Token token = stringToToken(temp);
				if(token != null)
				{
					tokens.add(token);
				}
				temp = "";
				tokens.add(Token.getToken(Token.Type.ALSODERIVES));
			}
			else
			{
				temp += c;
			}
		}
		
		Token token = stringToToken(temp);
		if(token != null)
		{
			tokens.add(token);
		}
	}
	
	private Token stringToToken(String s) throws IllegalCharacterException
	{
		if(s.equals(""))
		{
			return null;
		}
		else if(s.equals("EPSILON") || s.equals("epsilon") || s.equals("Epsilon"))
		{
			return Token.getToken(Token.Type.EPSILON);
		}
		else
		{
			for(int i=0; i<s.length(); i++)
			{
				char c = s.charAt(i);
				if(!((c >= 'a' && c <= 'z') || ( c >= 'A' && c <= 'Z' ) || ( c >= '0' && c <= '0' )))
				{
					throw new IllegalCharacterException(s,c);
				}
			}
			
			return Token.getToken(Token.Type.SYMBOL, s);
		}
	}

}
