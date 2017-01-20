package application;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import infrastruction.KafkaConsumerManager;
import infrastruction.LogUtils;

public class LogCollector {
	
	public static final List<String> TOPICS = Arrays.asList(
			"radar_readhtml","radar_readhtml_success","radar_readhtml_fail",
			"radar_readimage","radar_readimage_success","radar_readimage_fail",
			"radar_geopimage"
//			"radar_processimage","radar_processimage_success","radar_processimage_fail"
			);
	private static ExecutorService exec = Executors.newSingleThreadExecutor();
	
	public static void execute(){
		exec.execute(new LogCollectTask());
		exec.shutdown();
	}
}

class LogCollectTask implements Runnable{

	@Override
	public void run() {
		while(true){
			collect();
		}
	}

	private void collect() {
		Map<String,Map<String,String>> msgs = KafkaConsumerManager.pullMsgs(LogCollector.TOPICS );
		LogUtils.save(msgs);
	}
}