package application.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.json.JSONArray;
import org.json.JSONObject;

import infrastruction.StatisticUtils;

@Path("/")
public class StatisticApi {
	
	@Path("getRecentStatistic")
	@GET
	public String getRecentStatistic(){
		
		//获取服务器当前时间
		long currentTime = System.currentTimeMillis();
		JSONObject result = StatisticUtils.getRecentIntervalStatisticData(currentTime,10000,50);
		return result.toString();
	}
	
	@Path("getRecentStatistic_test")
	@GET
	public String getRecentStatisticTest(){
		String result = 
				"{\"timeStmps\":[\"201601\",\"201602\",\"201603\",\"201604\",\"201605\","
				+ "\"201606\",\"201607\",\"201608\",\"201609\",\"201610\",\"201611\",\"201612\"],"
				+"\"map\":{\"leo\":[1,2,3,4,5,6,7,8,9,10,11,12],"
				+ "\"kaka\":[2,3,6,8,6,4,3,6,2,4,5,9],"
				+ "\"juju\":[3,9,7,4,5,2,8,5,5,3,6,2],"
				+ "\"wuli\":[2,2,9,5,7,5,8,5,3,3,5,9],"
				+ "\"dodo\":[8,6,3,6,9,3,2,4,2,3,4,10]"
				+ "}"
				+ "}";
		return result;
	}
	
	@Path("getRecentStatistic_testJson")
	@GET
	public String getRecentStatisticTestJSON(){
		JSONObject result = new JSONObject();
		JSONArray timeStmps = new JSONArray();
		JSONObject map = new JSONObject();
		Random ran = new Random();
		List<String> topics = new ArrayList<>(Arrays.asList("leo","jin","kaka","dodo","pupu"));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		for(String topic:topics){
			map.put(topic, new JSONArray());
		}
		int interval = 300000; 
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < 12; i++) {
			timeStmps.put(format.format(new Date(startTime)));
			for(String topic:topics){
				long c = ran.nextInt(100);
				((JSONArray)map.get(topic)).put(c);
			}
			startTime+=interval;
		}
		result.put("timeStmps", timeStmps);
		result.put("map", map);	
		return result.toString();
	}
	
}
