package infrastruction.radarprocess;

import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Time {
	public int year;
	public int month;
	public int day;
	public int hour;
	public int minute;
	public int second;
	
	public Time(int year, int month, int day, int hour, int minute, int second) {
		super();
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}

	public Time() {
		super();
	}

	public long getTimeMills() {
		GregorianCalendar g = new  GregorianCalendar(year, month-1, day, hour, minute, second);
		g.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		return g.getTimeInMillis();
	}
	
	public String getString(){
		return year+""+(month<10?("0"+month):month)+""+(day<10?("0"+day):day)
				+(hour<10?("0"+hour):hour)+(minute<10?("0"+minute):minute)
				+(second<10?("0"+second):second);
	}
	
	public String getYearMonthString(){
		return year+""+(month<10?("0"+month):month);
	}
	
	public String getDayString(){
		return (day<10?("0"+day):day+"");
	}
	
}
