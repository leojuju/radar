package infrastruction.radarprocess;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import infrastruction.Gzip;
import infrastruction.db.RadarMongoDao;
import infrastruction.radarprocess.geo.GeoProcessForRadarImage;
import infrastruction.radarprocess.geo.GridProcess;
import infrastruction.radarprocess.geo.LatAndLongt;
import infrastruction.radarprocess.geo.MataData;
import infrastruction.radarprocess.geo.MataDatas;
import infrastruction.radarprocess.geo.MercatorProjection;

public class Jigsaw {

	private static Logger logger = LoggerFactory.getLogger(Jigsaw.class);

	/**
	 * 根据传入的参数，去数据库获取相应图片，并进行拼图处理
	 * @param startLAL
	 * @param endLAL
	 * @param scale 表示图像缩小的等级，范围为>=1的整数，1表示不缩小，16表示图像缩小为1/16.
	 * @param time 
	 * @param requestID
	 * @return gif图像的二进制数组
	 */
	public static byte[] jigsaw(LatAndLongt startLAL, LatAndLongt endLAL, int scale, long time, String requestID) {
		//获取请求区域内相关站点.
		long startTime = System.currentTimeMillis();
		List<String> searchStationIDs = GeoProcessForRadarImage.getNearbyStations(startLAL, endLAL);
		logger.info("[RequestID:" + requestID + "] NearBy station num:" + searchStationIDs.size());
		//请求数据库,获取该请求区域内相关站点的接近该时刻的可用图像.
		Map<String, byte[]> results = RadarMongoDao.search(time, searchStationIDs, requestID);
		logger.info("[RequestID:" + requestID + "] Available station num:" + results.size());
		long endTime = System.currentTimeMillis();
		logger.info("[RequestID:" + requestID + "] time for search pics:" + (endTime-startTime));
		startTime = endTime;
		//拼图处理:
		double latScale = 0.005;
		double longtScale = 0.005;
		int h = (int) ((startLAL.latitude - endLAL.latitude) / latScale) + 1;
		int w = (int) ((-startLAL.longtitude + endLAL.longtitude) / longtScale) + 1;
		MataData tarMataData = new MataData("china", w, h, latScale, longtScale, startLAL, endLAL);
		logger.info("[RequestID:" + requestID + "] Picture size before mercator projection:w=" + w + ",h=" + h + ".");
			//创建一个空白底图.
		byte[] chinaDetailMatrix = new byte[w * h];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				//初始化,-1表示没有数据.
				chinaDetailMatrix[i * w + j] = -1;
			}
		}
		Set<String> stationIDs = results.keySet();
		for (String stationID : stationIDs) {
			byte[] matrix = null;
			try {
				matrix = Gzip.decompress(results.get(stationID));
			} catch (IOException e) {
				e.printStackTrace();
			}
			MataData mataData = MataDatas.mataDatas.get(stationID);
			//将每个站的图像拼合到底图上.
			GridProcess.combine(chinaDetailMatrix, tarMataData, matrix, mataData);
		}
		endTime = System.currentTimeMillis();
		logger.info("[RequestID:" + requestID + "] time for pics combination:" + (endTime-startTime));
		startTime = endTime;
		// 图片下采样(即图像缩小).
		byte[] chinaMatrix = null;
		if (scale > 1) {
			chinaMatrix = GridProcess.subSample(chinaDetailMatrix, tarMataData, scale);
			w = tarMataData.w;
			h = tarMataData.h;
		} else {
			chinaMatrix = chinaDetailMatrix;
		}
		// 针对baidu地图进行Mercator投影变换
		chinaMatrix = MercatorProjection.transform(chinaMatrix, tarMataData);
		BufferedImage imageChina = RadarUtils.unsimplefy(chinaMatrix, w, chinaMatrix.length / w);
		logger.info("[RequestID:" + requestID + "] Picture size after mercator projection:w=" + imageChina.getWidth()
				+ ",h=" + imageChina.getHeight() + ".");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ImageIO.write(imageChina, "gif", bos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		endTime = System.currentTimeMillis();
		logger.info("[RequestID:" + requestID + "] time for other porcess:" + (endTime-startTime));
		return bos.toByteArray();
	}
}
