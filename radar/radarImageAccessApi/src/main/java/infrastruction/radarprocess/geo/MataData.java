package infrastruction.radarprocess.geo;

public class MataData {
	
	public String stationID;
	public int w;
	public int h;
	public double latScale;
	public double longtScale;
	public LatAndLongt startLAL;
	public LatAndLongt endLAL;
	
	public MataData(String stationID, int w, int h, double latScale, double longtScale, LatAndLongt startLAL,
			LatAndLongt endLAL) {
		super();
		this.stationID = stationID;
		this.w = w;
		this.h = h;
		this.latScale = latScale;
		this.longtScale = longtScale;
		this.startLAL = startLAL;
		this.endLAL = endLAL;
	}

	public static String fieldNamesToString() {
		return "stationID,w,h,latScale,longtScale,startLat,startLongt,endLat,endLongt";
	}
}
