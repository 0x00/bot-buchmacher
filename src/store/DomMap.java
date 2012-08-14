package store;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dom.Game;

public class DomMap {

	public static List<Game> games(String json) {

		List<Game> entries = new Gson().fromJson(json,
				new TypeToken<List<Game>>() {
				}.getType());


		return entries;
	}

}
