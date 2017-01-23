package infrastruction;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import infrastruction.radarprocess.ImageInfo;
import infrastruction.radarprocess.Station;
import infrastruction.radarprocess.Stations;

public class FilesSaveManager {
	
	private static Logger logger = LoggerFactory.getLogger(FilesSaveManager.class);
	private static final String DIRPATH="/data/";
	
	public static void saveSimpleProcessedRadarImage(BufferedImage image, String stationID, ImageInfo info){
		Station station = Stations.stations.get(stationID);
		String dir = station.province+"/"+station.name+"/"+info.time.getYearMonthString()+"/"
				+info.time.getDayString()+"/";
		try {			
			File file = new File(dir);
			if(!file.exists()){
				file.mkdirs();
			}
			ImageIO.write(image, "gif", new File(DIRPATH+dir+info.getString()));
		} catch (Exception e) {
			logger.info("Save image file to "+DIRPATH+dir+info.getString()+" failed!");
		}
	}
	
}
