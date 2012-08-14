package de.botliga;

import internet.Http;
import internet.WebResult;

public class Post {
	public static String token = "hellow";
	
	static String url = "http://botliga.de/api/guess?";
	//static String url = "http://0x00.ws?";
	static String rawparam = "match_id=%s&result=%s:%s&token="+token;
	
	public static void go(long id, int a, int b){
		
		String param = String.format(rawparam, id,a,b);
		WebResult res = Http.fetch(url+param, "POST");
		System.out.println(res.url);
		System.out.println(" "+res.content);
	}
	
	public static void main(String args[]){
		Post.go(19702, 2, 1);
	}
}
