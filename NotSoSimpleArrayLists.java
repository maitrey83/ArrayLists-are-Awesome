/*
 * File: NotSoSimpleArrayLists.java
 * ===============================================================
 * A program that shows off ArrayList and wrapper objects.
 */

import acm.program.*;
import java.util.*;

public class NotSoSimpleArrayLists extends ConsoleProgram {
	public void run() {
		setFont("DejaVuSerif-BOLD-24");
		
		ArrayList<int> list = new ArrayList<int>();
		
		while (true) {
			int value = readInt("Enter an int: ");
			if (value == 0) break;
			
			list.add(value);
		}
		
		/* Print out the numbers that were entered. */
		for (int i = 0; i < list.size(); i++) {
			println("Entry #" + i + ": " + list.get(i));
		}
	}
}
