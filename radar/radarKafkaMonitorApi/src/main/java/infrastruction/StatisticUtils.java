package infrastruction;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import application.LogCollector;
import infrastruction.db.RadarLogMongoDao;

public class StatisticUtils {

	public static JSONObject getRecentIntervalStatisticData(long currentTime, int interval, int num) {
		JSONObject result = new JSONObject();
		JSONArray timeStmps = new JSONArray();
		JSONObject map = new JSONObject();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(String topic:LogCollector.TOPICS){
			map.put(topic, new JSONArray());
		}
		long startTime = currentTime-num*interval;
		for (int i = 0; i < num; i++) {
			long endTime = startTime+interval;
			timeStmps.put(format.format(new Date(startTime)));
			for(String topic:LogCollector.TOPICS){
				long c = RadarLogMongoDao.countLogs(topic, startTime, endTime);
				((JSONArray)map.get(topic)).put(c);
			}
			startTime+=interval;
		}
		result.put("timeStmps", timeStmps);
		result.put("map", map);	
		return result;
	}

}
