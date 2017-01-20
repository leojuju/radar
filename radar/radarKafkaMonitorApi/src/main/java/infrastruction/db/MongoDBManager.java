package infrastruction.db;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

public class MongoDBManager {
	private static String HOST ; 
    private static int PORT ;
//    private final static int POOLSIZE = 100;// 连接数量  
//    private final static int BLOCKSIZE = 100; // 等待队列长度  

    public static void init(){
    	HOST =System.getenv("MONGODB_SERVICE_HOST");
    	String port =System.getenv("MONGODB_SERVICE_PORT");
    	if(HOST==null||port==null){
    		System.out.println("MONGODB_SERVICE_HOST &MONGODB_SERVICE_POST env value load failed!");
    		System.exit(0);
    	}
    	PORT=Integer.valueOf(port);
    }
 
    public static MongoClient connect() {  
        // 其他参数根据实际情况进行添加  
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
//        builder.connectionsPerHost(POOLSIZE);// 与目标数据库可以建立的最大链接数
//        builder.connectTimeout(1000 * 60 * 20);// 与数据库建立链接的超时时间
//        builder.maxWaitTime(100 * 60 * 5);// 一个线程成功获取到一个可用数据库之前的最大等待时间
//        builder.threadsAllowedToBlockForConnectionMultiplier(BLOCKSIZE);
//        builder.maxConnectionIdleTime(0);
//        builder.maxConnectionLifeTime(0);
//        builder.socketTimeout(0);
//        builder.socketKeepAlive(true);
        MongoClientOptions myOptions = builder.build();
        return new MongoClient(new ServerAddress(HOST, PORT), myOptions);
    }
    
    public static void disconnected(MongoClient mongoClient){
    	mongoClient.close();
    }
}
