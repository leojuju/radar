package infrastruction.db;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

public class RadarLogMongoDao {

	public static String MONGODB_RADAR_DB_LOG;
	public static String MONGODB_RADAR_DB_SPDATA;
	
	public static void init() {
		MongoDBManager.init();
		MONGODB_RADAR_DB_LOG = System.getenv("MONGODB_RADAR_DB_LOG");
		MONGODB_RADAR_DB_SPDATA = System.getenv("MONGODB_RADAR_DB_SPDATA");
		if(MONGODB_RADAR_DB_LOG==null||MONGODB_RADAR_DB_SPDATA==null){
			System.out.println("MONGODB_RADAR_DB_LOG and MONGODB_RADAR_DB_ORGDATA env value load failed!");
			System.exit(1);
		}
	}
	
	public static boolean testConnection(){
		MongoClient client = null;
		try {
			client = MongoDBManager.connect();
			MongoDatabase db = client.getDatabase(MONGODB_RADAR_DB_LOG);
			MongoCollection<Document> collection =db.getCollection("test");
			collection.insertOne(new Document("test", "test"));
			collection.deleteOne(new Document("test", "test"));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(client!=null){
				client.close();
			}
		}
		return false;
	}

	
	public static boolean saveLogs(String topic, List<Document> logs){
		MongoClient client = null;
		try {
			client = MongoDBManager.connect();
			MongoDatabase db = client.getDatabase(MONGODB_RADAR_DB_LOG);
			MongoCollection<Document> collection =db.getCollection(topic);
			collection.insertMany(logs);
			return true;
		} catch (Exception e) {
			System.out.println("Save Logs to mongodb failed.");
		} finally {
			if(client!=null){
				client.close();
			}
		}
		return false;
	}

	public static List<Document> searchLogs(String topic, long startTime ,long endTime) {
		MongoClient client = null;
		List<Document> result = new ArrayList<>();
		try {
			client = MongoDBManager.connect();
			MongoDatabase db = client.getDatabase(MONGODB_RADAR_DB_LOG);
			MongoCollection<Document> collection =db.getCollection(topic);
			FindIterable<Document> findIterable = collection.find(new Document("time_"+topic,
					new Document("$gte", startTime).append("$lt", endTime)));
			MongoCursor<Document> mongoCursor = findIterable.iterator();
			while(mongoCursor.hasNext()){
				result.add(mongoCursor.next());
			}
			return result;
		} catch (Exception e) {
			System.out.println("Search Logs on mongodb failed.");
		} finally {
			if(client!=null){
				client.close();
			}
		}
		return null;
	}
	
	
	public static long countLogs(String topic, long startTime ,long endTime) {
		MongoClient client = null;
		try {
			client = MongoDBManager.connect();
			MongoDatabase db = client.getDatabase(MONGODB_RADAR_DB_LOG);
			MongoCollection<Document> collection =db.getCollection(topic);
			long num = collection.count(new Document("time_"+topic,
					new Document("$gte", startTime).append("$lt", endTime)));
			return num;
		} catch (Exception e) {
			System.out.println("Search Logs on mongodb failed.");
		} finally {
			if(client!=null){
				client.close();
			}
		}
		return 0;
	}
	
	
//	public static boolean saveDownloadLog(String stationID , Document log){
//		MongoClient client = null;
//		try {
//			client = MongoDBManager.connect();
//			MongoDatabase db = client.getDatabase(MONGODB_RADAR_DB_DLOG);
//			MongoCollection<Document> collection =db.getCollection(stationID);
//			collection.insertOne(log);
////			collection =db.getCollection("statistics");
////			UpdateOptions upop = new UpdateOptions();
////			upop.upsert(true);
////			collection.updateOne(new Document("stationID",stationID), 
////					new Document("$inc",new Document("count", 1)),upop);
//			return true;
//		} catch (Exception e) {
//			System.out.println("Save DownloadLog to mongodb failed.");
//		} finally {
//			if(client!=null){
//				client.close();
//			}
//		}
//		return false;
//	}
	
	
	public static long countOrgImagesById(String stationID, long startTime ,long endTime) {
		MongoClient client = null;
		try {
			client = MongoDBManager.connect();
			MongoDatabase db = client.getDatabase(MONGODB_RADAR_DB_SPDATA);
			MongoCollection<Document> collection =db.getCollection(stationID);
			long num = collection.count(new Document("time",
					new Document("$gte", startTime).append("$lt", endTime)));
			return num;
		} catch (Exception e) {
			System.out.println("Search Logs on mongodb failed.");
		} finally {
			if(client!=null){
				client.close();
			}
		}
		return 0;
	}
	
	public static long countOrgImagesById(String stationID) {
		MongoClient client = null;
		try {
			client = MongoDBManager.connect();
			MongoDatabase db = client.getDatabase(MONGODB_RADAR_DB_SPDATA);
			MongoCollection<Document> collection =db.getCollection(stationID);
			long num = collection.count();
			return num;
		} catch (Exception e) {
			System.out.println("Search Logs on mongodb failed.");
		} finally {
			if(client!=null){
				client.close();
			}
		}
		return 0;
	}
	
	public static List<String> findAllOrgDataCollection(){
		MongoClient client = null;
		List<String> result = new ArrayList<>();
		try {
			client = MongoDBManager.connect();
			MongoDatabase db = client.getDatabase(MONGODB_RADAR_DB_SPDATA);
			MongoIterable<String> collections = db.listCollectionNames();
			collections.into(result);
			return result;
		} catch (Exception e) {
			System.out.println("Search Logs on mongodb failed.");
		} finally {
			if(client!=null){
				client.close();
			}
		}
		return null;
	}

	public static long findEarliestOrgDataTime(String stationID) {
		return findExtremOrgDataTime(stationID, 1);
	}
	
	public static long findLatestOrgDataTime(String stationID) {
		return findExtremOrgDataTime(stationID, -1);
	}
	
	public static long findExtremOrgDataTime(String stationID,int inc) {
		MongoClient client = null;
		inc = inc<0?-1:1;
		try {
			client = MongoDBManager.connect();
			MongoDatabase db = client.getDatabase(MONGODB_RADAR_DB_SPDATA);
			MongoCollection<Document> collection =db.getCollection(stationID);
			FindIterable<Document> findIterable = collection.find()
					.sort(new Document("time",inc)).limit(1);
			MongoCursor<Document> mongoCursor = findIterable.iterator();
			if(mongoCursor.hasNext()){
				return mongoCursor.next().getLong("time");
			}
		} catch (Exception e) {
			System.out.println("Search Logs on mongodb failed.");
		} finally {
			if(client!=null){
				client.close();
			}
		}
		return 0;
	}
}
