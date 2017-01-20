package infrastruction.db;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class RadarMongoDao {
	
	private static Logger logger = LoggerFactory.getLogger(RadarMongoDao.class);

	public static String MONGODB_RADAR_DB_SPDATA;
	
	public static void init() {
		MongoDBManager.init();
		MONGODB_RADAR_DB_SPDATA = System.getenv("MONGODB_RADAR_DB_SPDATA");
		if(MONGODB_RADAR_DB_SPDATA==null){
			logger.error("MONGODB_RADAR_DB_SPDATA env value load failed,system exit 1.");
			System.exit(1);
		}
		logger.info("Successfully load MongoDB properties.");
	}
	
	public static void testConnection(){
		MongoClient client = null;
		try {
			client = MongoDBManager.connect();
			MongoDatabase db = client.getDatabase(MONGODB_RADAR_DB_SPDATA);
			MongoCollection<Document> collection =db.getCollection("test");
			collection.insertOne(new Document("test", "test"));
			collection.deleteOne(new Document("test", "test"));
			logger.info("Test Connection to MongoDB succeed.");
		} catch (Exception e) {
			logger.error("Test Connection to MongoDB failed,system exit 1.");
			System.exit(1);
		} finally{
			if(client!=null){
				client.close();
			}
		}
	}
	
	
	public static boolean saveOriginImage(String stationID, byte[] data, long time) {
		MongoClient client = null;
		try {
			client = MongoDBManager.connect();
			MongoDatabase db = client.getDatabase(MONGODB_RADAR_DB_SPDATA);
			MongoCollection<Document> collection =db.getCollection(stationID);
			Document dbObject = new Document("time", time).append("data", data);
			collection.insertOne(dbObject);
			return true;
		} catch (Exception e) {
			logger.error("Save OriginImage to mongodb failed:[stationID:"+stationID
					+",time:"+time+"].");
		} finally {
			if(client!=null){
				client.close();
			}
		}
		return false;
	}

	public static boolean isImageExist(String stationID, long linkTime) {
		MongoClient client = null;
		try {
			client = MongoDBManager.connect();
			MongoDatabase db = client.getDatabase(MONGODB_RADAR_DB_SPDATA);
			MongoCollection<Document> collection =db.getCollection(stationID);
			long num = collection.count(new Document("linkTime",linkTime));
			if(num>0){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			logger.error("Check Image if exist in mongodb failed:[stationID:"
					+stationID+",linkTime:"+linkTime+"].");
		} finally {
			if(client!=null){
				client.close();
			}
		}
		return false;
	}
	
}
