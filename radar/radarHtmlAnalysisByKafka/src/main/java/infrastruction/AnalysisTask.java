package infrastruction;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalysisTask implements Runnable {
	
	private static Logger logger = LoggerFactory.getLogger(AnalysisTask.class);

	private String stationID;
	private JSONObject msgValue;

	public AnalysisTask(String stationID, JSONObject msgValue) {
		super();
		this.stationID = stationID;
		this.msgValue = msgValue;
	}

	@Override
	public void run() {
		logger.info("stationID:"+stationID
				+",htmlLink:"+msgValue.get("htmlLink")+",msgCreateTime:"+msgValue.get("time_radar_readhtml"));
		analysis(stationID,msgValue);
	}

	public static void analysis(String stationID, JSONObject linkMsgValue) {
		String link = linkMsgValue.getString("htmlLink");
		String htmlSrc = HttpUtils.getHtmlSrc(link);
		if(htmlSrc!=null){
			List<String> imageLinks = LinkUtils.findImageLinks(htmlSrc,LinkUtils.pattern);
			if(imageLinks.size()==0){
				logger.warn("No imageLinks in htmlSrc for stationID:"+stationID);
			}
			List<String> imageLink4Download = LinkUtils.filter(stationID, imageLinks);
			Map<String,String> imageLinkMsgs = 
					LinkUtils.getImageLinkMsgs(stationID,linkMsgValue,imageLink4Download);
			if(imageLinkMsgs.size()>0){
				KafkaProducerManager.distribute("radar_readimage",imageLinkMsgs);
			}
			linkMsgValue.remove("htmlLink");
			Map<String,String> msg = Collections.singletonMap(stationID, linkMsgValue.toString());
			KafkaProducerManager.distribute("radar_readhtml_success", msg);
		}else{
			Map<String,String> msg = Collections.singletonMap(stationID, linkMsgValue.toString());
 			KafkaProducerManager.distribute("radar_readhtml", msg);
			KafkaProducerManager.distribute("radar_readhtml_fail", msg);
		}
		
	}
	
	
}
