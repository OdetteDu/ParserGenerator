
public class IllegalCharacterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IllegalCharacterException(String token, char c)
	{
		super("The token \""+token+"\" contains an illegal character: "+c);
	}
}
