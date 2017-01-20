package infrastruction.radarprocess.geo;

import infrastruction.radarprocess.algebra.AngleFormat;
import infrastruction.radarprocess.algebra.Vector;
import infrastruction.radarprocess.algebra.XYZ;

/**
 * for latitude and longitude to store;
 * @author linj
 *
 */
public class LatAndLongt {
	
	public double latitude;
	public double longtitude;
	
	public LatAndLongt() {
		super();
	}


	public LatAndLongt(double latitude, double longtitude) {
		super();
		this.latitude = latitude;
		this.longtitude = longtitude;
	}
	
	/**
	 * 由一个地心指出来的向量来得到该向量指向的地点的经纬度坐标
	 * @param v
	 */
	public LatAndLongt(Vector v){
		if(v==null||v.isZero()){
			System.out.println("vector does't exist or is zero,cannot be change to LatAndLongt object");
			return;
		}
		this.latitude = Math.asin(v.z/v.mod())*180/Math.PI;
		double l=Math.sqrt(v.x*v.x+v.y*v.y);
		if(l==0){
			this.longtitude=0;
		}else{
			this.longtitude = Math.acos(v.x/l)*180/Math.PI;
			if(v.y<0){
				this.longtitude =-this.longtitude;
			}
		}
	}
			
	/**
	 * Transform latitude and longitude (x,y,z) in a coordinate system as 坐标转化示意图.gif;
	 * @return
	 */
	public XYZ getXYZ(){
		double lat=this.latitude/180*Math.PI;
		double longt=this.longtitude/180*Math.PI;
		
		double x=G.R*Math.cos(lat)*Math.cos(longt);
		double y=G.R*Math.cos(lat)*Math.sin(longt);
		double z=G.R*Math.sin(lat);
		
		return new XYZ(x, y, z);
	}
	
	/**
	 * get north direction identity vector;
	 * @return
	 */
	public Vector getNorthVector(){
		
		double lat=this.latitude/180*Math.PI;
		double longt=this.longtitude/180*Math.PI;
		double x=-Math.sin(lat)*Math.cos(longt);
		double y=-Math.sin(lat)*Math.sin(longt);
		double z=Math.cos(lat);
		
		double qsum =Math.sqrt(x*x+y*y+z*z);
		return new Vector(x/qsum, y/qsum, z/qsum);
	}
	
	/**
	 * get east direction identity vector
	 * @return
	 */
	public Vector getEastVector(){
		
		double longt=this.longtitude/180*Math.PI;
		
		double x=-Math.sin(longt);
		double y=Math.cos(longt);
		double mod =Math.sqrt(x*x+y*y);
		
		return new Vector(x/mod, y/mod, 0);
	}
	
	/**
	 * 获取沿正东直线方向eastLineD(km)距离和沿正北直线方向northLineD(km)距离的点的经纬度坐标
	 * @param eastLineD
	 * @param northLineD
	 * @return
	 */
	public LatAndLongt getLALFromDistance(double eastLineD,double northLineD){
		Vector nVector = getNorthVector();
		Vector eVector = getEastVector();
		XYZ xyz = getXYZ();
		Vector newPoint=xyz.toVector().add(nVector.mul(northLineD)).add(eVector.mul(eastLineD));
		return new LatAndLongt(newPoint);
	}

	
	
	public double getLatCircleRadiu(){
		return G.R*Math.cos(this.latitude/180*Math.PI);
	}
	
	/**
	 * change of longtitude
	 * @return
	 */
	public double getLongtChange1KmAlongLat(){
		return 1.0/getLatCircleRadiu()*180/Math.PI;
	}
	
	
	@Override
	public String toString() {
		return "LatAndLongt [latitude=" + latitude + ", longtitude=" + longtitude + "]";
	}
	
	public String toDegreeString(){

		return "LatAndLongt [latitude=" +AngleFormat.toAngleString(latitude)+ ", longtitude=" +AngleFormat.toAngleString(longtitude)+ "]";
	}
}
