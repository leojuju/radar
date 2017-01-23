package infrastruction.radarprocess;


public class ImageInfo {
	
	public Time time;
	public int range;
	public String angleInfo;
	
	public ImageInfo(Time time, int range, String angleInfo) {
		super();
		this.time = time;
		this.range = range;
		this.angleInfo = angleInfo;
	}

	public ImageInfo() {
		super();
	}

	public long getTimeMills() {
		return time.getTimeMills();
	}	
	
	public String getString(){
		return time.getString()+"-"+range+"-"+angleInfo+".gif";
	}
	
}
