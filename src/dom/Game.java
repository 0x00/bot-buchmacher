package dom;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Game implements Serializable {
	private static final long serialVersionUID = 1L;

	public String _id;
	public long id;
	public String season;
	public String location;
	public int viewers;

	public String date;
	public String group;

	public int guestGoals;
	public long guestId;
	public String guestName;

	public int hostGoals;
	public long hostId;
	public String hostName;

	public double w = 1;

	public Date date() {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sd.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String toString() {
		return id + "\t" + date + "\t" + hostName + " vs " + guestName + " \t"
				+ "(" + hostGoals + ":" + guestGoals + ") " + group + " "
				+ season + " " + location + " " + viewers;
	}
	
	public String classifyForHost(){
		
		if(hostGoals>guestGoals){
			return "win";
		}
		
		if(hostGoals<guestGoals){
			return "fail";
		}

		if(hostGoals==guestGoals){
			return "draw";
		}
		
		return "";
	}
	public String classifyForGuest(){
		
		if(hostGoals>guestGoals){
			return "fail";
		}
		
		if(hostGoals<guestGoals){
			return "win";
		}

		if(hostGoals==guestGoals){
			return "draw";
		}
		
		return "";
	}

}
