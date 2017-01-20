package infrastruction.radarprocess.geo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import infrastruction.radarprocess.Stations;

public class GeoProcessForRadarImage {			
	
	public static List<String> getNearbyStations(LatAndLongt startLAL,LatAndLongt endLAL){
		List<String> results = new ArrayList<String>();
		LatAndLongt centerLAL = new LatAndLongt((startLAL.latitude+endLAL.latitude)/2, (startLAL.longtitude+endLAL.longtitude)/2);
		double limit = LatAndLongtUtils.getDistanceOnEarth(startLAL, endLAL)/2;
		Set<String> stationIDs = Stations.stations.keySet();
		for(String stationID:stationIDs){
			LatAndLongt station =  Stations.stations.get(stationID).location;
			double d = LatAndLongtUtils.getDistanceOnEarth(centerLAL, station);
			if(d<limit+Stations.stations.get(stationID).range){
				results.add(stationID);
			}
		}
		return results;
	}
	
}
