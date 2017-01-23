package infrastruction.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaProducerProperties {
	
	private static Logger logger = LoggerFactory.getLogger(KafkaProducerProperties.class);
	
	public static Properties props = new Properties();
	private static String propsPath = "properties/producer.propertes";
	
	public static void init(){
		loadPropertiesFromFile();
		loadPropertiesFromSysRnv();
	}

	private static void loadPropertiesFromSysRnv() {
		String bootstrap_servers =System.getenv("KAFKA_SERVICE_BOOTSTRAPSERVERS");
    	if(bootstrap_servers==null){
    		logger.error("KAFKA_SERVICE_BOOTSTRAPSERVERS env value load failed,"
    				+ ",system exit 1.");
    		System.exit(1);
    	}
    	props.put("bootstrap.servers",bootstrap_servers);
	}

	private static void loadPropertiesFromFile() {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(propsPath);
			props.load(fis);
			
			
			
			logger.info("Kafka ProducerProperties successfully loaded.");
		} catch (Exception e) {
			logger.error("Kafka ProducerProperties load failed,system exit 1.");
			System.exit(1);
		} finally{
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
