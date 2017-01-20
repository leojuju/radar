package application;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import infrastruction.properties.KafkaProducerProperties;
import infrastruction.properties.Links;

public class InitApplication {
	public static void init(){
		//关闭Kafka client的日志输出
		Logger.getLogger("org.apache.kafka").setLevel(Level.OFF);
		Links.init();
		KafkaProducerProperties.init();
	}
}
