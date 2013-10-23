
public class Symbol {
	
	public enum Type { NONTERMINAL, TERMINAL };
	private Type type;
	private String value;
	
	public Symbol(Type type, String value)
	{
		this.type = type;
		this.value = value;
	}
	
	public Symbol(String value)
	{
		this(null, value);
	}
	
	public Type getType() 
	{
		return type;
	}
	
	public void setType(Type type)
	{
		this.type = type;
	}
	
	public String getValue() 
	{
		return value;
	}

}
