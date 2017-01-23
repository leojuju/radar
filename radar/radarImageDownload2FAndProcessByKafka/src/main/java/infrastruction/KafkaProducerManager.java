package infrastruction;

import java.util.Map;
import java.util.Set;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import infrastruction.properties.KafkaProducerProperties;

public class KafkaProducerManager {

	public static void distribute(String topic,Map<String, String> msg){
		Producer<String,String> producer= new KafkaProducer<>(KafkaProducerProperties.props);
		Set<String> keys = msg.keySet();
		for (String key : keys)
			producer.send(new ProducerRecord<String, String>(topic, key, msg.get(key)));
		producer.close();
	}

}
