package guess.vendor.bwin;

import internet.WebResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dom.Game;

import tools.Debug;
import tools.Distance;

public class BWin extends Debug {

	public static String url1 = "https://www.bwin.com/de/betViewIframe.aspx?SportID=4&bv=bb&selectedLeagues=0";

	Pattern rows = Pattern.compile(
			"<tr class=\"normal\">.*?</tr>.*?<!-- /entry -->", Pattern.DOTALL);

	Pattern cols = Pattern.compile("<td.*?>.*?</td>", Pattern.DOTALL);

	public static void main(String[] args) {
		BWin w = new BWin();
		List<Map<String, String>> predictions = w.process();
		for (Map<String, String> p : predictions) {
			for (String k : p.keySet()) {
				System.out.println(k + ": " + p.get(k));
			}
			System.out.println();
		}
	}

	public double odd(Game g) {
		List<Map<String, String>> odds = process();

		print("\n" + g);
		Map<String, String> minimal = null;
		int min = 1000;
		for (Map<String, String> m : odds) {
			int d = Distance.prune(g, m.get("Team1"), m.get("Team2"));
			// System.out.println(" "+d+" "+m);
			if (d < min) {
				min = d;
				minimal = m;
			}
		}
		print(" minimal:(" + min + ") " + minimal);
		//System.out.println(" minimal:(" + min + ") " + minimal);

		double odd = new Double(minimal.get("odd2"))
				- new Double(minimal.get("odd1"));

		return odd;
	}

	public List<Map<String, String>> process() {
		List<Map<String, String>> list = new Vector<Map<String, String>>();

		WebResult result = WebResult.load("db/bwin.glob", url1);
		// debugSave(result.content);

		Matcher matcher = rows.matcher(result.content);

		while (matcher.find()) {
			String row = matcher.group();
			if (debug)
				print(row);
			Matcher cm = cols.matcher(row);

			Map<String, String> item = new HashMap<String, String>();
			int i = 0;
			while (cm.find()) {
				String label = "";
				label = i == 0 ? "Uhrzeit" : label;
				label = i == 1 ? "Team1" : label;
				label = i == 2 ? "odd1" : label;
				label = i == 3 ? "" : label;
				label = i == 4 ? "odd_draw" : label;
				label = i == 5 ? "Team2" : label;
				label = i == 6 ? "odd2" : label;

				String col = cm.group().replaceAll("\\<.*?>", "").trim()
						.replaceAll("\\&.*?;", "");
				print(" " + label + "(" + i + "):\t" + col);

				if (label.length() > 0) {
					item.put(label, col);
				}

				i++;
			}
			list.add(item);
		}

		return list;
	}
}
