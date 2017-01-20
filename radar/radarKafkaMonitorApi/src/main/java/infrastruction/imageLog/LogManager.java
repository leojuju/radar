package infrastruction.imageLog;

public class LogManager {
	
	private static Logger logger = new FileLogger();
	
	public static void changeLogger(Logger l){
		logger = l;
	}
	
	public static boolean save(String stationID,long time){
		return logger.save(stationID, time);
	}
}
