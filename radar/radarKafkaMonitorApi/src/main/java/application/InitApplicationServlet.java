package application;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import infrastruction.db.RadarLogMongoDao;
import infrastruction.imageLog.FileLogger;
import infrastruction.properties.KafkaConsumerProperties;
import infrastruction.properties.Stations;


@SuppressWarnings("serial")
public class InitApplicationServlet extends HttpServlet{
	@Override
	public void init() throws ServletException {
		//关闭mongo driver的日志输出
		Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
		// 关闭Kafka client的日志输出
		Logger.getLogger("org.apache.kafka").setLevel(Level.OFF);
		String contentPath = getServletContext().getRealPath("/");
		Stations.init(contentPath);
		FileLogger.init(contentPath);
		KafkaConsumerProperties.init(contentPath);
		RadarLogMongoDao.init();
		if(RadarLogMongoDao.testConnection()){
			System.out.println("Test connection to MongoDao pass.");
		}else{
			System.out.println("Test connection to MongoDao fail,system exit 1.");
			System.exit(1);
		}
		LogCollector.execute();
	}
}
