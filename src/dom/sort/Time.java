package dom.sort;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import dom.Game;

public class Time implements Comparator<Game> {

	@Override
	public int compare(Game o1, Game o2) {

		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date d1 = f.parse(o1.date.replace("T", " "));
			Date d2 = f.parse(o2.date.replace("T", " "));

			return (int) (d2.getTime() / 1000 - d1.getTime() / 1000);

		} catch (ParseException e) {
			e.printStackTrace();
			System.exit(0);
		}

		return 0;
	}

}
