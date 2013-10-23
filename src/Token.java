
public class Token {
	
	public enum Type {SEMICOLON, DERIVES, ALSODERIVES, EPSILON, SYMBOL, EOF};
	
	private Type type;
	private String value;
	
	private Token(Type type, String value)
	{
		this.type = type;
		this.value = value;
	}
	
	public static Token getToken(Type type)
	{
		if(type != Type.SYMBOL)
		{
			return new Token(type, null);
		}
		else
		{
			return null;
		}
	}
	
	public static Token getToken(Type type, String value)
	{
		if(type == Type.SYMBOL)
		{
			return new Token(type, value);
		}
		else
		{
			return null;
		}
	}
	
	public String toString()
	{
		return "("+type+","+value+")";
	}

	public Type getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	
}
