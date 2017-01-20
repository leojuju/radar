package infrastruction.db;

import org.bson.Document;
import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class RadarMongoDao {

	private static Logger logger = LoggerFactory.getLogger(RadarMongoDao.class);
	
	public static String MONGODB_RADAR_DB_GPDATA ;
	public static String MONGODB_RADAR_DB_SPDATA ;
	
	public static void init() {
		MongoDBManager.init();
		MONGODB_RADAR_DB_GPDATA = System.getenv("MONGODB_RADAR_DB_GPDATA");
		MONGODB_RADAR_DB_SPDATA = System.getenv("MONGODB_RADAR_DB_SPDATA");
		if(MONGODB_RADAR_DB_GPDATA==null||MONGODB_RADAR_DB_SPDATA==null){
			logger.error("MONGODB_RADAR_DB_GPDATA or MONGODB_RADAR_DB_SPDATA or"
					+ " MONGODB_RADAR_DB_ORGDATA env value load failed,,system exit 1.");
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
	
//	
//	public static boolean saveOriginImage(String stationID, byte[] data, long time) {
//		MongoClient client = null;
//		try {
//			client = MongoDBManager.connect();
//			MongoDatabase db = client.getDatabase(MONGODB_RADAR_DB_ORGDATA);
//			MongoCollection<Document> collection =db.getCollection(stationID);
//			Document dbObject = new Document("time", time).append("data", data);
//			collection.insertOne(dbObject);
//			return true;
//		} catch (Exception e) {
//			System.out.println("Save OriginImage to mongodb failed:[stationID:"+stationID
//					+",time:"+time+"].");
//		} finally {
//			if(client!=null){
//				client.close();
//			}
//		}
//		return false;
//	}
//
//	public static boolean isOriginImageExist(String stationID, long time) {
//		MongoClient client = null;
//		try {
//			client = MongoDBManager.connect();
//			MongoDatabase db = client.getDatabase(MONGODB_RADAR_DB_ORGDATA);
//			MongoCollection<Document> collection =db.getCollection(stationID);
//			long num = collection.count(new Document("time",time));
//			if(num>0){
//				return true;
//			}else{
//				return false;
//			}
//		} catch (Exception e) {
//			System.out.println("Check OriginImage if exist in mongodb failed:[stationID:"
//					+stationID+",time:"+time+"].");
//		} finally {
//			if(client!=null){
//				client.close();
//			}
//		}
//		return false;
//	}
//
//	public static byte[] getOriginImage(String stationID,long imageTime){
//		MongoClient client = null;
//		try {
//			client = MongoDBManager.connect();
//			MongoDatabase db = client.getDatabase(MONGODB_RADAR_DB_ORGDATA);
//			MongoCollection<Document> collection =db.getCollection(stationID);
//			FindIterable<Document> findIterable = collection.find(new Document("time", imageTime));
//			MongoCursor<Document> mongoCursor = findIterable.iterator();
//			if(mongoCursor.hasNext()){
//				Document obj = mongoCursor.next();
//				Object result = obj.get("data");
//				if(result !=null){
//					return ((Binary)result).getData();
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("Read OriginImage from mongodb failed:station:"+stationID+",imageTime:"+imageTime+".");
//		} finally {
//			if(client!=null){
//				client.close();
//			}
//		}
//		return null;
//	}
	
	public static boolean saveSimpleProcessedRadarByte(String stationID,long linkTime, long time, byte[] data) {
		MongoClient client = null;
		try {
			client = MongoDBManager.connect();
			MongoDatabase db = client.getDatabase(MONGODB_RADAR_DB_SPDATA);
			MongoCollection<Document> collection =db.getCollection(stationID);
			Document dbObject = new Document("linkTime", linkTime).append("time", time).append("data", data);
			collection.insertOne(dbObject);
			return true;
		} catch (Exception e) {
			logger.error("Save SimpleProcessedRadarByte to mongodb failed:station:"+stationID
					+",linkTime:"+linkTime+",time:"+time+".");
		} finally {
			if(client!=null){
				client.close();
			}
		}
		return false;
	}


	public static boolean saveGeoProcessedRadarByte(String stationID, long time, byte[] data) {
		MongoClient client = null;
		try {
			client = MongoDBManager.connect();
			MongoDatabase db = client.getDatabase(MONGODB_RADAR_DB_GPDATA);
			MongoCollection<Document> collection =db.getCollection(stationID);
			Document dbObject = new Document("time", time).append("data", data);
			collection.insertOne(dbObject);
			return true;
		} catch (Exception e) {
			logger.error("Save GeoProcessedRadarByte to mongodb failed:station:"+stationID
					+",time:"+time+".");
		} finally {
			if(client!=null){
				client.close();
			}
		}
		return false;
	}
	
	public static byte[] getSPImage(String stationID,long time){
		MongoClient client = null;
		try {
			client = MongoDBManager.connect();
			MongoDatabase db = client.getDatabase(MONGODB_RADAR_DB_SPDATA);
			MongoCollection<Document> collection =db.getCollection(stationID);
			FindIterable<Document> findIterable = collection.find(new Document("time", time));
			MongoCursor<Document> mongoCursor = findIterable.iterator();
			if(mongoCursor.hasNext()){
				Document obj = mongoCursor.next();
				Object result = obj.get("data");
				if(result !=null){
					return ((Binary)result).getData();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Read SPImage from mongodb failed:station:"+stationID+",time:"+time+".");
		} finally {
			if(client!=null){
				client.close();
			}
		}
		return null;
	}

	public static byte[] getGPImage(String stationID,long time){
		MongoClient client = null;
		try {
			client = MongoDBManager.connect();
			MongoDatabase db = client.getDatabase(MONGODB_RADAR_DB_GPDATA);
			MongoCollection<Document> collection =db.getCollection(stationID);
			FindIterable<Document> findIterable = collection.find(new Document("time", time));
			MongoCursor<Document> mongoCursor = findIterable.iterator();
			if(mongoCursor.hasNext()){
				Document obj = mongoCursor.next();
				Object result = obj.get("data");
				if(result !=null){
					return ((Binary)result).getData();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Read GPImage from mongodb failed:station:"+stationID+",time:"+time+".");
		} finally {
			if(client!=null){
				client.close();
			}
		}
		return null;
	}

	public static boolean deleteSimpleProcessedRadarByte(String stationID, long time) {
		MongoClient client = null;
		try {
			client = MongoDBManager.connect();
			MongoDatabase db = client.getDatabase(MONGODB_RADAR_DB_SPDATA);
			MongoCollection<Document> collection =db.getCollection(stationID);
			Document dbObject = new Document("time", time);
			collection.findOneAndDelete(dbObject);
			return true;
		} catch (Exception e) {
			logger.error("delete SimpleProcessedRadarByte from mongodb failed:station:"+stationID
					+",time:"+time+".");
		} finally {
			if(client!=null){
				client.close();
			}
		}
		return false;
	}
}
