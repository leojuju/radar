package infrastruction.radarprocess;

import infrastruction.redarprocess.geo.LatAndLongt;

public class Station {
	
	public String name;
	public String province;
	public String stationID;
	public LatAndLongt location;
	public int range;
	
	@Override
	public String toString() {
		return "Station [name=" + name + ", province=" + province + ", stationID=" + stationID + ", location="
				+ location +", range=" + range + "]";
	}
}
