package guess.vendor.betclic;

import guess.Guess;
import internet.WebResult;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import dom.Game;

import tools.Debug;
import tools.Distance;

public class BetClic extends Debug {

	String src = "http://xml.betclick.com/odds_en.xml";

	/*
	 * String src =
	 * "http://www.bet-at-home.com/oddxml.aspx?lang=en&subscriber=util@0x00.ws";
	 * http://www.sportspunter.com/betting
	 */

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BetClic clic = new BetClic();

		BetClic.debug = true;
		for (Map<String, String> c : clic.process()) {
			System.out.println(c);
		}
	}

	Map<String, String> minimal = null;
	public Guess odd(Game g) {
		List<Map<String, String>> odds = process();

		print("\n" + g);
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
		System.out.println(" minimal:(" + min + ") " + minimal);

		Guess guess = new Guess() {

			@Override
			public int goal1() {
				try {
					return new Integer(minimal.get("goal1"));
				} catch (Exception e) {
					return -1;
				}
			}

			@Override
			public int goal2() {
				try {
					return new Integer(minimal.get("goal2"));
				} catch (Exception e) {
					return -1;
				}
			}
		};

		return guess;
	}

	public List<Map<String, String>> process() {
		List<Map<String, String>> list = new Vector<Map<String, String>>();

		WebResult web = WebResult.load("db/betclick.glob", src);

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(web.content));

		try {
			Document doc = db.parse(is);
			NodeList events = doc.getElementsByTagName("event");
			for (int i = 0; i < events.getLength(); i++) {

				Node eventNode = events.item(i);
				// print(eventNode.getNodeName());
				NamedNodeMap attr = eventNode.getAttributes();
				boolean goOn = false;
				for (int a = 0; a < attr.getLength(); a++) {
					Node at = attr.item(a);
					if (at.getNodeName().equals("name"))
						print(at.getNodeName() + " " + at.getNodeValue());
					if (at.getNodeName().equals("start_date"))
						print(at.getNodeName() + " " + at.getNodeValue());
					if (at.getNodeName().equals("name")
							&& at.getNodeValue().contains("German Bundesliga")) {
						goOn = true;
					}
				}
				if (!goOn) {
					continue;
				}

				NodeList matches = eventNode.getChildNodes();
				for (int j = 0; j < matches.getLength(); j++) {
					Map<String, String> item = new HashMap<String, String>();
					Node c = matches.item(j);
					print("Match " + c.getNodeName() + " " + c.getTextContent());

					attr = c.getAttributes();
					for (int a = 0; a < attr.getLength(); a++) {
						Node at = attr.item(a);

						if (at.getNodeName().equals("name")) {
							item.put("Team1",
									at.getNodeValue().split("-")[0].trim());
							item.put("Team2",
									at.getNodeValue().split("-")[1].trim());
						}

						print("   " + at.getNodeName() + " "
								+ at.getNodeValue());
					}

					NodeList betsA = c.getChildNodes();
					NodeList bets = betsA.item(0).getChildNodes();
					for (int b = 0; b < bets.getLength(); b++) {
						boolean scoring = false;

						Node bet = bets.item(b);
						print("      " + bet.getNodeName());
						attr = bet.getAttributes();
						for (int a = 0; a < attr.getLength(); a++) {
							Node at = attr.item(a);

							print("        " + at.getNodeName() + " "
									+ at.getNodeValue());

							if (at.getNodeValue().equals("Correct Score")) {
								scoring = true;
							}
						}
						NodeList choices = bet.getChildNodes();
						double min = 1000;
						for (int choiceI = 0; choiceI < choices.getLength(); choiceI++) {
							Node choice = choices.item(choiceI);
							attr = choice.getAttributes();
							print("          " + choice.getNodeName());

							if (scoring) {
								String goals = "";
								for (int a = 0; a < attr.getLength(); a++) {
									Node at = attr.item(a);
									print("           " + at.getNodeName()
											+ " " + at.getNodeValue());

									if (at.getNodeName().equals("name")) {
										goals = at.getNodeValue();
									}

									if (at.getNodeName().equals("odd")) {
										if (min > (new Double(at.getNodeValue()))) {
											item.put("odd", at.getNodeValue());
											item.put("goal1",
													goals.split("-")[0].trim());
											item.put("goal2",
													goals.split("-")[1].trim());
											min = new Double(at.getNodeValue());
										}
									}
								}
							}
						}
					}
					list.add(item);
				}
			}

		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}
}
