package infrastruction.redarprocess.geo;

import infrastruction.radarprocess.Pixel;
import infrastruction.radarprocess.algebra.Vector;

public class GeoUtils {

	public static double getDistanceOnEarth(LatAndLongt p1,LatAndLongt p2){
		return getDeltaAngle(p1, p2)*G.R;
	}


	private static double getDeltaAngle(LatAndLongt p1, LatAndLongt p2) {
		double lat1 = p1.latitude*Math.PI/180;
		double long1 = p1.longtitude*Math.PI/180;
		double lat2 = p2.latitude*Math.PI/180;
		double long2 = p2.longtitude*Math.PI/180;
				
		double delta_lung = long1-long2;
		double delta_lat =lat1-lat2;
		
		double c =2*Math.asin(Math.sqrt(Math.sin(delta_lat/2)*Math.sin(delta_lat/2)
				+Math.cos(lat1)*Math.cos(lat2)*Math.sin(delta_lung/2)*Math.sin(delta_lung/2)));
		return c;
	}
	
	
	public static Pixel[][] getPixelsLocationRelativeTo(LatAndLongt center,double scale,int dim){
		
		double dLat = G.latChange1KmAlongLongt;
		double dLongt = center.getLongtChange1KmAlongLat()*scale;
		Pixel centerPixel = new Pixel(dim/2, dim/2);
		
		Pixel[][] pixles = new Pixel[dim][dim];
		
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				LatAndLongt point = new LatAndLongt(center.latitude-(i-centerPixel.y)*dLat, center.longtitude+(j-centerPixel.x)*dLongt);
				pixles[i][j]=getPixelLocationRelativeTo(center, scale, point);
			}
		}
		return pixles;
	}
	
	
	public static Pixel getPixelLocationRelativeTo(LatAndLongt center,double scale,LatAndLongt obj){
		
		Vector centerPoint = center.getXYZ().toVector();
		double dAngle = getDeltaAngle(center, obj);
		Vector newPoint = obj.getXYZ().toVector().mul(1.0/Math.cos(dAngle));
		Vector newVector = new Vector(centerPoint, newPoint);
		Vector eVector = center.getEastVector();
		Vector nVector = center.getNorthVector();
		int x = (int)Math.round(newVector.vmul(eVector)/scale);
		int y = -(int)Math.round(newVector.vmul(nVector)/scale);
		return new Pixel(x, y);
	}
	
}


