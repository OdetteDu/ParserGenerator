import static org.junit.Assert.*;

import org.junit.Test;


public class ParserGeneratorTest {

//	@Test
//	public void testHelpTag() throws InvalidCommandLineArgumentException, IllegalCharacterException, ParseException {
//		String [] args = {"-?"};
//		ParserGenerator.main(args);
//	}
	
	@Test
	public void testCEGLR() throws InvalidCommandLineArgumentException, IllegalCharacterException, ParseException, NotLLOneGrammarException {
		String [] args = {"-d", "test/CEG-LR"};
		ParserGenerator.main(args);
	}
	
	@Test
	public void testCEGRR() throws InvalidCommandLineArgumentException, IllegalCharacterException, ParseException, NotLLOneGrammarException {
		String [] args = {"-d", "test/CEG-RR"};
		ParserGenerator.main(args);
	}
	
	@Test
	public void testFactor() throws InvalidCommandLineArgumentException, IllegalCharacterException, ParseException, NotLLOneGrammarException {
		String [] args = {"-d", "test/Factor-LL1-RR"};
		ParserGenerator.main(args);
	}
	
	@Test
	public void testInvocation() throws InvalidCommandLineArgumentException, IllegalCharacterException, ParseException, NotLLOneGrammarException {
		String [] args = {"-d", "test/Invocation-nonLL1"};
		ParserGenerator.main(args);
	}
	
	@Test
	public void testParens() throws InvalidCommandLineArgumentException, IllegalCharacterException, ParseException, NotLLOneGrammarException {
		String [] args = {"-d", "test/Parens"};
		ParserGenerator.main(args);
	}
	
	@Test
	public void testParensAlt() throws InvalidCommandLineArgumentException, IllegalCharacterException, ParseException, NotLLOneGrammarException {
		String [] args = {"-d", "test/Parens-Alt"};
		ParserGenerator.main(args);
	}
	
	@Test
	public void testSNnonLL1RR() throws InvalidCommandLineArgumentException, IllegalCharacterException, ParseException, NotLLOneGrammarException {
		String [] args = {"-d", "test/SN-nonLL1-RR"};
		ParserGenerator.main(args);
	}
	
	@Test
	public void testSNRRLL1() throws InvalidCommandLineArgumentException, IllegalCharacterException, ParseException, NotLLOneGrammarException {
		String [] args = {"-d", "test/SN-RR-LL1"};
		ParserGenerator.main(args);
	}

}
