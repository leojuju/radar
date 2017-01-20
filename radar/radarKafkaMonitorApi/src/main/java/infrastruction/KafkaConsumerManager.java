package infrastruction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import infrastruction.properties.KafkaConsumerProperties;


public class KafkaConsumerManager {
	
	public static final String SUBFIX = "monitor";
	private static Map<String,KafkaConsumer<String, String>> consumers = new HashMap<>();
	
	public static void registConsumer(String topic){
		if(consumers.get(topic)==null){
			KafkaConsumer<String, String> consumer 
				= new KafkaConsumer<>(KafkaConsumerProperties.getProperties(topic+"_"+SUBFIX));
			consumer.subscribe(Arrays.asList(topic));
			consumers.put(topic, consumer);
		}
	}
	
	public static Map<String,String> pullMsgs(String topic){
		registConsumer(topic);
		Map<String,String> result = new HashMap<String,String>();
		ConsumerRecords<String, String> records = consumers.get(topic).poll(100);
		for (ConsumerRecord<String, String> record:records) {
			StringBuffer value = new StringBuffer(record.value().trim());
			value.deleteCharAt(value.length()-1);
			long latestTime = record.timestamp();
			value.append(",\"time_"+topic+"\":"+latestTime);
			value.append("}");
			result.put(record.key(), value.toString());
		}
		return result;
	}

	public static int getConsumersNum() {
		return consumers.size();
	}

	public static Map<String, Map<String, String>> pullMsgs(List<String> topics) {
		Map<String, Map<String, String>> result = new HashMap<>();
		for(String topic:topics){
			Map<String, String> value = pullMsgs(topic);
			result.put(topic, value);
		}
		return result;
	}
	
}
