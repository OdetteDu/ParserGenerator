
public class Token {
	
	public enum Type {SEMICOLON, DERIVES, ALSODERIVES, EPSILON, SYMBOL, EOF};
	
	private Type type;
	private String value;

	public Token(Type type, String value)
	{
		this.type = type;
		this.value = value;
	}
	
	public String toString()
	{
		return "("+type+","+value+")";
	}
}
