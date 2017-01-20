package application;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import infrastruction.LinkUtils;
import infrastruction.db.RadarMongoDao;
import infrastruction.properties.KafkaConsumerProperties;
import infrastruction.properties.KafkaProducerProperties;

public class InitApplication {
	public static void init(){
		//关闭mongo driver的日志输出
		Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
		//关闭Kafka client的日志输出
		Logger.getLogger("org.apache.kafka").setLevel(Level.OFF);
		KafkaConsumerProperties.init();
		KafkaProducerProperties.init();
		RadarMongoDao.init();
		RadarMongoDao.testConnection();
		LinkUtils.setLinkPickPattern(LinkUtils.SMALL);
	}
}
