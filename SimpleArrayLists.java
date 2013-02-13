import acm.program.*;
import java.util.*;

public class SimpleArrayLists extends ConsoleProgram {
	public void run() {
		/* ArrayLists are too awesome to have small fonts! */
		setFont("DejaVuSerif-BOLD-24");
		
		ArrayList<String> list = new ArrayList<String>();
		
		while (true) {
			String line = readLine("Enter a string: ");
			if (line.equals("")) break;
			
			list.add(line);
		}
		
		for (int i = 0; i < list.size(); i++) {
			println("Line #" + i + " is " + list.get(i));
		}
	}
}
