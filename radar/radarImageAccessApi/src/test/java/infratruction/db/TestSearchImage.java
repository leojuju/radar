package infratruction.db;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import infrastruction.db.MongoDBManager;
import infrastruction.db.RadarMongoDao;

public class TestSearchImage {
	public static void test() {
		RadarMongoDao.init();
		MongoClient client = MongoDBManager.connect();
		MongoDatabase db = client.getDatabase("radardata_gp");
		byte[] matrix = RadarMongoDao.searchGPImageAround("Z9755", 1483968241000L, db,"1");
		System.out.println(matrix.length);
	}
}
