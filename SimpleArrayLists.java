import acm.program.*;
import java.util.*;

public class SimpleArrayLists extends ConsoleProgram {
	public void run() {
		/* ArrayLists are too awesome to have small fonts! */
		setFont("DejaVuSerif-BOLD-24");
		
		/* Create a list of all input we'll read in. */
		ArrayList<String> list = new ArrayList<String>();
		
		/* Read data until we get the empty string. */
		while (true) {
			String line = readLine("Enter a string: ");
			if (line.equals("")) break;
			
			list.add(line);
		}
		
		/* Output all the strings we read. */ 
		for (int i = 0; i < list.size(); i++) {
			println("Line #" + i + " is " + list.get(i));
		}
	}
}
