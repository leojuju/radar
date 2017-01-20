package application;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import infrastruction.KafkaProducerManager;
import infrastruction.properties.Links;

public class AnalysisTaskDistributor {
	
	private static Logger logger = LoggerFactory.getLogger(AnalysisTaskDistributor.class);
	
	public static void main(String[] args) {
		InitApplication.init();
		//start distribute schedule
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				logger.info("Start to distribute download tasks.");
				KafkaProducerManager.distribute("radar_readhtml",Links.toMsgFormat());
				logger.info("Finished distribution of download tasks.");
			}
		}, 10000, 300000);
	}
}
