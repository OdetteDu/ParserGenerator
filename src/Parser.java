import java.util.ArrayList;

public class Parser {
	
	public Parser()
	{
		
	}
	
	public void parse(ArrayList<ProductionSet> productionSets)
	{
		for (int i=0; i<productionSets.size(); i++)
		{
			parseProductionSet(productionSets.get(i));
		}
	}
	
	private void parseProductionSet(ProductionSet productionSet)
	{
		
	}

}
