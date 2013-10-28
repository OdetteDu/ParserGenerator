import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class ParserGenerator {

	private String inputGrammarPath;
	BufferedReader bufferedFileReader;
	Scanner scanner;
	Parser parser;
	TableGenerator generator;

	public ParserGenerator(String inputGrammarPath)
	{
		this.inputGrammarPath=inputGrammarPath;
	}

	public void run(boolean shouldRemoveLeftRecursion) throws InvalidCommandLineArgumentException, IllegalCharacterException, ParseException, NotLLOneGrammarException
	{
		bufferedFileReader = openFile(inputGrammarPath);
		scanner = new Scanner();
		ArrayList<ProductionSet> productionSets=scanner.scan(bufferedFileReader);
		parser = new Parser();
		ArrayList<Production> productions = parser.parse(productionSets);
		HashMap<String, Symbol> nonTerminalSymbols = parser.getNonTerminalSymbols();

		if(shouldRemoveLeftRecursion && (!parser.getTerminalSymbols().containsKey(Symbol.EPSILON.getValue())))
		{
			LeftRecursionRemover leftRecursionRemover = new LeftRecursionRemover(productions, parser.getNonTerminalSymbols());
			leftRecursionRemover.removeLeftRecursion();

			productions = leftRecursionRemover.getProductions();
			nonTerminalSymbols = leftRecursionRemover.getNonTerminalSymbols();
		}

		generator = new TableGenerator(parser.getStartSymbol(), productions, 
				parser.getTerminalSymbols(), nonTerminalSymbols);
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
		return generator.getLLOneTableInYAML();
	}

	public String getProductions()
	{
		return generator.getProductions();	
	}

	public String getFirstSets()
	{
		return generator.getFirstSets();
	}

	public String getFollowSets()
	{
		return generator.getFollowSets();
	}

	public String getFirstPlusSets()
	{
		return generator.getFirstPlusSets();
	}

	public static void main (String [] args) throws InvalidCommandLineArgumentException, IllegalCharacterException, ParseException, NotLLOneGrammarException
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
					"-a Print everything in a human readable form\n"+
					"-? Print a list of the valid command-line flags");
		}
		else
		{
			boolean shouldRemoveLeftRecursion = false;
			String filePath = "";
			for (int i=0; i<args.length; i++)
			{
				String argument = args[i];
				if (argument.charAt(0) != '-')
				{
					if(filePath.isEmpty())
					{
						filePath = argument;
					}
					else
					{
						throw new InvalidCommandLineArgumentException("There are more than one arguments which isn't a flag in the command line argument. "
								+ "The program can't determine which one is the file path. ");
					}
				}

				if (argument.equals("-r"))
				{
					shouldRemoveLeftRecursion = true;
				}
			}

			if (filePath.isEmpty())
			{
				throw new InvalidCommandLineArgumentException("Did not provide any input file.");
			}

			ParserGenerator parserGenerator = new ParserGenerator(filePath);
			parserGenerator.run(shouldRemoveLeftRecursion);

			for( int i=0; i<args.length; i++)
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
				else if(args[i].equals("-a"))
				{
					System.out.println(parserGenerator.getLLOneTable());
					System.out.println(parserGenerator.getProductions());
					System.out.println(parserGenerator.getFirstSets());
					System.out.println(parserGenerator.getFollowSets());
					System.out.println(parserGenerator.getFirstPlusSets());
				}
				else if(args[i].equals("-?"))
				{
					System.out.println("Help Menu: \n"+
							"-t Print the LL(1) table in YAML format\n"+
							"-p Print the productions as recognized by the parser in a human readable form\n"+
							"-f Print the FIRST sets for each grammar symbol in a human readable form\n"+
							"-g Print the FOLLOW sets for each nonterminal in a human readable form\n"+
							"-h Print the FIRST+ sets for each production in a human readable form\n"+
							"-a Print everything in a human readable form\n"+
							"-? Print a list of the valid command-line flags");
				}

			}
		}
	}
}
