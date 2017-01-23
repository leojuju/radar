package application;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import infrastruction.db.RadarMongoDao;
import infrastruction.digitRecognize.DRUtils;
import infrastruction.properties.KafkaConsumerProperties;
import infrastruction.properties.KafkaProducerProperties;
import infrastruction.radarprocess.Stations;

public class InitApplication {
	public static void init(){
		//关闭mongo driver的日志输出
		Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
		//关闭Kafka client的日志输出
		Logger.getLogger("org.apache.kafka").setLevel(Level.OFF);
		Stations.init();
		DRUtils.init();
		KafkaConsumerProperties.init();
		KafkaProducerProperties.init();
		RadarMongoDao.init();
		RadarMongoDao.testConnection();
	}
}
