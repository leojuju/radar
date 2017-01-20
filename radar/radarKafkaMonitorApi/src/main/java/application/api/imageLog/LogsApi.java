package application.api.imageLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import infrastruction.imageLog.StationUtils;


@Path("getLogs")
public class LogsApi {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getLogDirs(@QueryParam("stationID")String stationID,@QueryParam("logDir")String logDir){
		Map<String,List<String>> logs = StationUtils.findLogs(stationID,logDir);
		StringBuffer result = new StringBuffer();
		List<String> days = new ArrayList<>(logs.keySet());
		Collections.sort(days);;
		result.append("{");
		result.append("\"num\":"+logs.size()+",");
		result.append("\"map\":{");
		for (String day:days) {
			result.append("\""+day+"\":");
			result.append("[");
			List<String> logEntrys = logs.get(day);
			Collections.sort(logEntrys);
			for(String logEntry:logEntrys){
				result.append(logEntry+",");
			}
			result.deleteCharAt(result.length()-1);
			result.append("]");
			result.append(",");
		}
		result.deleteCharAt(result.length()-1);
		result.append("}");
		result.append("}");
		return result.toString();
	}
}
