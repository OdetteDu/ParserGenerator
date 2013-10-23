import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class Printer<E> {

	public static void print(ArrayList<?> toBePrinted)
	{
		for (int i=0; i<toBePrinted.size(); i++)
		{
			System.out.println(toBePrinted.get(i));
		}
		
		System.out.println();
	}
	
	public static void print(HashMap<?, ?> toBePrinted)
	{
		Iterator<?> iter = toBePrinted.keySet().iterator();
		while (iter.hasNext())
		{
			Object key = iter.next();
			System.out.println(key+": "+toBePrinted.get(key));
		}
		
		System.out.println();
	}
}
