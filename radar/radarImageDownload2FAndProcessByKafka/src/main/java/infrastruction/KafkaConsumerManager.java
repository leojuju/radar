package infrastruction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.json.JSONObject;

import infrastruction.properties.KafkaConsumerProperties;


public class KafkaConsumerManager {
	
	public static final String SUBFIX = "download";
	private static Map<String,KafkaConsumer<String, String>> consumers = new HashMap<>();
	
	public static void registConsumer(String topic){
		if(consumers.get(topic)==null){
			KafkaConsumer<String, String> consumer = 
					new KafkaConsumer<>(KafkaConsumerProperties.getProperties(topic+"_"+SUBFIX));
			consumer.subscribe(Arrays.asList(topic));
			consumers.put(topic, consumer);
		}
	}
	
	public static Map<String,JSONObject> pullMsgs(String topic){
		registConsumer(topic);
		Map<String,JSONObject> result = new HashMap<String,JSONObject>();
		ConsumerRecords<String, String> records = consumers.get(topic).poll(Long.MAX_VALUE);
		for (ConsumerRecord<String, String> record:records) {
			JSONObject value = new JSONObject(record.value());
			long latestTime = record.timestamp();
			Long earliestTime = null;
			try {
				earliestTime = value.getLong("time_"+topic);
			} catch (Exception e) {
				earliestTime = null;
			}
			if(earliestTime==null){
				value.put("time_"+topic, latestTime);
			}else{
				if(latestTime-(long)earliestTime>290000){
					continue;
				}
			}
			result.put(record.key(), value);
		}
		return result;
	}
	
}
