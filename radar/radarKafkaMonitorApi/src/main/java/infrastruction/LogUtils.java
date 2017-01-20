package infrastruction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import infrastruction.db.RadarLogMongoDao;
import infrastruction.imageLog.LogManager;

public class LogUtils {

	public static void save(Map<String, Map<String, String>> msgs) {
		for (String topic:msgs.keySet()) {
			Map<String, String> msgsValue = msgs.get(topic);
			if(!msgsValue.isEmpty()){
				RadarLogMongoDao.saveLogs(topic, toDocumentLogs(msgsValue));
				if(topic.equals("radar_readimage_success")){
					saveImageLogs(msgsValue);
				}
			}
		}
	}

	private static void saveImageLogs(Map<String, String> msgsValue) {
		for (String key:msgsValue.keySet()) {
			String[] strs = key.split("/");
			LogManager.save(strs[0], Long.valueOf(strs[1]));
		}
	}

	private static List<Document> toDocumentLogs(Map<String, String> msgsValue) {
		List<Document> logs = new ArrayList<>();
		for(String log:msgsValue.values()){
			logs.add(Document.parse(log));
		}
		return logs;
	}

}
