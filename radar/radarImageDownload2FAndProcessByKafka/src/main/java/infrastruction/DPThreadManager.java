package infrastruction;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DPThreadManager {
	
	private static Logger logger = LoggerFactory.getLogger(DPThreadManager.class);
	
	public static ExecutorService exec = Executors.newFixedThreadPool(20);
	
	public static void execute(Map<String, JSONObject> msgs){
		logger.info("Get Tasks Num:"+msgs.size());
		for(String key:msgs.keySet()){
			String[] strs = key.split("/");
			exec.execute(new DPTask(strs[0], strs[1],msgs.get(key)));
		}
	}
}
