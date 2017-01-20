package application.api.imageLog;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import infrastruction.TimeUtils;
import infrastruction.imageLog.StationsUtils;

@Path("/")
public class StationsApi {
	@Path("stations")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllStations(){
		Map<String,String> map = StationsUtils.findAllStation();
		Set<String> stationIDs = map.keySet();
		StringBuffer result = new StringBuffer();
		result.append("{");
		result.append("\"num\":"+stationIDs.size()+",");
		result.append("\"map\":{");
		for (String stationID:stationIDs) {
			result.append("\""+stationID+"\":\""+map.get(stationID)+"\",");
		}
		if(!stationIDs.isEmpty()){
			result.deleteCharAt(result.length()-1);
		}
		result.append("}");
		result.append("}");
		return result.toString();
	}
	
	
	@Path("realtime_stations")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getRealTimeStations(){
		String time = TimeUtils.getRealTimeOnGMT();
		if(time==null){
			System.out.println("Get RealTime from web failed,use local system time");
			time = TimeUtils.getRealTimeOnGMTFromLocalSystem();
		}
		Map<String,List<String>> map = StationsUtils.findStationLogsOn(time.substring(0, 8));
		Set<String> stationIDs = map.keySet();
		StringBuffer result = new StringBuffer();
		result.append("{");
		result.append("\"num\":"+stationIDs.size()+",");
		result.append("\"time\":"+time.substring(0, 12)+",");
		result.append("\"map\":{");
		for (String stationID:stationIDs) {
			result.append("\""+stationID+"\":{");
			result.append("\"stationName\":\""+StationsUtils.getStationName(stationID)+"\",");
			result.append("\"list\":[");
			List<String> logs = map.get(stationID);
			for(String log:logs){
				result.append(log+",");
			}
			if(!logs.isEmpty()){
				result.deleteCharAt(result.length()-1);
			}
			result.append("]},");
		}
		if(!stationIDs.isEmpty()){
			result.deleteCharAt(result.length()-1);
		}
		result.append("}");
		result.append("}");
		return result.toString();
	}
}
