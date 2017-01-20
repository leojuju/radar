package application;

import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import infrastruction.AnalysisThreadManager;
import infrastruction.KafkaConsumerManager;

public class AnalysisTaskHandler {
	
	private static Logger logger = LoggerFactory.getLogger(AnalysisTaskHandler.class);
	
	public static void main(String[] args) {
		
		InitApplication.init();
		
		logger.info("Start to recieve analysis tasks.");
		
		while(true){
			Map<String,JSONObject> msgs = KafkaConsumerManager.pullMsgs("radar_readhtml");
			if(msgs.size()>0){
				AnalysisThreadManager.execute(msgs);
			}
		}
	}
}
