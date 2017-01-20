package infrastruction.imageLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StationUtils {

	public static List<String> findStationLogDirs(String stationID) {
		File dir = new File(FileLogger.LogSavePath+stationID+"/");
		List<String> list = new ArrayList<>();
		if(dir.exists()){
			File[] stationDirs = dir.listFiles();
			for (File stationDir:stationDirs) {
				list.add(stationDir.getName());
			}
		}
		return list;
	}

	public static Map<String, List<String>> findLogs(String stationID, String logDir) {
		File dir = new File(FileLogger.LogSavePath+stationID+"/"+logDir+"/");
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		if(dir.exists()){
			File[] logFiles = dir.listFiles();
			for (File logFile:logFiles) {
				if(logFile.getName().matches("[\\s\\S]{8,}\\.log")){
					List<String> logEntrys = getLogEntrys(logFile);
					map.put(logFile.getName(), logEntrys);
				}
			}
		}
		return map;
	}

	public static List<String> getLogEntrys(File logFile) {
		BufferedReader br = null;
		List<String> result = new ArrayList<>();
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(logFile)));
			String line = null;
			while((line=br.readLine())!=null){
				result.add(line);
			}
			Collections.sort(result);
		} catch (Exception e) {
			System.out.println("Read logfile "+logFile.getAbsolutePath()+" error.");
		} finally {
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
}
