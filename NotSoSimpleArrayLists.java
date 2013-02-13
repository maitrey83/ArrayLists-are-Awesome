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
		
		ArrayList<Integer> lines = new ArrayList<Integer>();
		
		while (true) {
			int value = readInt("Enter a value: ");
			if (value == 0) break;
			
			lines.add(line);
		}
		
		for (int i = 0; i < lines.size(); i++) {
			println("Line #" + i + ": " + lines.get(i));
		}
	}
}
