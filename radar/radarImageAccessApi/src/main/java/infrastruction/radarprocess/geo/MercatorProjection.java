package infrastruction.radarprocess.geo;

public class MercatorProjection {
	public static double transFromLat(double lat){
		return Math.log(Math.tan(Math.PI*(1.0/4.0+lat/180.0/2.0)))*180/Math.PI;
	}
	
	public static double transToLat(double mlat){
		return Math.atan(Math.exp(mlat/180.0*Math.PI))*180.0*2/Math.PI-90.0;
	}
	
	
	public static byte[] transform(byte[] matrix,MataData mataData){
		int w = mataData.w;
		double latScale = mataData.latScale;
		double startLat = transFromLat(mataData.endLAL.latitude);
		double endLat = transFromLat(mataData.startLAL.latitude);
		int h = (int)Math.round((endLat-startLat)/latScale);
		
		byte[] result = new byte[h*w];
		
		for (int i = 0; i < h; i++) {
			double lat = transToLat(endLat-i*latScale);
			int y = (int)Math.round((mataData.startLAL.latitude-lat)/latScale);
			if(y>=mataData.h){
				y=y-1;
			}
			for (int j = 0; j < w; j++) {
				result[i*w+j]=matrix[y*w+j];
			}
		}
		
		return result;
	}
}
