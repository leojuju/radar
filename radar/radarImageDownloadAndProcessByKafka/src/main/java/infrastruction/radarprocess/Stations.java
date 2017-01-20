package infrastruction.radarprocess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import infrastruction.redarprocess.geo.LatAndLongt;


public class Stations {
	
	private static Logger logger = LoggerFactory.getLogger(Stations.class);
	
	public static Map<String,Station> stations = new HashMap<String,Station>();
	private static String locaitonFilePath = "properties/stations_location";
	
	public static void init(){
		BufferedReader br4location =null;
		try {
			String line = null;
			//读取location信息
			br4location = new BufferedReader(new InputStreamReader(
					new FileInputStream(locaitonFilePath), "UTF-8"));
			br4location.readLine();
			while((line=br4location.readLine())!=null){
				String[] strs = line.split(",");
				Station station = new Station();
				station.name=strs[0];
				station.stationID=strs[1];
				station.location = new LatAndLongt(Double.valueOf(strs[3]), Double.valueOf(strs[2]));
				station.range = Integer.valueOf(strs[4]);
				stations.put(strs[1], station);
			}
		} catch (Exception e) {
			File file = new File(locaitonFilePath);
			logger.error(file.getAbsolutePath()+" Stations properties load failed,system exit 1.");
			System.exit(1);
		}finally {
			try {
				if(br4location!=null){
					br4location.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("Stations properties successfully loaded.");
	}

	public static boolean exist(String station) {
		return stations.get(station)!=null;
	}
	
	
	
	
	
}
