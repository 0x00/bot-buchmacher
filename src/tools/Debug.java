package tools;

import java.io.FileWriter;
import java.io.IOException;

public class Debug {
	public static boolean debug = false;

	public void print(Object n) {
		if (!debug)
			return;
		print(n.toString());
	}

	public void print() {
		if (!debug)
			return;
		System.out.println();
	}

	public void print(String n) {
		if (!debug)
			return;
		System.out.println(n);
	}

	public void debugSave(String c) {
		try {
			FileWriter w = new FileWriter("/Users/f/Desktop/debug.htm");
			w.write(c);
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
