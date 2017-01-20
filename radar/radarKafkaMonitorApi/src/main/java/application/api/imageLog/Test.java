package application.api.imageLog;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("test_imageLog")
public class Test {
	
	@GET
	public String test(){
		return "Hello KafkaMonitor ImageLog";
	}
}
