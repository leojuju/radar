package infrastruction.imageLog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import infrastruction.properties.Stations;

public class StationsUtils {
	
	public static Map<String,String> findAllStation(){
		File dir = new File(FileLogger.LogSavePath);
		Map<String,String> map = new HashMap<>();
		if(dir.exists()){
			File[] stationDirs = dir.listFiles();
			for (File stationDir:stationDirs) {
				String stationID = stationDir.getName();
				map.put(stationID, getStationName(stationID));
			}
		}
		return map;
	}
	
	
	public static String getStationName(String stationID){
		return Stations.stationNames.get(stationID);
	}
	
	public static String getStationID(String stationName){
		return Stations.stationIDs.get(stationName);
	}


	public static Map<String, List<String>> findStationLogsOn(String date) {
		File dir = new File(FileLogger.LogSavePath);
		Map<String,List<String>> map = new HashMap<>();
		if(dir.exists()){
			File[] stationDirs = dir.listFiles();
			for (File stationDir:stationDirs) {
				String stationID = stationDir.getName();
				String logsFilePath = FileLogger.LogSavePath+stationID 
						+"/"+date.substring(0, 6)+"/"+stationID+"-"+date+".log";
				File logsFile = new File(logsFilePath);
				List<String> list = null;
				if(logsFile.exists()){
					list = StationUtils.getLogEntrys(logsFile);
				}else{
					list = new ArrayList<>();
				}
				map.put(stationID, list);
			}
		}
		return map;
	}
}
