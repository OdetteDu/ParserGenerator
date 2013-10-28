import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class Printer<E> {

	public static void print(ArrayList<?> toBePrinted)
	{
		for (int i=0; i<toBePrinted.size(); i++)
		{
			System.out.println(i+": "+toBePrinted.get(i));
		}
		
		System.out.println();
	}
	
	public static void print(String name, ArrayList<?> toBePrinted)
	{
		System.out.println(name);
		print(toBePrinted);
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
	
	public static void print(String name, HashMap<?, ?> toBePrinted)
	{
		System.out.println(name);
		print(toBePrinted);
	}
	
	public static void print(Symbol [] toBePrinted)
	{
		for (int i=0; i<toBePrinted.length; i++)
		{
			System.out.println(i+": "+toBePrinted[i]);
		}
		System.out.println();
	}
}
