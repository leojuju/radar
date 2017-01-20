package infrastruction.radarprocess.geo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import infrastruction.radarprocess.Pixel;

public class GridProcess {
	
	//有个问题是该方法针对放大情况没有进行平均考虑
	public static void combine(byte[] targetMatrix,MataData tarMataData,byte[] srcMatrix,MataData srcMataData){
		
		//取得范围
		int startX = (int)Math.ceil((srcMataData.startLAL.longtitude-tarMataData.startLAL.longtitude)/tarMataData.longtScale);
		int startY = (int)Math.ceil((-srcMataData.startLAL.latitude+tarMataData.startLAL.latitude)/tarMataData.latScale);
		int endX = (int)Math.floor((srcMataData.endLAL.longtitude-tarMataData.startLAL.longtitude)/tarMataData.longtScale);
		int endY = (int)Math.floor((-srcMataData.endLAL.latitude+tarMataData.startLAL.latitude)/tarMataData.latScale);

		for (int i = (startY<0?0:startY); i < (endY+1>tarMataData.h?tarMataData.h:(endY+1)); i++) {
			for (int j = (startX<0?0:startX); j < (endX+1>tarMataData.w?tarMataData.w:(endX+1)); j++) {
				int idx = i*tarMataData.w+j;
				LatAndLongt lal = new LatAndLongt(tarMataData.startLAL.latitude-i*tarMataData.latScale, tarMataData.startLAL.longtitude+j*tarMataData.longtScale);
				int x = (int)Math.round((lal.longtitude-srcMataData.startLAL.longtitude)/srcMataData.longtScale);
				int y = (int)Math.round((-lal.latitude+srcMataData.startLAL.latitude)/srcMataData.latScale);
				int idx2 = y*srcMataData.w+x;
				if(targetMatrix[idx]<srcMatrix[idx2]){
					targetMatrix[idx]=srcMatrix[idx2];
				}
			}
		}
		
	}

	public static void combine(byte[][] targetMatrix, MataData tarMataData, byte[] srcMatrix, MataData srcMataData) {
		
		int startX = (int)Math.ceil((srcMataData.startLAL.longtitude-tarMataData.startLAL.longtitude)/tarMataData.longtScale);
		int startY = (int)Math.ceil((-srcMataData.startLAL.latitude+tarMataData.startLAL.latitude)/tarMataData.latScale);
		int endX = (int)Math.floor((srcMataData.endLAL.longtitude-tarMataData.startLAL.longtitude)/tarMataData.longtScale);
		int endY = (int)Math.floor((-srcMataData.endLAL.latitude+tarMataData.startLAL.latitude)/tarMataData.latScale);

		for (int i = startY<0?0:startY; i < ((endY+1)>tarMataData.h?tarMataData.h:(endY+1)); i++) {
			for (int j = startX<0?0:startX; j < ((endX+1)>tarMataData.w?tarMataData.w:(endX+1)); j++) {
				LatAndLongt lal = new LatAndLongt(tarMataData.startLAL.latitude-i*tarMataData.latScale, tarMataData.startLAL.longtitude+j*tarMataData.longtScale);
				int x = (int)Math.round((lal.longtitude-srcMataData.startLAL.longtitude)/srcMataData.longtScale);
				int y = (int)Math.round((-lal.latitude+srcMataData.startLAL.latitude)/srcMataData.latScale);
				int idx2 = y*srcMataData.w+x;
				if(targetMatrix[i][j]<srcMatrix[idx2]){
					targetMatrix[i][j]=srcMatrix[idx2];
				}
			}
		}
	}

	public static void map(Map<Short,Pixel> [][] dataMaps,MataData tarMataData,MataData srcMataData,short num){
		
		int startX = (int)Math.ceil((srcMataData.startLAL.longtitude-tarMataData.startLAL.longtitude)/tarMataData.longtScale);
		int startY = (int)Math.ceil((-srcMataData.startLAL.latitude+tarMataData.startLAL.latitude)/tarMataData.latScale);
		int endX = (int)Math.floor((srcMataData.endLAL.longtitude-tarMataData.startLAL.longtitude)/tarMataData.longtScale);
		int endY = (int)Math.floor((-srcMataData.endLAL.latitude+tarMataData.startLAL.latitude)/tarMataData.latScale);
		
		for (int i = startY<0?0:startY; i < ((endY+1)>tarMataData.h?tarMataData.h:(endY+1)); i++) {
			for (int j = startX<0?0:startX; j < ((endX+1)>tarMataData.w?tarMataData.w:(endX+1)); j++) {
				LatAndLongt lal = new LatAndLongt(tarMataData.startLAL.latitude-i*tarMataData.latScale, tarMataData.startLAL.longtitude+j*tarMataData.longtScale);
				int x = (int)Math.round((lal.longtitude-srcMataData.startLAL.longtitude)/srcMataData.longtScale);
				int y = (int)Math.round((-lal.latitude+srcMataData.startLAL.latitude)/srcMataData.latScale);
				if(dataMaps[i][j]==null){
					dataMaps[i][j] = new HashMap<Short,Pixel>();
				}
				dataMaps[i][j].put(num, new Pixel(x, y));
			}
		}
	}

	//下采样,传进来的tarMataData会受到修改
	public static byte[] subSample(byte[] chinaDetailMatrix, MataData tarMataData, int scale) {
		int w =tarMataData.w;
		int h = tarMataData.h;
		int rw = (w+scale-1)/scale;
		int rh = (h+scale-1)/scale;
		byte[] result = new byte[rw*rh];
		for (int i = 0; i < rh; i++) {
			for (int j = 0; j < rw; j++) {
				result[i*rw+j] = getSurroundingMidValue(chinaDetailMatrix,j*scale,i*scale,scale,w,h);
			}
		}
		tarMataData.h=rh;
		tarMataData.w=rw;
		tarMataData.latScale *=scale;
		tarMataData.longtScale *=scale;
		return result;
	}

	private static byte getSurroundingMidValue(byte[] chinaDetailMatrix, int x, int y, int scale, int w , int h) {
		List<Byte> valList = new ArrayList<Byte>();
		int r = scale/2;
		for (int i = 0; i < scale; i++) {
			for (int j = 0; j < scale; j++) {
				int _x = x-r+j;
				int _y = y-r+i;
				if(_x>=0&&_x<w&&_y>=0&&_y<h){
					valList.add(chinaDetailMatrix[_y*w+_x]);
				}
			}
		}
		Collections.sort(valList);
		return valList.get(valList.size()/2);
	}
 	
}
