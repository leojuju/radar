package infrastruction;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalysisThreadManager {
	
	private static Logger logger = LoggerFactory.getLogger(AnalysisThreadManager.class);
	
	public static ExecutorService exec = Executors.newFixedThreadPool(10);
	
	public static void execute(Map<String, JSONObject> msgs){
		logger.info("Get Tasks Num:"+msgs.size());
		for(String stationID:msgs.keySet()){
			JSONObject msgValue = msgs.get(stationID);
			exec.execute(new AnalysisTask(stationID,msgValue));
		}
	}
}
