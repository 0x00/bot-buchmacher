package internet;


import store.Store;

public class WebResult extends Store  {

	private static final long serialVersionUID = 1L;
	public String url;
	public String content;

	public static WebResult load(String filename, String url) {

		WebResult web = (WebResult) load(filename);

		if (web == null) {
			web = Http.fetch(url);
			web.save(filename);
		}

		return web;
	}
}
