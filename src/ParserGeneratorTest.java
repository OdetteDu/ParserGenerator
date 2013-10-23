import static org.junit.Assert.*;

import org.junit.Test;


public class ParserGeneratorTest {

//	@Test
//	public void testHelpTag() throws InvalidCommandLineArgumentException, IllegalCharacterException, ParseException {
//		String [] args = {"-?"};
//		ParserGenerator.main(args);
//	}
	
	@Test
	public void testTest() throws InvalidCommandLineArgumentException, IllegalCharacterException, ParseException {
		String [] args = {"-d", "test/test"};
		ParserGenerator.main(args);
	}

}
