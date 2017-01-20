package infrastruction;

import java.util.Map;
import java.util.Set;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import infrastruction.properties.KafkaProducerProperties;

public class KafkaProducerManager {
	
	private static Logger logger = LoggerFactory.getLogger(KafkaProducerManager.class);
	
	public static void distribute(String topic,Map<String, String> msg){
		Producer<String,String> producer= new KafkaProducer<>(KafkaProducerProperties.props);
		Set<String> keys = msg.keySet();
		logger.info("Topic is:"+topic+".");
		logger.info("Msgs Num:"+keys.size()+".");
		for (String key : keys){
			String value = msg.get(key);
			producer.send(new ProducerRecord<String, String>(topic, key, value));
			logger.debug("Msgs:{key:"+key+",value:"+value+"}.");
		}
		producer.close();
	}
	
}
