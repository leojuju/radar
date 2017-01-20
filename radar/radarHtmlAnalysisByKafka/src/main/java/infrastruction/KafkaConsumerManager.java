package infrastruction;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.json.JSONObject;

import infrastruction.properties.KafkaConsumerProperties;


public class KafkaConsumerManager {
	
	public static final String SUBFIX = "analysis";
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
		for (ConsumerRecord<String, String> record : records) {
			JSONObject value = new JSONObject(record.value());
			long latestTime = record.timestamp();
			Long earliestTime = null;
			try {
				earliestTime = value.getLong("time_" + topic);
			} catch (Exception e) {
				earliestTime = null;
			}
			if (earliestTime == null) {
				value.put("time_" + topic, latestTime);
			} else {
				if (latestTime - (long) earliestTime > 290000) {
					continue;
				}
			}
			result.put(record.key(), value);
		}
		return result;
	}
	
	public static Map<String,JSONObject> pullMsgsSynsCommit(String topic){
		registConsumer(topic);
		Map<String,JSONObject> result = new HashMap<String,JSONObject>();
		ConsumerRecords<String, String> records = consumers.get(topic).poll(Long.MAX_VALUE);
		for (TopicPartition partition : records.partitions()) {
            List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
            for (ConsumerRecord<String, String> record:partitionRecords) {
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
            long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
            consumers.get(topic).commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
		}
		return result;
	}
	
}
