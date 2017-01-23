package application;

import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import infrastruction.DPThreadManager;
import infrastruction.KafkaConsumerManager;

public class DPTaskHandler {
	
	private static Logger logger = LoggerFactory.getLogger(DPTaskHandler.class);
	
	public static void main(String[] args) {
		
		InitApplication.init();

		logger.info("Start to recieve download and process tasks.");
		
		while(true){
			
			Map<String,JSONObject> msgs = KafkaConsumerManager.pullMsgs("radar_readimage");
			if(msgs.size()>0){
				DPThreadManager.execute(msgs);
			}
		}
	}
}
