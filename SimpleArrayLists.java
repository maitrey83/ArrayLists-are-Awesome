import acm.program.*;
import java.util.*;

public class SimpleArrayLists extends ConsoleProgram {
	public void run() {
		/* ArrayLists are too awesome to have small fonts! */
		setFont("DejaVuSerif-BOLD-24");
		
		ArrayList<String> lines = new ArrayList<String>();
		
		while (true) {
			String line = readLine("Enter something: ");
			
			if (line.equals(""))
				break;
			
			lines.add(line);
		}
		
		/* Print out what we've read. */
		for (int i = 0; i < lines.size(); i++) {
			println("Line #" + i + " = " + lines.get(i));
		}
	}
}
