package fail;

import guess.Guess;
import guess.vendor.betclic.BetClic;
import guess.vendor.bwin.BWin;
import internet.WebResult;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import store.DomMap;
import de.botliga.Post;
import dom.Game;
import dom.sort.Time;

public class BuchmacherBot {

	public static String matches2012 = "http://botliga.de/api/matches/2012";

	public static void main(String[] args) {

		WebResult res2012 = WebResult.load("db/test_2012", matches2012);
		List<Game> botligaGames = DomMap.games(res2012.content);
		Collections.sort(botligaGames, new Time());

		System.out.println("This is buchmacher bot. Next 10 games:\n");
		int next = 10;
		for (Game g : botligaGames) {
			if (g.date().after(new Date())) {
				new BetClic().odd(g);
				System.out.println(g+"\n");
				next--;
			}
			if (next <= 0)
				break;
		}

		System.out.println("\nBotliga:");
		for (Game g : botligaGames) {

			Calendar nowc = Calendar.getInstance();
			// nowc.set(2012, 8 - 1, 23, 0, 0, 0);
			nowc.setTime(new Date());
			long nowm = nowc.getTimeInMillis();

			Calendar thenc = Calendar.getInstance();
			Date then = g.date();
			thenc.setTime(then);
			long thenm = thenc.getTimeInMillis();

			long diff = (thenm - nowm) / 1000 / 60 / 60;
			if (diff > 1 && diff < 24 * 2) {
				double odd = new BWin().odd(g);
				int g1 = odd > 0 ? 2 : 0;
				int g2 = odd < 0 ? 1 : 1;

				Guess betclicodd = new BetClic().odd(g);

				if (betclicodd.goal1() != -1) {
					g1 = betclicodd.goal1();
					g2 = betclicodd.goal2();
				}

				System.out.println("Posting next match: " + g + " odd(" + odd
						+ ", " + g1 + ":" + g2 + ")");
				Post.go(g.id, g1, g2);
				System.out.println();
			}

		}
	}

}
