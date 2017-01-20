package application.api.imageLog;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import infrastruction.imageLog.StationUtils;
import infrastruction.imageLog.StationsUtils;


@Path("station")
public class StationApi {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getLogDirs(@QueryParam("stationID") String stationID){
		List<String> dirs = StationUtils.findStationLogDirs(stationID);
		StringBuffer result = new StringBuffer();
		result.append("{");
		result.append("\"num\":"+dirs.size()+",");
		result.append("\"stationName\":\""+StationsUtils.getStationName(stationID)+"\",");
		result.append("\"list\":[");
		for (String dir:dirs) {
			result.append("\""+dir+"\",");
		}
		if(!dirs.isEmpty()){
			result.deleteCharAt(result.length()-1);
		}
		result.append("]");
		result.append("}");
		return result.toString();
	}
}
