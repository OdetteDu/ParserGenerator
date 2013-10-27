
public class NotLLOneGrammarException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotLLOneGrammarException()
	{
		super("This grammar is not a LL(1) grammar");
	}

}
