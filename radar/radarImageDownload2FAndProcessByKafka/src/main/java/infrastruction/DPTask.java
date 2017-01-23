package infrastruction;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import infrastruction.db.RadarMongoDao;
import infrastruction.radarprocess.ImageInfo;
import infrastruction.radarprocess.RadarUtils;

public class DPTask implements Runnable {
	
	private static Logger logger = LoggerFactory.getLogger(DPTask.class);

	private JSONObject msg;
	private String stationID,imageTime;

	public DPTask(String stationID,String imageTime,JSONObject msg) {
		super();
		this.msg = msg;
		this.stationID = stationID;
		this.imageTime = imageTime;
	}

	@Override
	public void run() {
		downloadAndProcess(stationID,imageTime, msg);
		logger.info("downloaded and processed,saved image[stationID:"+stationID+",imageTime"+imageTime+"].");
	}

	public static void downloadAndProcess(String stationID, String imageTime, JSONObject linkMsgValue) {
		String link = linkMsgValue.getString("imageLink");
		byte[] orgbytes = HttpUtils.getBytes(link);
		if(orgbytes!=null){
			BufferedImage orgImage = ImageUtils.readImageFromBytes(orgbytes);
			if(orgImage!=null){
				ImageInfo info = RadarUtils.getInfo(orgImage);
				byte[] radarBytes = RadarUtils.getRadarRegionByteArray(orgImage);
				byte[] gpRadarBytes = RadarUtils.geoProcess(
						radarBytes, orgImage.getHeight(), info, stationID);
				//存到文件中
				FilesSaveManager.saveSimpleProcessedRadarImage(
						RadarUtils.unsimplefy(radarBytes, orgImage.getHeight(), orgImage.getHeight())
						,stationID,info);
				if(RadarMongoDao.saveSimpleProcessedRadarByte(
						stationID,Long.valueOf(imageTime),info.getTimeMills(),Gzip.compress(radarBytes))){
					linkMsgValue.remove("imageLink");
					Map<String,String> msg = Collections.singletonMap(stationID+"/"+imageTime, linkMsgValue.toString());
					if(RadarMongoDao.saveGeoProcessedRadarByte(
							stationID,info.getTimeMills(),Gzip.compress(gpRadarBytes))){
						KafkaProducerManager.distribute("radar_readimage_success", msg);
						return;
					}else{
						KafkaProducerManager.distribute("radar_geopimage",msg);
					}
				}
			}
		}
		Map<String,String> msg = Collections.singletonMap(stationID+"/"+imageTime, linkMsgValue.toString());
		KafkaProducerManager.distribute("radar_readimage", msg);
		KafkaProducerManager.distribute("radar_readimage_fail", msg);
	}
	
	
}
