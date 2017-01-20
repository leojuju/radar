package application.api;

import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;

import infrastruction.KafkaConsumerManager;


public class TestParallelism {
	
	@GET
	public String test(@QueryParam("topic") String topic){
		
		KafkaConsumerManager.registConsumer(topic);
		return "Consumer Num:"+KafkaConsumerManager.getConsumersNum()+".";
	}
}
