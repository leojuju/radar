package infrastruction.radarprocess.geo;

/**
 * 一些地理相关常数
 * @author linj
 *
 */
public class G {
	
	//地球半径,unit:km
	public static final double R=6378.137;
	//沿经线一公里的纬度值变化量
	public static final double latChange1KmAlongLongt =1.0/G.R*180/Math.PI;
	
}
