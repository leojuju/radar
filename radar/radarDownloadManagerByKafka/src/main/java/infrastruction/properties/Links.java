package infrastruction.properties;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Links {
	
	private static Logger logger = LoggerFactory.getLogger(Links.class);
	
	private static final String URLPREFIX = "http://www.nmc.cn";
	private static String urlFilePath = "properties/station_links";
	public static Map<String, String> links = new HashMap<String, String>();
	
	public static void init(){
		BufferedReader br4url =null;
		try {
			//读取url信息
			br4url = new BufferedReader(new InputStreamReader(new FileInputStream(urlFilePath), "UTF-8"));
			String line = null;
			br4url.readLine();
			while((line=br4url.readLine())!=null){
				String[] strs = line.split(",");
				links.put(strs[2], URLPREFIX+strs[3]);
			}
			logger.info("Stations links successfully loaded.");
		} catch (Exception e) {
			logger.error("Stations links load failed,system exit 1.");
			System.exit(1);
		} finally {
			try {
				if(br4url!=null){
					br4url.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
	
	public static Map<String,String> toMsgFormat(){
		Map<String,String> result = new HashMap<>();
		for(String stationID:links.keySet()){
			JSONObject json = new JSONObject();
			json.put("htmlLink", links.get(stationID));
			json.put("stationID", stationID);
			result.put(stationID, json.toString());
		}
		return result;	
	}
}
