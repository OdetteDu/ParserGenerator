import java.util.ArrayList;


public class Production {
	
	private Symbol leftHandSide;
	private ArrayList<Symbol> rightHandSide;
	
	public Production(Symbol leftHandSide, ArrayList<Symbol> rightHandSide)
	{
		this.leftHandSide = leftHandSide;
		this.rightHandSide = rightHandSide;
	}
	
	public String toString()
	{
		String s = leftHandSide.getValue() + "->";
		for (int i=0; i<rightHandSide.size(); i++)
		{
			s += rightHandSide.get(i).getValue() + " ";
		}
		return s;
	}

}
