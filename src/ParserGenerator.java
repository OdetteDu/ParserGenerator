import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class ParserGenerator {
	
	private String inputGrammarPath;
	
	public ParserGenerator(String inputGrammarPath)
	{
		this.inputGrammarPath=inputGrammarPath;
	}
	
	public void run() throws InvalidCommandLineArgumentException, IllegalCharacterException, ParseException
	{
		BufferedReader bufferedFileReader = openFile(inputGrammarPath);
		Scanner scanner = new Scanner();
		ArrayList<ProductionSet> productionSets=scanner.scan(bufferedFileReader);
		Parser parser = new Parser();
		ArrayList<Production> productions = parser.parse(productionSets);
		Printer.print(productions);
		TableGenerator generator = new TableGenerator(productions, 
				parser.getTerminalSymbols(), parser.getNonTerminalSymbols());
		generator.generate();
	}
	
	
	
	private BufferedReader openFile(String filePath) throws InvalidCommandLineArgumentException
	{
		try
		{
			FileReader fr=new FileReader(filePath);
			BufferedReader br=new BufferedReader(fr);
			return br;
		} 
		catch (FileNotFoundException e)
		{
			throw new InvalidCommandLineArgumentException("The filePath is invalid or the file can not be found.");
		}
	}
	
	public String getLLOneTable()
	{
		return null;
	}
	
	public String getProductions()
	{
		return null;	
	}
	
	public String getFirstSets()
	{
		return null;
	}
	
	public String getFollowSets()
	{
		return null;
	}
	
	public String getFirstPlusSets()
	{
		return null;
	}

	public static void main (String [] args) throws InvalidCommandLineArgumentException, IllegalCharacterException, ParseException
	{
		if(args.length==0)
		{
			throw new InvalidCommandLineArgumentException("You need to enter a flag to tell the program what to do. Enter -? to get all the valid command-line flags.");
		}
		else if(args.length==1 && args[0].equals("-?"))
		{
			System.out.println("-t Print the LL(1) table in YAML format\n"+
					"-p Print the productions as recognized by the parser in a human readable form\n"+
					"-f Print the FIRST sets for each grammar symbol in a human readable form\n"+
					"-g Print the FOLLOW sets for each nonterminal in a human readable form\n"+
					"-h Print the FIRST+ sets for each production in a human readable form\n"+
					"-? Print a list of the valid command-line flags");
		}
		else
		{
			String filePath = args[args.length-1];
			if(filePath.charAt(0)=='-')
			{
				throw new InvalidCommandLineArgumentException("You must provide a file of input grammar.");
			}
			
			ParserGenerator parserGenerator = new ParserGenerator(filePath);
			parserGenerator.run();
			
			for( int i=0; i<args.length-1; i++)
			{
				if(args[i].equals("-t"))
				{
					System.out.println(parserGenerator.getLLOneTable());
				}
				else if(args[i].equals("-p"))
				{
					System.out.println(parserGenerator.getProductions());
				}
				else if(args[i].equals("-f"))
				{
					System.out.println(parserGenerator.getFirstSets());
				}
				else if(args[i].equals("-g"))
				{
					System.out.println(parserGenerator.getFollowSets());
				}
				else if(args[i].equals("-h"))
				{
					System.out.println(parserGenerator.getFirstPlusSets());
				}
			}
		}
	}
}
