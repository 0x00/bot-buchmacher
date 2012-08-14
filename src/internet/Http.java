package internet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Http {

	public static String agent = "hivemind crawler (http://hivemind.0x00.ws)";

	public static List<String> cookies;

	public static WebResult fetch(String site, String method) {
		String out = "";

		URL url = null;
		try {
			url = new URL(site);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			String useragent = "Internet Explorer";
			connection.setRequestProperty("User-Agent", useragent );
			connection.setRequestMethod(method);
			
			//System.out.println(connection.getResponseCode()+" "+connection.getResponseMessage());
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                  
			String inputLine;
			while ((inputLine = reader.readLine()) != null){
				out += inputLine;
			}
			reader.close();
			

		} catch (IOException e) {
			//e.printStackTrace();
		}
		
		WebResult r = new WebResult();
		r.url = site;
		r.content = out;
		return r;
	}

	public static WebResult fetch(String site) {
		return fetch(site, "GET");
	}
}
