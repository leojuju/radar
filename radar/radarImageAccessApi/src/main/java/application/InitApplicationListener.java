package application;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import infrastruction.db.RadarMongoDao;
import infrastruction.radarprocess.Stations;
import infrastruction.radarprocess.geo.MataDatas;

public class InitApplicationListener implements ServletContextListener{
	@Override
	public void contextDestroyed(ServletContextEvent sce){
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		//关闭mongo driver的日志输出
		Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
		String contentPath = sce.getServletContext().getRealPath("/");
		Stations.init(contentPath);
		MataDatas.init(contentPath);
		RadarMongoDao.init();
		RadarMongoDao.testConnection();
	}
}
